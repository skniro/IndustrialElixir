package com.skniro.industrial_elixir.screen.handler.machine;

import com.skniro.industrial_elixir.block.entity.machine.PatternStorageBlockEntity;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.slot.BatteryFuelSlot;
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

public class PatternStorageScreenHandler extends AbstractContainerMenu {
    final Container inventory;
    final ContainerData propertyDelegate;
    public final PatternStorageBlockEntity blockEntity;

    // Button ID for copy action
    public static final int COPY_BUTTON_ID = 0;

    // Client-side constructor
    public PatternStorageScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(pos), new SimpleContainerData(2));
    }

    // Server-side constructor
    public PatternStorageScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData delegate) {
        super(AlchemyScreenHandlerType.PatternStorage, syncId);
        checkContainerSize((Container) blockEntity, 12);
        this.inventory = (Container) blockEntity;
        this.blockEntity = (PatternStorageBlockEntity) blockEntity;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = delegate;

        // Slot 1: Crystal input (left side - blank crystal goes here)
        this.addSlot(new Slot(inventory, 1, 74, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL);
            }
        });

        // Slot 9: Item to scan (center)
        this.addSlot(new Slot(inventory, 9, 23, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return !stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL);
            }
        });

        // Slot 3: Energy / Battery (bottom-right area)
        this.addSlot(new BatteryFuelSlot(inventory, 3, 111, 34, this.blockEntity.getEnergyTier()));

        // Slot 10: Copy target crystal (right side, for copying patterns)
        this.addSlot(new Slot(inventory, 10, 114, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL)
                        && !PatternStorageBlockEntity.hasPatternData(stack);
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addUpgradeSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    private void addUpgradeSlots() {
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
        int progressArrowSize = 22;
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledEnergyHeight() {
        long energy = blockEntity.energyContainer.amount;
        long capacity = blockEntity.energyContainer.getCapacity();
        int energyBarSize = 14;
        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    // Button handling for copy action
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == COPY_BUTTON_ID) {
            if (!player.level().isClientSide()) {
                this.blockEntity.copyPattern();
            }
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < 8) {
                // From machine to player inventory
                if (!this.moveItemStackTo(originalStack, 8, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player to machine
                // Try crystal slot first
                if (originalStack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL)) {
                    if (!PatternStorageBlockEntity.hasPatternData(originalStack)) {
                        // Blank crystal -> slot 1 (crystal slot) or slot 10 (copy target)
                        if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                            // Slot 0 is crystal, slot 3 is copy target (index 3 in our slots)
                            if (!this.moveItemStackTo(originalStack, 3, 4, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                } else {
                    // Non-crystal -> slot 1 (scan item, index 1)
                    if (!this.moveItemStackTo(originalStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
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
