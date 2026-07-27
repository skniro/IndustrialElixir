package com.skniro.industrial_elixir.datagen.recipe.material;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MaterialRecipeGenerator extends FabricRecipeProvider {
    public MaterialRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, GrowableOresItems.ENERGIUM_DUST)
                        .define('R', Items.REDSTONE).define('D', GrowableOresItems.DIAMOND_DUST)
                        .pattern("RDR").pattern("DRD").pattern("RDR")
                        .unlockedBy("has_item", this.has(Items.REDSTONE)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.BRONZE_DUST)
                        .requires(GrowableOresItems.SMALL_BRONZE_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_BRONZE_DUST)).save(this.output,"small_bronze_dust_to_bronze_dust");

                shapeless(RecipeCategory.MISC, GrowableOresItems.COPPER_DUST)
                        .requires(GrowableOresItems.SMALL_COPPER_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_BRONZE_DUST)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.BRONZE_DUST, 4)
                        .define('R', ModItemTags.DUST_TIN).define('D', ModItemTags.DUST_COPPER)
                        .pattern("RD ").pattern("DD ").pattern("   ")
                        .unlockedBy("has_item_tags", this.has(ModItemTags.DUST_COPPER)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.GOLD_DUST)
                        .requires(GrowableOresItems.SMALL_GOLD_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_GOLD_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.IRON_DUST)
                        .requires(GrowableOresItems.SMALL_IRON_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_IRON_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.LAPIS_DUST)
                        .requires(GrowableOresItems.SMALL_LAPIS_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_LAPIS_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.LEAD_DUST)
                        .requires(GrowableOresItems.SMALL_LEAD_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_LEAD_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.LITHIUM_DUST)
                        .requires(GrowableOresItems.SMALL_LITHIUM_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_LITHIUM_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.OBSIDIAN_DUST)
                        .requires(GrowableOresItems.SMALL_OBSIDIAN_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_OBSIDIAN_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.SILVER_DUST)
                        .requires(GrowableOresItems.SMALL_SILVER_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_SILVER_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.SULFUR_DUST)
                        .requires(GrowableOresItems.SMALL_SULFUR_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_SULFUR_DUST)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.TIN_DUST)
                        .requires(GrowableOresItems.SMALL_TIN_DUST,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.SMALL_TIN_DUST)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.ALLOY_INGOT)
                        .define('I', GrowableOresItems.IRON_PLATE).define('B', GrowableOresItems.BRONZE_PLATE).define('T', GrowableOresItems.TIN_PLATE)
                        .pattern("III").pattern("BBB").pattern("TTT")
                        .unlockedBy("has_item", this.has(GrowableOresItems.BRONZE_PLATE)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.BRONZE_INGOT, 9)
                        .requires(GeneralBlocks.Bronze_Block)
                        .unlockedBy("has_item", this.has(GeneralBlocks.Bronze_Block)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.LEAD_INGOT, 9)
                        .requires(GeneralBlocks.Lead_Block)
                        .unlockedBy("has_item", this.has(GeneralBlocks.Lead_Block)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.SILVER_INGOT, 9)
                        .requires(GeneralBlocks.Silver_Block)
                        .unlockedBy("has_item", this.has(GeneralBlocks.Silver_Block)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.STEEL_INGOT, 9)
                        .requires(GeneralBlocks.Steel_Block)
                        .unlockedBy("has_item", this.has(GeneralBlocks.Steel_Block)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.SACRED_INGOT, 9)
                        .requires(GeneralBlocks.SACRED_Block)
                        .unlockedBy("has_item", this.has(GeneralBlocks.SACRED_Block)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.TIN_INGOT, 9)
                        .requires(GeneralBlocks.Tin_Block)
                        .unlockedBy("has_item", this.has(GeneralBlocks.Tin_Block)).save(this.output);

                oreSmelting(List.of(GrowableOresItems.Sticky_Resin), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.Rubber, 0.45F, 300, "sticky_resin");

                shaped(RecipeCategory.MISC, GrowableOresItems.Circuit)
                        .define('I', GrowableOresItems.IRON_PLATE).define('B', GrowableOresBlocks.INSULATED_COPPER_CABLE).define('T', Items.REDSTONE)
                        .pattern("BBB").pattern("TIT").pattern("BBB")
                        .unlockedBy("has_item", this.has(GrowableOresItems.IRON_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.Advanced_Circuit)
                        .define('I', GrowableOresItems.Circuit).define('B', Items.GLOWSTONE_DUST).define('T', Items.REDSTONE).define('L', Items.LAPIS_LAZULI)
                        .pattern("TBT").pattern("LIL").pattern("TBT")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.IRIDIUM_PLATE)
                        .define('I', Items.DIAMOND).define('B', GrowableOresItems.ALLOY_PLATE).define('T', GrowableOresItems.IRIDIUM_ORE)
                        .pattern("TBT").pattern("BIB").pattern("TBT")
                        .unlockedBy("has_item", this.has(GrowableOresItems.ALLOY_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.CARBON_FIBRE)
                        .define('I', GrowableOresItems.COAL_DUST)
                        .pattern("II ").pattern("II ").pattern("   ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.COAL_DUST)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.CARBON_MESH)
                        .define('I', GrowableOresItems.CARBON_FIBRE)
                        .pattern("II ").pattern("   ").pattern("   ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.CARBON_FIBRE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.COAL_BALL)
                        .define('I', GrowableOresItems.COAL_DUST).define('B', Items.FLINT)
                        .pattern("III").pattern("IBI").pattern("III")
                        .unlockedBy("has_item", this.has(GrowableOresItems.COAL_DUST)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.COAL_CHUNK)
                        .define('I', GrowableOresItems.COAL_BALL).define('B', Items.IRON_BLOCK)
                        .pattern("III").pattern("IBI").pattern("III")
                        .unlockedBy("has_item", this.has(GrowableOresItems.COAL_BALL)).save(this.output);


                shaped(RecipeCategory.MISC, GrowableOresItems.RE_BATTERY)
                        .define('T', GrowableOresBlocks.INSULATED_TIN_CABLE).define('I', GrowableOresItems.IRON_CASING).define('B', Items.REDSTONE)
                        .pattern(" T ").pattern("IBI").pattern("IBI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.IRON_CASING)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.ADVANCED_RE_BATTERY)
                        .define('T', GrowableOresBlocks.INSULATED_COPPER_CABLE).define('I', GrowableOresItems.BRONZE_CASING).define('B', GrowableOresItems.SULFUR_DUST).define('C', GrowableOresItems.LEAD_DUST)
                        .pattern("TIT").pattern("IBI").pattern("ICI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.BRONZE_CASING)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.LAPOTRON_CRYSTAL)
                        .define('T', GrowableOresItems.Advanced_Circuit).define('I', GrowableOresItems.LAPIS_DUST).define('B', GrowableOresItems.ENERGY_CRYSTAL)
                        .pattern("ITI").pattern("IBI").pattern("ITI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.ENERGY_CRYSTAL)).save(this.output);

                shaped(RecipeCategory.MISC, MapleArmorItems.ROLLING)
                        .define('T', Items.IRON_INGOT).define('I', Items.STICK)
                        .pattern("TT ").pattern("TII").pattern("TT ")
                        .unlockedBy("has_item", this.has(Items.IRON_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, MapleArmorItems.ROLLING)
                        .define('T', Items.IRON_INGOT).define('I', Items.STICK)
                        .pattern(" TT").pattern("IIT").pattern(" TT")
                        .unlockedBy("has_item", this.has(Items.IRON_INGOT)).save(this.output, "right_side_tool_rolling");

                shaped(RecipeCategory.MISC, MapleArmorItems.CUTTING)
                        .define('T', Items.IRON_INGOT).define('I', GrowableOresItems.IRON_PLATE)
                        .pattern("I I").pattern(" I ").pattern("T T")
                        .unlockedBy("has_item", this.has(Items.IRON_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.Coil)
                        .define('T', Items.IRON_INGOT).define('I', GrowableOresBlocks.COPPER_CABLE)
                        .pattern("III").pattern("ITI").pattern("III")
                        .unlockedBy("has_item", this.has(Items.IRON_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, GeneralBlocks.Machine)
                        .define('I', GrowableOresItems.IRON_PLATE)
                        .pattern("III").pattern("I I").pattern("III")
                        .unlockedBy("has_item", this.has(GrowableOresItems.IRON_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GeneralBlocks.Advanced_Machine)
                        .define('I', GrowableOresItems.STEEL_PLATE).define('M', GeneralBlocks.Machine).define('C', GrowableOresItems.CARBON_PLATE).define('A', GrowableOresItems.ALLOY_PLATE)
                        .pattern("ICI").pattern("AMA").pattern("ICI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.IRON_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GeneralBlocks.Super_Machine)
                        .define('I', GrowableOresItems.Advanced_Circuit).define('M', GeneralBlocks.Advanced_Machine).define('C', ModItemTags.Diamond).define('A', GrowableOresBlocks.GLASSFIBER_CABLE)
                        .pattern("ICI").pattern("AMA").pattern("ICI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.IRON_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.Electric_Motor)
                        .define('I', GrowableOresItems.Coil).define('C', GrowableOresItems.IRON_CASING).define('M', Items.IRON_INGOT)
                        .pattern(" C ").pattern("IMI").pattern(" C ")
                        .unlockedBy("has_item", this.has(Items.IRON_INGOT)).save(this.output);

                shapeless(RecipeCategory.MISC, GrowableOresItems.Scrap_box)
                        .requires(GrowableOresItems.Scrap,9)
                        .unlockedBy("has_item", this.has(GrowableOresItems.Scrap)).save(this.output);

                oreSmelting(List.of(GrowableOresItems.CRUSHED_COPPER, GrowableOresItems.PURIFIED_COPPER, GrowableOresItems.COPPER_DUST), RecipeCategory.MISC, CookingBookCategory.MISC, Items.COPPER_INGOT, 0.45F, 300, "copper_ingot");
                oreSmelting(List.of(GrowableOresItems.BRONZE_DUST), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.BRONZE_INGOT, 0.45F, 300, "bronze_ingot");
                oreSmelting(List.of(GeneralBlocks.SACRED_Ore, GeneralBlocks.Deepslate_SACRED_Ore, GrowableOresItems.CRUSHED_SACRED, GrowableOresItems.PURIFIED_SACRED), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.SACRED_INGOT, 0.45F, 300, "sacred_ingot");
                oreSmelting(List.of(GrowableOresItems.TIN_DUST, GeneralBlocks.Tin_Ore, GeneralBlocks.Deepslate_Tin_Ore, GrowableOresItems.CRUSHED_TIN, GrowableOresItems.PURIFIED_TIN), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.TIN_INGOT, 0.45F, 300, "tin_ingot");
                oreSmelting(List.of(GrowableOresItems.LEAD_DUST, GeneralBlocks.Lead_Ore, GeneralBlocks.Deepslate_Lead_Ore, GrowableOresItems.CRUSHED_LEAD, GrowableOresItems.PURIFIED_LEAD), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.LEAD_INGOT, 0.45F, 300, "lead_ingot");
                oreSmelting(List.of(GrowableOresItems.CRUSHED_SILVER, GrowableOresItems.PURIFIED_SILVER, GrowableOresItems.SILVER_DUST), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.SILVER_INGOT, 0.45F, 300, "silver_ingot");
                oreSmelting(List.of(GrowableOresItems.CRUSHED_IRON, GrowableOresItems.PURIFIED_IRON, GrowableOresItems.IRON_DUST), RecipeCategory.MISC, CookingBookCategory.MISC, Items.IRON_INGOT, 0.45F, 300, "iron_ingot");
                oreSmelting(List.of(GrowableOresItems.CRUSHED_GOLD, GrowableOresItems.PURIFIED_GOLD, GrowableOresItems.GOLD_DUST), RecipeCategory.MISC, CookingBookCategory.MISC, Items.GOLD_INGOT, 0.45F, 300, "gold_ingot");

                //Nuclear
                shaped(RecipeCategory.MISC, GrowableOresItems.SACRED_SHARD)
                        .define('U', GrowableOresItems.SACRED_ESSENCE).define('P', AdvancedItems.LUMINITE_ALLOY)
                        .pattern(" U ").pattern(" P ").pattern(" U ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.SACRED_ESSENCE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.SACRED_CORE)
                        .define('U', GrowableOresItems.SACRED_SHARD).define('P', AdvancedItems.ENRICHED_LUMINITE_ALLOY)
                        .pattern("UPU").pattern("   ").pattern("UPU")
                        .unlockedBy("has_item", this.has(GrowableOresItems.SACRED_SHARD)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.COOLANT_CELL_10K)
                        .define('C', GrowableOresItems.WATER_CELL).define('P', GrowableOresItems.TIN_PLATE)
                        .pattern(" P ").pattern("PCP").pattern(" P ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.WATER_CELL)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.COOLANT_CELL_30K)
                        .define('C', GrowableOresItems.COOLANT_CELL_10K).define('P', GrowableOresItems.TIN_PLATE)
                        .pattern("PPP").pattern("CCC").pattern("PPP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.TIN_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.COOLANT_CELL_60K)
                        .define('C', GrowableOresItems.COOLANT_CELL_10K).define('P', GrowableOresItems.TIN_PLATE)
                        .define('A', GrowableOresItems.IRON_PLATE)
                        .pattern("PCP").pattern("PAP").pattern("PCP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.TIN_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.HEAT_VENT)
                        .define('P', GrowableOresItems.IRON_PLATE)
                        .define('B', Items.IRON_BARS)
                        .define('C', GrowableOresItems.COPPER_PLATE)
                        .pattern("BPB").pattern("PCP").pattern("BPB")
                        .unlockedBy("has_item", this.has(GrowableOresItems.COPPER_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.ADVANCED_HEAT_VENT)
                        .define('D', Items.DIAMOND)
                        .define('H', GrowableOresItems.HEAT_VENT)
                        .define('B', Items.IRON_BARS)
                        .pattern("BHB").pattern("BDB").pattern("BHB")
                        .unlockedBy("has_item", this.has(Items.IRON_BARS)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.OVERCLOCKED_HEAT_VENT)
                        .define('H', GrowableOresItems.ADVANCED_HEAT_VENT)
                        .define('G', GrowableOresItems.GOLD_PLATE)
                        .pattern(" G ").pattern("GHG").pattern(" G ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.GOLD_PLATE)).save(this.output);

/*                shaped(RecipeCategory.MISC, GrowableOresItems.REACTOR_PLATING)
                        .define('P', GrowableOresItems.LEAD_PLATE).define('C', GrowableOresItems.CARBON_PLATE)
                        .pattern("PPP").pattern("PCP").pattern("PPP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.LEAD_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.NEUTRON_REFLECTOR)
                        .define('T', GrowableOresItems.TIN_PLATE).define('C', GrowableOresItems.COAL_DUST)
                        .pattern("TTT").pattern("CCC").pattern("TTT")
                        .unlockedBy("has_item", this.has(GrowableOresItems.COAL_DUST)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.HEAT_EXCHANGER)
                        .define('P', GrowableOresItems.COPPER_PLATE).define('V', GrowableOresItems.HEAT_VENT).define('C', GrowableOresItems.Circuit)
                        .pattern(" P ").pattern("VCV").pattern(" P ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.HEAT_VENT)).save(this.output);*/

                shaped(RecipeCategory.MISC, GrowableOresItems.HEAT_CONDUCTOR)
                        .define('P', GrowableOresItems.COPPER_PLATE).define('C', GrowableOresItems.Rubber)
                        .pattern("CPC").pattern("CPC").pattern("CPC")
                        .unlockedBy("has_item", this.has(GrowableOresItems.COPPER_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GeneralBlocks.Reinforced_Glass,7)
                        .define('P', Blocks.GLASS).define('C', GrowableOresItems.ALLOY_PLATE)
                        .pattern("PPP").pattern("CPC").pattern("PPP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.ALLOY_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GeneralBlocks.Reinforced_Stone,8)
                        .define('P', Blocks.STONE)
                        .define('C', GrowableOresItems.ALLOY_PLATE)
                        .pattern("PPP").pattern("PCP").pattern("PPP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.ALLOY_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GeneralBlocks.Reinforced_DOOR)
                        .define('P', GrowableOresItems.IRON_PLATE)
                        .define('C', GrowableOresItems.LEAD_PLATE)
                        .pattern("PCP").pattern("PCP").pattern("PCP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.LEAD_PLATE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL)
                        .define('I', GrowableOresItems.SILICON_DIOXIDE_DUST)
                        .define('E', GrowableOresItems.OBSIDIAN_DUST)
                        .pattern("IEI").pattern("EIE").pattern("IEI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.SILICON_DIOXIDE_DUST))
                        .save(this.output);

                oreSmelting(List.of(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL), RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.PATTERN_STORAGE_CRYSTAL, 0.45F, 300, "pattern_storage_crystal");

                shaped(RecipeCategory.BUILDING_BLOCKS, GeneralBlocks.PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', Blocks.SAND)
                        .define('i', Blocks.BAMBOO_BLOCK)
                        .unlockedBy(getHasName(Blocks.SAND),
                                has(Blocks.SAND))
                        .unlockedBy(getHasName(Blocks.BAMBOO_BLOCK),
                                has(Blocks.BAMBOO_BLOCK))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.GREEN_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.GREEN_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.GREEN_DYE),
                                has(Items.GREEN_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.ORANGE_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.ORANGE_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.ORANGE_DYE),
                                has(Items.ORANGE_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.MAGENTA_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.MAGENTA_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.MAGENTA_DYE),
                                has(Items.MAGENTA_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.LIGHT_BLUE_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.LIGHT_BLUE_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.LIGHT_BLUE_DYE),
                                has(Items.LIGHT_BLUE_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.YELLOW_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.YELLOW_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.YELLOW_DYE),
                                has(Items.YELLOW_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.LIME_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.LIME_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.LIME_DYE),
                                has(Items.LIME_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.PINK_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.PINK_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.PINK_DYE),
                                has(Items.PINK_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.GRAY_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.GRAY_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.GRAY_DYE),
                                has(Items.GRAY_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.LIGHT_GRAY_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.LIGHT_GRAY_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.LIGHT_GRAY_DYE),
                                has(Items.LIGHT_GRAY_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.CYAN_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.CYAN_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.CYAN_DYE),
                                has(Items.CYAN_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.PURPLE_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.PURPLE_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.PURPLE_DYE),
                                has(Items.PURPLE_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.BLUE_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.BLUE_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.BLUE_DYE),
                                has(Items.BLUE_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.BROWN_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.BROWN_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.BROWN_DYE),
                                has(Items.BROWN_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.RED_PLASTER,8).pattern("bbb").pattern("bib").pattern("bbb")
                        .define('b', GeneralBlocks.PLASTER)
                        .define('i', Items.GREEN_DYE)
                        .unlockedBy(getHasName(GeneralBlocks.PLASTER),
                                has(GeneralBlocks.PLASTER))
                        .unlockedBy(getHasName(Items.RED_DYE),
                                has(Items.RED_DYE))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS ,GeneralBlocks.COBBLESTONE_STAIRS,6)
                        .pattern("b  ")
                        .pattern("bi ")
                        .pattern("bbb")
                        .define('b', Blocks.COBBLESTONE)
                        .define('i', Items.BLUE_DYE)
                        .unlockedBy(getHasName(Items.BLUE_DYE),
                                has(Items.BLUE_DYE))
                        .save(output);

            }
        };
    }

    @Override
    public String getName() {
        return "Material";
    }
}