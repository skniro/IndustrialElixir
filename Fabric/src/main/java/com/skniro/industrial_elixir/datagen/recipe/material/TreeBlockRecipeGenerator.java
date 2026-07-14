package com.skniro.industrial_elixir.datagen.recipe.material;

import com.skniro.industrial_elixir.api.data.family.GrowableOresBlockFamilies;
import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import java.util.concurrent.CompletableFuture;

public class TreeBlockRecipeGenerator extends FabricRecipeProvider {
    public TreeBlockRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                this.offerPlanksRecipe(GeneralBlocks.Rubber_PLANKS, GeneralBlocks.Rubber_LOG, 4);
                this.woodFromLogs(GeneralBlocks.Rubber_WOOD, GeneralBlocks.Rubber_LOG);
                this.woodFromLogs(GeneralBlocks.STRIPPED_Rubber_WOOD, GeneralBlocks.STRIPPED_Rubber_LOG);
                this.woodenBoat(GrowableOresItems.RUBBER_BOAT, GeneralBlocks.Rubber_PLANKS);
                this.shelf(GeneralBlocks.Rubber_SHELF, GeneralBlocks.STRIPPED_Rubber_LOG);
                this.chestBoat(GrowableOresItems.RUBBER_CHEST_BOAT, GrowableOresItems.RUBBER_BOAT);
                this.hangingSign(MapleSignBlocks.Rubber_HANGING_SIGN, GeneralBlocks.STRIPPED_Rubber_LOG);
                generateRecipes(GrowableOresBlockFamilies.RUBBER_PLANKS, FeatureFlags.VANILLA_SET);

                shapeless(RecipeCategory.MISC, GeneralBlocks.Bronze_Block)
                        .requires(GrowableOresItems.BRONZE_INGOT,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.BRONZE_INGOT)).save(this.output);

                shapeless(RecipeCategory.MISC, GeneralBlocks.Tin_Block)
                        .requires(GrowableOresItems.TIN_INGOT,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.TIN_INGOT)).save(this.output);

                shapeless(RecipeCategory.MISC, GeneralBlocks.Lead_Block)
                        .requires(GrowableOresItems.LEAD_INGOT,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.LEAD_INGOT)).save(this.output);

                shapeless(RecipeCategory.MISC, GeneralBlocks.Steel_Block)
                        .requires(GrowableOresItems.STEEL_INGOT,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.STEEL_INGOT)).save(this.output);

                shapeless(RecipeCategory.MISC, GeneralBlocks.SACRED_Block)
                        .requires(GrowableOresItems.SACRED_INGOT,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SACRED_INGOT)).save(this.output);

                shapeless(RecipeCategory.MISC, GeneralBlocks.Silver_Block)
                        .requires(GrowableOresItems.SILVER_INGOT,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SILVER_INGOT)).save(this.output);
            }

            public void offerPlanksRecipe(ItemLike output, ItemLike logTag, int count) {
                this.shapeless(RecipeCategory.BUILDING_BLOCKS, output, count).requires(logTag).group("planks").unlockedBy("has_logs", this.has(logTag)).save(this.output);
            }
        };
    }


    @Override
    public String getName() {
        return "TreeBlock";
    }
}