package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;

public class BrewReactorCraftingRecipe extends AbstractMachineCraftingRecipe {

    private final Optional<Ingredient> ingredient2;
    private final Optional<Identifier> requiredFluid;
    private final Optional<Integer> requiredFluidAmount;

    public BrewReactorCraftingRecipe(Ingredient ingredients, Optional<Ingredient> ingredient2, int requiredCount, ItemStackTemplate output) {
        super(ingredients, requiredCount, output);
        this.ingredient2 = ingredient2;
        this.requiredFluid = Optional.empty();
        this.requiredFluidAmount = Optional.empty();
    }

    public BrewReactorCraftingRecipe(Ingredient ingredients, Optional<Ingredient> ingredient2, int requiredCount, ItemStackTemplate output, Optional<Identifier> requiredFluid, Optional<Integer> requiredFluidAmount) {
        super(ingredients, requiredCount, output);
        this.ingredient2 = ingredient2;
        this.requiredFluid = requiredFluid != null ? requiredFluid : Optional.empty();
        this.requiredFluidAmount = requiredFluidAmount != null ? requiredFluidAmount : Optional.empty();
    }

    public BrewReactorCraftingRecipe(Ingredient ingredients, Optional<Ingredient> ingredient2, ItemStackTemplate output) {
        super(ingredients, output);
        this.ingredient2 = ingredient2;
        this.requiredFluid = Optional.empty();
        this.requiredFluidAmount = Optional.empty();
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.BREW_REACTOR.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.BREW_REACTOR.type;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final MapCodec<BrewReactorCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient")
                            .forGetter(recipe -> recipe.ingredient),

                    Ingredient.CODEC.optionalFieldOf("ingredient2")
                            .forGetter(recipe -> recipe.ingredient2),

                    Codec.INT.fieldOf("count")
                            .forGetter(recipe -> recipe.requiredCount),

                    ItemStackTemplate.CODEC.fieldOf("result")
                            .forGetter(recipe -> recipe.output),

                    Identifier.CODEC.optionalFieldOf("fluid")
                            .forGetter(recipe -> recipe.requiredFluid),

                    Codec.INT.optionalFieldOf("fluid_amount")
                            .forGetter(recipe -> recipe.requiredFluidAmount)

            ).apply(inst, (ing, ing2Opt, count, out, fluidOpt, fluidAmtOpt) -> new BrewReactorCraftingRecipe(ing, ing2Opt, count, out, fluidOpt, fluidAmtOpt)));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewReactorCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient2,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStackTemplate.STREAM_CODEC,AbstractMachineCraftingRecipe::output,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC), recipe -> recipe.requiredFluid,
                    ByteBufCodecs.optional(ByteBufCodecs.INT), recipe -> recipe.requiredFluidAmount,
                    (ing, ing2, count, out, fluidOpt, fluidAmtOpt) -> new BrewReactorCraftingRecipe(ing, ing2, count, out, fluidOpt, fluidAmtOpt));

    public static final RecipeSerializer<BrewReactorCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public MapCodec<BrewReactorCraftingRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, BrewReactorCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public boolean matches(AlchemyCraftingRecipeInput input, net.minecraft.world.level.Level world) {
        if (world.isClientSide()) {
            return false;
        }

        net.minecraft.world.item.ItemStack stackA = input.getItem(0);
        net.minecraft.world.item.ItemStack stackB = input.getItem(1);
        boolean firstMatches = ingredient.test(stackA) && stackA.getCount() >= requiredCount;
        if (!firstMatches) return false;

        if (this.ingredient2 == null) return true; // safety: but ingredient2 is Optional, keep for legacy

        if (!this.ingredient2.isPresent()) return true;

        return ingredient2.get().test(stackB);
    }

    /**
     * Returns the second ingredient for JEI/GUI consumers. May be null if the recipe does not require a second input.
     */
    public Ingredient ingredient2() {
        return this.ingredient2 != null && this.ingredient2.isPresent() ? this.ingredient2.get() : null;
    }

    public Optional<Identifier> requiredFluid() {
        return this.requiredFluid;
    }

    public Optional<Integer> requiredFluidAmount() {
        return this.requiredFluidAmount;
    }

}
