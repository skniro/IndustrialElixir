package com.skniro.industrial_elixir.screen.handler.generator.heat;

import com.skniro.industrial_elixir.block.entity.generator.heat.ElectricHeaterBlockEntity;
import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryFuelSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class ElectricHeaterScreenHandler extends AbstractContainerMenu {
    final Container inventory;
    final ContainerData propertyDelegate;
    public final ElectricHeaterBlockEntity blockEntity;;

    public ElectricHeaterScreenHandler(int syncId, Inventory playerInventory, BlockPos pos){
        this(syncId,playerInventory, playerInventory.player.level().getBlockEntity(pos),new SimpleContainerData(2));
    }


    public ElectricHeaterScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.ElectricHeater, syncId);
        checkContainerSize((Container) blockEntity,21);
        this.inventory = (Container) blockEntity;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.blockEntity = (ElectricHeaterBlockEntity) blockEntity;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 5; col++) {
                this.addSlot(new Slot(inventory, 10 + row * 5 + col, 44 + col * 18, 27 + row * 18));
            }
        }
        this.addSlot(new BatteryFuelSlot(inventory, 3, 8, 62, ((ElectricHeaterBlockEntity) blockEntity).getEnergyTier()));

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

    public Component getHeatTooltips() {
        return Component.literal(blockEntity.getHeatProduction() + " HU/t / 100 HU/t");
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
