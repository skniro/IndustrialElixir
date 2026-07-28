package com.skniro.industrial_elixir.screen.handler.generator;

import com.skniro.industrial_elixir.block.entity.generator.NuclearReactorBlockEntity;
import com.skniro.industrial_elixir.item.init.ReactorComponentItem;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryChargeSlot;
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

public class NuclearReactorScreenHandler extends AbstractContainerMenu {
    private static final int REACTOR_GRID_X = 26;
    private static final int REACTOR_GRID_Y = 25;
    private static final int REACTOR_SLOT_SPACING = 18;
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final NuclearReactorBlockEntity blockEntity;

    public NuclearReactorScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()), new SimpleContainerData(4));
    }

    public NuclearReactorScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData propertyDelegate) {
        super(AlchemyScreenHandlerType.NuclearReactor.get(), syncId);
        checkContainerSize((Container) blockEntity, NuclearReactorBlockEntity.REACTOR_SLOT_COUNT + 1);
        this.inventory = (Container) blockEntity;
        this.blockEntity = (NuclearReactorBlockEntity) blockEntity;
        this.propertyDelegate = propertyDelegate;

        for (int row = 0; row < NuclearReactorBlockEntity.GRID_HEIGHT; row++) {
            for (int column = 0; column < NuclearReactorBlockEntity.GRID_WIDTH; column++) {
                int slot = row * NuclearReactorBlockEntity.GRID_WIDTH + column;
                this.addSlot(new ReactorSlot(inventory, slot, REACTOR_GRID_X + column * REACTOR_SLOT_SPACING, REACTOR_GRID_Y + row * REACTOR_SLOT_SPACING));
            }
        }

        this.addSlot(new BatteryChargeSlot(inventory, NuclearReactorBlockEntity.BATTERY_SLOT, 152, 96, com.skniro.industrial_elixir.api.energytier.EnergyTier.TIER4));
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

    public int getScaledHeatWidth() {
        int maxHeat = getMaxHeat();
        return maxHeat > 0 && getHeat() > 0 ? Math.min(54, getHeat() * 54 / maxHeat) : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            int machineSlots = NuclearReactorBlockEntity.REACTOR_SLOT_COUNT + 1;
            if (invSlot < machineSlots) {
                if (!this.moveItemStackTo(originalStack, machineSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.is(ModItemTags.BATTERY)) {
                if (!this.moveItemStackTo(originalStack, NuclearReactorBlockEntity.BATTERY_SLOT, NuclearReactorBlockEntity.BATTERY_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof ReactorComponentItem) {
                if (!this.moveItemStackTo(originalStack, 0, NuclearReactorBlockEntity.REACTOR_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
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
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 26 + l * 18, 161 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 26 + i * 18, 219));
        }
    }

    private static class ReactorSlot extends Slot {
        public ReactorSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof ReactorComponentItem;
        }
    }
}