package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.MolecularTransformerCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.MolecularTransformerScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class MolecularTransformerBlockEntity extends AbstractMachineEntity {

    public long energyProgress = 0;
    public long energyRequired = 0;
    public long energyInputPerTick = 0;

    public MolecularTransformerBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.MolecularTransformer_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipeOpt = getCurrentRecipe();
        if(recipeOpt.isPresent() && canInsertIntoOutputSlot()) {
            MolecularTransformerCraftingRecipe recipe = (MolecularTransformerCraftingRecipe) recipeOpt.get().value();
            energyRequired = recipe.getEnergyRequired();

            long availableEnergy = Math.min(energyContainer.amount, energyRequired - energyProgress);
            this.energyInputPerTick = availableEnergy;
            if(availableEnergy > 0) {
                energyContainer.getSideStorage(null).extract(availableEnergy, false);
                energyProgress += availableEnergy;
            }

            setChanged(world, pos, state);

            if (energyProgress >= energyRequired) {
                craftItem();
                resetProgress();
            }
        }
    }

    public void resetProgress() {
        this.energyProgress = 0;
        this.energyRequired = 0;
    }

    public int getProgressScaled(int scale) {
        if (energyRequired == 0) return 0;
        return (int) ((energyProgress * scale) / energyRequired);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putLong("molecular_transformer.energy_progress", energyProgress);
        nbt.putLong("molecular_transformer.energy_required", energyRequired);
        nbt.putLong("molecular_transformer.energy_input_per_tick", energyInputPerTick);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        energyProgress = (nbt.contains("molecular_transformer.energy_progress") ? nbt.getLong("molecular_transformer.energy_progress") : energyProgress);
        energyRequired = (nbt.contains("molecular_transformer.energy_required") ? nbt.getLong("molecular_transformer.energy_required") : energyRequired);
        energyInputPerTick = (nbt.contains("molecular_transformer.energy_input_per_tick") ? nbt.getLong("molecular_transformer.energy_input_per_tick") : energyInputPerTick);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.MolecularTransformer);
    }

    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new MolecularTransformerScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
}