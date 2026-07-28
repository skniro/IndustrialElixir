package com.skniro.industrial_elixir.world.gen.trunk;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.init.LogCropBlock;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class RubberTrunkPlacer extends StraightTrunkPlacer {
    private BlockState blockState;
    private final float extraChance;

    public RubberTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, BlockState blockState, float extraChance) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.blockState = blockState;
        this.extraChance = extraChance;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        int specialHeight = random.nextInt(height);
        for (int i = 0; i < height; i++) {
            BlockPos pos = startPos.above(i);
            if (i == specialHeight) {
                replacer.accept(pos, blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y).setValue(LogCropBlock.AGE, 2));
                continue;
            }

            if (random.nextFloat() < extraChance) {
                replacer.accept(pos, blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y).setValue(LogCropBlock.AGE, 2));
            } else {
                replacer.accept(pos, GeneralBlocks.Rubber_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            }
        }
        return List.of(new FoliagePlacer.FoliageAttachment(startPos.above(height), 0, false));
    }
}