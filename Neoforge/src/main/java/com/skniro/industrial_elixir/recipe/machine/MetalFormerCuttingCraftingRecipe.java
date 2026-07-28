package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MetalFormerCuttingCraftingRecipe extends AbstractMachineCraftingRecipe {

    public MetalFormerCuttingCraftingRecipe(Ingredient ingredients, int requiredCount, ItemStackTemplate output) {
        super(ingredients, requiredCount, output);
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.METALFORMER_CUTTING.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.METALFORMER_CUTTING.type.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

        public static final MapCodec<MetalFormerCuttingCraftingRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst -> inst.group(
                        Ingredient.CODEC.fieldOf("ingredient")
                                .forGetter(recipe -> recipe.ingredient),

                        Codec.INT.fieldOf("count")
                                .forGetter(recipe -> recipe.requiredCount),

                        ItemStackTemplate.CODEC.fieldOf("result")
                                .forGetter(recipe -> recipe.output)

                ).apply(inst, MetalFormerCuttingCraftingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MetalFormerCuttingCraftingRecipe> STREAM_CODEC =
                StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                        ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                        ItemStackTemplate.STREAM_CODEC,AbstractMachineCraftingRecipe::output,
                        MetalFormerCuttingCraftingRecipe::new);

    public static final RecipeSerializer<MetalFormerCuttingCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);


        public MapCodec<MetalFormerCuttingCraftingRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, MetalFormerCuttingCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

}
