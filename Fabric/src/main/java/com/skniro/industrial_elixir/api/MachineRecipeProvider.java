package com.skniro.industrial_elixir.api;

import net.minecraft.world.item.crafting.RecipeType;

@FunctionalInterface
public interface MachineRecipeProvider {

    RecipeType<?> getCurrentRecipeType();

}