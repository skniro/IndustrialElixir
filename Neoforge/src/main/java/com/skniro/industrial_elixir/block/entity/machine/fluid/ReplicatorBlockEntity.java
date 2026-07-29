package com.skniro.industrial_elixir.block.entity.machine.fluid;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.block.entity.machine.PatternStorageBlockEntity;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.ReplicatorScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class ReplicatorBlockEntity extends AbstractFluidMachineEntity {
    public enum Mode {
        STOP,
        SINGLE,
        LOOP
    }

    private Mode mode = Mode.STOP;

    // Synthesis progress
    private long energyProgress = 0;
    private int fluidProgress = 0;

    // Pattern data from crystal
    @Nullable private Identifier replicatingItemId;
    private long replicatingEnergyCost = 0;
    private int replicatingUUCost = 0;

    // Cooldown to prevent continuous rapid checks
    private int tickCounter = 0;

    public ReplicatorBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.REPLICATOR_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Replicator);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return null; // No recipe - uses crystal pattern data
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ReplicatorScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    // ---- Mode switching (called from networking) ----
    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.STOP) {
            // Reset progress when stopping
            this.energyProgress = 0;
            this.fluidProgress = 0;
        }
        setChanged();
    }

    public Mode getMode() {
        return mode;
    }

    // ---- Read pattern from crystal ----
    private void updatePatternFromCrystal() {
        ItemStack crystal = inventory.get(INPUT_SLOT);
        if (crystal.isEmpty() || !crystal.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get())) {
            this.replicatingItemId = null;
            return;
        }
        if (!PatternStorageBlockEntity.hasPatternData(crystal)) {
            this.replicatingItemId = null;
            return;
        }
        Identifier itemId = PatternStorageBlockEntity.getPatternItem(crystal);
        int uuCost = PatternStorageBlockEntity.getPatternUUCost(crystal);
        int energyCost = PatternStorageBlockEntity.getPatternEnergyCost(crystal);
        if (itemId != null) {
            this.replicatingItemId = itemId;
            this.replicatingUUCost = uuCost;
            this.replicatingEnergyCost = energyCost;
        }
    }

    private boolean canProduceOutput() {
        ItemStack outputSlot = inventory.get(OUTPUT_SLOT);
        if (replicatingItemId == null) return false;
        Item targetItem = BuiltInRegistries.ITEM.getOptional(replicatingItemId).orElse(null);
        if (targetItem == null) return false;
        if (outputSlot.isEmpty()) return true;
        if (!outputSlot.is(targetItem)) return false;
        return outputSlot.getCount() < outputSlot.getMaxStackSize();
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;

        tickCounter++;

        // Charge battery in energy slot
        charge(ENERGY_ITEM_SLOT);

        // Fill fluid tank from fluid containers
        if (hasFluidStackInFluidSlot()) {
            fillUpFluidTank();
        }

        // Update pattern data from crystal periodically
        if (tickCounter % 20 == 0) {
            updatePatternFromCrystal();
        }

        boolean working = false;

        if (mode != Mode.STOP && replicatingItemId != null && canProduceOutput()) {
            // Pull energy continuously
            if (energyProgress < replicatingEnergyCost) {
                long needed = replicatingEnergyCost - energyProgress;
                long toExtract = Math.min(energyContainer.amount, needed);
                toExtract = Math.min(toExtract, getEffectiveTier().getMaxInput());
                if (toExtract > 0) {
                    try (Transaction tx = Transaction.openRoot()) {
                        long extracted = energyContainer.getSideStorage(null).extract(toExtract, tx);
                        if (extracted > 0) {
                            tx.commit();
                            energyProgress += extracted;
                            working = true;
                        }
                    }
                }
            }

            // Pull UU fluid continuously (rate-limited by tier)
            if (fluidProgress < replicatingUUCost) {
                int needed = replicatingUUCost - fluidProgress;
                int toExtract = Math.min(needed, 5);
                try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                    long extracted = fluidContainer.extract(
                            FluidVariant.of(IndustrialElixirFluids.STILL_Fluid_UU.get()), toExtract, tx);
                    if (extracted > 0) {
                        tx.commit();
                        fluidProgress += (int) extracted;
                        working = true;
                    }
                }
            }

            // Check if synthesis is complete
            if (energyProgress >= replicatingEnergyCost
                    && fluidProgress >= replicatingUUCost) {

                if (produceReplicatedItem()) {

                    if (mode == Mode.SINGLE) {
                        mode = Mode.STOP;
                    }

                    // LOOP continues automatically
                    // because progress was reset
                }
            }
        }

        if (state.getValue(AbstractMachineblock.LIT) != working) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, working), 3);
        }

        if (tickCounter % 10 == 0) {
            setChanged(world, pos, state);
        }
    }

    private boolean produceReplicatedItem() {
        if (replicatingItemId == null) {
            return false;
        }

        Item targetItem = BuiltInRegistries.ITEM
                .getOptional(replicatingItemId)
                .orElse(null);

        if (targetItem == null) {
            return false;
        }

        ItemStack output = inventory.get(OUTPUT_SLOT);

        if (output.isEmpty()) {
            inventory.set(
                    OUTPUT_SLOT,
                    new ItemStack(targetItem, 1)
            );
        } else {
            if (!output.is(targetItem)) {
                return false;
            }

            if (output.getCount() >= output.getMaxStackSize()) {
                return false;
            }

            output.grow(1);
        }

        energyProgress = 0;
        fluidProgress = 0;

        setChanged();

        return true;
    }

    // ---- Getters for display ----
    public long getEnergyProgress() { return energyProgress; }
    public long getReplicatingEnergyCost() { return replicatingEnergyCost; }
    public int getFluidProgress() { return fluidProgress; }
    public int getReplicatingUUCost() { return replicatingUUCost; }
    @Nullable public Identifier getReplicatingItemId() { return replicatingItemId; }

    public int getOverallProgressPercent() {
        if (replicatingEnergyCost == 0 && replicatingUUCost == 0) return 0;
        int ep = replicatingEnergyCost == 0 ? 100 : (int) (energyProgress * 100 / replicatingEnergyCost);
        int fp = replicatingUUCost == 0 ? 100 : fluidProgress * 100 / replicatingUUCost;
        return Math.min(ep, fp);
    }

    // ---- Save/Load ----
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("replicator.mode", mode.ordinal());
        output.putLong("replicator.energy_progress", energyProgress);
        output.putInt("replicator.fluid_progress", fluidProgress);
        if (replicatingItemId != null) {
            output.putString("replicator.item_id", replicatingItemId.toString());
        }
        output.putLong("replicator.energy_cost", replicatingEnergyCost);
        output.putInt("replicator.uu_cost", replicatingUUCost);
        SingleVariantStorage.writeValue(fluidContainer, FluidVariant.CODEC, output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        int modeOrdinal = input.getIntOr("replicator.mode", 0);
        this.mode = Mode.values()[Math.min(modeOrdinal, Mode.values().length - 1)];
        energyProgress = input.getLongOr("replicator.energy_progress", 0);
        fluidProgress = input.getIntOr("replicator.fluid_progress", 0);
        String itemId = input.getStringOr("replicator.item_id", null);
        this.replicatingItemId = itemId != null ? Identifier.parse(itemId) : null;
        replicatingEnergyCost = input.getLongOr("replicator.energy_cost", 0);
        replicatingUUCost = input.getIntOr("replicator.uu_cost", 0);
        SingleVariantStorage.readValue(fluidContainer, FluidVariant.CODEC, FluidVariant::blank, input);
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT) return stack.is(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get());
        if (slot == FLUID_ITEM_SLOT) return true; // Fluid containers (UU matter)
        if (slot == ENERGY_ITEM_SLOT) return true;
        return slot >= UPGRADE_START && slot <= UPGRADE_END;
    }

    @Override
    public int[] getSlotsForFace(net.minecraft.core.Direction direction) {
        if (direction != net.minecraft.core.Direction.DOWN) {
            return new int[]{INPUT_SLOT, FLUID_ITEM_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT, EMPTY_FLUID_ITEM_SLOT};
        }
    }
}
