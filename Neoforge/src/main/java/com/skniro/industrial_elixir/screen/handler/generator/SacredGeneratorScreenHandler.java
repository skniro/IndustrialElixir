package com.skniro.industrial_elixir.screen.handler.generator;

import com.skniro.industrial_elixir.block.entity.generator.GeneratorSolarPanelBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.SacredGeneratorBlockEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.item.init.ReactorComponentItem;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryChargeSlot;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SacredGeneratorScreenHandler extends AbstractContainerMenu {
    private static final int GRID_X = 60;
    private static final int GRID_Y = 15;
    private static final int SLOT_SPACING = 20;
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final SacredGeneratorBlockEntity blockEntity;

    public SacredGeneratorScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(4));
    }

    public SacredGeneratorScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData propertyDelegate) {
        super(AlchemyScreenHandlerType.SACRED_GENERATOR.get(), syncId);
        checkContainerSize((Container) blockEntity, SacredGeneratorBlockEntity.REACTOR_SLOT_COUNT + 5);
        this.inventory = (Container) blockEntity;
        this.blockEntity = (SacredGeneratorBlockEntity) blockEntity;
        this.propertyDelegate = propertyDelegate;
        AbstractMachineblock machineblock = (AbstractMachineblock) ((SacredGeneratorBlockEntity)blockEntity).getBlockState().getBlock();

        this.addSlot(new BatteryChargeSlot(inventory, 10, 150, 9, machineblock.getEnergyTier()));
        this.addSlot(new BatteryChargeSlot(inventory, 11, 150, 27,machineblock.getEnergyTier()));
        this.addSlot(new BatteryChargeSlot(inventory, 12, 150, 45, machineblock.getEnergyTier()));
        this.addSlot(new BatteryChargeSlot(inventory, 13, 150, 63, machineblock.getEnergyTier()));

        for (int row = 0; row < SacredGeneratorBlockEntity.GRID_HEIGHT; row++) {
            for (int column = 0; column < SacredGeneratorBlockEntity.GRID_WIDTH; column++) {
                int slot = row * SacredGeneratorBlockEntity.GRID_WIDTH + column;
                this.addSlot(new Slot(inventory, slot, GRID_X + column * SLOT_SPACING, GRID_Y + row * SLOT_SPACING) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof ReactorComponentItem;
                    }
                });
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    public int getHeat() {
        return propertyDelegate.get(0);
    }

    public int getMaxHeat() {
        return propertyDelegate.get(1);
    }

    public int getGeneration() {
        return propertyDelegate.get(2);
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


