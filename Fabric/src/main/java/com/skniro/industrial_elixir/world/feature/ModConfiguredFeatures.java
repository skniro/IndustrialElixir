package com.skniro.industrial_elixir.world.feature;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> Deepslate_Lead_Ore_KEY = registerKey("deepslate_lead_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> Deepslate_Tin_Ore_KEY = registerKey("deepslate_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> Deepslate_SACRED_Ore_KEY = registerKey("deepslate_sacred_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> Lead_Ore_KEY = registerKey("lead_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> Tin_Ore_KEY = registerKey("tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SACRED_Ore_KEY = registerKey("sacred_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new BlockMatchTest(Blocks.STONE);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest endStoneReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> deepslateleadOres =
                List.of(OreConfiguration.target(deepslateReplaceables, GeneralBlocks.Deepslate_Lead_Ore.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> deepslateTinOres =
                List.of(OreConfiguration.target(deepslateReplaceables, GeneralBlocks.Deepslate_Tin_Ore.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> deepslateSACREDOres =
                List.of(OreConfiguration.target(deepslateReplaceables, GeneralBlocks.Deepslate_SACRED_Ore.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> leadOres =
                List.of(OreConfiguration.target(stoneReplaceables, GeneralBlocks.Lead_Ore.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> TinOres =
                List.of(OreConfiguration.target(stoneReplaceables, GeneralBlocks.Tin_Ore.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> SACREDOres =
                List.of(OreConfiguration.target(stoneReplaceables, GeneralBlocks.SACRED_Ore.defaultBlockState()));


        register(context, Deepslate_Lead_Ore_KEY, Feature.ORE, new OreConfiguration(deepslateleadOres, 8));
        register(context, Deepslate_Tin_Ore_KEY, Feature.ORE, new OreConfiguration(deepslateTinOres, 10));
        register(context, Deepslate_SACRED_Ore_KEY, Feature.ORE, new OreConfiguration(deepslateSACREDOres, 6));
        register(context, Lead_Ore_KEY, Feature.ORE, new OreConfiguration(leadOres, 8));
        register(context, Tin_Ore_KEY, Feature.ORE, new OreConfiguration(TinOres, 10));
        register(context, SACRED_Ore_KEY, Feature.ORE, new OreConfiguration(SACREDOres, 6));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                   ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}

