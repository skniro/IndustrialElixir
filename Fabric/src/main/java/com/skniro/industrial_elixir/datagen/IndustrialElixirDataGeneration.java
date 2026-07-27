package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.datagen.dynamic.ModDamageTypeProvider;
import com.skniro.industrial_elixir.datagen.recipe.RecipeDataGeneration;
import com.skniro.industrial_elixir.world.feature.AgreeTreeConfiguredFeatures;
import com.skniro.industrial_elixir.world.feature.AgreeTreePlacedFeatures;
import com.skniro.industrial_elixir.world.feature.ModConfiguredFeatures;
import com.skniro.industrial_elixir.world.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class IndustrialElixirDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(IndustrialElixirModelProvider::new);
        fabricDataGenerator.createPack().addProvider(IndustrialElixirLootTableGenerator::new);
        fabricDataGenerator.createPack().addProvider(IndustrialElixirEnglishLanguageProvider::new);
        fabricDataGenerator.createPack().addProvider(IndustrialElixirSimplifiedChineseLanguageProvider::new);
        fabricDataGenerator.createPack().addProvider(IndustrialElixirBlockTagGenerator::new);
        fabricDataGenerator.createPack().addProvider(IndustrialElixirItemTagGenerator::new);
        fabricDataGenerator.createPack().addProvider(IndustrialElixirBiomeTagGeneration::new);
        fabricDataGenerator.createPack().addProvider(ModDynamicGenerator::new);
        RecipeDataGeneration.onInit(fabricDataGenerator);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::damageTypes);
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        registryBuilder.add(Registries.CONFIGURED_FEATURE, AgreeTreeConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, AgreeTreePlacedFeatures::bootstrap);
    }
}
