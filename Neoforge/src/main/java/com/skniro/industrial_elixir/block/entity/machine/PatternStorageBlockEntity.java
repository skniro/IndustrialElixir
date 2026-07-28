package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.api.item.replicator.ReplicatorCost;
import com.skniro.industrial_elixir.api.item.replicator.ReplicatorValueMap;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.handler.machine.PatternStorageScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class PatternStorageBlockEntity extends AbstractMachineEntity {
    private static final int SCAN_ENERGY_PER_TICK = 64;
    private static final int SCAN_MAX_PROGRESS = 120;

    // Scanned pattern data
    @Nullable
    private Identifier scannedItemId;
    private int uuCost = 0;
    private int energyCost = 0;

    // Copy cooldown to prevent rapid clicks
    private int copyCooldown = 0;

    public PatternStorageBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.PATTERN_STORAGE_BE.get(), pos, state);
        this.maxProgress = SCAN_MAX_PROGRESS;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Pattern_Storage);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new PatternStorageScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    // Check if slot 1 contains a blank crystal (no pattern data)
    private boolean hasBlankCrystalInSlot() {
        ItemStack stack = inventory.get(INPUT_SLOT);
        if (!stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get())) return false;
        return !hasPatternData(stack);
    }

    // Check if a crystal has pattern data stored
    public static boolean hasPatternData(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.copyTag().contains("pattern_item");
    }

    // Read pattern data from a crystal
    @Nullable
    public static Identifier getPatternItem(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        return tag.getString("pattern_item")
                .map(Identifier::tryParse)
                .orElse(null);
    }

    // Read UU cost from a crystal
    public static int getPatternUUCost(ItemStack stack) {
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();;
        if (custom.contains("uu_cost")) {
            return custom.getInt("uu_cost").orElse(0);
        }
        return 0;
    }

    // Read energy cost from a crystal
    public static int getPatternEnergyCost(ItemStack stack) {
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();;
        if (custom.contains("energy_cost")) {
            return custom.getInt("energy_cost").orElse(0);
        }
        return 0;
    }

    public static void writePatternData(ItemStack stack, Identifier itemId, int uu, int energy) {
        stack.update(
                DataComponents.CUSTOM_DATA,
                CustomData.EMPTY,
                customData -> {
                    CompoundTag tag = customData.copyTag();

                    tag.putString("pattern_item", itemId.toString());
                    tag.putInt("uu_cost", uu);
                    tag.putInt("energy_cost", energy);

                    return CustomData.of(tag);
                }
        );
    }

    // Calculate matter costs - first check the ReplicatorValueMap, fallback to stack-size-based formula
    private void calculateCosts(ItemStack scannedItem) {
        Item item = scannedItem.getItem();

        ReplicatorCost registered = ReplicatorValueMap.get(item);
        if (registered != null) {
            this.uuCost = registered.uuCost();
            this.energyCost = (int) registered.energyCost();
        }
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;

        charge(ENERGY_ITEM_SLOT);

        // Update scanned item from crystal in slot 1 for display
        ItemStack crystalSlot = inventory.get(INPUT_SLOT);
        if (!crystalSlot.isEmpty() && hasPatternData(crystalSlot)) {
            Identifier stored = getPatternItem(crystalSlot);
            if (stored != null && !stored.equals(this.scannedItemId)) {
                this.scannedItemId = stored;
                this.uuCost = getPatternUUCost(crystalSlot);
                this.energyCost = getPatternEnergyCost(crystalSlot);
            }
        }

        // Handle scanning process
        ItemStack scanItem = inventory.get(SECOND_INPUT_SLOT);
        boolean canScan = hasBlankCrystalInSlot()
                && !scanItem.isEmpty()
                && !scanItem.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get())
                && hasEnoughEnergyToCraft();

        if (canScan) {
            increaseCraftingProgress();
            useScanEnergy();
            setChanged(world, pos, state);

            if (hasCraftingFinished()) {
                // Complete the scan
                Identifier itemId = BuiltInRegistries.ITEM.getKey(scanItem.getItem());
                calculateCosts(scanItem);
                writePatternData(crystalSlot, itemId, uuCost, energyCost);
                this.scannedItemId = itemId;

                // Consume scanned item
                scanItem.shrink(1);
                resetProgress();
            }
        } else if (!canScan && progress > 0) {
            resetProgress();
        }

        // Update copy cooldown
        if (copyCooldown > 0) {
            copyCooldown--;
        }

        boolean isWorking = progress > 0 && canScan;
        if (state.getValue(AbstractMachineblock.LIT) != isWorking) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, isWorking), 3);
        }

        setChanged(world, pos, state);
    }

    // Called from screen handler when copy button is pressed
    public void copyPattern() {
        if (copyCooldown > 0) return;

        ItemStack sourceCrystal = inventory.get(INPUT_SLOT);
        ItemStack targetCrystal = inventory.get(OUTPUT_SLOT_2);

        if (sourceCrystal.isEmpty() || !hasPatternData(sourceCrystal)) return;
        if (targetCrystal.isEmpty() || !targetCrystal.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get())) return;
        if (hasPatternData(targetCrystal)) return; // Target already has data

        Identifier patternItem = getPatternItem(sourceCrystal);
        int uu = getPatternUUCost(sourceCrystal);
        int energy = getPatternEnergyCost(sourceCrystal);

        if (patternItem != null) {
            writePatternData(targetCrystal, patternItem, uu, energy);
            copyCooldown = 10;
            setChanged();
        }
    }

    private void useScanEnergy() {
        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx =
                     net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            long effectiveUse = (long) (SCAN_ENERGY_PER_TICK * getScanEnergyMultiplier());
            energyContainer.getSideStorage(null).extract(effectiveUse, tx);
            tx.commit();
        }
    }

    private double getScanEnergyMultiplier() {
        double multiplier = 1.0;
        for (int i = UPGRADE_START; i <= UPGRADE_END; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof com.skniro.industrial_elixir.item.init.ItemUpgradeModule u) {
                multiplier = u.getEnergyDemandMultiplier(stack);
            }
        }
        return multiplier;
    }

    // ---- Getters for display ----
    @Nullable
    public Identifier getScannedItemId() {
        return scannedItemId;
    }

    public int getUUCost() {
        return uuCost;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    // ---- Save/Load ----
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (scannedItemId != null) {
            output.putString("pattern.scanned_item", scannedItemId.toString());
        }
        output.putInt("pattern.uu_cost", uuCost);
        output.putInt("pattern.energy_cost", energyCost);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        String id = input.getStringOr("pattern.scanned_item", null);
        this.scannedItemId = id != null ? Identifier.parse(id) : null;
        this.uuCost = input.getIntOr("pattern.uu_cost", 0);
        this.energyCost = input.getIntOr("pattern.energy_cost", 0);
    }

    // ---- Sided I/O ----
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT) return stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get());
        if (slot == SECOND_INPUT_SLOT) return !stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get());
        if (slot == OUTPUT_SLOT_2) return stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get());
        if (slot == ENERGY_ITEM_SLOT) return true;
        return slot >= UPGRADE_START && slot <= UPGRADE_END;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if (slot == INPUT_SLOT && hasPatternData(stack)) {
            // Allow pulling patterned crystals from the input slot
            return true;
        }
        return super.canTakeItemThroughFace(slot, stack, side);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT, SECOND_INPUT_SLOT, OUTPUT_SLOT_2};
        } else {
            return new int[]{INPUT_SLOT};
        }
    }
}
