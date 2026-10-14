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

public class MolecularTransformerCraftingRecipe extends AbstractMachineCraftingRecipe {

    private final long energyRequired;

    public MolecularTransformerCraftingRecipe(Ingredient ingredients, ItemStack output, long energyRequired) {
        super(ingredients, output);
        this.energyRequired = energyRequired;
    }

    public long getEnergyRequired() {
        return this.energyRequired;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.MOLECULAR_TRANSFORMER.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.MOLECULAR_TRANSFORMER.type;
    }



    public static final MapCodec<MolecularTransformerCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    Codec.LONG.fieldOf("energy").forGetter(recipe -> recipe.energyRequired)
            ).apply(inst, MolecularTransformerCraftingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MolecularTransformerCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, MolecularTransformerCraftingRecipe::ingredient,
                    ItemStack.STREAM_CODEC, MolecularTransformerCraftingRecipe::output,
                    ByteBufCodecs.VAR_LONG, MolecularTransformerCraftingRecipe::getEnergyRequired,
                    MolecularTransformerCraftingRecipe::new
            );

    public static final RecipeSerializer<MolecularTransformerCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);

    public MapCodec<MolecularTransformerCraftingRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, MolecularTransformerCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}