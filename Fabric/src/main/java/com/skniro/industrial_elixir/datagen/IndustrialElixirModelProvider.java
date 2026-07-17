package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.api.data.family.GrowableOresBlockFamilies;
import com.skniro.industrial_elixir.api.data.model.MapleItemModelDatagenHelper;
import com.skniro.industrial_elixir.api.data.model.MapleModelDatagenHelper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import com.skniro.industrial_elixir.item.init.equipment.MapleEquipmentAssetKeys;
import com.skniro.growableoresir.block.GrowableICOresBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

import static net.minecraft.client.data.models.ItemModelGenerators.*;

public class IndustrialElixirModelProvider extends FabricModelProvider {
    public IndustrialElixirModelProvider(FabricPackOutput dataGenerator){
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.family(GeneralBlocks.Deepslate_Lead_Ore);
        blockStateModelGenerator.family(GeneralBlocks.Deepslate_Tin_Ore);
        blockStateModelGenerator.family(GeneralBlocks.Deepslate_SACRED_Ore);
        blockStateModelGenerator.family(GeneralBlocks.Lead_Ore);
        blockStateModelGenerator.family(GeneralBlocks.Tin_Ore);
        blockStateModelGenerator.family(GeneralBlocks.SACRED_Ore);
        blockStateModelGenerator.family(GeneralBlocks.Raw_Lead_Block);
        blockStateModelGenerator.family(GeneralBlocks.Raw_Tin_Block);
        blockStateModelGenerator.family(GeneralBlocks.Raw_SACRED_Block);
        blockStateModelGenerator.family(GeneralBlocks.Lead_Block);
        blockStateModelGenerator.family(GeneralBlocks.Tin_Block);
        blockStateModelGenerator.family(GeneralBlocks.SACRED_Block);
        blockStateModelGenerator.family(GeneralBlocks.Silver_Block);
        blockStateModelGenerator.family(GeneralBlocks.Bronze_Block);
        blockStateModelGenerator.family(GeneralBlocks.Steel_Block);
        blockStateModelGenerator.family(GeneralBlocks.Machine);
        blockStateModelGenerator.family(GeneralBlocks.Advanced_Machine);
        blockStateModelGenerator.family(GeneralBlocks.Super_Machine);

        BlockModelGenerators.BlockFamilyProvider MaplePool = blockStateModelGenerator.family(GeneralBlocks.Rubber_PLANKS)
                .generateFor(GrowableOresBlockFamilies.RUBBER_PLANKS);

        blockStateModelGenerator.woodProvider(GeneralBlocks.Rubber_LOG).logWithHorizontal(GeneralBlocks.Rubber_LOG).wood(GeneralBlocks.Rubber_WOOD);
        blockStateModelGenerator.woodProvider(GeneralBlocks.STRIPPED_Rubber_LOG).logWithHorizontal(GeneralBlocks.STRIPPED_Rubber_LOG).wood(GeneralBlocks.STRIPPED_Rubber_WOOD);

        blockStateModelGenerator.createPlantWithDefaultItem(GeneralBlocks.Rubber_SAPLING, GeneralBlocks.POTTED_Rubber_SAPLING, BlockModelGenerators.PlantType.NOT_TINTED);
        blockStateModelGenerator.createTrivialCube(GeneralBlocks.Rubber_LEAVES);
        blockStateModelGenerator.createShelf(GeneralBlocks.Rubber_SHELF, GeneralBlocks.STRIPPED_Rubber_WOOD);

        blockStateModelGenerator.createHangingSign(GeneralBlocks.STRIPPED_Rubber_LOG, MapleSignBlocks.Rubber_HANGING_SIGN, MapleSignBlocks.Rubber_WALL_HANGING_SIGN);

        //PLASTER
        blockStateModelGenerator.family(GeneralBlocks.GREEN_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.ORANGE_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.MAGENTA_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.LIGHT_BLUE_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.YELLOW_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.LIME_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.PINK_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.GRAY_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.LIGHT_GRAY_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.CYAN_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.PURPLE_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.BLUE_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.BROWN_PLASTER);
        blockStateModelGenerator.family(GeneralBlocks.RED_PLASTER);

        blockStateModelGenerator.createDoor(GeneralBlocks.Reinforced_DOOR);

        MapleModelDatagenHelper mapleModelDatagenHelper = new MapleModelDatagenHelper(blockStateModelGenerator);

        mapleModelDatagenHelper.registerModLogs(GeneralBlocks.Rubber_Rubber_LOG);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.COAL_GENERATOR, true);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.NUCLEAR_REACTOR, true);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.Macerator_Block, true);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.Compressor_Block, true);
        mapleModelDatagenHelper.registerMachineSameNorthSouth(GrowableOresBlocks.GENERATOR_Wind_Mill, false);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.MetalFormerBlock, true);
        mapleModelDatagenHelper.registerMachineDiffBottomBack(GrowableOresBlocks.EnergyBox, false);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.CESU, false);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.MFE, false);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.MFSU, false);
        mapleModelDatagenHelper.registerMachineDiffBottomBack(GrowableOresBlocks.ChargePad, true);
        mapleModelDatagenHelper.registerMachineDiffBottomBack(GrowableOresBlocks.CESU_CHARGE_PAD, false);
        mapleModelDatagenHelper.registerMachineDiffBottomBack(GrowableOresBlocks.MFE_CHARGE_PAD, false);
        mapleModelDatagenHelper.registerMachineDiffBottomBack(GrowableOresBlocks.MFSU_CHARGE_PAD, false);
        mapleModelDatagenHelper.registerMachineDiffBottomBack(GrowableOresBlocks.Iron_Furnace_Block, true);

        mapleModelDatagenHelper.registerMachineSameSide(GrowableOresBlocks.GENERATOR_SolarPanel);
        mapleModelDatagenHelper.registerMachineSameSide(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL);
        mapleModelDatagenHelper.registerMachineSameSide(GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL);
        mapleModelDatagenHelper.registerMachineSameSide(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL);
        mapleModelDatagenHelper.registerMachineSameSide(GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL);

        mapleModelDatagenHelper.registerMachineExtractor(GrowableOresBlocks.Extractor_Block, true);

        mapleModelDatagenHelper.registerMachineTransformer(GrowableOresBlocks.LV_TRANSFORMER, true);
        mapleModelDatagenHelper.registerMachineTransformer(GrowableOresBlocks.MV_TRANSFORMER, true);
        mapleModelDatagenHelper.registerMachineTransformer(GrowableOresBlocks.HV_TRANSFORMER, true);
        mapleModelDatagenHelper.registerMachineTransformer(GrowableOresBlocks.EV_TRANSFORMER, true);

        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.ElectricFurnace_Block, true);
        mapleModelDatagenHelper.registerMachineExtractor(GrowableOresBlocks.INDUCTION_FURNACE, true);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.RECYCLER_Block, true);
        mapleModelDatagenHelper.registerMachineExtractor(GrowableOresBlocks.CUTTING_Block, true);

        blockStateModelGenerator.createCrossBlockWithDefaultItem(GrowableICOresBlocks.IER_Bronze_Cane, BlockModelGenerators.PlantType.TINTED);
        blockStateModelGenerator.createCrossBlockWithDefaultItem(GrowableICOresBlocks.IER_silver_Cane, BlockModelGenerators.PlantType.TINTED);
        blockStateModelGenerator.createCrossBlockWithDefaultItem(GrowableICOresBlocks.IER_Tin_Cane, BlockModelGenerators.PlantType.TINTED);
        blockStateModelGenerator.createCrossBlockWithDefaultItem(GrowableICOresBlocks.IER_SACRED_Cane, BlockModelGenerators.PlantType.TINTED);
        blockStateModelGenerator.createCrossBlockWithDefaultItem(GrowableICOresBlocks.IER_steel_Cane, BlockModelGenerators.PlantType.TINTED);
        blockStateModelGenerator.createCrossBlockWithDefaultItem(GrowableICOresBlocks.IER_LEAD_Cane, BlockModelGenerators.PlantType.TINTED);

        mapleModelDatagenHelper.registerMachineExtractor(GrowableOresBlocks.MolecularTransformerBlock, true);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.Brew_Reactor_BLOCK, true);
        mapleModelDatagenHelper.registerMachineExtractor(GrowableOresBlocks.Ore_Washing_Block, true);
        mapleModelDatagenHelper.registerMachineSameNorthSouth(GrowableOresBlocks.FLUID_TANK_BLOCK, false);
        mapleModelDatagenHelper.registerMachineElectricHeater(GrowableOresBlocks.Electric_Heater_Block, true);
        mapleModelDatagenHelper.registerMachineBlastFurnace(GrowableOresBlocks.BLAST_FURNACE_BLOCK, true);
        mapleModelDatagenHelper.registerMachineHeatCentrifuge(GrowableOresBlocks.HEAT_CENTRIFUGE, true);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.SACRED_GENERATOR, true);
        mapleModelDatagenHelper.registerMachine(GrowableOresBlocks.MATTER_GENERATOR, true);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.FLUID_GENERATOR, true);
        mapleModelDatagenHelper.registerMachineChunkLoader(GrowableOresBlocks.CHUNK_LOADER, true);
        mapleModelDatagenHelper.registerMachineDiffBottom(GrowableOresBlocks.PATTERN_STORAGE, true);
        mapleModelDatagenHelper.registerBaseMachineBlock(GrowableOresBlocks.Replicator, true);
        mapleModelDatagenHelper.registerMachineSolidHeater(GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR, true);
    }
    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(GrowableOresItems.Rubber, ModelTemplates.FLAT_ITEM);
        //Sign
        //itemModelGenerator.register(Item.fromBlock(MapleSignBlocks.Rubber_SIGN), Models.GENERATED);
       // itemModelGenerator.register(Item.fromBlock(MapleSignBlocks.Rubber_HANGING_SIGN), Models.GENERATED);
        itemModelGenerator.generateFlatItem(GrowableOresItems.RUBBER_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.RUBBER_CHEST_BOAT, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.ENERGY_STORAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.OVERCLOCKER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.TRANSFORMER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.REDSTONE_INVERTER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SACRED_ESSENCE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SACRED_SHARD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SACRED_CORE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.EMPTY_VESSEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.IMPURE_SACRED_STONE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COOLANT_CELL_10K, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COOLANT_CELL_30K, ModelTemplates.FLAT_ITEM);;
        itemModelGenerator.generateFlatItem(GrowableOresItems.COOLANT_CELL_60K, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.HEAT_VENT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.ADVANCED_HEAT_VENT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.OVERCLOCKED_HEAT_VENT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.REACTOR_PLATING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.NEUTRON_REFLECTOR, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.HEAT_EXCHANGER, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.Raw_Lead, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Raw_Tin, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Raw_SACRED, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_COPPER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_GOLD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_IRON, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_LEAD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_SILVER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_TIN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CRUSHED_SACRED, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_COPPER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_GOLD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_IRON, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_LEAD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_SILVER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_TIN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PURIFIED_SACRED, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.BRONZE_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CLAY_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COAL_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COPPER_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DIAMOND_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.ENERGIUM_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.GOLD_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.IRON_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LAPIS_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LEAD_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LITHIUM_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.OBSIDIAN_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SILICON_DIOXIDE_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SILVER_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.STONE_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SULFUR_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.TIN_DUST, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_BRONZE_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_COPPER_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_GOLD_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_IRON_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_LAPIS_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_LEAD_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_LITHIUM_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_OBSIDIAN_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_SILVER_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_SULFUR_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SMALL_TIN_DUST, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.BRONZE_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LEAD_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SILVER_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.STEEL_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.TIN_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.REFINED_IRON_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.SACRED_INGOT, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.BRONZE_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COPPER_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.GOLD_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.IRON_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LAPIS_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LEAD_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.OBSIDIAN_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.STEEL_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.TIN_PLATE, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_BRONZE_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_COPPER_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_GOLD_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_IRON_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_LAPIS_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_LEAD_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_OBSIDIAN_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_STEEL_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.DENSE_TIN_PLATE, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.BRONZE_CASING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COPPER_CASING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.GOLD_CASING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.IRON_CASING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LEAD_CASING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.STEEL_CASING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.TIN_CASING, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateTrimmableItem(MapleArmorItems.Quantum_HELMET, MapleEquipmentAssetKeys.QUANTUM, TRIM_PREFIX_HELMET, false);
        itemModelGenerator.generateTrimmableItem(MapleArmorItems.Quantum_CHESTPLATE, MapleEquipmentAssetKeys.QUANTUM,TRIM_PREFIX_CHESTPLATE, false);
        itemModelGenerator.generateTrimmableItem(MapleArmorItems.Quantum_LEGGINGS, MapleEquipmentAssetKeys.QUANTUM,TRIM_PREFIX_LEGGINGS, false);
        itemModelGenerator.generateTrimmableItem(MapleArmorItems.Quantum_BOOTS, MapleEquipmentAssetKeys.QUANTUM,TRIM_PREFIX_BOOTS, false);

        itemModelGenerator.generateFlatItem(AdvancedItems.IRRADIANT_SEPTRIN_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.IRRADIANT_GLASS_PANE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.LUMINITE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.ENRICHED_LUMINITE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.LUMINITE_ALLOY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.ENRICHED_LUMINITE_ALLOY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.IRIDIUM_AMETHYST_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.IRRADIANT_REINFORCED_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.LUMINITE_Part, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.Iridium_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.Quantum_Core, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(AdvancedItems.MT_Core, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.CARBON_FIBRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CARBON_MESH, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.CARBON_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COAL_BALL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COAL_BLOCK, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.COAL_CHUNK, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.INDUSTRIAL_DIAMOND, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.IRIDIUM_SHARD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.IRIDIUM_ORE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.ALLOY_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.ALLOY_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Sticky_Resin, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.IRIDIUM_PLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Circuit, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Advanced_Circuit, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Coil, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Electric_Motor, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Scrap, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.Scrap_box, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(MapleArmorItems.CUTTING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(MapleArmorItems.ROLLING, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateTrimmableItem(MapleArmorItems.BRONZE_BOOTS, MapleEquipmentAssetKeys.BRONZE, TRIM_PREFIX_HELMET, false);
        itemModelGenerator.generateTrimmableItem(MapleArmorItems.BRONZE_CHESTPLATE,MapleEquipmentAssetKeys.BRONZE, TRIM_PREFIX_CHESTPLATE, false);
        itemModelGenerator.generateTrimmableItem(MapleArmorItems.BRONZE_HELMET, MapleEquipmentAssetKeys.BRONZE, TRIM_PREFIX_LEGGINGS, false);
        itemModelGenerator.generateTrimmableItem(MapleArmorItems.BRONZE_LEGGINGS, MapleEquipmentAssetKeys.BRONZE, TRIM_PREFIX_BOOTS,false);

        itemModelGenerator.generateFlatItem(MapleArmorItems.BRONZE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(MapleArmorItems.BRONZE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(MapleArmorItems.BRONZE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(MapleArmorItems.BRONZE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(MapleArmorItems.BRONZE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.EMPTY_CELL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.WATER_CELL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.LAVA_CELL, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(GrowableOresItems.SLAG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.ASHES, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.PATTERN_STORAGE_CRYSTAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(GrowableOresItems.HEAT_CONDUCTOR, ModelTemplates.FLAT_ITEM);

        MapleItemModelDatagenHelper mapleItemModelDatagenHelper = new MapleItemModelDatagenHelper(itemModelGenerator);
        mapleItemModelDatagenHelper.registerDurabilityItem(GrowableOresItems.RE_BATTERY);
        mapleItemModelDatagenHelper.registerDurabilityItem(GrowableOresItems.ADVANCED_RE_BATTERY);
        mapleItemModelDatagenHelper.registerDurabilityItem(GrowableOresItems.ENERGY_CRYSTAL);
        mapleItemModelDatagenHelper.registerDurabilityItem(GrowableOresItems.LAPOTRON_CRYSTAL);

        itemModelGenerator.generateFlatItem(IndustrialElixirFluidItems.UU_CELL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(IndustrialElixirFluidItems.AIR_CELL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(IndustrialElixirFluidItems.Fluid_UU_BUCKET, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(IndustrialElixirFluidItems.Fluid_AIR_BUCKET, ModelTemplates.FLAT_ITEM);
    }
}
