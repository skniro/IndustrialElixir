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

import java.util.Optional;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;

public class CropFarmCraftingRecipe extends AbstractMachineCraftingRecipe {
    private final Optional<ItemStack> output2;
    private final Optional<ItemStack> output3;
    private final int processTime;

    public CropFarmCraftingRecipe(Ingredient ingredient, int requiredCount, ItemStack output,
                                  Optional<ItemStack> output2, Optional<ItemStack> output3,
                                  int processTime) {
        super(ingredient, requiredCount, output);
        this.output2 = output2;
        this.output3 = output3;
        this.processTime = processTime;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.CROP_FARM.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.CROP_FARM.type.get();
    }


    public Optional<ItemStack> output2() {
        return output2;
    }

    public Optional<ItemStack> output3() {
        return output3;
    }

    public int processTime() {
        return processTime;
    }

    public static final MapCodec<CropFarmCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.requiredCount),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    ItemStack.CODEC.optionalFieldOf("result2").forGetter(recipe -> recipe.output2),
                    ItemStack.CODEC.optionalFieldOf("result3").forGetter(recipe -> recipe.output3),
                    Codec.INT.fieldOf("process_time").forGetter(recipe -> recipe.processTime)
            ).apply(inst, CropFarmCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CropFarmCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStack.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), CropFarmCraftingRecipe::output2,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), CropFarmCraftingRecipe::output3,
                    ByteBufCodecs.INT, CropFarmCraftingRecipe::processTime,
                    CropFarmCraftingRecipe::new);

    public static final RecipeSerializer<CropFarmCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);
}
