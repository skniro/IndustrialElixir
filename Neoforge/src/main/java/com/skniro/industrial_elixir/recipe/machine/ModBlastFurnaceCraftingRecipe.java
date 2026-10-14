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

public class ModBlastFurnaceCraftingRecipe extends AbstractMachineCraftingRecipe {
    private final Optional<ItemStack> output2;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;

    public ModBlastFurnaceCraftingRecipe(Ingredient ingredient, int requiredCount, ItemStack output,
                                         Optional<ItemStack> output2, ResourceLocation requiredFluid, int requiredFluidAmount) {
        super(ingredient, requiredCount, output);
        this.output2 = output2 != null ? output2 : Optional.empty();
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.MOD_BLAST_FURNACE.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.MOD_BLAST_FURNACE.type.get();
    }


    public Optional<ItemStack> output2() {
        return output2;
    }

    public ResourceLocation requiredFluid() {
        return requiredFluid;
    }

    public int requiredFluidAmount() {
        return requiredFluidAmount;
    }

    public static final MapCodec<ModBlastFurnaceCraftingRecipe> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.requiredCount),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                    ItemStack.CODEC.optionalFieldOf("result2").forGetter(recipe -> recipe.output2),
                    ResourceLocation.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.requiredFluid),
                    Codec.INT.fieldOf("fluid_amount").forGetter(recipe -> recipe.requiredFluidAmount)
            ).apply(inst, ModBlastFurnaceCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModBlastFurnaceCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractMachineCraftingRecipe::ingredient,
                    ByteBufCodecs.INT, AbstractMachineCraftingRecipe::requiredCount,
                    ItemStack.STREAM_CODEC, AbstractMachineCraftingRecipe::output,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC), ModBlastFurnaceCraftingRecipe::output2,
                    ResourceLocation.STREAM_CODEC, ModBlastFurnaceCraftingRecipe::requiredFluid,
                    ByteBufCodecs.INT, ModBlastFurnaceCraftingRecipe::requiredFluidAmount,
                    ModBlastFurnaceCraftingRecipe::new);

    public static final RecipeSerializer<ModBlastFurnaceCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);
}
