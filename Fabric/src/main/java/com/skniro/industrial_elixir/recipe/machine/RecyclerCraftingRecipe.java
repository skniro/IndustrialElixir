package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecyclerCraftingRecipe implements Recipe<AlchemyCraftingRecipeInput> {
    private final ItemStackTemplate output;

    public RecyclerCraftingRecipe(ItemStackTemplate output) {
        this.output = output;
    }

    public ItemStackTemplate output() {
        return output;
    }

    @Override
    public boolean matches(AlchemyCraftingRecipeInput input, Level world) {
        if (world.isClientSide()) {
            return false;
        }
        ItemStack stack = input.getItem(1);
        return !stack.isEmpty();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public ItemStack assemble(AlchemyCraftingRecipeInput input) {
        return output.create();
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.RECYCLER.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.RECYCLER.type;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

        public static final MapCodec<RecyclerCraftingRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst -> inst.group(
                        ItemStackTemplate.CODEC.fieldOf("result")
                                .forGetter(RecyclerCraftingRecipe::output)
                ).apply(inst, RecyclerCraftingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RecyclerCraftingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ItemStackTemplate.STREAM_CODEC,
                        RecyclerCraftingRecipe::output,
                        RecyclerCraftingRecipe::new
                );


    public static final RecipeSerializer<RecyclerCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

        public MapCodec<RecyclerCraftingRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, RecyclerCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

}
