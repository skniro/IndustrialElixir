package com.skniro.industrial_elixir.screen.handler.generator.heat;

import com.skniro.industrial_elixir.block.entity.generator.heat.SolidFuelHeaterEntity;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.FriendlyByteBuf;

public class SolidFuelHeaterScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final SolidFuelHeaterEntity blockEntity;
    final ContainerData propertyDelegate;

    public SolidFuelHeaterScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public SolidFuelHeaterScreenHandler(int syncId, Inventory playerInventory, BlockEntity entity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.SolidFuelHeater.get(), syncId);
        blockEntity = (SolidFuelHeaterEntity) entity;
        checkContainerSize(blockEntity, 12);
        this.propertyDelegate = delegate;
        this.inventory = blockEntity;
        inventory.startOpen(playerInventory.player);

        // Fuel slot
        this.addSlot(new Slot(inventory, 1, 80, 35));
        // Ash output slot
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 117, 35));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    public float getFuelProgress() {
        int i = this.propertyDelegate.get(1);
        if (i == 0) {
            i = 200;
        }
        return Mth.clamp((float)this.propertyDelegate.get(0) / (float)i, 0.0f, 1.0f);
    }

    public Component getHeatTooltips() {
        return Component.literal(blockEntity.heatContainer.amount + " / " + blockEntity.getHeatCapacity() + " H");
    }

    public boolean isBurning() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledBurnProgress() {
        int burnTime = propertyDelegate.get(0);
        int burnDuration = propertyDelegate.get(1);
        if (burnDuration == 0) return 0;
        return burnTime * 13 / burnDuration;
    }

    public int getScaledHeatHeight() {
        long heat = blockEntity.heatContainer.amount;
        long capacity = blockEntity.getHeatCapacity();
        int barHeight = 50;
        return Math.toIntExact(heat != 0 ? heat * barHeight / capacity : 0);
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

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
