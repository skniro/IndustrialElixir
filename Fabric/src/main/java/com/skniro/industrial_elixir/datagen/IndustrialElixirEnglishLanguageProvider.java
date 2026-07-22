package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import com.skniro.industrial_elixir.item.ModCreativeTab;
import com.skniro.growableoresir.block.GrowableICOresBlocks;
import com.skniro.industrial_elixir.keybind.ModKeyMappings;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class IndustrialElixirEnglishLanguageProvider extends FabricLanguageProvider {
    public IndustrialElixirEnglishLanguageProvider(FabricPackOutput dataGenerator, CompletableFuture<HolderLookup.Provider> registryLookup){
        super(dataGenerator,"en_us",registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(FurnitureStrings.Macerator, "Macerator");
        translationBuilder.add(FurnitureStrings.CaneConverter, "Cane Converter");
        translationBuilder.add(FurnitureStrings.Compressor, "Compressor");
        translationBuilder.add(FurnitureStrings.MetalFormer, "Metal Former");
        translationBuilder.add(FurnitureStrings.GeneratorWindMill, "Wind Mill Generator");
        translationBuilder.add(FurnitureStrings.CoalGenerator, "Coal Generator");
        translationBuilder.add(FurnitureStrings.SacredReactor, "Sacred Reactor");
        translationBuilder.add(FurnitureStrings.NuclearReactor, "Nuclear Reactor");
        translationBuilder.add(FurnitureStrings.Extractor, "Extractor");
        translationBuilder.add(FurnitureStrings.ROLLING, "Rolling");
        translationBuilder.add(FurnitureStrings.CUTTING, "Cutting");
        translationBuilder.add(FurnitureStrings.EXTRUDING, "Extruding");
        translationBuilder.add(FurnitureStrings.ERROR, "Invalid Mode");
        translationBuilder.add(GrowableOresBlocks.GrowableOres_Block, "Cane Converter");
        translationBuilder.add(FurnitureStrings.ElectricFurnace, "Electric Furnace");
        translationBuilder.add(FurnitureStrings.Block_Cutter, "Block Cutter");
        translationBuilder.add(FurnitureStrings.Recycler, "Recycler");

        translationBuilder.add(FurnitureStrings.Charge_Pad, "Charge Pad");
        translationBuilder.add(FurnitureStrings.Charge_Pad_CESU, "CESU Charge Pad");
        translationBuilder.add(FurnitureStrings.Charge_Pad_MFE, "MFE Charge Pad");
        translationBuilder.add(FurnitureStrings.Charge_Pad_MFSU, "MFSU Charge Pad");
        translationBuilder.add(FurnitureStrings.Energy_Box, "Energy Box");
        translationBuilder.add(FurnitureStrings.Energy_Box_CESU, "CESU Energy Box");
        translationBuilder.add(FurnitureStrings.Energy_Box_MFE, "MFE Energy Box");
        translationBuilder.add(FurnitureStrings.Energy_Box_MFSU, "MFSU Energy Box");

        translationBuilder.add(FurnitureStrings.GENERATOR_SolarPanel, "Solar Panel");
        translationBuilder.add(FurnitureStrings.GENERATOR_ADVANCED_SOLAR_PANEL, "Advanced Solar Panel");
        translationBuilder.add(FurnitureStrings.GENERATOR_HYBRID_SOLAR_PANEL, "Hybrid Solar Panel");
        translationBuilder.add(FurnitureStrings.GENERATOR_ULTIMATE_SOLAR_PANEL, "Ultimate Solar Panel");
        translationBuilder.add(FurnitureStrings.GENERATOR_QUANTUM_SOLAR_PANEL, "Quantum Solar Panel");

        translationBuilder.add(FurnitureStrings.MolecularTransformer, "Molecular Transformer");

        translationBuilder.add(FurnitureStrings.IRON_FURNACE, "Iron Furnace");
        translationBuilder.add(FurnitureStrings.Fluid_Cell, "Fluid Cell");
        translationBuilder.add(FurnitureStrings.InductionFurnace, "Induction Furnace");
        translationBuilder.add(FurnitureStrings.BrewReactor, "Brew Reactor");
        translationBuilder.add(FurnitureStrings.OreWashing, "Ore Washing Machine");
        translationBuilder.add(FurnitureStrings.Fluid_Tank, "Fluid Tank");
        translationBuilder.add(FurnitureStrings.Blast_Furnace, "Blast Furnace");
        translationBuilder.add(FurnitureStrings.Electric_Heater, "Electric Heater");
        translationBuilder.add(FurnitureStrings.Heat_Centrifuge, "Heat Centrifuge");
        translationBuilder.add(FurnitureStrings.Solid_Fuel_Heater, "Solid Fuel Heater");
        translationBuilder.add(FurnitureStrings.Pattern_Storage, "Pattern Storage");
        translationBuilder.add(FurnitureStrings.Replicator, "Replicator");
        translationBuilder.add(FurnitureStrings.Fluid_Generator, "Fluid Generator");
        translationBuilder.add(FurnitureStrings.ChunkLoader, "Chunk Loader");
        translationBuilder.add(FurnitureStrings.Matter_Generator, "Matter Generator");

        translationBuilder.add("gui.industrial_elixir.pattern_storage.copy", "Copy");
        translationBuilder.add("gui.industrial_elixir.pattern_storage.uu_tooltip", "UU Matter cost per replication");
        translationBuilder.add("gui.industrial_elixir.pattern_storage.energy_tooltip", "Energy cost per replication");

        translationBuilder.add("tooltip.industrial_elixir.pattern_storage.stored_item", "Stored Item");
        translationBuilder.add("tooltip.industrial_elixir.pattern_storage.energy_cost", "Energy Cost");
        translationBuilder.add("tooltip.industrial_elixir.pattern_storage.uu_cost", "UU Matter Cost");

        translationBuilder.add("gui.industrial_elixir.replicator.stop", "Stop");
        translationBuilder.add("gui.industrial_elixir.replicator.single", "Craft 1");
        translationBuilder.add("gui.industrial_elixir.replicator.loop", "Loop");

        translationBuilder.add(GeneralBlocks.Lead_Ore, "Lead Ore");
        translationBuilder.add(GeneralBlocks.Tin_Ore, "Tin Ore");
        translationBuilder.add(GeneralBlocks.SACRED_Ore, "Sacred Ore");
        translationBuilder.add(GeneralBlocks.Deepslate_Lead_Ore, "Deepslate Lead Ore");
        translationBuilder.add(GeneralBlocks.Deepslate_Tin_Ore, "Deepslate Tin Ore");
        translationBuilder.add(GeneralBlocks.Deepslate_SACRED_Ore, "Deepslate Sacred Ore");

        translationBuilder.add(GeneralBlocks.Raw_Lead_Block, "Block of Raw Lead");
        translationBuilder.add(GeneralBlocks.Raw_Tin_Block, "Block of Raw Tin");
        translationBuilder.add(GeneralBlocks.Raw_SACRED_Block, "Block of Raw Sacred");

        translationBuilder.add(GeneralBlocks.Lead_Block, "Lead Block");
        translationBuilder.add(GeneralBlocks.Tin_Block, "Tin Block");
        translationBuilder.add(GeneralBlocks.SACRED_Block, "Sacred Block");

        translationBuilder.add(GeneralBlocks.Silver_Block, "Silver Block");
        translationBuilder.add(GeneralBlocks.Bronze_Block, "Bronze Block");
        translationBuilder.add(GeneralBlocks.Steel_Block, "Steel Block");

        translationBuilder.add(GeneralBlocks.Super_Machine, "Super Machine Casing");
        translationBuilder.add(GeneralBlocks.Advanced_Machine, "Advanced Machine Casing");
        translationBuilder.add(GeneralBlocks.Machine, "Machine Casing");

        translationBuilder.add(GeneralBlocks.Rubber_SAPLING, "Rubber Sapling");
        translationBuilder.add(GeneralBlocks.POTTED_Rubber_SAPLING, "Potted Rubber Sapling");
        translationBuilder.add(GeneralBlocks.Rubber_LEAVES, "Rubber Leaves");

        translationBuilder.add(GeneralBlocks.Rubber_Rubber_LOG, "Resin Rubber Log");

        translationBuilder.add(GeneralBlocks.Rubber_LOG, "Rubber Log");
        translationBuilder.add(GeneralBlocks.STRIPPED_Rubber_LOG, "Stripped Rubber Log");
        translationBuilder.add(GeneralBlocks.STRIPPED_Rubber_WOOD, "Stripped Rubber Wood");
        translationBuilder.add(GeneralBlocks.Rubber_WOOD, "Rubber Wood");

        translationBuilder.add(GeneralBlocks.Rubber_PLANKS, "Rubber Planks");
        translationBuilder.add(GeneralBlocks.Rubber_BUTTON, "Rubber Button");
        translationBuilder.add(GeneralBlocks.Rubber_STAIRS, "Rubber Stairs");
        translationBuilder.add(GeneralBlocks.Rubber_SLAB, "Rubber Slab");
        translationBuilder.add(GeneralBlocks.Rubber_FENCE_GATE, "Rubber Fence Gate");
        translationBuilder.add(GeneralBlocks.Rubber_FENCE, "Rubber Fence");
        translationBuilder.add(GeneralBlocks.Rubber_DOOR, "Rubber Door");
        translationBuilder.add(GeneralBlocks.Rubber_TRAPDOOR, "Rubber Trapdoor");
        translationBuilder.add(GeneralBlocks.Rubber_PRESSURE_PLATE, "Rubber Pressure Plate");
        translationBuilder.add(GeneralBlocks.Rubber_SHELF, "Rubber Shelf");
        translationBuilder.add(GeneralBlocks.Reinforced_Glass, "Reinforced Glass");
        translationBuilder.add(GeneralBlocks.Reinforced_Stone, "Reinforced Stone");
        translationBuilder.add(GeneralBlocks.Reinforced_DOOR, "Reinforced Door");

        translationBuilder.add(GrowableOresBlocks.Macerator_Block, "Macerator");
        translationBuilder.add(GrowableOresBlocks.Compressor_Block, "Compressor");
        translationBuilder.add(GrowableOresBlocks.MetalFormerBlock, "Metal Former");
        translationBuilder.add(GrowableOresBlocks.Ore_Washing_Block, "Ore Washing Machine");

        translationBuilder.add(GrowableOresBlocks.COPPER_CABLE, "Copper Cable");
        translationBuilder.add(GrowableOresBlocks.TIN_CABLE, "Tin Cable");
        translationBuilder.add(GrowableOresBlocks.GOLD_CABLE, "Gold Cable");
        translationBuilder.add(GrowableOresBlocks.HV_CABLE, "HV Cable");
        translationBuilder.add(GrowableOresBlocks.GLASSFIBER_CABLE, "Glass Fiber Cable");

        translationBuilder.add(GrowableOresBlocks.INSULATED_TIN_CABLE, "Insulated Tin Cable");
        translationBuilder.add(GrowableOresBlocks.INSULATED_COPPER_CABLE, "Insulated Copper Cable");
        translationBuilder.add(GrowableOresBlocks.INSULATED_GOLD_CABLE, "Insulated Gold Cable");
        translationBuilder.add(GrowableOresBlocks.INSULATED_HV_CABLE, "Insulated HV Cable");

        translationBuilder.add(GrowableOresBlocks.COAL_GENERATOR, "Coal Generator");
        translationBuilder.add(GrowableOresBlocks.NUCLEAR_REACTOR, "Nuclear Reactor");
        translationBuilder.add(GrowableOresBlocks.SACRED_GENERATOR, "Sacred Reactor");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_Wind_Mill, "Wind Mill");

        translationBuilder.add(GrowableOresBlocks.EnergyBox, "Energy Box");
        translationBuilder.add(GrowableOresBlocks.ChargePad, "Charge Pad");

        translationBuilder.add(GrowableOresBlocks.CESU, "CESU");
        translationBuilder.add(GrowableOresBlocks.MFE, "MFE");
        translationBuilder.add(GrowableOresBlocks.MFSU, "MFSU");

        translationBuilder.add(GrowableOresBlocks.CESU_CHARGE_PAD, "CESU Charge Pad");
        translationBuilder.add(GrowableOresBlocks.MFE_CHARGE_PAD, "MFE Charge Pad");
        translationBuilder.add(GrowableOresBlocks.MFSU_CHARGE_PAD, "MFSU Charge Pad");

        translationBuilder.add(GrowableOresBlocks.MolecularTransformerBlock, "Molecular Transformer");

        translationBuilder.add(GrowableOresBlocks.GENERATOR_SolarPanel, "Solar Panel");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL, "Advanced Solar Panel");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL, "Hybrid Solar Panel");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL, "Ultimate Solar Panel");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL, "Quantum Solar Panel");

        translationBuilder.add(GrowableOresBlocks.Extractor_Block, "Extractor");

        translationBuilder.add(GrowableOresBlocks.EV_TRANSFORMER, "EV Transformer");
        translationBuilder.add(GrowableOresBlocks.HV_TRANSFORMER, "HV Transformer");
        translationBuilder.add(GrowableOresBlocks.MV_TRANSFORMER, "MV Transformer");
        translationBuilder.add(GrowableOresBlocks.LV_TRANSFORMER, "LV Transformer");

        translationBuilder.add(GrowableOresBlocks.Iron_Furnace_Block, "Iron Furnace");
        translationBuilder.add(GrowableOresBlocks.INDUCTION_FURNACE, "Induction Furnace");
        translationBuilder.add(GrowableOresBlocks.Brew_Reactor_BLOCK, "Brew Reactor");
        translationBuilder.add(GrowableOresBlocks.Electric_Heater_Block, "Electric Heater");
        translationBuilder.add(GrowableOresBlocks.BLAST_FURNACE_BLOCK, "Heat Blast Furnace");
        translationBuilder.add(GrowableOresBlocks.HEAT_CENTRIFUGE, "Heat Centrifuge");
        translationBuilder.add(GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR, "Solid Fuel Heater");
        translationBuilder.add(GrowableOresBlocks.PATTERN_STORAGE, "Pattern Storage");
        translationBuilder.add(GrowableOresBlocks.Replicator, "Replicator");
        translationBuilder.add(GrowableOresBlocks.FLUID_GENERATOR, "Fluid Generator");
        translationBuilder.add(GrowableOresBlocks.CHUNK_LOADER, "Chunk Loader");
        translationBuilder.add(GrowableOresBlocks.FLUID_TANK_BLOCK, "Fluid Tank");
        translationBuilder.add(GrowableOresBlocks.Pipe_Stone_Fluid_Block, "Stone Fluid Pipe");
        translationBuilder.add(GrowableOresBlocks.Pipe_Stone_Item_Block, "Stone Item Pipe");
        translationBuilder.add(GrowableOresBlocks.Pipe_Wooden_Fluid_Block, "Wooden Fluid Pipe");
        translationBuilder.add(GrowableOresBlocks.Pipe_Wooden_Iten_Block, "Wooden Item Pipe");
        translationBuilder.add(GrowableOresBlocks.MATTER_GENERATOR, "Matter Generator");

        translationBuilder.add(MapleSignBlocks.Rubber_SIGN, "Rubber Sign");
        translationBuilder.add(MapleSignBlocks.Rubber_WALL_SIGN, "Rubber Wall Sign");
        translationBuilder.add(MapleSignBlocks.Rubber_HANGING_SIGN, "Rubber Hanging Sign");
        translationBuilder.add(MapleSignBlocks.Rubber_WALL_HANGING_SIGN, "Rubber Wall Hanging Sign");

        translationBuilder.add(AdvancedItems.IRRADIANT_SACRED_INGOT, "Irradiant Sacred Ingot");
        translationBuilder.add(AdvancedItems.IRRADIANT_GLASS_PANE, "Irradiant Glass Pane");
        translationBuilder.add(AdvancedItems.LUMINITE, "Luminite");
        translationBuilder.add(AdvancedItems.ENRICHED_LUMINITE, "Enriched Luminite");
        translationBuilder.add(AdvancedItems.LUMINITE_ALLOY, "Luminite Alloy");
        translationBuilder.add(AdvancedItems.ENRICHED_LUMINITE_ALLOY, "Enriched Luminite Alloy");
        translationBuilder.add(AdvancedItems.IRIDIUM_AMETHYST_PLATE, "Iridium Amethyst Plate");
        translationBuilder.add(AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE, "Reinforced Iridium Amethyst Plate");
        translationBuilder.add(AdvancedItems.IRRADIANT_REINFORCED_PLATE, "Irradiant Reinforced Amethyst Plate");
        translationBuilder.add(AdvancedItems.LUMINITE_Part, "Luminite Component");
        translationBuilder.add(AdvancedItems.Iridium_INGOT, "Iridium Ingot");
        translationBuilder.add(AdvancedItems.Quantum_Core, "Quantum Core");
        translationBuilder.add(AdvancedItems.MT_Core, "Molecular Transformer Core");
        translationBuilder.add(AdvancedItems.PIPE_PLUG, "Pipe Plug");

        translationBuilder.add(GrowableOresItems.RE_BATTERY, "RE Battery");
        translationBuilder.add(GrowableOresItems.ADVANCED_RE_BATTERY, "Advanced RE Battery");
        translationBuilder.add(GrowableOresItems.ENERGY_CRYSTAL, "Energy Crystal");
        translationBuilder.add(GrowableOresItems.LAPOTRON_CRYSTAL, "Lapotron Crystal");

        translationBuilder.add(GrowableOresItems.Rubber, "Rubber");
        translationBuilder.add(GrowableOresItems.RUBBER_BOAT, "Rubber Boat");
        translationBuilder.add(GrowableOresItems.RUBBER_CHEST_BOAT, "Rubber Chest Boat");

        translationBuilder.add(GrowableOresItems.OVERCLOCKER, "Overclocker Upgrade");
        translationBuilder.add(GrowableOresItems.ENERGY_STORAGE, "Energy Storage Upgrade");
        translationBuilder.add(GrowableOresItems.TRANSFORMER, "Transformer Upgrade");
        translationBuilder.add(GrowableOresItems.REDSTONE_INVERTER, "Redstone Inverter Upgrade");
        translationBuilder.add(GrowableOresItems.EMPTY_CELL, "Empty Cell");
        translationBuilder.add(GrowableOresItems.SACRED_ESSENCE, "Sacred Essence");
        translationBuilder.add(GrowableOresItems.SACRED_SHARD, "Sacred Shard");
        translationBuilder.add(GrowableOresItems.SACRED_CORE, "Sacred Core");
        translationBuilder.add(GrowableOresItems.EMPTY_VESSEL, "Empty Vessel");
        translationBuilder.add(GrowableOresItems.IMPURE_SACRED_STONE, "Impure Sacred Stone");
        translationBuilder.add(GrowableOresItems.COOLANT_CELL_10K, "10k Coolant Cell");
        translationBuilder.add(GrowableOresItems.COOLANT_CELL_30K, "30k Coolant Cell");
        translationBuilder.add(GrowableOresItems.COOLANT_CELL_60K, "60k Coolant Cell");
        translationBuilder.add(GrowableOresItems.HEAT_VENT, "Heat Vent");
        translationBuilder.add(GrowableOresItems.ADVANCED_HEAT_VENT, "Advanced Heat Vent");
        translationBuilder.add(GrowableOresItems.OVERCLOCKED_HEAT_VENT, "Overclocked Heat Vent");
        translationBuilder.add(GrowableOresItems.REACTOR_PLATING, "Reactor Plating");
        translationBuilder.add(GrowableOresItems.NEUTRON_REFLECTOR, "Neutron Reflector");
        translationBuilder.add(GrowableOresItems.HEAT_EXCHANGER, "Heat Exchanger");

        translationBuilder.add(GrowableOresItems.Raw_Lead, "Raw Lead");
        translationBuilder.add(GrowableOresItems.Raw_Tin, "Raw Tin");
        translationBuilder.add(GrowableOresItems.Raw_SACRED, "Raw Sacred");

        translationBuilder.add(GrowableOresItems.CRUSHED_COPPER, "Crushed Copper Ore");
        translationBuilder.add(GrowableOresItems.CRUSHED_GOLD, "Crushed Gold Ore");
        translationBuilder.add(GrowableOresItems.CRUSHED_IRON, "Crushed Iron Ore");
        translationBuilder.add(GrowableOresItems.CRUSHED_LEAD, "Crushed Lead Ore");
        translationBuilder.add(GrowableOresItems.CRUSHED_SILVER, "Crushed Silver Ore");
        translationBuilder.add(GrowableOresItems.CRUSHED_TIN, "Crushed Tin Ore");
        translationBuilder.add(GrowableOresItems.CRUSHED_SACRED, "Crushed Sacred Ore");

        translationBuilder.add(GrowableOresItems.PURIFIED_COPPER, "Purified Copper Ore");
        translationBuilder.add(GrowableOresItems.PURIFIED_GOLD, "Purified Gold Ore");
        translationBuilder.add(GrowableOresItems.PURIFIED_IRON, "Purified Iron Ore");
        translationBuilder.add(GrowableOresItems.PURIFIED_LEAD, "Purified Lead Ore");
        translationBuilder.add(GrowableOresItems.PURIFIED_SILVER, "Purified Silver Ore");
        translationBuilder.add(GrowableOresItems.PURIFIED_TIN, "Purified Tin Ore");
        translationBuilder.add(GrowableOresItems.PURIFIED_SACRED, "Purified Sacred Ore");

        translationBuilder.add(GrowableOresItems.BRONZE_INGOT, "Bronze Ingot");
        translationBuilder.add(GrowableOresItems.LEAD_INGOT, "Lead Ingot");
        translationBuilder.add(GrowableOresItems.SILVER_INGOT, "Silver Ingot");
        translationBuilder.add(GrowableOresItems.STEEL_INGOT, "Steel Ingot");
        translationBuilder.add(GrowableOresItems.TIN_INGOT, "Tin Ingot");
        translationBuilder.add(GrowableOresItems.SACRED_INGOT, "Sacred Ingot");

        translationBuilder.add(GrowableOresItems.CARBON_FIBRE, "Carbon Fibre");
        translationBuilder.add(GrowableOresItems.CARBON_MESH, "Carbon Mesh");
        translationBuilder.add(GrowableOresItems.CARBON_PLATE, "Carbon Plate");
        translationBuilder.add(GrowableOresItems.COAL_BALL, "Coal Ball");
        translationBuilder.add(GrowableOresItems.COAL_BLOCK, "Coal Block");
        translationBuilder.add(GrowableOresItems.COAL_CHUNK, "Coal Chunk");
        translationBuilder.add(GrowableOresItems.INDUSTRIAL_DIAMOND, "Industrial Diamond");

        translationBuilder.add(GrowableOresItems.Circuit, "Circuit");
        translationBuilder.add(GrowableOresItems.Advanced_Circuit, "Advanced Circuit");
        translationBuilder.add(GrowableOresItems.Coil, "Coil");

        translationBuilder.add(GrowableOresItems.IRIDIUM_SHARD, "Iridium Shard");
        translationBuilder.add(GrowableOresItems.IRIDIUM_ORE, "Iridium Ore");
        translationBuilder.add(GrowableOresItems.IRIDIUM_PLATE, "Iridium Plate");

        translationBuilder.add(GrowableOresItems.ALLOY_INGOT, "Alloy Ingot");
        translationBuilder.add(GrowableOresItems.ALLOY_PLATE, "Alloy Plate");

        translationBuilder.add(GrowableOresItems.Sticky_Resin, "Sticky Resin");

        translationBuilder.add(MapleArmorItems.ROLLING, "Hammer");
        translationBuilder.add(MapleArmorItems.CUTTING, "Cutter");

        translationBuilder.add(MapleArmorItems.Quantum_HELMET, "Quantum Helmet");
        translationBuilder.add(MapleArmorItems.Quantum_CHESTPLATE, "Quantum Chestplate");
        translationBuilder.add(MapleArmorItems.Quantum_LEGGINGS, "Quantum Leggings");
        translationBuilder.add(MapleArmorItems.Quantum_BOOTS, "Quantum Boots");

        translationBuilder.add(MapleArmorItems.Electric_Jetpack, "Electric Jetpack");

        translationBuilder.add(GrowableOresItems.BRONZE_DUST, "Bronze Dust");
        translationBuilder.add(GrowableOresItems.CLAY_DUST, "Clay Dust");
        translationBuilder.add(GrowableOresItems.COAL_DUST, "Coal Dust");
        translationBuilder.add(GrowableOresItems.COPPER_DUST, "Copper Dust");
        translationBuilder.add(GrowableOresItems.DIAMOND_DUST, "Diamond Dust");
        translationBuilder.add(GrowableOresItems.ENERGIUM_DUST, "Energium Dust");
        translationBuilder.add(GrowableOresItems.GOLD_DUST, "Gold Dust");
        translationBuilder.add(GrowableOresItems.IRON_DUST, "Iron Dust");
        translationBuilder.add(GrowableOresItems.LAPIS_DUST, "Lapis Dust");
        translationBuilder.add(GrowableOresItems.LEAD_DUST, "Lead Dust");
        translationBuilder.add(GrowableOresItems.LITHIUM_DUST, "Lithium Dust");
        translationBuilder.add(GrowableOresItems.OBSIDIAN_DUST, "Obsidian Dust");
        translationBuilder.add(GrowableOresItems.SILICON_DIOXIDE_DUST, "Silicon Dioxide Dust");
        translationBuilder.add(GrowableOresItems.SILVER_DUST, "Silver Dust");
        translationBuilder.add(GrowableOresItems.STONE_DUST, "Stone Dust");
        translationBuilder.add(GrowableOresItems.SULFUR_DUST, "Sulfur Dust");
        translationBuilder.add(GrowableOresItems.TIN_DUST, "Tin Dust");

        translationBuilder.add(GrowableOresItems.SMALL_BRONZE_DUST, "Small Bronze Dust");
        translationBuilder.add(GrowableOresItems.SMALL_COPPER_DUST, "Small Copper Dust");
        translationBuilder.add(GrowableOresItems.SMALL_GOLD_DUST, "Small Gold Dust");
        translationBuilder.add(GrowableOresItems.SMALL_IRON_DUST, "Small Iron Dust");
        translationBuilder.add(GrowableOresItems.SMALL_LAPIS_DUST, "Small Lapis Dust");
        translationBuilder.add(GrowableOresItems.SMALL_LEAD_DUST, "Small Lead Dust");
        translationBuilder.add(GrowableOresItems.SMALL_LITHIUM_DUST, "Small Lithium Dust");
        translationBuilder.add(GrowableOresItems.SMALL_OBSIDIAN_DUST, "Small Obsidian Dust");
        translationBuilder.add(GrowableOresItems.SMALL_SILVER_DUST, "Small Silver Dust");
        translationBuilder.add(GrowableOresItems.SMALL_SULFUR_DUST, "Small Sulfur Dust");
        translationBuilder.add(GrowableOresItems.SMALL_TIN_DUST, "Small Tin Dust");

        translationBuilder.add(GrowableOresItems.BRONZE_PLATE, "Bronze Plate");
        translationBuilder.add(GrowableOresItems.COPPER_PLATE, "Copper Plate");
        translationBuilder.add(GrowableOresItems.GOLD_PLATE, "Gold Plate");
        translationBuilder.add(GrowableOresItems.IRON_PLATE, "Iron Plate");
        translationBuilder.add(GrowableOresItems.LAPIS_PLATE, "Lapis Plate");
        translationBuilder.add(GrowableOresItems.LEAD_PLATE, "Lead Plate");
        translationBuilder.add(GrowableOresItems.OBSIDIAN_PLATE, "Obsidian Plate");
        translationBuilder.add(GrowableOresItems.STEEL_PLATE, "Steel Plate");
        translationBuilder.add(GrowableOresItems.TIN_PLATE, "Tin Plate");

        translationBuilder.add(GrowableOresItems.DENSE_BRONZE_PLATE, "Dense Bronze Plate");
        translationBuilder.add(GrowableOresItems.DENSE_COPPER_PLATE, "Dense Copper Plate");
        translationBuilder.add(GrowableOresItems.DENSE_GOLD_PLATE, "Dense Gold Plate");
        translationBuilder.add(GrowableOresItems.DENSE_IRON_PLATE, "Dense Iron Plate");
        translationBuilder.add(GrowableOresItems.DENSE_LAPIS_PLATE, "Dense Lapis Plate");
        translationBuilder.add(GrowableOresItems.DENSE_LEAD_PLATE, "Dense Lead Plate");
        translationBuilder.add(GrowableOresItems.DENSE_OBSIDIAN_PLATE, "Dense Obsidian Plate");
        translationBuilder.add(GrowableOresItems.DENSE_STEEL_PLATE, "Dense Steel Plate");
        translationBuilder.add(GrowableOresItems.DENSE_TIN_PLATE, "Dense Tin Plate");

        translationBuilder.add(GrowableOresItems.BRONZE_CASING, "Bronze Casing");
        translationBuilder.add(GrowableOresItems.COPPER_CASING, "Copper Casing");
        translationBuilder.add(GrowableOresItems.GOLD_CASING, "Gold Casing");
        translationBuilder.add(GrowableOresItems.IRON_CASING, "Iron Casing");
        translationBuilder.add(GrowableOresItems.LEAD_CASING, "Lead Casing");
        translationBuilder.add(GrowableOresItems.STEEL_CASING, "Steel Casing");
        translationBuilder.add(GrowableOresItems.TIN_CASING, "Tin Casing");

        translationBuilder.add(ModCreativeTab.General, "General");
        translationBuilder.add(ModCreativeTab.Generators_And_Wiring, "Generators & Wiring");
        translationBuilder.add(ModCreativeTab.Reactor, "Sacred Reactor");
        translationBuilder.add(ModCreativeTab.Machine, "Machines");
        translationBuilder.add(ModCreativeTab.Tool_And_Utilities, "Tools & Utilities");
        translationBuilder.add(ModCreativeTab.Combat, "Combat");
        translationBuilder.add(ModCreativeTab.Materials, "Materials");

        translationBuilder.add(GrowableOresBlocks.ElectricFurnace_Block, "Electric Furnace");
        translationBuilder.add(GrowableOresBlocks.RECYCLER_Block, "Recycler");
        translationBuilder.add(GrowableOresBlocks.CUTTING_Block, "Block Cutter");
        translationBuilder.add(GrowableOresItems.Electric_Motor, "Electric Motor");
        translationBuilder.add(GrowableOresItems.Scrap, "Scrap");
        translationBuilder.add(GrowableOresItems.Scrap_box, "Scrap Box");

        translationBuilder.add(MapleArmorItems.BRONZE_BOOTS, "Bronze Boots");
        translationBuilder.add(MapleArmorItems.BRONZE_CHESTPLATE, "Bronze Chestplate");
        translationBuilder.add(MapleArmorItems.BRONZE_HELMET, "Bronze Helmet");
        translationBuilder.add(MapleArmorItems.BRONZE_LEGGINGS, "Bronze Leggings");

        translationBuilder.add(MapleArmorItems.BRONZE_PICKAXE, "Bronze Pickaxe");
        translationBuilder.add(MapleArmorItems.BRONZE_AXE, "Bronze Axe");
        translationBuilder.add(MapleArmorItems.BRONZE_SHOVEL, "Bronze Shovel");
        translationBuilder.add(MapleArmorItems.BRONZE_SWORD, "Bronze Sword");
        translationBuilder.add(MapleArmorItems.BRONZE_HOE, "Bronze Hoe");

        translationBuilder.add(GrowableOresItems.SLAG, "Slag");
        translationBuilder.add(GrowableOresItems.ASHES, "Small Pile of Ash");
        translationBuilder.add(GrowableOresItems.PATTERN_STORAGE_CRYSTAL, "Pattern Storage Crystal");
        translationBuilder.add(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL, "Raw Pattern Storage Crystal");
        translationBuilder.add(GrowableOresItems.WATER_CELL, "Water Cell");
        translationBuilder.add(GrowableOresItems.LAVA_CELL, "Lava Cell");
        translationBuilder.add(IndustrialElixirFluidItems.UU_CELL, "UU Matter Cell");

        translationBuilder.add(IndustrialElixirFluidItems.Fluid_UU_BUCKET, "UU Matter Bucket");
        translationBuilder.add(IndustrialElixirFluidItems.Fluid_AIR_BUCKET, "Compressed Air Bucket");
        translationBuilder.add(IndustrialElixirFluidItems.AIR_CELL, "Compressed Air Cell");
        translationBuilder.add(GrowableOresItems.HEAT_CONDUCTOR, "Heat Conductor");

        translationBuilder.add(GrowableICOresBlocks.IER_Bronze_Cane, "Bronze Reed(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_silver_Cane, "Silver Reed(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_Tin_Cane, "Tin Reed(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_SACRED_Cane, "Sacred Reed(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_steel_Cane, "Steel Reed(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_LEAD_Cane, "Lead Reed(Industrial Elixir)");

        //PLASTER
        translationBuilder.add(GeneralBlocks.GREEN_PLASTER,"Green Plaster");
        translationBuilder.add(GeneralBlocks.PLASTER,"Plaster");
        translationBuilder.add(GeneralBlocks.ORANGE_PLASTER,"Orange Plaster");
        translationBuilder.add(GeneralBlocks.MAGENTA_PLASTER,"Magenta Plaster");
        translationBuilder.add(GeneralBlocks.LIGHT_BLUE_PLASTER,"Light Blue Plaster");
        translationBuilder.add(GeneralBlocks.YELLOW_PLASTER,"Yellow Plaster");
        translationBuilder.add(GeneralBlocks.LIME_PLASTER,"Lime Plaster");
        translationBuilder.add(GeneralBlocks.PINK_PLASTER,"Pink Plaster");
        translationBuilder.add(GeneralBlocks.GRAY_PLASTER,"Gray Plaster");
        translationBuilder.add(GeneralBlocks.LIGHT_GRAY_PLASTER,"Light Gray Plaster");
        translationBuilder.add(GeneralBlocks.CYAN_PLASTER,"Cyan Plaster");
        translationBuilder.add(GeneralBlocks.PURPLE_PLASTER,"Purple Plaster");
        translationBuilder.add(GeneralBlocks.BLUE_PLASTER,"Blue Plaster");
        translationBuilder.add(GeneralBlocks.BROWN_PLASTER,"Brown Plaster");
        translationBuilder.add(GeneralBlocks.RED_PLASTER,"Red Plaster");

        translationBuilder.add(IndustrialElixirFluidBlocks.Fluid_UU_BLOCK,"UU Fluid");
        translationBuilder.add("block.industrial_elixir.fluid_uu","UU Fluid");
        translationBuilder.add("fluid.industrial_elixir.fluid_air_block","Air Fluid");
        translationBuilder.add("block.industrial_elixir.fluid_air","Air Fluid");

        translationBuilder.add("key.category.industrial_elixir.industrial_elixir.keybinds", "Industrial Elixir");
        translationBuilder.add("key.industrial_elixir.alt_key", "ALT key");
        translationBuilder.add("key.industrial_elixir.mode_switch_key", "Mode Switch Key");
        translationBuilder.add("key.industrial_elixir.boost_key", "Boost Key");
        translationBuilder.add("industrial_elixir.tooltip.liquid.amount.with.capacity", "Capacity:");
        translationBuilder.add("industrial_elixir.tooltip.liquid.amount", "Liquid Amount:");
    }
}
