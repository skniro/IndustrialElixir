package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.item.ModFuelRegistry;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.generator.CoalGeneratorBlock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.generator.CoalGeneratorScreenHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;


public class CoalGeneratorBlockEntity extends NewBaseGeneratorBlockEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);


    private static final int INPUT_SLOT = 0;
    private static final int Battery_SLOT = 1;

    protected final ContainerData propertyDelegate;
    private int burnProgress;
    private int maxBurnProgress;
    private boolean isBurning = false;
    private static final int ENERGY_TRANSFER_AMOUNT = 320;

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.COAL_GENERATOR_BE.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CoalGeneratorBlockEntity.this.burnProgress;
                    case 1 -> CoalGeneratorBlockEntity.this.maxBurnProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: CoalGeneratorBlockEntity.this.burnProgress = value;
                    case 1: CoalGeneratorBlockEntity.this.maxBurnProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putLong(("coal_generator.energy"), energyContainer.amount);
        nbt.putInt(("coal_generator.burn_progress"), burnProgress);
        nbt.putInt(("coal_generator.max_burn_progress"), maxBurnProgress);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        energyContainer.amount = nbt.getLongOr("coal_generator.energy", 0);
        burnProgress = nbt.getIntOr("coal_generator.burn_progress", 0);
        maxBurnProgress = nbt.getIntOr("coal_generator.max_burn_progress", 0);
        super.loadAdditional(nbt);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.CoalGenerator);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CoalGeneratorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    private int getFuelTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;

        Integer custom = ModFuelRegistry.getModFuel().get(stack.getItem());
        if (custom != null) return custom;

        for (var entry : ModFuelRegistry.getModFuelTags().entrySet()) {
            if (stack.is(entry.getKey())) {
                return entry.getValue();
            }
        }

        if (this.level != null) {
            return this.level.fuelValues().burnDuration(stack) / 2;
        }

        return 0;
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world != null && !world.isClientSide()) {

            discharge(Battery_SLOT);

            pushEnergyToNeighbours();

            if (isBurning) {
                if (energyContainer.amount < energyContainer.getCapacity()) {
                    increaseBurnTimer();
                    fillUpOnEnergy();
                }

                if (currentFuelDoneBurning()) {
                    resetBurning();
                }

                world.setBlock(pos, state.setValue(CoalGeneratorBlock.LIT, isBurning), Block.UPDATE_ALL);
            }

            if (!isBurning && hasFuelItemInSlot() && energyContainer.amount < energyContainer.getCapacity()) {
                startBurning();
            }
        }
    }

    public void pushEnergyToNeighbours() {
        if (energyContainer.amount <= 0) return;

        for (Direction direction : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.getCapability(
                    level,
                    worldPosition.relative(direction), null,null,
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

    private void fillUpOnEnergy() {
        try (Transaction transaction = Transaction.openRoot()) {
            this.energyContainer.getSideStorage(null).insert(energyTier.getMaxInput(), transaction);
            transaction.commit();
        }
    }

    private boolean hasFuelItemInSlot() {
        ItemStack stack = this.getItem(INPUT_SLOT);
        return getFuelTime(stack) > 0;
    }

    private boolean isBurningFuel() {
        return isBurning;
    }

    private void startBurning() {
        ItemStack stack = this.getItem(INPUT_SLOT);

        int burnTime = getFuelTime(stack);
        if (burnTime <= 0) return;

        this.removeItem(INPUT_SLOT, 1);

        this.burnProgress = burnTime;
        this.maxBurnProgress = this.burnProgress;
        this.isBurning = true;
    }

    private void increaseBurnTimer() {
        --this.burnProgress;
    }

    private boolean currentFuelDoneBurning() {
        return this.burnProgress <= 0;
    }

    private void resetBurning() {
        isBurning = false;
    }


    public long getFreeSpace() {
        return this.energyContainer.getCapacity() - energyContainer.amount;
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
}