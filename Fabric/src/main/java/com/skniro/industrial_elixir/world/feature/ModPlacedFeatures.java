package com.skniro.industrial_elixir.world.feature;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> Deepslate_Lead_Ore_PLACED_KEY = registerKey("deepslate_lead_ore_placed");
    public static final ResourceKey<PlacedFeature> Deepslate_Tin_Ore_PLACED_KEY = registerKey("deepslate_tin_ore_placed");
    public static final ResourceKey<PlacedFeature> Deepslate_SACRED_Ore_PLACED_KEY = registerKey("deepslate_sacred_ore_placed");
    public static final ResourceKey<PlacedFeature> Lead_Ore_PLACED_KEY = registerKey("lead_ore_placed");
    public static final ResourceKey<PlacedFeature> Tin_Ore_PLACED_KEY = registerKey("tin_ore_placed");
    public static final ResourceKey<PlacedFeature> SACRED_Ore_PLACED_KEY = registerKey("sacred_ore_placed");
    public static final ResourceKey<PlacedFeature> PATCH_COFFEE_COMMON = registerKey("patch_coffee_common");
    public static final ResourceKey<PlacedFeature> PATCH_COFFEE_RARE = registerKey("patch_coffee_rare");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> registryEntry2 = configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.PATCH_COFFEE);

        register(context, Deepslate_Lead_Ore_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.Deepslate_Lead_Ore_KEY),
                modifiersWithCount(9, // Veins per Chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));

        register(context,Deepslate_Tin_Ore_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.Deepslate_Tin_Ore_KEY),
                modifiersWithCount(12, // Veins per Chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));

        register(context, Deepslate_SACRED_Ore_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.Deepslate_SACRED_Ore_KEY),
                modifiersWithCount(7, // Veins per Chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));

        register(context, Lead_Ore_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.Lead_Ore_KEY),
                modifiersWithCount(7, // Veins per Chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(63))));

        register(context,Tin_Ore_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.Tin_Ore_KEY),
                modifiersWithCount(10, // Veins per Chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(40))));

        register(context, SACRED_Ore_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.SACRED_Ore_KEY),
                modifiersWithCount(5, // Veins per Chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));

        register(context, PATCH_COFFEE_COMMON, registryEntry2, RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
        register(context, PATCH_COFFEE_RARE, registryEntry2, RarityFilter.onAverageOnceEvery(768), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }
    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                                                                   Holder<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }

    // Used here because the vanilla ones are private
    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacement.of(count), heightModifier);
    }

    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilter.onAverageOnceEvery(chance), heightModifier);
    }
}
