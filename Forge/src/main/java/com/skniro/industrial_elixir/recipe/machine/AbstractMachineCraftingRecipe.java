package com.skniro.industrial_elixir.recipe.machine;


import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;


public abstract class AbstractMachineCraftingRecipe implements Recipe<AlchemyCraftingRecipeInput> {
    protected final ItemStackTemplate output;
    protected final Ingredient ingredient;
    protected final int requiredCount;
    @Nullable
    private PlacementInfo ingredientPlacement;


    public AbstractMachineCraftingRecipe(Ingredient ingredients, int requiredCount, ItemStackTemplate output) {
        this.output = output;
        this.ingredient = ingredients;
        this.requiredCount = requiredCount;
    }

    public AbstractMachineCraftingRecipe(Ingredient ingredients, ItemStackTemplate output) {
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

    public ItemStackTemplate output() {
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
    public ItemStack assemble(AlchemyCraftingRecipeInput input) {
        return output.create();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = PlacementInfo.create(this.ingredient);
        }

        return this.ingredientPlacement;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }
}









