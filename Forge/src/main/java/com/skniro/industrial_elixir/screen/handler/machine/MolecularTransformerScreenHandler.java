package com.skniro.industrial_elixir.screen.handler.machine;

import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.block.entity.machine.MolecularTransformerBlockEntity;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
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

public class MolecularTransformerScreenHandler extends AbstractContainerMenu {
    final Container inventory;
    final ContainerData propertyDelegate;
    public final AbstractMachineEntity blockEntity;

    public MolecularTransformerScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf){
        this(syncId,playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()),new SimpleContainerData(2));
    }


    public MolecularTransformerScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.MolecularTransformer.get(), syncId);
        checkContainerSize((Container) blockEntity,8);
        this.inventory = (Container) blockEntity;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.blockEntity = (MolecularTransformerBlockEntity) blockEntity;
        this.addSlot(new Slot(inventory, 1, 52, 33));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 100, 34));

        addBasic(inventory, playerInventory, delegate);
    }

    private void addBasic(Container inventory, Inventory playerInventory, ContainerData propertyDelegate){
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }



    public int getScaledEnergyHeight() {
        long energy = blockEntity.energyContainer.amount;
        long capacity = blockEntity.energyContainer.getCapacity();
        int energyBarSize = 14;

        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    public int getScaledProgress() {
        MolecularTransformerBlockEntity molecularTransformerBlockEntity = (MolecularTransformerBlockEntity) blockEntity;
        if (molecularTransformerBlockEntity.energyRequired == 0) return 0;
        return (int) ((double) molecularTransformerBlockEntity.energyProgress * 17 / molecularTransformerBlockEntity.energyRequired);
    }

    public int getProgressPercent() {
        MolecularTransformerBlockEntity be = (MolecularTransformerBlockEntity) blockEntity;
        if (be.energyRequired == 0) return 0;
        return (int) (be.energyProgress * 100 / be.energyRequired);
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
