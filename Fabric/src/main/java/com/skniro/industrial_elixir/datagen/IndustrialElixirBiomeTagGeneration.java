package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.api.Helper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;


public class IndustrialElixirBiomeTagGeneration extends FabricTagProvider<Biome> {


    public IndustrialElixirBiomeTagGeneration(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        // generate tags under data/maple/tags/worldgen/biome/has_structure/
        TagKey<Biome> villageSakura = TagKey.create(Registries.BIOME, Helper.id("has_structure/industrial_elixir_hot_spring_baths"));
        getOrCreateTagBuilder(villageSakura).add(ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("minecraft", "cherry_grove")));
    }
}
