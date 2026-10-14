package com.skniro.industrial_elixir.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Builds a 1.21.1 {@link RecipeSerializer} out of a codec pair. 26.1 had a
 * {@code RecipeSerializer} record that could simply be constructed; 1.21.1 has
 * it as an interface instead.
 */
public final class ModRecipeSerializers {
    private ModRecipeSerializers() {
    }

    public static <R extends Recipe<?>> RecipeSerializer<R> of(MapCodec<R> codec,
                                                              StreamCodec<RegistryFriendlyByteBuf, R> streamCodec) {
        return new RecipeSerializer<R>() {
            @Override
            public MapCodec<R> codec() {
                return codec;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
                return streamCodec;
            }
        };
    }
}
