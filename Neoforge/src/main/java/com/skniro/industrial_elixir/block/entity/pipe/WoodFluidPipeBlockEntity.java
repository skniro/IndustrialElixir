package com.skniro.industrial_elixir.block.entity.pipe;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.pipe.WoodFluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class WoodFluidPipeBlockEntity extends FluidPipeBlockEntity {
    public WoodFluidPipeBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.PIPE_Wooden_Fluid_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;
        super.tick(world, pos, state);
        moveFluids(world, pos);
        extractFluids(world, pos);
    }

    @Override
    protected void onExtractDirectionChanged(@Nullable Direction direction) {
        if (level == null || level.isClientSide()) return;
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof WoodFluidPipeBlock)) return;

        BlockState newState = state;
        for (Direction dir : Direction.values()) {
            BooleanProperty property = WoodFluidPipeBlock.PULL_PROPERTY_MAP.get(dir);
            boolean active = dir == direction;
            if (newState.getValue(property) != active) {
                newState = newState.setValue(property, active);
            }
        }

        if (newState != state) {
            level.setBlock(worldPosition, newState, Block.UPDATE_CLIENTS);
        }
    }
}
