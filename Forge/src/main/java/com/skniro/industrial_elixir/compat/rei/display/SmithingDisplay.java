package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import me.shedaniel.rei.api.common.display.Display;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap.OutOfBoundsStrategy;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

public interface SmithingDisplay extends Display {
    @Nullable SmithingRecipeType type();

    @Experimental
    public static enum SmithingRecipeType {
        TRIM,
        TRANSFORM;

        public static final Codec<SmithingRecipeType> CODEC = Codec.STRING.xmap(SmithingRecipeType::valueOf, Enum::name);
        public static final IntFunction<SmithingRecipeType> BY_ID = ByIdMap.continuous((ToIntFunction<SmithingRecipeType>) Enum::ordinal, values(), OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, SmithingRecipeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
    }

    public interface Trimming {
        Holder<TrimPattern> pattern();
    }
}