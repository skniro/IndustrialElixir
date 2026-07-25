package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.CropFarmCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.CropFarmScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CropFarmBlockEntity extends AbstractMachineEntity {

    public CropFarmBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.CROP_FARM_BLOCK_ENTITY, pos, state);
        this.inventory = NonNullList.withSize(12, ItemStack.EMPTY);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.CropFarm);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return new CropFarmScreenHandler(syncId, inventory, this, propertyDelegate);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.CROP_FARM.type;
    }

    @Override
    public Optional<RecipeHolder<AbstractMachineCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<AbstractMachineCraftingRecipe>) getCurrentRecipeType(),
                        new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT)), this.getLevel());
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> opt = getCurrentRecipe();
        if (opt.isEmpty()) return false;

        var recipeHolder = opt.get();
        var recipe = recipeHolder.value();
        CropFarmCraftingRecipe cropRecipe = (CropFarmCraftingRecipe) recipe;

        // Set maxProgress from the recipe
        this.maxProgress = cropRecipe.processTime();

        ItemStack output = cropRecipe.output().create();
        if (!canInsertIntoSlot(OUTPUT_SLOT, output)) return false;

        if (cropRecipe.output2().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_2, cropRecipe.output2().get().create())) return false;
        if (cropRecipe.output3().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_3, cropRecipe.output3().get().create())) return false;

        return hasEnoughEnergyToCraft();
    }

    @Override
    protected void craftItem() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        CropFarmCraftingRecipe cropRecipe = (CropFarmCraftingRecipe) recipe.get().value();

        insertOutput(OUTPUT_SLOT, cropRecipe.output().create());
        cropRecipe.output2().ifPresent(output -> insertOutput(OUTPUT_SLOT_2, output.create()));
        cropRecipe.output3().ifPresent(output -> insertOutput(OUTPUT_SLOT_3, output.create()));
    }

    private void insertOutput(int slot, ItemStack output) {
        if (output.isEmpty()) return;
        if (this.getItem(slot).isEmpty()) {
            this.setItem(slot, output.copy());
        } else {
            this.getItem(slot).grow(output.getCount());
        }
    }

    private boolean canInsertIntoSlot(int slot, ItemStack output) {
        if (output.isEmpty()) return true;
        ItemStack stack = this.getItem(slot);
        int maxCount = stack.isEmpty() ? output.getMaxStackSize() : stack.getMaxStackSize();
        return (stack.isEmpty() || ItemStack.isSameItemSameComponents(stack, output))
                && maxCount >= stack.getCount() + output.getCount();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2 && slot != OUTPUT_SLOT_3;
    }

    /* SIDED INVENTORY */
    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT, ENERGY_ITEM_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT, OUTPUT_SLOT_2, OUTPUT_SLOT_3};
        }
    }
}
