package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.tags.BlockTags.*;

public class IndustrialElixirBlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
   public IndustrialElixirBlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
   }
   @Override
   protected void addTags(HolderLookup.Provider arg) {
      valueLookupBuilder(MINEABLE_WITH_PICKAXE)
              .add(GeneralBlocks.Deepslate_Lead_Ore)
              .add(GeneralBlocks.Deepslate_Tin_Ore)
              .add(GeneralBlocks.Deepslate_SACRED_Ore)
              .add(GeneralBlocks.Lead_Ore)
              .add(GeneralBlocks.Tin_Ore)
              .add(GeneralBlocks.SACRED_Ore)
              .add(GrowableOresBlocks.GrowableOres_Block)
              .add(GrowableOresBlocks.Macerator_Block,
                      GrowableOresBlocks.Compressor_Block,
                      GrowableOresBlocks.MetalFormerBlock,

                      GrowableOresBlocks.COPPER_CABLE,
                      GrowableOresBlocks.TIN_CABLE,
                      GrowableOresBlocks.GOLD_CABLE,
                      GrowableOresBlocks.HV_CABLE,
                      GrowableOresBlocks.GLASSFIBER_CABLE,

                      GrowableOresBlocks.INSULATED_TIN_CABLE,
                      GrowableOresBlocks.INSULATED_COPPER_CABLE,
                      GrowableOresBlocks.INSULATED_GOLD_CABLE,
                      GrowableOresBlocks.INSULATED_HV_CABLE,

                      GrowableOresBlocks.COAL_GENERATOR,
                      GrowableOresBlocks.GENERATOR_Wind_Mill,

                      GrowableOresBlocks.EnergyBox,
                      GrowableOresBlocks.ChargePad,
                      GrowableOresBlocks.CESU,
                      GrowableOresBlocks.MFE,
                      GrowableOresBlocks.MFSU,

                      GrowableOresBlocks.CESU_CHARGE_PAD,
                      GrowableOresBlocks.MFE_CHARGE_PAD,
                      GrowableOresBlocks.MFSU_CHARGE_PAD,

                      GrowableOresBlocks.MolecularTransformerBlock,

                      GrowableOresBlocks.GENERATOR_SolarPanel,
                      GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL,
                      GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL,
                      GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL,
                      GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL,

                      GrowableOresBlocks.Extractor_Block,

                      GrowableOresBlocks.EV_TRANSFORMER,
                      GrowableOresBlocks.HV_TRANSFORMER,
                      GrowableOresBlocks.MV_TRANSFORMER,
                      GrowableOresBlocks.LV_TRANSFORMER,

                      GrowableOresBlocks.Iron_Furnace_Block,
                      GeneralBlocks.Raw_Lead_Block,
                      GeneralBlocks.Raw_Tin_Block,
                      GeneralBlocks.Raw_SACRED_Block,

                      GeneralBlocks.Lead_Block,
                      GeneralBlocks.Tin_Block,
                      GeneralBlocks.SACRED_Block,
                      GeneralBlocks.Silver_Block,
                      GeneralBlocks.Bronze_Block,
                      GeneralBlocks.Steel_Block,

                      GeneralBlocks.Advanced_Machine,
                      GeneralBlocks.Machine,
                      GrowableOresBlocks.ElectricFurnace_Block,
                      GrowableOresBlocks.RECYCLER_Block,
                      GrowableOresBlocks.CUTTING_Block,

                      GrowableOresBlocks.MATTER_GENERATOR,
                      GrowableOresBlocks.FLUID_GENERATOR,
                      GrowableOresBlocks.NUCLEAR_REACTOR,
                      GrowableOresBlocks.SACRED_GENERATOR,
                      GrowableOresBlocks.INDUCTION_FURNACE,
                      GrowableOresBlocks.HEAT_CENTRIFUGE,
                      GrowableOresBlocks.PATTERN_STORAGE,

                      GrowableOresBlocks.Pipe_Wooden_Iten_Block,
                      GrowableOresBlocks.Pipe_Stone_Item_Block,
                      GrowableOresBlocks.Pipe_Wooden_Fluid_Block,
                      GrowableOresBlocks.Pipe_Stone_Fluid_Block,

                      GrowableOresBlocks.FLUID_TANK_BLOCK,
                      GrowableOresBlocks.Brew_Reactor_BLOCK,
                      GrowableOresBlocks.Ore_Washing_Block,
                      GrowableOresBlocks.Replicator,

                      GrowableOresBlocks.CHUNK_LOADER,
                      GrowableOresBlocks.Electric_Heater_Block,
                      GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR,
                      GrowableOresBlocks.BLAST_FURNACE_BLOCK)
              .setReplace(false);
      valueLookupBuilder(NEEDS_IRON_TOOL)
              .add(GeneralBlocks.Deepslate_Lead_Ore)
              .add(GeneralBlocks.Deepslate_SACRED_Ore)
              .add(GeneralBlocks.Lead_Ore)
              .add(GeneralBlocks.SACRED_Ore)
              .add(GrowableOresBlocks.Macerator_Block,
                      GrowableOresBlocks.Compressor_Block,
                      GrowableOresBlocks.MetalFormerBlock,

                      GrowableOresBlocks.GOLD_CABLE,
                      GrowableOresBlocks.HV_CABLE,
                      GrowableOresBlocks.GLASSFIBER_CABLE,

                      GrowableOresBlocks.INSULATED_TIN_CABLE,
                      GrowableOresBlocks.INSULATED_COPPER_CABLE,
                      GrowableOresBlocks.INSULATED_GOLD_CABLE,
                      GrowableOresBlocks.INSULATED_HV_CABLE,

                      GrowableOresBlocks.COAL_GENERATOR,
                      GrowableOresBlocks.GENERATOR_Wind_Mill,

                      GrowableOresBlocks.CESU,
                      GrowableOresBlocks.MFE,
                      GrowableOresBlocks.MFSU,

                      GrowableOresBlocks.CESU_CHARGE_PAD,
                      GrowableOresBlocks.MFE_CHARGE_PAD,
                      GrowableOresBlocks.MFSU_CHARGE_PAD,

                      GrowableOresBlocks.MolecularTransformerBlock,

                      GrowableOresBlocks.GENERATOR_SolarPanel,
                      GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL,
                      GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL,
                      GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL,
                      GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL,

                      GrowableOresBlocks.Extractor_Block,

                      GrowableOresBlocks.HV_TRANSFORMER,
                      GrowableOresBlocks.MV_TRANSFORMER,
                      GrowableOresBlocks.LV_TRANSFORMER,

                      GrowableOresBlocks.Iron_Furnace_Block,
                      GeneralBlocks.Raw_Lead_Block,
                      GeneralBlocks.Raw_SACRED_Block,

                      GeneralBlocks.Lead_Block,
                      GeneralBlocks.SACRED_Block,
                      GeneralBlocks.Silver_Block,
                      GeneralBlocks.Bronze_Block,
                      GeneralBlocks.Steel_Block,

                      GrowableOresBlocks.ElectricFurnace_Block,
                      GrowableOresBlocks.RECYCLER_Block,
                      GrowableOresBlocks.CUTTING_Block,

                      GrowableOresBlocks.MATTER_GENERATOR,
                      GrowableOresBlocks.FLUID_GENERATOR,
                      GrowableOresBlocks.NUCLEAR_REACTOR,
                      GrowableOresBlocks.SACRED_GENERATOR,
                      GrowableOresBlocks.INDUCTION_FURNACE,
                      GrowableOresBlocks.HEAT_CENTRIFUGE,
                      GrowableOresBlocks.PATTERN_STORAGE,

                      GrowableOresBlocks.Pipe_Wooden_Iten_Block,
                      GrowableOresBlocks.Pipe_Stone_Item_Block,
                      GrowableOresBlocks.Pipe_Wooden_Fluid_Block,
                      GrowableOresBlocks.Pipe_Stone_Fluid_Block,

                      GrowableOresBlocks.FLUID_TANK_BLOCK,
                      GrowableOresBlocks.Brew_Reactor_BLOCK,
                      GrowableOresBlocks.Ore_Washing_Block,
                      GrowableOresBlocks.Replicator,

                      GrowableOresBlocks.CHUNK_LOADER,
                      GrowableOresBlocks.Electric_Heater_Block,
                      GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR,
                      GrowableOresBlocks.BLAST_FURNACE_BLOCK)
              .setReplace(false);
      valueLookupBuilder(NEEDS_STONE_TOOL)
              .add(GeneralBlocks.Deepslate_Tin_Ore)
              .add(GeneralBlocks.Tin_Ore)
              .add(GeneralBlocks.Advanced_Machine,
                      GeneralBlocks.Machine,
                      GeneralBlocks.Tin_Block,
                      GeneralBlocks.Raw_Tin_Block,
                      GrowableOresBlocks.TIN_CABLE,
                      GrowableOresBlocks.COPPER_CABLE,
                      GrowableOresBlocks.EV_TRANSFORMER,
                      GrowableOresBlocks.EnergyBox,
                      GrowableOresBlocks.ChargePad)
              .setReplace(false);
      valueLookupBuilder(BlockTags.LEAVES)
              .add(GeneralBlocks.Rubber_LEAVES)
              .setReplace(false);
      valueLookupBuilder(BlockTags.SAPLINGS)
              .add(GeneralBlocks.Rubber_SAPLING)
              .setReplace(false);
      valueLookupBuilder(LOGS_THAT_BURN)
              .add(GeneralBlocks.Rubber_LOG)
              .add(GeneralBlocks.Rubber_Rubber_LOG)
              .add(GeneralBlocks.Rubber_WOOD)
              .add(GeneralBlocks.STRIPPED_Rubber_LOG)
              .add(GeneralBlocks.STRIPPED_Rubber_WOOD)
              .setReplace(false);
      valueLookupBuilder(WOODEN_BUTTONS)
              .add(GeneralBlocks.Rubber_BUTTON)
              .setReplace(false);
      valueLookupBuilder(WOODEN_DOORS)
              .add(GeneralBlocks.Rubber_DOOR)
              .setReplace(false);
      valueLookupBuilder(WOODEN_TRAPDOORS)
              .add(GeneralBlocks.Rubber_TRAPDOOR)
              .setReplace(false);
      valueLookupBuilder(WOODEN_PRESSURE_PLATES)
              .add(GeneralBlocks.Rubber_PRESSURE_PLATE)
              .setReplace(false);
      valueLookupBuilder(WOODEN_SLABS)
              .add(GeneralBlocks.Rubber_SLAB)
              .setReplace(false);
      valueLookupBuilder(WOODEN_STAIRS)
              .add(GeneralBlocks.Rubber_STAIRS)
              .setReplace(false);
      valueLookupBuilder(PLANKS)
              .add(GeneralBlocks.Rubber_PLANKS)
              .setReplace(false);
      valueLookupBuilder(FENCES)
              .add(GeneralBlocks.Rubber_FENCE)
              .setReplace(false);
      valueLookupBuilder(DOORS)
              .add(GeneralBlocks.Reinforced_DOOR)
              .setReplace(false);


   }
}