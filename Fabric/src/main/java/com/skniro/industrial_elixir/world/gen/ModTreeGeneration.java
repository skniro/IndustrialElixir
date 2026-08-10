package com.skniro.industrial_elixir.world.gen;

import com.skniro.industrial_elixir.world.feature.AgreeTreePlacedFeatures;
import com.skniro.industrial_elixir.world.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModTreeGeneration {

    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, AgreeTreePlacedFeatures.Rubber_TREE_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_COFFEE_COMMON);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_PINE_TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_COFFEE_COMMON);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_PINE_TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_COFFEE_RARE);
    }
}
