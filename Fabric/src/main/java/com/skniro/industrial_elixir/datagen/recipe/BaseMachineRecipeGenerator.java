package com.skniro.industrial_elixir.datagen.recipe;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleFoodComponents;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BaseMachineRecipeGenerator extends FabricRecipeProvider {
    public BaseMachineRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, GrowableOresBlocks.COAL_GENERATOR)
                        .define('G', GrowableOresBlocks.Iron_Furnace_Block).define('E', GrowableOresItems.RE_BATTERY).define('S', GrowableOresItems.IRON_PLATE)
                        .pattern(" E ").pattern("SSS").pattern(" G ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.RE_BATTERY)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.COAL_GENERATOR)
                        .define('G', Items.FURNACE).define('E', GrowableOresItems.RE_BATTERY).define('S', GeneralBlocks.Machine)
                        .pattern(" E ").pattern(" S ").pattern(" G ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.RE_BATTERY)).save(this.output,"base_machine_to_coal_generator");

                shaped(RecipeCategory.MISC, GrowableOresBlocks.NUCLEAR_REACTOR)
                        .define('M', GeneralBlocks.Advanced_Machine).define('C', GrowableOresItems.Advanced_Circuit).define('P', GrowableOresItems.REACTOR_PLATING).define('U', GrowableOresItems.SACRED_INGOT)
                        .pattern("PCP").pattern("UMU").pattern("PCP")
                        .unlockedBy("has_item", this.has(GrowableOresItems.SACRED_INGOT)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_SolarPanel)
                        .define('G', Items.GLASS).define('E', GrowableOresItems.COAL_DUST).define('S', GrowableOresBlocks.COAL_GENERATOR).define('C', GrowableOresItems.Circuit)
                        .pattern("EGE").pattern("GEG").pattern("CSC")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.COAL_GENERATOR)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.GENERATOR_Wind_Mill)
                        .define('G', Items.IRON_INGOT).define('S', GrowableOresBlocks.COAL_GENERATOR)
                        .pattern("G G").pattern(" S ").pattern("G G")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.COAL_GENERATOR)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Compressor_Block)
                        .define('G', Items.STONE).define('S', GeneralBlocks.Machine).define('C', GrowableOresItems.Circuit)
                        .pattern("G G").pattern("GSG").pattern("GCG")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine)).save(this.output);

                //临时配方
                shaped(RecipeCategory.MISC, GrowableOresBlocks.Extractor_Block)
                        .define('G', GrowableOresItems.Sticky_Resin).define('S', GeneralBlocks.Machine).define('C', GrowableOresItems.Circuit)
                        .pattern("   ").pattern("GSG").pattern("GCG")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Iron_Furnace_Block)
                        .define('G', GrowableOresItems.IRON_PLATE).define('C', Items.FURNACE)
                        .pattern(" G ").pattern("G G").pattern("GCG")
                        .unlockedBy("has_item", this.has(Items.FURNACE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Macerator_Block)
                        .define('G', Items.STONE).define('S', GeneralBlocks.Machine).define('C', GrowableOresItems.Circuit).define('F', Items.FLINT)
                        .pattern("FFF").pattern("GSG").pattern(" C ")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MetalFormerBlock)
                        .define('G', GrowableOresItems.BRONZE_CASING).define('S', GeneralBlocks.Machine).define('C', GrowableOresItems.Circuit).define('F', GrowableOresItems.Coil)
                        .pattern(" C ").pattern("GSG").pattern("FFF")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.ChargePad)
                        .define('G', Items.STONE_PRESSURE_PLATE).define('S', GrowableOresBlocks.EnergyBox).define('C', GrowableOresItems.Circuit).define('F', GrowableOresItems.Rubber)
                        .pattern("   ").pattern("CGC").pattern("FSF")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.EnergyBox)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.CESU_CHARGE_PAD)
                        .define('G', Items.STONE_PRESSURE_PLATE).define('S', GrowableOresBlocks.CESU).define('C', GrowableOresItems.Circuit).define('F', GrowableOresItems.Rubber)
                        .pattern("   ").pattern("CGC").pattern("FSF")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.CESU)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MFE_CHARGE_PAD)
                        .define('G', Items.STONE_PRESSURE_PLATE).define('S', GrowableOresBlocks.MFE).define('C', GrowableOresItems.Circuit).define('F', GrowableOresItems.Rubber)
                        .pattern("   ").pattern("CGC").pattern("FSF")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.MFE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MFSU_CHARGE_PAD)
                        .define('G', Items.STONE_PRESSURE_PLATE).define('S', GrowableOresBlocks.MFSU).define('C', GrowableOresItems.Circuit).define('F', GrowableOresItems.Rubber)
                        .pattern("   ").pattern("CGC").pattern("FSF")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.MFSU)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.EnergyBox)
                        .define('G', ItemTags.PLANKS).define('S', GrowableOresBlocks.INSULATED_TIN_CABLE).define('F', GrowableOresItems.RE_BATTERY)
                        .pattern("GSG").pattern("FFF").pattern("GGG")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_TIN_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.CESU)
                        .define('G', GrowableOresItems.BRONZE_PLATE).define('S', GrowableOresBlocks.INSULATED_COPPER_CABLE).define('F', GrowableOresItems.ADVANCED_RE_BATTERY)
                        .pattern("GSG").pattern("FFF").pattern("GGG")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_COPPER_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MFE)
                        .define('G', GrowableOresItems.ENERGY_CRYSTAL).define('S', GrowableOresBlocks.INSULATED_GOLD_CABLE).define('F', GeneralBlocks.Machine)
                        .pattern("SGS").pattern("GFG").pattern("SGS")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_GOLD_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MFSU)
                        .define('G', GrowableOresItems.LAPOTRON_CRYSTAL).define('S', GeneralBlocks.Advanced_Machine).define('F', GrowableOresBlocks.MFE).define('C', GrowableOresItems.Advanced_Circuit)
                        .pattern("GCG").pattern("GFG").pattern("GSG")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.MFE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.LV_TRANSFORMER)
                        .define('G', ItemTags.PLANKS).define('F', GrowableOresBlocks.INSULATED_TIN_CABLE).define('C', GrowableOresItems.Coil)
                        .pattern("GFG").pattern("GCG").pattern("GFG")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_TIN_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MV_TRANSFORMER)
                        .define('G', GeneralBlocks.Machine).define('F', GrowableOresBlocks.INSULATED_COPPER_CABLE)
                        .pattern(" F ").pattern(" G ").pattern(" F ")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_COPPER_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.HV_TRANSFORMER)
                        .define('G', GrowableOresBlocks.MV_TRANSFORMER).define('F', GrowableOresBlocks.INSULATED_GOLD_CABLE).define('C', GrowableOresItems.Circuit).define('R', GrowableOresItems.ADVANCED_RE_BATTERY)
                        .pattern(" F ").pattern("CGR").pattern(" F ")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_GOLD_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.EV_TRANSFORMER)
                        .define('G', GrowableOresBlocks.HV_TRANSFORMER).define('F', GrowableOresBlocks.INSULATED_HV_CABLE).define('C', GrowableOresItems.Advanced_Circuit).define('R', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .pattern(" F ").pattern("CGR").pattern(" F ")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.INSULATED_HV_CABLE)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.ElectricFurnace_Block)
                        .define('G', GrowableOresBlocks.Iron_Furnace_Block).define('C', GrowableOresItems.Circuit).define('R', Items.REDSTONE)
                        .pattern("   ").pattern(" C ").pattern("RGR")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.Iron_Furnace_Block)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.INDUCTION_FURNACE)
                        .define('E', GrowableOresBlocks.ElectricFurnace_Block).define('A', GrowableOresItems.Advanced_Circuit).define('C', GrowableOresItems.COPPER_PLATE).define('M', GeneralBlocks.Advanced_Machine)
                        .pattern("CAC").pattern("EAE").pattern("CMC")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.ElectricFurnace_Block)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.RECYCLER_Block)
                        .define('G', GrowableOresBlocks.Compressor_Block).define('C', Items.GLOWSTONE_DUST).define('R', Items.IRON_INGOT).define('D', ItemTags.DIRT)
                        .pattern(" C ").pattern("DGD").pattern("RDR")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.Compressor_Block)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.CUTTING_Block)
                        .define('G', GeneralBlocks.Machine).define('C', GrowableOresItems.Circuit).define('R', GrowableOresItems.Electric_Motor)
                        .pattern(" C ").pattern(" G ").pattern(" R ")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit)).save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Ore_Washing_Block)
                        .define('G', GeneralBlocks.Machine).define('I', GrowableOresItems.IRON_PLATE).define('B', Items.BUCKET).define('C', GrowableOresItems.Circuit).define('R', GrowableOresItems.Electric_Motor)
                        .pattern("III").pattern("BGB").pattern("RCR")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Brew_Reactor_BLOCK)
                        .define('G', Blocks.BREWING_STAND).define('I', GrowableOresItems.IRON_CASING).define('C', GrowableOresItems.Circuit)
                        .pattern("III").pattern("IGI").pattern("ICI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.Circuit))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.BLAST_FURNACE_BLOCK)
                        .define('G', GeneralBlocks.Machine).define('I', GrowableOresItems.IRON_CASING).define('C', GrowableOresItems.HEAT_CONDUCTOR)
                        .pattern("III").pattern("IGI").pattern("ICI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.IRON_CASING))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.HEAT_CENTRIFUGE)
                        .define('G', GeneralBlocks.Advanced_Machine).define('I', Items.IRON_INGOT).define('L', GrowableOresItems.LAPOTRON_CRYSTAL).define('M', GrowableOresItems.Electric_Motor).define('C', GrowableOresItems.Coil)
                        .pattern("CLC").pattern("IGI").pattern("IMI")
                        .unlockedBy("has_item", this.has(Items.IRON_INGOT))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.MATTER_GENERATOR)
                        .define('G', GeneralBlocks.Advanced_Machine)
                        .define('I', Items.GLOWSTONE)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .define('C', GrowableOresItems.Advanced_Circuit)
                        .pattern("ICI").pattern("GLG").pattern("ICI")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Advanced_Machine))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Replicator)
                        .define('R', GeneralBlocks.Reinforced_Stone)
                        .define('G', GeneralBlocks.Reinforced_Glass)
                        .define('S', GeneralBlocks.Super_Machine)
                        .define('D', GrowableOresBlocks.MFE)
                        .define('C', GrowableOresBlocks.HV_TRANSFORMER)
                        .pattern("RGR").pattern("SSS").pattern("CDC")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Super_Machine))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.PATTERN_STORAGE)
                        .define('R', GeneralBlocks.Reinforced_Stone)
                        .define('P', GrowableOresItems.PATTERN_STORAGE_CRYSTAL)
                        .define('M', GeneralBlocks.Advanced_Machine)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .define('C', GrowableOresItems.Advanced_Circuit)
                        .pattern("RRR").pattern("PMP").pattern("LCL")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Advanced_Machine))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.CHUNK_LOADER)
                        .define('E', Items.ENDER_PEARL)
                        .define('T', GrowableOresItems.TIN_PLATE)
                        .define('M', GeneralBlocks.Machine)
                        .define('L', Items.LAPIS_LAZULI)
                        .define('C', GrowableOresItems.Circuit)
                        .pattern("TET").pattern("LML").pattern("TCT")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.FLUID_GENERATOR)
                        .define('G', Items.GLASS)
                        .define('T', GrowableOresItems.EMPTY_CELL)
                        .define('M', GrowableOresBlocks.COAL_GENERATOR)
                        .define('I', GrowableOresItems.IRON_CASING)
                        .pattern("GTG").pattern("GTG").pattern("IMI")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.COAL_GENERATOR))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Electric_Heater_Block)
                        .define('B', GrowableOresItems.RE_BATTERY)
                        .define('I', GrowableOresItems.IRON_CASING)
                        .define('C', GrowableOresItems.Circuit)
                        .define('H', GrowableOresItems.HEAT_CONDUCTOR)
                        .pattern("IBI").pattern("ICI").pattern("IHI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.RE_BATTERY))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR)
                        .define('I', GrowableOresItems.IRON_PLATE)
                        .define('C', GrowableOresBlocks.Iron_Furnace_Block)
                        .define('H', GrowableOresItems.HEAT_CONDUCTOR)
                        .pattern(" H ").pattern("III").pattern(" C ")
                        .unlockedBy("has_item", this.has(GrowableOresBlocks.Iron_Furnace_Block))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Pipe_Stone_Fluid_Block, 8)
                        .define('G', Items.GLASS)
                        .define('I', GrowableOresItems.Sticky_Resin)
                        .define('C', Blocks.STONE)
                        .pattern("   ").pattern("CGC").pattern(" I ")
                        .unlockedBy("has_item", this.has(Blocks.STONE))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.Pipe_Wooden_Fluid_Block, 8)
                        .define('G', Items.GLASS)
                        .define('I', GrowableOresItems.Sticky_Resin)
                        .define('C', ItemTags.PLANKS)
                        .pattern("   ").pattern("CGC").pattern(" I ")
                        .unlockedBy("has_item", this.has(Blocks.STONE))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.FLUID_TANK_BLOCK)
                        .define('I', GrowableOresItems.IRON_PLATE)
                        .define('E', GrowableOresItems.EMPTY_CELL)
                        .pattern("IEI").pattern("E E").pattern("IEI")
                        .unlockedBy("has_item", this.has(GrowableOresItems.EMPTY_CELL))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.SACRED_GENERATOR)
                        .define('C', GrowableOresItems.Advanced_Circuit)
                        .define('L', GrowableOresItems.DENSE_LEAD_PLATE)
                        .define('G', GrowableOresBlocks.COAL_GENERATOR)
                        .define('A', GeneralBlocks.Advanced_Machine)
                        .define('M', GeneralBlocks.Machine)
                        .pattern("LCL").pattern("MAM").pattern("LGL")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.CROP_FARM_Block)
                        .define('C', GrowableOresItems.Advanced_Circuit)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .define('A', GrowableOresItems.ALLOY_PLATE)
                        .define('S', GeneralBlocks.Super_Machine)
                        .define('M', GeneralBlocks.Advanced_Machine)
                        .pattern("ACA").pattern("MSM").pattern("ALA")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Super_Machine))
                        .save(this.output);

                shaped(RecipeCategory.MISC, GrowableOresBlocks.COFFEE_MACHINE_Block)
                        .define('C', MapleFoodComponents.Coffee_Beans)
                        .define('L', GrowableOresItems.RE_BATTERY)
                        .define('A', GrowableOresItems.IRON_CASING)
                        .define('I', GrowableOresItems.IRON_PLATE)
                        .define('M', GeneralBlocks.Machine)
                        .pattern("ACA").pattern("IMI").pattern("ALA")
                        .unlockedBy("has_item", this.has(GeneralBlocks.Machine))
                        .save(this.output);
            }
        };
    }

    @Override
    public String getName() {
        return "BaseMachine";
    }
}
