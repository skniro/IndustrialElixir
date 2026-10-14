package com.skniro.industrial_elixir.recipe;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;


public class AlchemyCraftingRecipe implements Recipe<AlchemyCraftingRecipeInput> {
    final ItemStack output;
    final Ingredient ingredient;


    public AlchemyCraftingRecipe(Ingredient ingredients, ItemStack output) {
        this.output = output;
        this.ingredient = ingredients;
    }

    public Ingredient ingredient() {
        return this.ingredient;
    }

    public ItemStack output() {
        return this.output;
    }

    @Override
    public boolean matches(AlchemyCraftingRecipeInput input, Level world) {
        if (world.isClientSide()) {
            return false;
        }
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(AlchemyCraftingRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String getGroup() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.CANE_CONVERTER.serializer.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.CANE_CONVERTER.type.get();
    }



    public static final MapCodec<AlchemyCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> {
                return recipe.ingredient;
            }),
            ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> {
                return recipe.output;
            })
    ).apply(inst, AlchemyCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, AlchemyCraftingRecipe::ingredient,
                    ItemStack.STREAM_CODEC, AlchemyCraftingRecipe::output,
                    AlchemyCraftingRecipe::new);

    public static final RecipeSerializer<AlchemyCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);

    public MapCodec<AlchemyCraftingRecipe> codec() {
        return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, AlchemyCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}









