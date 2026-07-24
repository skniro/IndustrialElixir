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

public class CoffeeMachineCraftingRecipe extends AbstractMachineCraftingRecipe {

    private final Optional<Ingredient> ingredient2;
    private final Optional<Ingredient> ingredient3;
    private final Identifier requiredFluid;
    private final int requiredFluidAmount;

    public CoffeeMachineCraftingRecipe(Ingredient ingredient, Optional<Ingredient> ingredient2,
                                       Optional<Ingredient> ingredient3, int requiredCount,
                                       ItemStackTemplate output, Identifier requiredFluid, int requiredFluidAmount) {
        super(ingredient, requiredCount, output);
        this.ingredient2 = ingredient2;
        this.ingredient3 = ingredient3;
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.COFFEE_MACHINE.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.COFFEE_MACHINE.type;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public Optional<Ingredient> ingredient2() {
        return ingredient2;
    }

    public Optional<Ingredient> ingredient3() {
        return ingredient3;
    }

    public Identifier requiredFluid() {
        return requiredFluid;
    }

    public int requiredFluidAmount() {
        return requiredFluidAmount;
    }

    public static final MapCodec<CoffeeMachineCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Ingredient.CODEC.optionalFieldOf("ingredient2").forGetter(recipe -> recipe.ingredient2),
                    Ingredient.CODEC.optionalFieldOf("ingredient3").forGetter(recipe -> recipe.ingredient3),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.requiredCount),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    Identifier.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.requiredFluid),
                    Codec.INT.fieldOf("fluid_amount").forGetter(recipe -> recipe.requiredFluidAmount)
            ).apply(inst, CoffeeMachineCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CoffeeMachineCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient2,
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient3,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStackTemplate.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    Identifier.STREAM_CODEC, CoffeeMachineCraftingRecipe::requiredFluid,
                    ByteBufCodecs.INT, CoffeeMachineCraftingRecipe::requiredFluidAmount,
                    CoffeeMachineCraftingRecipe::new);

    public static final RecipeSerializer<CoffeeMachineCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(AlchemyCraftingRecipeInput input, net.minecraft.world.level.Level world) {
        if (world.isClientSide()) {
            return false;
        }

        net.minecraft.world.item.ItemStack stackA = input.getItem(0);
        net.minecraft.world.item.ItemStack stackB = input.size() > 1 ? input.getItem(1) : net.minecraft.world.item.ItemStack.EMPTY;
        net.minecraft.world.item.ItemStack stackC = input.size() > 2 ? input.getItem(2) : net.minecraft.world.item.ItemStack.EMPTY;

        boolean firstMatches = ingredient.test(stackA) && stackA.getCount() >= requiredCount;
        if (!firstMatches) return false;

        if (ingredient2 != null && ingredient2.isPresent()) {
            if (!ingredient2.get().test(stackB)) return false;
        }

        if (ingredient3 != null && ingredient3.isPresent()) {
            if (!ingredient3.get().test(stackC)) return false;
        }

        return true;
    }
}
