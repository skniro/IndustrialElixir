package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.api.Helper;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;


public class IndustrialElixirBiomeTagGeneration extends FabricTagsProvider<Biome> {


    public IndustrialElixirBiomeTagGeneration(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        // generate tags under data/maple/tags/worldgen/biome/has_structure/
        TagKey<Biome> villageSakura = TagKey.create(Registries.BIOME, Helper.id("has_structure/industrial_elixir_hot_spring_baths"));
        builder(villageSakura).add(ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("minecraft", "cherry_grove")));
    }
}
