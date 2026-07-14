package com.skniro.industrial_elixir.datagen.recipe.material;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class CableUpgradeRecipeGenerator extends FabricRecipeProvider {
    public CableUpgradeRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {

                shapeless(RecipeCategory.MISC, GrowableOresBlocks.INSULATED_COPPER_CABLE)
                        .requires(GrowableOresBlocks.COPPER_CABLE).requires(GrowableOresItems.Rubber)
                        .unlockedBy("has_item", this.has(GrowableOresItems.Rubber)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresBlocks.INSULATED_TIN_CABLE)
                        .requires(GrowableOresBlocks.TIN_CABLE).requires(GrowableOresItems.Rubber)
                        .unlockedBy("has_item", this.has(GrowableOresItems.Rubber)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresBlocks.INSULATED_GOLD_CABLE)
                        .requires(GrowableOresBlocks.GOLD_CABLE).requires(GrowableOresItems.Rubber)
                        .unlockedBy("has_item", this.has(GrowableOresItems.Rubber)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresBlocks.INSULATED_HV_CABLE)
                        .requires(GrowableOresBlocks.HV_CABLE).requires(GrowableOresItems.Rubber)
                        .unlockedBy("has_item", this.has(GrowableOresItems.Rubber)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GLASSFIBER_CABLE, 6)
                        .define('G', Items.GLASS).define('E',GrowableOresItems.ENERGIUM_DUST).define('S',GrowableOresItems.SILVER_DUST)
                        .pattern("GGG").pattern("ESE").pattern("GGG")
                        .unlockedBy("has_item", this.has(GrowableOresItems.ENERGIUM_DUST)).save(this.output);

                //临时配方
                shaped(RecipeCategory.MISC, GrowableOresItems.OVERCLOCKER)
                        .define('G', Items.LAPIS_LAZULI).define('E', GrowableOresItems.Circuit).define('S',GrowableOresBlocks.INSULATED_COPPER_CABLE)
                        .pattern("   ").pattern("GGG").pattern("SES")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit)).save(this.output);

                //临时配方
                shaped(RecipeCategory.MISC, GrowableOresItems.OVERCLOCKER, 6)
                        .define('G', Items.LAPIS_BLOCK).define('E', GrowableOresItems.Circuit).define('S',GrowableOresBlocks.INSULATED_COPPER_CABLE)
                        .pattern("   ").pattern("GGG").pattern("SES")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit)).save(this.output,"get_more_overclocker");

                shaped(RecipeCategory.MISC, GrowableOresItems.ENERGY_STORAGE)
                        .define('G', Items.GLASS).define('E', GrowableOresItems.Circuit).define('S',GrowableOresBlocks.INSULATED_GOLD_CABLE).define('M',GrowableOresBlocks.MV_TRANSFORMER)
                        .pattern("GGG").pattern("SMS").pattern("GEG")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.TRANSFORMER)
                        .define('G', ItemTags.PLANKS).define('E', GrowableOresItems.Circuit).define('S',GrowableOresBlocks.INSULATED_COPPER_CABLE).define('M',GrowableOresItems.RE_BATTERY)
                        .pattern("GGG").pattern("SMS").pattern("GEG")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.REDSTONE_INVERTER)
                        .define('G', GrowableOresItems.TIN_PLATE).define('M',Items.LEVER)
                        .pattern("G G").pattern(" M ").pattern("G G")
                        .unlockedBy("has_item", this.has(Items.LEVER)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.REDSTONE_INVERTER, 9)
                        .define('G', GrowableOresItems.DENSE_TIN_PLATE).define('M',Items.LEVER)
                        .pattern("G G").pattern(" M ").pattern("G G")
                        .unlockedBy("has_item", this.has(Items.LEVER)).save(this.output,"get_more_redstone_inverter");
            }
        };
    }

    @Override
    public String getName() {
        return "Cable";
    }
}