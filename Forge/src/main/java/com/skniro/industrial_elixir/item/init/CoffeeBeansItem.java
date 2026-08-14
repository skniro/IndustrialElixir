package com.skniro.industrial_elixir.item.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CoffeeBeansItem extends Item {

    private final Block plant;

    public CoffeeBeansItem(Block plant, Properties properties) {
        super(properties);
        this.plant = plant;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction direction = context.getClickedFace();

        BlockPos placePos = clickedPos.relative(direction);

        BlockState clickedState = level.getBlockState(clickedPos);

        boolean canPlantOn = clickedState.is(Blocks.DIRT) || clickedState.is(Blocks.GRASS_BLOCK) || clickedState.is(Blocks.PODZOL) || clickedState.is(Blocks.SAND) || clickedState.is(Blocks.FARMLAND);

        if (!canPlantOn) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            level.setBlock(placePos, plant.defaultBlockState(), Block.UPDATE_ALL);
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}