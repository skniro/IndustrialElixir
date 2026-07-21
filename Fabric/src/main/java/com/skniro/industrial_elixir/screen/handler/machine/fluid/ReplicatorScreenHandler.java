package com.skniro.industrial_elixir.screen.handler.machine.fluid;

import com.skniro.industrial_elixir.block.entity.machine.fluid.ReplicatorBlockEntity;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryFuelSlot;
import com.skniro.industrial_elixir.screen.slot.UpgradeSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ReplicatorScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final ReplicatorBlockEntity blockEntity;
    final ContainerData propertyDelegate;

    public static final int BUTTON_STOP = 0;
    public static final int BUTTON_SINGLE = 1;
    public static final int BUTTON_LOOP = 2;

    public ReplicatorScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(pos), new SimpleContainerData(2));
    }

    public ReplicatorScreenHandler(int syncId, Inventory playerInventory, BlockEntity entity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.Replicator, syncId);
        blockEntity = (ReplicatorBlockEntity) entity;
        checkContainerSize(blockEntity, 12);
        this.propertyDelegate = delegate;
        this.inventory = blockEntity;
        inventory.startOpen(playerInventory.player);

        // Same layout as BrewReactor, but slot 1 = crystal, removing slot 9 second input

        // Slot 0: Fluid container input (bucket/cell with UU matter)
        this.addSlot(new Slot(inventory, 0, 8, 58));

        // Slot 1: Pattern Storage Crystal (replaces BrewReactor's material input)
        this.addSlot(new Slot(inventory, 1, 53, 21) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL);
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        // Slot 2: Output (replicated item)
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 2, 107, 22));

        // Slot 3: Battery / Energy
        this.addSlot(new BatteryFuelSlot(inventory, 3, 131, 63, this.blockEntity.getEnergyTier()));

        // Slot 8: Empty fluid container return (like BrewReactor)
        this.addSlot(new Slot(inventory, 8, 26, 58));

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
        return blockEntity.getMode() != ReplicatorBlockEntity.Mode.STOP && (blockEntity.getEnergyProgress() > 0 || blockEntity.getFluidProgress() > 0);
    }

    public int getScaledProgress() {
        int percent = blockEntity.getOverallProgressPercent();
        return percent * 11 / 100; // 11px wide arrow (same as BrewReactor)
    }

    public int getScaledEnergyHeight() {
        long energy = blockEntity.energyContainer.amount;
        long capacity = blockEntity.energyContainer.getCapacity();
        int energyBarSize = 16; // Same as BrewReactor
        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (!player.level().isClientSide()) {
            switch (id) {
                case BUTTON_STOP -> this.blockEntity.setMode(ReplicatorBlockEntity.Mode.STOP);
                case BUTTON_SINGLE -> this.blockEntity.setMode(ReplicatorBlockEntity.Mode.SINGLE);
                case BUTTON_LOOP -> this.blockEntity.setMode(ReplicatorBlockEntity.Mode.LOOP);
            }
        }
        return true;
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
