package com.skniro.industrial_elixir.block.entity.pipe;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StonePipeBlockEntity extends PipeBlockEntity {

    public StonePipeBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.PIPE_Stone_BLOCK_ENTITY.get(),pos, state);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;
        super.tick(world, pos, state);
        moveItems(world, pos, state);
    }
}
