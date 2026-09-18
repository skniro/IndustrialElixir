package com.skniro.industrial_elixir.world.feature;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.world.gen.trunk.RubberTrunkPlacer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BlockStateProviders;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class AgreeTreeConfiguredFeatures {
    public static final ResourceKey<Feature> Rubber_TREE = registerKey("rubber_tree");

    static WeightedList.Builder<BlockState> pool() {
        return WeightedList.builder();
    }

    public static void bootstrap(BootstrapContext<Feature> featureRegisterable) {
        HolderGetter<BlockStateProvider> blockStateProviders = featureRegisterable.lookup(Registries.BLOCK_STATE_PROVIDER);
        Holder<BlockStateProvider> belowTrunkProvider = blockStateProviders.getOrThrow(BlockStateProviders.SOIL_BENEATH_TREE);
        featureRegisterable.register(Rubber_TREE,
                new TreeFeature.Builder(
                        BlockStateProvider.of(GeneralBlocks.Rubber_LOG),
                        new RubberTrunkPlacer(4, 2, 0, GeneralBlocks.Rubber_Rubber_LOG.defaultBlockState(), 0.1F),
                        BlockStateProvider.of(GeneralBlocks.Rubber_LEAVES),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1),
                        belowTrunkProvider).build());
    }




    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }
}
