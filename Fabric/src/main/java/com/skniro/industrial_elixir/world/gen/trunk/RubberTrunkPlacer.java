package com.skniro.industrial_elixir.world.gen.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.init.LogCropBlock;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class RubberTrunkPlacer extends StraightTrunkPlacer {
    public static final MapCodec<RubberTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.intRange(0, 32).fieldOf("base_height").forGetter(p -> p.baseHeight),
                    Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(p -> p.heightRandA),
                    Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(p -> p.heightRandB),
                    BlockState.CODEC.fieldOf("state").forGetter(p -> p.blockState),
                    Codec.FLOAT.fieldOf("extra_chance").forGetter(p -> p.extraChance)
            ).apply(instance, RubberTrunkPlacer::new));

    public static final TrunkPlacerType<RubberTrunkPlacer> TYPE = new TrunkPlacerType<>(CODEC);

    private final BlockState blockState;
    private final float extraChance;

    public RubberTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, BlockState blockState, float extraChance) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.blockState = blockState;
        this.extraChance = extraChance;
    }

    public static void registerTrunkPlacerType() {
        Registry.register(
                BuiltInRegistries.TRUNK_PLACER_TYPE,
                Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "rubber_trunk_placer"),
                TYPE);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TYPE;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        BlockState rubberRubberState = blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y).setValue(LogCropBlock.AGE, 2);
        BlockState normalLogState = GeneralBlocks.Rubber_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);

        // 确保每棵树有 1-2 个 rubber_rubber_log，但至少有一个
        int specialHeight = height > 0 ? random.nextInt(height) : 0;
        for (int i = 0; i < height; i++) {
            BlockPos pos = startPos.above(i);
            replacer.accept(pos, i == specialHeight ? rubberRubberState : normalLogState);
        }

        // 有 extraChance 的概率额外放置第二个 rubber_rubber_log（总共不超过两个）
        if (height > 1 && random.nextFloat() < extraChance) {
            int extraHeight = (specialHeight + 1 + random.nextInt(height - 1)) % height;
            replacer.accept(startPos.above(extraHeight), rubberRubberState);
        }

        return List.of(new FoliagePlacer.FoliageAttachment(startPos.above(height), 0, false));
    }
}
