package com.skniro.industrial_elixir.api.block;

import net.minecraft.world.item.crafting.RecipeType;

@FunctionalInterface
public interface MachineRecipeProvider {

    RecipeType<?> getCurrentRecipeType();

}