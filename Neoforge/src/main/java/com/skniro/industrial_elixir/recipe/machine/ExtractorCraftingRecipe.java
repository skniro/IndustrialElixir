package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;

public class ExtractorCraftingRecipe extends AbstractMachineCraftingRecipe {

    public ExtractorCraftingRecipe(Ingredient ingredients, ItemStack output) {
        super(ingredients, output);
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.EXTRACTOR.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.EXTRACTOR.type.get();
    }


    public static final MapCodec<ExtractorCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> {
                return recipe.ingredient;
            }),
            ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> {
                return recipe.output;
            })
    ).apply(inst, ExtractorCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtractorCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ItemStack.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    ExtractorCraftingRecipe::new);

    public static final RecipeSerializer<ExtractorCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);

    public MapCodec<ExtractorCraftingRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, ExtractorCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
