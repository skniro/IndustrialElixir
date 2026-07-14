package com.skniro.industrial_elixir.screen.handler.generator;


import com.skniro.industrial_elixir.block.entity.generator.GeneratorSolarPanelBlockEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.*;
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

public class GeneratorSolarPanelScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final GeneratorSolarPanelBlockEntity blockEntity;

    public GeneratorSolarPanelScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(pos),
                new SimpleContainerData(4));
    }

    public GeneratorSolarPanelScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData arrayPropertyDelegate) {
        super(AlchemyScreenHandlerType.GENERATOR_Solar_Panel_SCREEN_HANDLER, syncId);
        checkContainerSize(((Container) blockEntity), 1);
        this.inventory = (Container)blockEntity;
        this.blockEntity = (GeneratorSolarPanelBlockEntity) blockEntity;
        this.propertyDelegate = arrayPropertyDelegate;

        AbstractMachineblock machineblock = (AbstractMachineblock) ((GeneratorSolarPanelBlockEntity)blockEntity).getBlockState().getBlock();

        this.addSlot(new BatteryChargeSlot(inventory, 0, 150, 9, machineblock.getEnergyTier()));
        this.addSlot(new BatteryChargeSlot(inventory, 1, 150, 27,machineblock.getEnergyTier()));
        this.addSlot(new BatteryChargeSlot(inventory, 2, 150, 45, machineblock.getEnergyTier()));
        this.addSlot(new BatteryChargeSlot(inventory, 3, 150, 63, machineblock.getEnergyTier()));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    public GeneratorSolarPanelBlockEntity.GeneratorState getState() {
        int ordinal = this.propertyDelegate.get(1);
        return GeneratorSolarPanelBlockEntity.GeneratorState.values()[ordinal];
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