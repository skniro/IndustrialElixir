package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.generator.GeneratorSolarPanelBlock;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorSolarPanelScreenHandler;
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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;


public class GeneratorSolarPanelBlockEntity extends NewBaseGeneratorBlockEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    protected final ContainerData propertyDelegate;
    private GeneratorState state = GeneratorState.IDLE;
    private int cachedPower;
    private int DayPower;
    private int NightPower;
    private EnergyTier energyTier;

    public GeneratorSolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.GENERATOR_SOLAR_PANEL_BLOCK_ENTITY, pos, state);
        this.DayPower = ((GeneratorSolarPanelBlock)state.getBlock()).getDayPower();
        this.NightPower = ((GeneratorSolarPanelBlock)state.getBlock()).getNightPower();
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GeneratorSolarPanelBlockEntity.this.cachedPower;
                    case 1 -> GeneratorSolarPanelBlockEntity.this.state.ordinal();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: GeneratorSolarPanelBlockEntity.this.cachedPower = value;
                    case 1: {
                            GeneratorState[] values = GeneratorState.values();
                            if (value >= 0 && value < values.length) {
                                GeneratorSolarPanelBlockEntity.this.state = values[value];
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
        nbt.putLong(("generator_solar_panel.energy"), energyContainer.amount);
        nbt.putInt("generator_solar_panel.day_power", this.DayPower);
        nbt.putInt("generator_solar_panel.night_power", this.NightPower);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        energyContainer.amount = nbt.getLongOr("generator_solar_panel.energy", 0);
        this.DayPower = nbt.getIntOr("generator_solar_panel.day_power", this.DayPower);
        this.NightPower = nbt.getIntOr("generator_solar_panel.night_power", this.NightPower);
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
        return switch (((GeneratorSolarPanelBlock)getBlockState().getBlock()).getEnergyTier().ordinal()) {
            case 0 -> Component.translatable(FurnitureStrings.GENERATOR_SolarPanel);
            case 1 -> Component.translatable(FurnitureStrings.GENERATOR_ADVANCED_SOLAR_PANEL);
            case 2 -> Component.translatable(FurnitureStrings.GENERATOR_HYBRID_SOLAR_PANEL);
            case 3 -> Component.translatable(FurnitureStrings.GENERATOR_ULTIMATE_SOLAR_PANEL);
            case 4 -> Component.translatable(FurnitureStrings.GENERATOR_QUANTUM_SOLAR_PANEL);
            default -> Component.translatable(FurnitureStrings.GENERATOR_SolarPanel);
        };
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new GeneratorSolarPanelScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world == null || world.isClientSide()) return;

        discharge(0);
        discharge(1);
        discharge(2);
        discharge(3);

        cachedPower = calculatePower(world, pos);
        updateState();
        //System.out.println("Wind: " + windStrength + " Power: " + cachedPower);
        setChanged();

        if (cachedPower > 0) {
            if (energyContainer.amount < energyContainer.getCapacity()) {
                try (Transaction transaction = Transaction.openOuter()) {
                    this.energyContainer.getSideStorage(null).insert(cachedPower, transaction);
                    transaction.commit();
                }
            }
        }
        pushEnergyToNeighbours();
    }

    private void updateState() {
        if (cachedPower <= 0) {
            state = GeneratorState.IDLE;
        } else {
            state = GeneratorState.GENERATING;
        }
    }

    private int calculatePower(Level world, BlockPos pos) {
        double power = 0;
        if (world.isBrightOutside() && !world.isRaining() && !world.isThundering()) {
            power = this.DayPower;
        } else {
            power = this.NightPower;
        }

        return (int) Math.ceil(power);
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
                    ((AbstractMachineblock)getBlockState().getBlock()).getEnergyTier().getMaxOutput(),
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
        GENERATING
    }
}