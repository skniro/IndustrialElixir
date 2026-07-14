package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorWindMillScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;


public class GeneratorWindMillBlockEntity extends BaseGeneratorBlockEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    private static final int Battery_SLOT = 0;

    protected final ContainerData propertyDelegate;
    private static final int ENERGY_TRANSFER_AMOUNT = 320;
    private static final int WIND_UPDATE_INTERVAL = 128;
    private int tickCounter = 0;
    private int windStrength = 10;
    private int cachedPower = 0;
    private GeneratorState state = GeneratorState.IDLE;

    public GeneratorWindMillBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.GENERATOR_WIND_MILL_BLOCK_ENTITY, pos, state, 100, EnergyTier.TIER1);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GeneratorWindMillBlockEntity.this.tickCounter;
                    case 1 -> GeneratorWindMillBlockEntity.this.windStrength;
                    case 2 -> GeneratorWindMillBlockEntity.this.cachedPower;
                    case 3 -> GeneratorWindMillBlockEntity.this.state.ordinal();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: GeneratorWindMillBlockEntity.this.tickCounter = value;
                    case 1: GeneratorWindMillBlockEntity.this.windStrength = value;
                    case 2: GeneratorWindMillBlockEntity.this.cachedPower = value;
                    case 3: {
                            GeneratorState[] values = GeneratorState.values();
                            if (value >= 0 && value < values.length) {
                                GeneratorWindMillBlockEntity.this.state = values[value];
                            }
                        }
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putLong(("generator_wind_mill.energy"), energyContainer.amount);
        nbt.putInt(("generator_wind_mill.windstrength"), windStrength);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        energyContainer.amount = nbt.getLongOr("generator_wind_mill.energy", 0);
        windStrength = nbt.getIntOr("generator_wind_mill.windstrength", 10);
        super.loadAdditional(nbt);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.GeneratorWindMill);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new GeneratorWindMillScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world == null || world.isClientSide()) return;

        discharge(Battery_SLOT);

        tickCounter++;

        if (tickCounter % WIND_UPDATE_INTERVAL == 0) {
            updateWindStrength(world);
            cachedPower = calculatePower(world, pos);
            updateState();
            //System.out.println("Wind: " + windStrength + " Power: " + cachedPower);
            setChanged();

            if (cachedPower >= 15) {
                tryBreakGenerator(world, pos);
            }
        }
        if (cachedPower > 0) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.energyContainer.getSideStorage(null).insert(cachedPower, transaction);
                transaction.commit();
            }
            pushEnergyToNeighbours();
        }
    }

    private void updateState() {

        if (cachedPower <= 0) {
            state = GeneratorState.IDLE;
            return;
        }

        if (cachedPower >= 15) {
                state = GeneratorState.DANGER;
            } else {
            state = GeneratorState.GENERATING;
        }
    }

    private int calculatePower(Level world, BlockPos pos) {

        int y = pos.getY();

        if (y < 64) return 0;

        int obstacleCount = countObstacles(world, pos);

        int h = y - obstacleCount;

        double weatherMultiplier = 3.0;

        if (world.isThundering()) {
            weatherMultiplier = 6.0;
        } else if (world.isRaining()) {
            weatherMultiplier = 8.0;
        }

        double power = weatherMultiplier * windStrength * (h - 64) / 750.0;

        if (power < 0) return 0;

        return (int) Math.ceil(power);
    }

    private void tryBreakGenerator(Level world, BlockPos pos) {
        RandomSource random = world.getRandom();

        double breakChance = (cachedPower / 11.46) * 0.001292;

        if (random.nextDouble() < breakChance) {

            world.setBlockAndUpdate(pos, GrowableOresBlocks.COAL_GENERATOR.defaultBlockState());

            int ironCount = random.nextInt(5);
            if (ironCount > 0) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_INGOT, ironCount));
            }
        }
    }

    private int countObstacles(Level world, BlockPos center) {
        int count = 0;

        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                for (int dy = -2; dy <= 4; dy++) {

                    BlockPos checkPos = center.offset(dx, dy, dz);

                    if (checkPos.equals(center)) continue;

                    BlockState state = world.getBlockState(checkPos);

                    if (!state.isAir()) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private void updateWindStrength(Level world) {
        RandomSource random = world.getRandom();
        int s = windStrength;

        if (s >= 0 && s <= 20 && random.nextFloat() < 0.10f) {
            s++;
        }

        if (s >= 20 && s <= 30 && random.nextFloat() < 0.10f) {
            s--;
        }

        if (s >= 0 && s <= 9 && random.nextFloat() < (s / 100f)) {
            s--;
        }

        if (s >= 21 && s <= 30 && random.nextFloat() < ((30 - s) / 100f)) {
            s++;
        }

        windStrength = Math.max(0, Math.min(30, s));
    }

    private void pushEnergyToNeighbours() {
        if (energyContainer.amount <= 0) return;

        for (Direction direction : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.find(
                    level,
                    worldPosition.relative(direction),
                    direction.getOpposite()
            );

            if (target == null) continue;

            EnergyStorageUtil.move(
                    this.energyContainer.getSideStorage(direction),
                    target,
                    ENERGY_TRANSFER_AMOUNT,
                    null
            );
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    public enum GeneratorState {
        IDLE,
        GENERATING,
        DANGER
    }
}