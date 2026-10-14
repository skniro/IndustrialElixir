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

public class OreWashingCraftingRecipe extends AbstractMachineCraftingRecipe {
    private final Optional<ItemStack> output2;
    private final Optional<ItemStack> output3;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;

    public OreWashingCraftingRecipe(Ingredient ingredient, int requiredCount, ItemStack output,
                                    Optional<ItemStack> output2, Optional<ItemStack> output3,
                                    ResourceLocation requiredFluid, int requiredFluidAmount) {
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


    public Optional<ItemStack> output2() {
        return output2;
    }

    public Optional<ItemStack> output3() {
        return output3;
    }

    public ResourceLocation requiredFluid() {
        return requiredFluid;
    }

    public int requiredFluidAmount() {
        return requiredFluidAmount;
    }

    public static final MapCodec<OreWashingCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.requiredCount),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    ItemStack.CODEC.optionalFieldOf("result2").forGetter(recipe -> recipe.output2),
                    ItemStack.CODEC.optionalFieldOf("result3").forGetter(recipe -> recipe.output3),
                    ResourceLocation.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.requiredFluid),
                    Codec.INT.fieldOf("fluid_amount").forGetter(recipe -> recipe.requiredFluidAmount)
            ).apply(inst, OreWashingCraftingRecipe::new));

    /**
     * 1.21.1's {@link StreamCodec#composite} only goes up to six components, so the
     * first six fields are packed into a small carrier record and the fluid amount
     * is appended afterwards.
     */
    private record Base(Ingredient ingredient, int requiredCount, ItemStack output,
                        Optional<ItemStack> output2, Optional<ItemStack> output3,
                        ResourceLocation requiredFluid) {
    }

    private static final StreamCodec<RegistryFriendlyByteBuf, Base> BASE_STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, Base::ingredient,
                    ByteBufCodecs.INT, Base::requiredCount,
                    ItemStack.STREAM_CODEC, Base::output,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), Base::output2,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), Base::output3,
                    ResourceLocation.STREAM_CODEC, Base::requiredFluid,
                    Base::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, OreWashingCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    BASE_STREAM_CODEC,
                    recipe -> new Base(recipe.ingredient, recipe.requiredCount, recipe.output,
                            recipe.output2, recipe.output3, recipe.requiredFluid),
                    ByteBufCodecs.INT, OreWashingCraftingRecipe::requiredFluidAmount,
                    (base, amount) -> new OreWashingCraftingRecipe(base.ingredient(), base.requiredCount(),
                            base.output(), base.output2(), base.output3(), base.requiredFluid(), amount));

    public static final RecipeSerializer<OreWashingCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);
}
