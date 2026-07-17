package com.skniro.industrial_elixir.datagen;


import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.growableoresir.block.GrowableICOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;


public class IndustrialElixirLootTableGenerator extends FabricBlockLootSubProvider {
    protected IndustrialElixirLootTableGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }
    public static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.048F, 0.0425F, 0.062333336F, 0.1F};


    @Override
    public void generate() {
        //MAPLE
        dropSelf(MapleSignBlocks.Rubber_SIGN);
        dropSelf(MapleSignBlocks.Rubber_WALL_SIGN);
        dropSelf(MapleSignBlocks.Rubber_HANGING_SIGN);
        dropSelf(MapleSignBlocks.Rubber_WALL_HANGING_SIGN);
        dropSelf(GeneralBlocks.Rubber_LOG);
        dropSelf(GeneralBlocks.Rubber_Rubber_LOG);
        dropSelf(GeneralBlocks.Rubber_WOOD);
        add(GeneralBlocks.Rubber_DOOR, createDoorTable(GeneralBlocks.Rubber_DOOR));
        dropSelf(GeneralBlocks.Rubber_SAPLING);
        dropPottedContents(GeneralBlocks.POTTED_Rubber_SAPLING);
        add(GeneralBlocks.Rubber_LEAVES, createLeavesDrops(GeneralBlocks.Rubber_LEAVES, GeneralBlocks.Rubber_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(GeneralBlocks.Rubber_BUTTON);
        dropSelf(GeneralBlocks.Rubber_FENCE);
        dropSelf(GeneralBlocks.Rubber_FENCE_GATE);
        dropSelf(GeneralBlocks.Rubber_PLANKS);
        dropSelf(GeneralBlocks.Rubber_PRESSURE_PLATE);
        dropSelf(GeneralBlocks.Rubber_SLAB);
        dropSelf(GeneralBlocks.Rubber_STAIRS);
        dropSelf(GeneralBlocks.Rubber_TRAPDOOR);
        dropSelf(GeneralBlocks.STRIPPED_Rubber_LOG);
        dropSelf(GeneralBlocks.STRIPPED_Rubber_WOOD);
        dropSelf(GeneralBlocks.Rubber_SHELF);

        dropSelf(GeneralBlocks.Raw_Lead_Block);
        dropSelf(GeneralBlocks.Raw_Tin_Block);
        dropSelf(GeneralBlocks.Raw_SACRED_Block);

        dropSelf(GeneralBlocks.Lead_Block);
        dropSelf(GeneralBlocks.Tin_Block);
        dropSelf(GeneralBlocks.SACRED_Block);

        dropSelf(GeneralBlocks.Silver_Block);
        dropSelf(GeneralBlocks.Bronze_Block);
        dropSelf(GeneralBlocks.Steel_Block);

        dropSelf(GeneralBlocks.Advanced_Machine);
        dropSelf(GeneralBlocks.Machine);
        dropSelf(GeneralBlocks.Super_Machine);
        dropSelf(GeneralBlocks.Reinforced_Stone);
        dropSelf(GeneralBlocks.Reinforced_Glass);
        add(GeneralBlocks.Reinforced_DOOR, createDoorTable(GeneralBlocks.Reinforced_DOOR));

        dropSelf(GrowableOresBlocks.GrowableOres_Block);

        dropSelf(GrowableOresBlocks.Macerator_Block);
        dropSelf(GrowableOresBlocks.Compressor_Block);
        dropSelf(GrowableOresBlocks.MATTER_GENERATOR);
        dropSelf(GrowableOresBlocks.FLUID_GENERATOR);
        dropSelf(GrowableOresBlocks.MetalFormerBlock);
        dropSelf(GrowableOresBlocks.ElectricFurnace_Block);
        dropSelf(GrowableOresBlocks.INDUCTION_FURNACE);
        dropSelf(GrowableOresBlocks.HEAT_CENTRIFUGE);
        dropSelf(GrowableOresBlocks.PATTERN_STORAGE);
        dropSelf(GrowableOresBlocks.RECYCLER_Block);
        dropSelf(GrowableOresBlocks.CUTTING_Block);

        dropSelf(GrowableOresBlocks.COPPER_CABLE);
        dropSelf(GrowableOresBlocks.TIN_CABLE);
        dropSelf(GrowableOresBlocks.GOLD_CABLE);
        dropSelf(GrowableOresBlocks.HV_CABLE);
        dropSelf(GrowableOresBlocks.GLASSFIBER_CABLE);

        dropSelf(GrowableOresBlocks.INSULATED_TIN_CABLE);
        dropSelf(GrowableOresBlocks.INSULATED_COPPER_CABLE);
        dropSelf(GrowableOresBlocks.INSULATED_GOLD_CABLE);
        dropSelf(GrowableOresBlocks.INSULATED_HV_CABLE);

        dropSelf(GrowableOresBlocks.COAL_GENERATOR);
        dropSelf(GrowableOresBlocks.NUCLEAR_REACTOR);
        dropSelf(GrowableOresBlocks.SACRED_GENERATOR);
        dropSelf(GrowableOresBlocks.GENERATOR_Wind_Mill);

        dropSelf(GrowableOresBlocks.EnergyBox);
        dropSelf(GrowableOresBlocks.ChargePad);

        dropSelf(GrowableOresBlocks.CESU);
        dropSelf(GrowableOresBlocks.MFE);
        dropSelf(GrowableOresBlocks.MFSU);

        dropSelf(GrowableOresBlocks.CESU_CHARGE_PAD);
        dropSelf(GrowableOresBlocks.MFE_CHARGE_PAD);
        dropSelf(GrowableOresBlocks.MFSU_CHARGE_PAD);

        dropSelf(GrowableOresBlocks.MolecularTransformerBlock);

        dropSelf(GrowableOresBlocks.GENERATOR_SolarPanel);
        dropSelf(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL);
        dropSelf(GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL);
        dropSelf(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL);
        dropSelf(GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL);

        dropSelf(GrowableOresBlocks.Extractor_Block);

        dropSelf(GrowableOresBlocks.EV_TRANSFORMER);
        dropSelf(GrowableOresBlocks.HV_TRANSFORMER);
        dropSelf(GrowableOresBlocks.MV_TRANSFORMER);
        dropSelf(GrowableOresBlocks.LV_TRANSFORMER);

        dropSelf(GrowableOresBlocks.Iron_Furnace_Block);

        dropSelf(GrowableOresBlocks.Pipe_Wooden_Iten_Block);
        dropSelf(GrowableOresBlocks.Pipe_Stone_Item_Block);
        dropSelf(GrowableOresBlocks.Pipe_Wooden_Fluid_Block);
        dropSelf(GrowableOresBlocks.Pipe_Stone_Fluid_Block);
        dropSelf(GrowableOresBlocks.FLUID_TANK_BLOCK);
        dropSelf(GrowableOresBlocks.Brew_Reactor_BLOCK);
        dropSelf(GrowableOresBlocks.Ore_Washing_Block);
        dropSelf(GrowableOresBlocks.Replicator);

        dropSelf(GrowableOresBlocks.CHUNK_LOADER);

        dropSelf(GrowableOresBlocks.Electric_Heater_Block);
        dropSelf(GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR);
        dropSelf(GrowableOresBlocks.BLAST_FURNACE_BLOCK);

        add(GeneralBlocks.Lead_Ore,
                createOreDrop(GeneralBlocks.Lead_Ore, GrowableOresItems.Raw_Lead));
        add(GeneralBlocks.Tin_Ore,
                createOreDrop(GeneralBlocks.Tin_Ore, GrowableOresItems.Raw_Tin));
        add(GeneralBlocks.SACRED_Ore,
                createOreDrop(GeneralBlocks.SACRED_Ore, GrowableOresItems.Raw_SACRED));
        add(GeneralBlocks.Deepslate_Lead_Ore,
                createOreDrop(GeneralBlocks.Deepslate_Lead_Ore, GrowableOresItems.Raw_Lead));
        add(GeneralBlocks.Deepslate_Tin_Ore,
                createOreDrop(GeneralBlocks.Deepslate_Tin_Ore, GrowableOresItems.Raw_Tin));
        add(GeneralBlocks.Deepslate_SACRED_Ore,
                createOreDrop(GeneralBlocks.Deepslate_SACRED_Ore, GrowableOresItems.Raw_SACRED));

        //PLASTER
        dropSelf(GeneralBlocks.GREEN_PLASTER);
        dropSelf(GeneralBlocks.PLASTER);
        dropSelf(GeneralBlocks.ORANGE_PLASTER);
        dropSelf(GeneralBlocks.MAGENTA_PLASTER);
        dropSelf(GeneralBlocks.LIGHT_BLUE_PLASTER);
        dropSelf(GeneralBlocks.YELLOW_PLASTER);
        dropSelf(GeneralBlocks.LIME_PLASTER);
        dropSelf(GeneralBlocks.PINK_PLASTER);
        dropSelf(GeneralBlocks.GRAY_PLASTER);
        dropSelf(GeneralBlocks.LIGHT_GRAY_PLASTER);
        dropSelf(GeneralBlocks.CYAN_PLASTER);
        dropSelf(GeneralBlocks.PURPLE_PLASTER);
        dropSelf(GeneralBlocks.BLUE_PLASTER);
        dropSelf(GeneralBlocks.BROWN_PLASTER);
        dropSelf(GeneralBlocks.RED_PLASTER);





        dropSelf(GrowableICOresBlocks.IER_Bronze_Cane);
        dropSelf(GrowableICOresBlocks.IER_silver_Cane);
        dropSelf(GrowableICOresBlocks.IER_Tin_Cane);
        dropSelf(GrowableICOresBlocks.IER_SACRED_Cane);
        dropSelf(GrowableICOresBlocks.IER_steel_Cane);
        dropSelf(GrowableICOresBlocks.IER_LEAD_Cane);

    }
}
