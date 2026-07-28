package com.skniro.industrial_elixir.screen.handler.machine;

import com.skniro.industrial_elixir.block.entity.machine.HeatCentrifugeEntity;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryFuelSlot;
import com.skniro.industrial_elixir.screen.slot.UpgradeSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.FriendlyByteBuf;

public class HeatCentrifugeScreenHandler extends AbstractContainerMenu {
    final Container inventory;
    final ContainerData propertyDelegate;
    public final HeatCentrifugeEntity blockEntity;

    public HeatCentrifugeScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(3));
    }

    public HeatCentrifugeScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.HeatCentrifuge.get(), syncId);
        checkContainerSize((Container) blockEntity, 12);
        this.inventory = (Container) blockEntity;
        this.blockEntity = (HeatCentrifugeEntity) blockEntity;
        this.propertyDelegate = delegate;

        this.addSlot(new Slot(inventory, 1, 52, 33));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 104, 16));
        this.addSlot(new BatteryFuelSlot(inventory, 3, 131, 63, this.blockEntity.getEnergyTier()));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 10, 104, 34));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 11, 104, 52));

        addUpgradeSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    private void addUpgradeSlots() {
        this.addSlot(new UpgradeSlot(inventory, 4, 151, 9));
        this.addSlot(new UpgradeSlot(inventory, 5, 151, 27));
        this.addSlot(new UpgradeSlot(inventory, 6, 151, 45));
        this.addSlot(new UpgradeSlot(inventory, 7, 151, 63));
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getHeat() {
        return propertyDelegate.get(2);
    }

    public int getScaledHeatWidth() {
        return getHeat() * 54 / 100;
    }

    public int getScaledEnergyHeight() {
        return blockEntity.getScaledEnergyHeight();
    }

    public int getScaledProgress() {
        int progress = propertyDelegate.get(0);
        int maxProgress = propertyDelegate.get(1);
        return maxProgress != 0 && progress != 0 ? progress * 21 / maxProgress : 0;
    }

    @Override
    public net.minecraft.world.item.ItemStack quickMoveStack(Player player, int invSlot) {
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
        return inventory.stillValid(player);
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


