package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;

public class MetalFormerExtrudingCraftingRecipe extends AbstractMachineCraftingRecipe {

    public MetalFormerExtrudingCraftingRecipe(Ingredient ingredients, int requiredCount, ItemStack output) {
        super(ingredients, requiredCount, output);
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.METALFORMER_EXTRUDING.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.METALFORMER_EXTRUDING.type;
    }



    public static final MapCodec<MetalFormerExtrudingCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient")
                            .forGetter(recipe -> recipe.ingredient),

                    Codec.INT.fieldOf("count")
                            .forGetter(recipe -> recipe.requiredCount),

                    ItemStack.CODEC.fieldOf("result")
                            .forGetter(recipe -> recipe.output)

            ).apply(inst, MetalFormerExtrudingCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MetalFormerExtrudingCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStack.STREAM_CODEC,AbstractMachineCraftingRecipe::output,
                    MetalFormerExtrudingCraftingRecipe::new);

    public static final RecipeSerializer<MetalFormerExtrudingCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);

    public MapCodec<MetalFormerExtrudingCraftingRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, MetalFormerExtrudingCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
