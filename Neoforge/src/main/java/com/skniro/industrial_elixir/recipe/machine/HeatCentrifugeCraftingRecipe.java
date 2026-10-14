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
import net.minecraft.world.item.crafting.*;

import java.util.Optional;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;

public class HeatCentrifugeCraftingRecipe extends AbstractMachineCraftingRecipe {
    private final Optional<ItemStack> output2;
    private final Optional<ItemStack> output3;

    public HeatCentrifugeCraftingRecipe(Ingredient ingredient, int requiredCount, ItemStack output,
                                        Optional<ItemStack> output2, Optional<ItemStack> output3) {
        super(ingredient, requiredCount, output);
        this.output2 = output2 != null ? output2 : Optional.empty();
        this.output3 = output3 != null ? output3 : Optional.empty();
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.HEAT_CENTRIFUGE.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.HEAT_CENTRIFUGE.type.get();
    }


    public Optional<ItemStack> output2() {
        return output2;
    }

    public Optional<ItemStack> output3() {
        return output3;
    }


    public static final MapCodec<HeatCentrifugeCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.requiredCount),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    ItemStack.CODEC.optionalFieldOf("result2").forGetter(recipe -> recipe.output2),
                    ItemStack.CODEC.optionalFieldOf("result3").forGetter(recipe -> recipe.output3)
            ).apply(inst, HeatCentrifugeCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HeatCentrifugeCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStack.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), HeatCentrifugeCraftingRecipe::output2,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), HeatCentrifugeCraftingRecipe::output3,
                    HeatCentrifugeCraftingRecipe::new);

    public static final RecipeSerializer<HeatCentrifugeCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);
}
