package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.MolecularTransformerCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.MolecularTransformerScreenHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
                try (Transaction tx = Transaction.openRoot()) {
                    energyContainer.getSideStorage(null).extract(availableEnergy, tx);
                    tx.commit();
                }
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
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putLong("molecular_transformer.energy_progress", energyProgress);
        nbt.putLong("molecular_transformer.energy_required", energyRequired);
        nbt.putLong("molecular_transformer.energy_input_per_tick", energyInputPerTick);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        energyProgress = nbt.getLongOr("molecular_transformer.energy_progress", energyProgress);
        energyRequired = nbt.getLongOr("molecular_transformer.energy_required", energyRequired);
        energyInputPerTick = nbt.getLongOr("molecular_transformer.energy_input_per_tick", energyInputPerTick);
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