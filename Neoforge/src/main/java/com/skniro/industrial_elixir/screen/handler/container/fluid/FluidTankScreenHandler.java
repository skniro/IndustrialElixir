package com.skniro.industrial_elixir.screen.handler.container.fluid;

import com.skniro.industrial_elixir.block.entity.container.fluid.FluidTankBlockEntity;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.UpgradeSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.FriendlyByteBuf;

public class FluidTankScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final FluidTankBlockEntity blockEntity;
    private final ContainerData propertyDelegate;

    public FluidTankScreenHandler(int pContainerId, Inventory inv, FriendlyByteBuf packetByteBuf) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public FluidTankScreenHandler(int pContainerId, Inventory playerInventory, BlockEntity entity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.FluidTank.get(), pContainerId);
        blockEntity = ((FluidTankBlockEntity) entity);
        this.propertyDelegate = delegate;
        this.inventory = blockEntity;
        this.addSlot(new Slot(inventory,0, 52, 13));
        this.addSlot(new Slot(inventory, 8, 52, 49));
        this.addSlot(new Slot(inventory,9, 106, 13));
        this.addSlot(new Slot(inventory, 10, 106, 49));
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