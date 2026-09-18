package com.skniro.industrial_elixir.world.feature;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.init.CoffeeBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<Feature> Deepslate_Lead_Ore_KEY = registerKey("deepslate_lead_ore");
    public static final ResourceKey<Feature> Deepslate_Tin_Ore_KEY = registerKey("deepslate_tin_ore");
    public static final ResourceKey<Feature> Deepslate_SACRED_Ore_KEY = registerKey("deepslate_sacred_ore");
    public static final ResourceKey<Feature> Lead_Ore_KEY = registerKey("lead_ore");
    public static final ResourceKey<Feature> Tin_Ore_KEY = registerKey("tin_ore");
    public static final ResourceKey<Feature> SACRED_Ore_KEY = registerKey("sacred_ore");
    public static final ResourceKey<Feature> PATCH_COFFEE = registerKey("patch_coffee");;

    public static void bootstrap(BootstrapContext<Feature> context) {
        RuleTest stoneReplaceables = new BlockMatchTest(Blocks.STONE);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest endStoneReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<BlockReplacement> deepslateleadOres =
                List.of(BlockReplacement.replace(deepslateReplaceables, GeneralBlocks.Deepslate_Lead_Ore.defaultBlockState()));
        List<BlockReplacement> deepslateTinOres =
                List.of(BlockReplacement.replace(deepslateReplaceables, GeneralBlocks.Deepslate_Tin_Ore.defaultBlockState()));
        List<BlockReplacement> deepslateSACREDOres =
                List.of(BlockReplacement.replace(deepslateReplaceables, GeneralBlocks.Deepslate_SACRED_Ore.defaultBlockState()));
        List<BlockReplacement> leadOres =
                List.of(BlockReplacement.replace(stoneReplaceables, GeneralBlocks.Lead_Ore.defaultBlockState()));
        List<BlockReplacement> TinOres =
                List.of(BlockReplacement.replace(stoneReplaceables, GeneralBlocks.Tin_Ore.defaultBlockState()));
        List<BlockReplacement> SACREDOres =
                List.of(BlockReplacement.replace(stoneReplaceables, GeneralBlocks.SACRED_Ore.defaultBlockState()));


        context.register(Deepslate_Lead_Ore_KEY, new OreFeature(deepslateleadOres, 6));
        context.register(Deepslate_Tin_Ore_KEY, new OreFeature(deepslateTinOres, 6));
        context.register(Deepslate_SACRED_Ore_KEY, new OreFeature(deepslateSACREDOres, 4));
        context.register(Lead_Ore_KEY, new OreFeature(leadOres, 6));
        context.register(Tin_Ore_KEY, new OreFeature(TinOres, 6));
        context.register(SACRED_Ore_KEY, new OreFeature(SACREDOres, 4));

        context.register(PATCH_COFFEE, new SimpleBlockFeature(BlockStateProvider.of(GeneralBlocks.Coffee_Block.defaultBlockState().setValue(CoffeeBlock.AGE, 3))));
    }

    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }
}

