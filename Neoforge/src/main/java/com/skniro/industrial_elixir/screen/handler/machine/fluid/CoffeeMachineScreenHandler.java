package com.skniro.industrial_elixir.screen.handler.machine.fluid;

import com.skniro.industrial_elixir.block.entity.machine.fluid.CoffeeMachineBlockEntity;
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

public class CoffeeMachineScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final CoffeeMachineBlockEntity blockEntity;
    final ContainerData propertyDelegate;

    public CoffeeMachineScreenHandler(int containerId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public CoffeeMachineScreenHandler(int containerId, Inventory playerInventory, BlockEntity entity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.CoffeeMachine.get(), containerId);
        blockEntity = (CoffeeMachineBlockEntity) entity;
        checkContainerSize(blockEntity, 13);
        this.propertyDelegate = delegate;
        this.inventory = blockEntity;
        inventory.startOpen(playerInventory.player);

        // Fluid item slot
        this.addSlot(new Slot(inventory,0, 8, 58));
        // Main input
        this.addSlot(new Slot(inventory, 1, 52, 15));
        // Output (coffee drink)
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 104, 34));
        // Battery/energy slot
        this.addSlot(new BatteryFuelSlot(inventory, 3, 131, 63, blockEntity.getEnergyTier()));
        // Fluid item return slot
        this.addSlot(new Slot(inventory,8, 26, 58));
        // Second input
        this.addSlot(new Slot(inventory, 9, 52, 33));
        // Third input
        this.addSlot(new Slot(inventory, 12, 52, 51));

        addBasic(inventory, playerInventory, delegate);
    }

    private void addBasic(Container inventory, Inventory playerInventory, ContainerData propertyDelegate) {
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

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledEnergyHeight() {
        long energy = blockEntity.energyContainer.amount;
        long capacity = blockEntity.energyContainer.getCapacity();
        int energyBarSize = 16;

        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
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
