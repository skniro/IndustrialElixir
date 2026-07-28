package com.skniro.industrial_elixir.block.init.pipe;

import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.block.entity.pipe.StoneFluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StoneFluidPipeBlock extends StonePipeBlock {
    public StoneFluidPipeBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoneFluidPipeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((StoneFluidPipeBlockEntity) blockEntity).tick(world1, pos, state1);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("PipeBlock does not support getCodec!");
    }
}
