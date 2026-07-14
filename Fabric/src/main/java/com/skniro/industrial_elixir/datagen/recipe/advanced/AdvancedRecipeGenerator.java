package com.skniro.industrial_elixir.datagen.recipe.advanced;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class AdvancedRecipeGenerator extends FabricRecipeProvider {
    public AdvancedRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.MISC, AdvancedItems.LUMINITE)
                        .requires(AdvancedItems.LUMINITE_Part,9)
                        .unlockedBy("has_item", this.has(AdvancedItems.LUMINITE_Part)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.LUMINITE_ALLOY)
                        .define('R', AdvancedItems.LUMINITE).define('D', GrowableOresItems.IRIDIUM_PLATE)
                        .pattern("DDD").pattern("DRD").pattern("DDD")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.IRIDIUM_PLATE)).save(this.output);

                 shaped(RecipeCategory.MISC, AdvancedItems.IRRADIANT_SEPTRIN_INGOT)
                        .define('R', Items.GLOWSTONE_DUST).define('D', GrowableOresItems.SACRED_INGOT)
                        .pattern(" R ").pattern("RDR").pattern(" R ")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.SACRED_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.ENRICHED_LUMINITE)
                        .define('R', AdvancedItems.LUMINITE).define('D', AdvancedItems.IRRADIANT_SEPTRIN_INGOT)
                        .pattern("DDD").pattern("DRD").pattern("DDD")
                        .unlockedBy("has_item_tags", this.has(AdvancedItems.IRRADIANT_SEPTRIN_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.ENRICHED_LUMINITE_ALLOY)
                        .define('R', AdvancedItems.ENRICHED_LUMINITE).define('D', AdvancedItems.LUMINITE_ALLOY)
                        .pattern(" R ").pattern("RDR").pattern(" R ")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.SACRED_INGOT)).save(this.output);

                //TEM
                shaped(RecipeCategory.MISC, AdvancedItems.IRRADIANT_GLASS_PANE)
                        .define('R', Items.GLOWSTONE_DUST).define('D', AdvancedItems.IRRADIANT_SEPTRIN_INGOT).define('G', Items.GLASS)
                        .pattern("GGG").pattern("DRD").pattern("GGG")
                        .unlockedBy("has_item_tags", this.has(AdvancedItems.IRRADIANT_SEPTRIN_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.IRIDIUM_AMETHYST_PLATE)
                        .define('R', AdvancedItems.Iridium_INGOT).define('D', GrowableOresItems.IRIDIUM_PLATE)
                        .pattern("DDD").pattern("DRD").pattern("DDD")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.IRIDIUM_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE)
                        .define('R', AdvancedItems.IRIDIUM_AMETHYST_PLATE).define('D', GrowableOresItems.CARBON_PLATE).define('A', GrowableOresItems.ALLOY_PLATE)
                        .pattern("ADA").pattern("DRD").pattern("ADA")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.CARBON_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.IRRADIANT_REINFORCED_PLATE)
                        .define('R', AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE).define('P', AdvancedItems.LUMINITE_Part).define('D', Items.DIAMOND).define('A', Items.REDSTONE).define('L', Items.LAPIS_LAZULI)
                        .pattern("APA").pattern("LRL").pattern("ADA")
                        .unlockedBy("has_item_tags", this.has(AdvancedItems.LUMINITE_Part)).save(this.output);

                //TEM
                shaped(RecipeCategory.MISC, AdvancedItems.MT_Core)
                        .define('R', AdvancedItems.IRRADIANT_GLASS_PANE).define('P', GrowableOresItems.COPPER_PLATE)
                        .pattern("RPR").pattern("R R").pattern("RPR")
                        .unlockedBy("has_item_tags", this.has(AdvancedItems.IRRADIANT_GLASS_PANE)).save(this.output);

                shaped(RecipeCategory.MISC, AdvancedItems.Quantum_Core)
                        .define('R', AdvancedItems.ENRICHED_LUMINITE_ALLOY).define('P', Items.NETHER_STAR).define('E', Items.ENDER_EYE)
                        .pattern("RPR").pattern("PEP").pattern("RPR")
                        .unlockedBy("has_item_tags", this.has(Items.NETHER_STAR)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MolecularTransformerBlock)
                        .define('R', AdvancedItems.MT_Core).define('P', GrowableOresItems.Advanced_Circuit).define('A', GeneralBlocks.Advanced_Machine).define('E', GrowableOresBlocks.EV_TRANSFORMER)
                        .pattern("AEA").pattern("PRP").pattern("AEA")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.Advanced_Circuit)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL)
                        .define('R', AdvancedItems.IRRADIANT_GLASS_PANE).define('P', GrowableOresItems.Advanced_Circuit).define('A', GrowableOresBlocks.GENERATOR_SolarPanel).define('E', AdvancedItems.IRRADIANT_REINFORCED_PLATE).define('D', GrowableOresItems.ALLOY_PLATE)
                        .pattern("RRR").pattern("DAD").pattern("PEP")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.Advanced_Circuit)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL)
                        .define('R', GrowableOresItems.CARBON_PLATE)
                        .define('L', Items.LAPIS_BLOCK)
                        .define('P', GrowableOresItems.Advanced_Circuit)
                        .define('A', GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL)
                        .define('E', AdvancedItems.ENRICHED_LUMINITE)
                        .define('D', GrowableOresItems.IRIDIUM_PLATE)
                        .pattern("RLR").pattern("DAD").pattern("PEP")
                        .unlockedBy("has_item_tags", this.has(GrowableOresItems.Advanced_Circuit)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL, 8)
                        .requires(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL)
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL)).save(this.output,"generator_ultimate_solar_panel_to_generator_hybrid_solar_panel");

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL)
                        .define('L', Items.LAPIS_BLOCK)
                        .define('A', GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL)
                        .define('E', AdvancedItems.ENRICHED_LUMINITE_ALLOY)
                        .define('D', GrowableOresItems.COAL_CHUNK)
                        .pattern(" L ").pattern("DAD").pattern("EDE")
                        .unlockedBy("has_item_tags", this.has(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL)).save(this.output);


                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL)
                        .define('A', GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL)
                        .define('P', GrowableOresItems.Advanced_Circuit)
                        .pattern("AAA").pattern("APA").pattern("AAA")
                        .unlockedBy("has_item_tags", this.has(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL)).save(this.output,"generator_hybrid_solar_panel_to_generator_ultimate_solar_panel");

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL)
                        .define('A', GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL)
                        .define('P', AdvancedItems.Quantum_Core)
                        .pattern("AAA").pattern("APA").pattern("AAA")
                        .unlockedBy("has_item_tags", this.has(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL)).save(this.output);

            }
        };
    }


    @Override
    public String getName() {
        return "Advanced";
    }
}