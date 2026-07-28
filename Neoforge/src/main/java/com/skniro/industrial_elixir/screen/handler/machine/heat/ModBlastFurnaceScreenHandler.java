package com.skniro.industrial_elixir.screen.handler.machine.heat;

import com.skniro.industrial_elixir.block.entity.machine.fluid.BrewReactorBlockEntity;
import com.skniro.industrial_elixir.block.entity.machine.heat.ModBlastFurnaceBlockEntity;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.UpgradeSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.FriendlyByteBuf;

public class ModBlastFurnaceScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final ModBlastFurnaceBlockEntity blockEntity;
    final ContainerData propertyDelegate;

    public ModBlastFurnaceScreenHandler(int pContainerId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(pContainerId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public ModBlastFurnaceScreenHandler(int pContainerId, Inventory playerInventory, BlockEntity entity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.ModBlastFurnace.get(), pContainerId);
        blockEntity = ((ModBlastFurnaceBlockEntity) entity);
        checkContainerSize(blockEntity, 10);
        this.propertyDelegate = delegate;
        this.inventory = blockEntity;
        inventory.startOpen(playerInventory.player);

        this.addSlot(new Slot(inventory,0, 8, 58));
        this.addSlot(new Slot(inventory, 1, 52, 34));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 101, 26));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 11, 101, 44));
        this.addSlot(new Slot(inventory,8, 26, 58));

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

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 21;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledHeatHeight() {
        long energy = blockEntity.heatContainer.amount;
        long capacity = blockEntity.heatContainer.getCapacity();
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
    public boolean stillValid(Player pPlayer) {
        return this.inventory.stillValid(pPlayer);
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
