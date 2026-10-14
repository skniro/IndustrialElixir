package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;

public class CoffeeMachineCraftingRecipe extends AbstractMachineCraftingRecipe {

    private final Optional<Ingredient> ingredient2;
    private final Optional<Ingredient> ingredient3;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;

    public CoffeeMachineCraftingRecipe(Ingredient ingredient, Optional<Ingredient> ingredient2,
                                       Optional<Ingredient> ingredient3, int requiredCount,
                                       ItemStack output, ResourceLocation requiredFluid, int requiredFluidAmount) {
        super(ingredient, requiredCount, output);
        this.ingredient2 = ingredient2;
        this.ingredient3 = ingredient3;
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.COFFEE_MACHINE.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.COFFEE_MACHINE.type.get();
    }


    public Optional<Ingredient> ingredient2() {
        return ingredient2;
    }

    public Optional<Ingredient> ingredient3() {
        return ingredient3;
    }

    public ResourceLocation requiredFluid() {
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
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    ResourceLocation.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.requiredFluid),
                    Codec.INT.fieldOf("fluid_amount").forGetter(recipe -> recipe.requiredFluidAmount)
            ).apply(inst, CoffeeMachineCraftingRecipe::new));

    /**
     * 1.21.1's {@link StreamCodec#composite} only goes up to six components, so the
     * first six fields are packed into a small carrier record and the fluid amount
     * is appended afterwards.
     */
    private record Base(Ingredient ingredient, Optional<Ingredient> ingredient2,
                        Optional<Ingredient> ingredient3, int requiredCount,
                        ItemStack output, ResourceLocation requiredFluid) {
    }

    private static final StreamCodec<RegistryFriendlyByteBuf, Base> BASE_STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, Base::ingredient,
                    ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), Base::ingredient2,
                    ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), Base::ingredient3,
                    ByteBufCodecs.INT, Base::requiredCount,
                    ItemStack.STREAM_CODEC, Base::output,
                    ResourceLocation.STREAM_CODEC, Base::requiredFluid,
                    Base::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, CoffeeMachineCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    BASE_STREAM_CODEC,
                    recipe -> new Base(recipe.ingredient, recipe.ingredient2, recipe.ingredient3,
                            recipe.requiredCount, recipe.output, recipe.requiredFluid),
                    ByteBufCodecs.INT, CoffeeMachineCraftingRecipe::requiredFluidAmount,
                    (base, amount) -> new CoffeeMachineCraftingRecipe(base.ingredient(), base.ingredient2(),
                            base.ingredient3(), base.requiredCount(), base.output(),
                            base.requiredFluid(), amount));

    public static final RecipeSerializer<CoffeeMachineCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);

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
