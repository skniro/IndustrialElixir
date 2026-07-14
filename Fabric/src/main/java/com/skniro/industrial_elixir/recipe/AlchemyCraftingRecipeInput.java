package com.skniro.industrial_elixir.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class AlchemyCraftingRecipeInput implements RecipeInput {
    private final ItemStack[] inputs;

    public AlchemyCraftingRecipeInput(ItemStack input) {
        this.inputs = new ItemStack[]{input};
    }

    public AlchemyCraftingRecipeInput(ItemStack... inputs) {
        this.inputs = inputs != null ? inputs : new ItemStack[0];
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= inputs.length) return ItemStack.EMPTY;
        return inputs[slot];
    }

    @Override
    public int size() {
        return inputs.length;
    }
}