package com.skniro.industrial_elixir.recipe.machine;


import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;


public abstract class AbstractMachineCraftingRecipe implements Recipe<AlchemyCraftingRecipeInput> {
    protected final ItemStack output;
    protected final Ingredient ingredient;
    protected final int requiredCount;


    public AbstractMachineCraftingRecipe(Ingredient ingredients, int requiredCount, ItemStack output) {
        this.output = output;
        this.ingredient = ingredients;
        this.requiredCount = requiredCount;
    }

    public AbstractMachineCraftingRecipe(Ingredient ingredients, ItemStack output) {
        this.output = output;
        this.ingredient = ingredients;
        this.requiredCount = 1;
    }

    public int requiredCount() {
        return requiredCount;
    }

    public Ingredient ingredient() {
        return this.ingredient;
    }

    public ItemStack output() {
        return this.output;
    }

    @Override
    public boolean matches(AlchemyCraftingRecipeInput input, Level world) {
        if (world.isClientSide()) {
            return false;
        }
        ItemStack stack = input.getItem(0);
        return ingredient.test(stack) && stack.getCount() >= requiredCount;
    }

    @Override
    public ItemStack assemble(AlchemyCraftingRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String getGroup() {
        return "";
    }
}









