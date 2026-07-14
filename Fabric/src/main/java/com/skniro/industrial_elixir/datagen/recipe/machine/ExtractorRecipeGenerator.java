package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class ExtractorRecipeGenerator extends FabricRecipeProvider {
    public ExtractorRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                createExtractor(Items.CLAY_BALL, 4).input(Items.CLAY)
                        .unlockedBy("has_base_item", has(Items.CLAY)).save(output);
                createExtractor(Items.SNOWBALL, 4).input(Items.SNOW_BLOCK)
                        .unlockedBy("has_base_item", has(Items.SNOW_BLOCK)).save(output);
                createExtractor(Items.BRICK, 4).input(Items.BRICKS)
                        .unlockedBy("has_base_item", has(Items.BRICKS)).save(output);
                createExtractor(Items.NETHER_BRICK, 4).input(Items.NETHER_BRICKS)
                        .unlockedBy("has_base_item", has(Items.NETHER_BRICKS)).save(output);
                createExtractor(GrowableOresItems.Rubber).input(GeneralBlocks.Rubber_LOG)
                        .unlockedBy("has_base_item", has(GeneralBlocks.Rubber_LOG)).save(output);
                createExtractor(GrowableOresItems.Rubber).input(GeneralBlocks.Rubber_Rubber_LOG)
                        .unlockedBy("has_base_item", has(GeneralBlocks.Rubber_Rubber_LOG)).save(output,"rubber_log_to_rubber");
                createExtractor(GrowableOresItems.Rubber).input(GeneralBlocks.Rubber_SAPLING)
                        .unlockedBy("has_base_item", has(GeneralBlocks.Rubber_SAPLING)).save(output,"rubber_sapling_to_rubber");
                createExtractor(GrowableOresItems.Rubber).input(GrowableOresItems.Sticky_Resin)
                        .unlockedBy("has_base_item", has(GrowableOresItems.Sticky_Resin)).save(output,"sticky_resin_to_rubber");
                createExtractor(GrowableOresItems.SULFUR_DUST).input(Items.GUNPOWDER)
                        .unlockedBy("has_base_item", has(Items.GUNPOWDER)).save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "Extractor";
    }
}
