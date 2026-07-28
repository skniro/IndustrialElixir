package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MaceratorCraftingRecipe extends AbstractMachineCraftingRecipe {

    public MaceratorCraftingRecipe(Ingredient ingredients, ItemStackTemplate output) {
        super(ingredients, output);
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.MACERATOR.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.MACERATOR.type;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final MapCodec<MaceratorCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> {
                return recipe.ingredient;
            }),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter((recipe) -> {
                return recipe.output;
            })
    ).apply(inst, MaceratorCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MaceratorCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ItemStackTemplate.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    MaceratorCraftingRecipe::new);

    public static final RecipeSerializer<MaceratorCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);


    public MapCodec<MaceratorCraftingRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, MaceratorCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
