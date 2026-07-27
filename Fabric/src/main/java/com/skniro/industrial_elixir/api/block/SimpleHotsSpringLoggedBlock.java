package com.skniro.industrial_elixir.api.block;

import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.util.ModBooleanPropertys;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface SimpleHotsSpringLoggedBlock extends BucketPickup, LiquidBlockContainer {
    default boolean canPlaceLiquid(final @Nullable LivingEntity user, final BlockGetter level, final BlockPos pos, final BlockState state, final Fluid type) {
        return type == IndustrialElixirFluids.STILL_Hot_Spring;
    }

    default boolean placeLiquid(final LevelAccessor level, final BlockPos pos, final BlockState state, final FluidState fluidState) {
        if (!(Boolean)state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED) && fluidState.is(IndustrialElixirFluids.STILL_Hot_Spring)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, (BlockState)state.setValue(ModBooleanPropertys.HOT_SPRING_LOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            return true;
        } else {
            return false;
        }
    }

    default ItemStack pickupBlock(final @Nullable LivingEntity user, final LevelAccessor level, final BlockPos pos, final BlockState state) {
        if (state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED)) {
            level.setBlock(pos, (BlockState)state.setValue(ModBooleanPropertys.HOT_SPRING_LOGGED, false), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }

            return new ItemStack(IndustrialElixirFluidItems.Hot_Spring_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    default Optional<SoundEvent> getPickupSound() {
        return IndustrialElixirFluids.STILL_Fluid_UU.getPickupSound();
    }
}
