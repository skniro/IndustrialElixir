package com.skniro.industrial_elixir.recipe.machine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import com.skniro.industrial_elixir.recipe.ModRecipeSerializers;

public class RecyclerCraftingRecipe implements Recipe<AlchemyCraftingRecipeInput> {
    private final ItemStack output;

    public RecyclerCraftingRecipe(ItemStack output) {
        this.output = output;
    }

    public ItemStack output() {
        return output;
    }

    @Override
    public boolean matches(AlchemyCraftingRecipeInput input, Level world) {
        if (world.isClientSide()) {
            return false;
        }
        ItemStack stack = input.getItem(0);
        return !stack.isEmpty();
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
    public ItemStack assemble(AlchemyCraftingRecipeInput input, net.minecraft.core.HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyCraftingRecipeInput>> getSerializer() {
        return AlchemyRecipeType.RECYCLER.serializer;
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyCraftingRecipeInput>> getType() {
        return AlchemyRecipeType.RECYCLER.type;
    }

        public static final MapCodec<RecyclerCraftingRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst -> inst.group(
                        ItemStack.CODEC.fieldOf("result")
                                .forGetter(RecyclerCraftingRecipe::output)
                ).apply(inst, RecyclerCraftingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RecyclerCraftingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ItemStack.STREAM_CODEC,
                        RecyclerCraftingRecipe::output,
                        RecyclerCraftingRecipe::new
                );


    public static final RecipeSerializer<RecyclerCraftingRecipe> SERIALIZER =
            ModRecipeSerializers.of(CODEC, STREAM_CODEC);

        public MapCodec<RecyclerCraftingRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, RecyclerCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

}
