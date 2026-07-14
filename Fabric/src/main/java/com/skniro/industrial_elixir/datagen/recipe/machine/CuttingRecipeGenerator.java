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

public class CuttingRecipeGenerator extends FabricRecipeProvider {
    public CuttingRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                createCutting(Items.IRON_BLOCK, GrowableOresItems.IRON_PLATE,9).save(output);
                createCutting(Items.OBSIDIAN, GrowableOresItems.OBSIDIAN_PLATE,4).save(output);
                createCutting(Items.GOLD_BLOCK, GrowableOresItems.GOLD_PLATE,9).save(output);
                createCutting(Items.LAPIS_BLOCK, GrowableOresItems.LAPIS_PLATE,9).save(output);
                createCutting(Items.COPPER_BLOCK, GrowableOresItems.COPPER_PLATE,9).save(output);
                createCutting(GeneralBlocks.Bronze_Block, GrowableOresItems.BRONZE_PLATE,9).save(output);
                createCutting(GeneralBlocks.Lead_Block, GrowableOresItems.LEAD_PLATE,9).save(output);
                createCutting(GeneralBlocks.Tin_Block, GrowableOresItems.TIN_PLATE,9).save(output);
                createCutting(GeneralBlocks.Steel_Block, GrowableOresItems.STEEL_PLATE,9).save(output);

                addWoodSet(output, Items.OAK_LOG, Items.STRIPPED_OAK_LOG, Items.OAK_WOOD, Items.STRIPPED_OAK_WOOD, Items.OAK_PLANKS, "oak");
                addWoodSet(output, Items.SPRUCE_LOG, Items.STRIPPED_SPRUCE_LOG, Items.SPRUCE_WOOD, Items.STRIPPED_SPRUCE_WOOD, Items.SPRUCE_PLANKS, "spruce");
                addWoodSet(output, Items.BIRCH_LOG, Items.STRIPPED_BIRCH_LOG, Items.BIRCH_WOOD, Items.STRIPPED_BIRCH_WOOD, Items.BIRCH_PLANKS, "birch");
                addWoodSet(output, Items.JUNGLE_LOG, Items.STRIPPED_JUNGLE_LOG, Items.JUNGLE_WOOD, Items.STRIPPED_JUNGLE_WOOD, Items.JUNGLE_PLANKS, "jungle");
                addWoodSet(output, Items.ACACIA_LOG, Items.STRIPPED_ACACIA_LOG, Items.ACACIA_WOOD, Items.STRIPPED_ACACIA_WOOD, Items.ACACIA_PLANKS, "acacia");
                addWoodSet(output, Items.DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_LOG, Items.DARK_OAK_WOOD, Items.STRIPPED_DARK_OAK_WOOD, Items.DARK_OAK_PLANKS, "dark_oak");
                addWoodSet(output, Items.MANGROVE_LOG, Items.STRIPPED_MANGROVE_LOG, Items.MANGROVE_WOOD, Items.STRIPPED_MANGROVE_WOOD, Items.MANGROVE_PLANKS, "mangrove");
                addWoodSet(output, Items.CHERRY_LOG, Items.STRIPPED_CHERRY_LOG, Items.CHERRY_WOOD, Items.STRIPPED_CHERRY_WOOD, Items.CHERRY_PLANKS, "cherry");
                addWoodSet(output, Items.BAMBOO_BLOCK, Items.STRIPPED_BAMBOO_BLOCK, Items.BAMBOO_BLOCK, Items.STRIPPED_BAMBOO_BLOCK, Items.BAMBOO_PLANKS, "bamboo");
                addWoodSet(output, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, "crimson");
                addWoodSet(output, Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE, Items.WARPED_PLANKS, "warped");
                addWoodSet(output, Items.PALE_OAK_LOG, Items.STRIPPED_PALE_OAK_LOG, Items.PALE_OAK_WOOD, Items.STRIPPED_PALE_OAK_WOOD, Items.PALE_OAK_PLANKS, "pale_oak");
                addWoodSet(output, GeneralBlocks.Rubber_LOG, GeneralBlocks.STRIPPED_Rubber_LOG, GeneralBlocks.Rubber_WOOD, GeneralBlocks.STRIPPED_Rubber_WOOD, GeneralBlocks.Rubber_PLANKS, "rubber");
                createCutting(GeneralBlocks.Rubber_Rubber_LOG, GeneralBlocks.Rubber_PLANKS,6).save(output, "rubber_rubber_log_to_planks");
            }
        };
    }

    @Override
    public String getName() {
        return "Cutting";
    }
}
