package com.skniro.industrial_elixir.screen.handler.machine;

import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
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

public class CompressorScreenHandler extends AbstractContainerMenu {
    final Container inventory;
    final ContainerData propertyDelegate;
    public final AbstractMachineEntity blockEntity;

    public CompressorScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf){
        this(syncId,playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()),new SimpleContainerData(2));
    }


    public CompressorScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.Compressor.get(), syncId);
        checkContainerSize((Container) blockEntity,8);
        this.inventory = (Container) blockEntity;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.blockEntity = (AbstractMachineEntity) blockEntity;
        this.addSlot(new Slot(inventory, 1, 52, 13));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 100, 34));
        this.addSlot(new BatteryFuelSlot(inventory, 3, 52, 49, ((AbstractMachineEntity) blockEntity).getEnergyTier()));

        addBasic(inventory, playerInventory, delegate);
    }

    private void addBasic(Container inventory, Inventory playerInventory, ContainerData propertyDelegate){
        addUpgradeSlot(inventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    private void addUpgradeSlot(Container inventory) {
        this.addSlot(new UpgradeSlot(inventory, 4, 151, 9));
        this.addSlot(new UpgradeSlot(inventory, 5, 151, 27));
        this.addSlot(new UpgradeSlot(inventory, 6, 151, 45));
        this.addSlot(new UpgradeSlot(inventory, 7, 151, 63));
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }



    public int getScaledEnergyHeight() {
        long energy = blockEntity.energyContainer.amount;
        long capacity = blockEntity.energyContainer.getCapacity();
        int energyBarSize = 16;

        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
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
