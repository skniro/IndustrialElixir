package com.skniro.industrial_elixir.block.entity.pipe;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface PipeExtractDirectionController {

    @Nullable
    Direction getPreferredExtractDirection();

    boolean setPreferredExtractDirection(@Nullable Direction direction);

    default boolean clearPreferredExtractDirection() {
        return setPreferredExtractDirection(null);
    }

    default boolean togglePreferredExtractDirection(Direction direction) {
        if (getPreferredExtractDirection() == direction) {
            return clearPreferredExtractDirection();
        }
        return setPreferredExtractDirection(direction);
    }
}
