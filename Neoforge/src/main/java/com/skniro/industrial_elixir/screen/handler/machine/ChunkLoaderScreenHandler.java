package com.skniro.industrial_elixir.screen.handler.machine;

import com.skniro.industrial_elixir.block.entity.machine.ChunkLoaderEntity;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryFuelSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.FriendlyByteBuf;

public class ChunkLoaderScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final ChunkLoaderEntity blockEntity;
    final ContainerData propertyDelegate;

    public ChunkLoaderScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public ChunkLoaderScreenHandler(int syncId, Inventory playerInventory, BlockEntity entity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.ChunkLoader.get(), syncId);
        blockEntity = (ChunkLoaderEntity) entity;
        checkContainerSize(blockEntity, 12);
        this.propertyDelegate = delegate;
        this.inventory = blockEntity;
        inventory.startOpen(playerInventory.player);
        addDataSlots(propertyDelegate);
    }

    public int getScaledEnergyHeight() {
        long energy = blockEntity.energyContainer.amount;
        long capacity = blockEntity.energyContainer.getCapacity();
        int energyBarSize = 50;
        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (!player.level().isClientSide() && id >= 0 && id < 25 && id != 12) {
            blockEntity.toggleChunk(id);
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }
}
