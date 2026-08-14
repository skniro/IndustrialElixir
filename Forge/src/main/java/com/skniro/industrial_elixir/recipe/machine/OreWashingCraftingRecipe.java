package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

public class OreWashingCraftingRecipe extends AbstractMachineCraftingRecipe {
    private final Optional<ItemStackTemplate> output2;
    private final Optional<ItemStackTemplate> output3;
    private final Identifier requiredFluid;
    private final int requiredFluidAmount;

    public OreWashingCraftingRecipe(Ingredient ingredient, int requiredCount, ItemStackTemplate output,
                                    Optional<ItemStackTemplate> output2, Optional<ItemStackTemplate> output3,
                                    Identifier requiredFluid, int requiredFluidAmount) {
        super(ingredient, requiredCount, output);
        this.output2 = output2 != null ? output2 : Optional.empty();
        this.output3 = output3 != null ? output3 : Optional.empty();
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.ORE_WASHING.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.ORE_WASHING.type.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public Optional<ItemStackTemplate> output2() {
        return output2;
    }

    public Optional<ItemStackTemplate> output3() {
        return output3;
    }

    public Identifier requiredFluid() {
        return requiredFluid;
    }

    public int requiredFluidAmount() {
        return requiredFluidAmount;
    }

    public static final MapCodec<OreWashingCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.requiredCount),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    ItemStackTemplate.CODEC.optionalFieldOf("result2").forGetter(recipe -> recipe.output2),
                    ItemStackTemplate.CODEC.optionalFieldOf("result3").forGetter(recipe -> recipe.output3),
                    Identifier.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.requiredFluid),
                    Codec.INT.fieldOf("fluid_amount").forGetter(recipe -> recipe.requiredFluidAmount)
            ).apply(inst, OreWashingCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, OreWashingCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStackTemplate.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), OreWashingCraftingRecipe::output2,
                    ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), OreWashingCraftingRecipe::output3,
                    Identifier.STREAM_CODEC, OreWashingCraftingRecipe::requiredFluid,
                    ByteBufCodecs.INT, OreWashingCraftingRecipe::requiredFluidAmount,
                    OreWashingCraftingRecipe::new);

    public static final RecipeSerializer<OreWashingCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);
}
