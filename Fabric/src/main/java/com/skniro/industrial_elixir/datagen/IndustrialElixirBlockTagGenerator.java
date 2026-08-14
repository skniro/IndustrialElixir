package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.tags.BlockItemTags.LOGS_THAT_BURN;
import static net.minecraft.tags.BlockTags.*;

public class IndustrialElixirBlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
   public IndustrialElixirBlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
   }
   @Override
   protected void addTags(HolderLookup.Provider arg) {
      builder(MINEABLE_WITH_PICKAXE)
              .add(GeneralBlocks.Deepslate_Lead_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Deepslate_Tin_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Deepslate_SACRED_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Lead_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Tin_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.SACRED_Ore.builtInRegistryHolder().key())
              .add(GrowableOresBlocks.GrowableOres_Block.builtInRegistryHolder().key())
              .add(GrowableOresBlocks.Macerator_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Compressor_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MetalFormerBlock.builtInRegistryHolder().key(),

                      GrowableOresBlocks.COPPER_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.TIN_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GOLD_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.HV_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GLASSFIBER_CABLE.builtInRegistryHolder().key(),

                      GrowableOresBlocks.INSULATED_TIN_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INSULATED_COPPER_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INSULATED_GOLD_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INSULATED_HV_CABLE.builtInRegistryHolder().key(),

                      GrowableOresBlocks.COAL_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_Wind_Mill.builtInRegistryHolder().key(),

                      GrowableOresBlocks.EnergyBox.builtInRegistryHolder().key(),
                      GrowableOresBlocks.ChargePad.builtInRegistryHolder().key(),
                      GrowableOresBlocks.CESU.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFSU.builtInRegistryHolder().key(),

                      GrowableOresBlocks.CESU_CHARGE_PAD.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFE_CHARGE_PAD.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFSU_CHARGE_PAD.builtInRegistryHolder().key(),

                      GrowableOresBlocks.MolecularTransformerBlock.builtInRegistryHolder().key(),

                      GrowableOresBlocks.GENERATOR_SolarPanel.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL.builtInRegistryHolder().key(),

                      GrowableOresBlocks.Extractor_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.EV_TRANSFORMER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.HV_TRANSFORMER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MV_TRANSFORMER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.LV_TRANSFORMER.builtInRegistryHolder().key(),

                      GrowableOresBlocks.Iron_Furnace_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Raw_Lead_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Raw_Tin_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Raw_SACRED_Block.builtInRegistryHolder().key(),

                      GeneralBlocks.Lead_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Tin_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.SACRED_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Silver_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Bronze_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Steel_Block.builtInRegistryHolder().key(),

                      GeneralBlocks.Advanced_Machine.builtInRegistryHolder().key(),
                      GeneralBlocks.Machine.builtInRegistryHolder().key(),
                      GrowableOresBlocks.ElectricFurnace_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.RECYCLER_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.CUTTING_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.MATTER_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.FLUID_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.NUCLEAR_REACTOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.SACRED_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INDUCTION_FURNACE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.HEAT_CENTRIFUGE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.PATTERN_STORAGE.builtInRegistryHolder().key(),

                      GrowableOresBlocks.Pipe_Wooden_Iten_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Pipe_Stone_Item_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Pipe_Wooden_Fluid_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Pipe_Stone_Fluid_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.FLUID_TANK_BLOCK.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Brew_Reactor_BLOCK.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Ore_Washing_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Replicator.builtInRegistryHolder().key(),

                      GrowableOresBlocks.CHUNK_LOADER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Electric_Heater_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.BLAST_FURNACE_BLOCK.builtInRegistryHolder().key(),
                      GrowableOresBlocks.VENDOR_MACHINE_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.CROP_FARM_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.COFFEE_MACHINE_Block.builtInRegistryHolder().key())
              .setReplace(false);
      builder(NEEDS_IRON_TOOL)
              .add(GeneralBlocks.Deepslate_Lead_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Deepslate_SACRED_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Lead_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.SACRED_Ore.builtInRegistryHolder().key())
              .add(GrowableOresBlocks.Macerator_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Compressor_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MetalFormerBlock.builtInRegistryHolder().key(),

                      GrowableOresBlocks.GOLD_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.HV_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GLASSFIBER_CABLE.builtInRegistryHolder().key(),

                      GrowableOresBlocks.INSULATED_TIN_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INSULATED_COPPER_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INSULATED_GOLD_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INSULATED_HV_CABLE.builtInRegistryHolder().key(),

                      GrowableOresBlocks.COAL_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_Wind_Mill.builtInRegistryHolder().key(),

                      GrowableOresBlocks.CESU.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFSU.builtInRegistryHolder().key(),

                      GrowableOresBlocks.CESU_CHARGE_PAD.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFE_CHARGE_PAD.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MFSU_CHARGE_PAD.builtInRegistryHolder().key(),

                      GrowableOresBlocks.MolecularTransformerBlock.builtInRegistryHolder().key(),

                      GrowableOresBlocks.GENERATOR_SolarPanel.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL.builtInRegistryHolder().key(),
                      GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL.builtInRegistryHolder().key(),

                      GrowableOresBlocks.Extractor_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.HV_TRANSFORMER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.MV_TRANSFORMER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.LV_TRANSFORMER.builtInRegistryHolder().key(),

                      GrowableOresBlocks.Iron_Furnace_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Raw_Lead_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Raw_SACRED_Block.builtInRegistryHolder().key(),

                      GeneralBlocks.Lead_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.SACRED_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Silver_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Bronze_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Steel_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.ElectricFurnace_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.RECYCLER_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.CUTTING_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.MATTER_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.FLUID_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.NUCLEAR_REACTOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.SACRED_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.INDUCTION_FURNACE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.HEAT_CENTRIFUGE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.PATTERN_STORAGE.builtInRegistryHolder().key(),

                      GrowableOresBlocks.Pipe_Wooden_Iten_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Pipe_Stone_Item_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Pipe_Wooden_Fluid_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Pipe_Stone_Fluid_Block.builtInRegistryHolder().key(),

                      GrowableOresBlocks.FLUID_TANK_BLOCK.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Brew_Reactor_BLOCK.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Ore_Washing_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Replicator.builtInRegistryHolder().key(),

                      GrowableOresBlocks.CHUNK_LOADER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.Electric_Heater_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR.builtInRegistryHolder().key(),
                      GrowableOresBlocks.BLAST_FURNACE_BLOCK.builtInRegistryHolder().key(),
                      GrowableOresBlocks.VENDOR_MACHINE_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.CROP_FARM_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.COFFEE_MACHINE_Block.builtInRegistryHolder().key())
              .setReplace(false);
      builder(NEEDS_STONE_TOOL)
              .add(GeneralBlocks.Deepslate_Tin_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Tin_Ore.builtInRegistryHolder().key())
              .add(GeneralBlocks.Advanced_Machine.builtInRegistryHolder().key(),
                      GeneralBlocks.Machine.builtInRegistryHolder().key(),
                      GeneralBlocks.Tin_Block.builtInRegistryHolder().key(),
                      GeneralBlocks.Raw_Tin_Block.builtInRegistryHolder().key(),
                      GrowableOresBlocks.TIN_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.COPPER_CABLE.builtInRegistryHolder().key(),
                      GrowableOresBlocks.EV_TRANSFORMER.builtInRegistryHolder().key(),
                      GrowableOresBlocks.EnergyBox.builtInRegistryHolder().key(),
                      GrowableOresBlocks.ChargePad.builtInRegistryHolder().key())
              .setReplace(false);
      builder(BlockTags.LEAVES)
              .add(GeneralBlocks.Rubber_LEAVES.builtInRegistryHolder().key())
              .setReplace(false);
      builder(BlockItemTags.SAPLINGS.block())
              .add(GeneralBlocks.Rubber_SAPLING.builtInRegistryHolder().key())
              .setReplace(false);
      builder(LOGS_THAT_BURN.block())
              .add(GeneralBlocks.Rubber_LOG.builtInRegistryHolder().key())
              .add(GeneralBlocks.Rubber_Rubber_LOG.builtInRegistryHolder().key())
              .add(GeneralBlocks.Rubber_WOOD.builtInRegistryHolder().key())
              .add(GeneralBlocks.STRIPPED_Rubber_LOG.builtInRegistryHolder().key())
              .add(GeneralBlocks.STRIPPED_Rubber_WOOD.builtInRegistryHolder().key())
              .setReplace(false);
      builder(WOODEN_BUTTONS)
              .add(GeneralBlocks.Rubber_BUTTON.builtInRegistryHolder().key())
              .setReplace(false);
      builder(WOODEN_DOORS)
              .add(GeneralBlocks.Rubber_DOOR.builtInRegistryHolder().key())
              .setReplace(false);
      builder(WOODEN_TRAPDOORS)
              .add(GeneralBlocks.Rubber_TRAPDOOR.builtInRegistryHolder().key())
              .setReplace(false);
      builder(WOODEN_PRESSURE_PLATES)
              .add(GeneralBlocks.Rubber_PRESSURE_PLATE.builtInRegistryHolder().key())
              .setReplace(false);
      builder(WOODEN_SLABS)
              .add(GeneralBlocks.Rubber_SLAB.builtInRegistryHolder().key())
              .setReplace(false);
      builder(WOODEN_STAIRS)
              .add(GeneralBlocks.Rubber_STAIRS.builtInRegistryHolder().key())
              .setReplace(false);
      builder(PLANKS)
              .add(GeneralBlocks.Rubber_PLANKS.builtInRegistryHolder().key())
              .setReplace(false);
      builder(FENCES)
              .add(GeneralBlocks.Rubber_FENCE.builtInRegistryHolder().key())
              .setReplace(false);
      builder(DOORS)
              .add(GeneralBlocks.Reinforced_DOOR.builtInRegistryHolder().key())
              .setReplace(false);
      builder(STAIRS)
              .add(GeneralBlocks.COBBLESTONE_STAIRS.builtInRegistryHolder().key())
              .setReplace(false);

   }
}