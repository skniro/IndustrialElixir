package com.skniro.industrial_elixir.item;

import com.skniro.growableoresir.block.GrowableICOresBlocks;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialElixir.MOD_ID);

    public static final Supplier<CreativeModeTab> General = CREATIVE_MODE_TABS.register("general",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GeneralBlocks.Machine.get().asItem()))
                    .title(Component.translatable("itemGroup.industrial_elixir.general"))
                    .displayItems((pParameters, content) -> {
                        content.accept(GeneralBlocks.Lead_Ore.get());
                        content.accept(GeneralBlocks.Tin_Ore.get());
                        content.accept(GeneralBlocks.SACRED_Ore.get());
                        content.accept(GeneralBlocks.Deepslate_Lead_Ore.get());
                        content.accept(GeneralBlocks.Deepslate_Tin_Ore.get());
                        content.accept(GeneralBlocks.Deepslate_SACRED_Ore.get());
                        content.accept(GeneralBlocks.Raw_Lead_Block.get());
                        content.accept(GeneralBlocks.Raw_Tin_Block.get());
                        content.accept(GeneralBlocks.Raw_SACRED_Block.get());
                        content.accept(GeneralBlocks.Lead_Block.get());
                        content.accept(GeneralBlocks.Tin_Block.get());
                        content.accept(GeneralBlocks.SACRED_Block.get());
                        content.accept(GeneralBlocks.Silver_Block.get());
                        content.accept(GeneralBlocks.Bronze_Block.get());
                        content.accept(GeneralBlocks.Steel_Block.get());

                        content.accept(GeneralBlocks.Rubber_PLANKS.get());
                        content.accept(GeneralBlocks.Rubber_STAIRS.get());
                        content.accept(GeneralBlocks.Rubber_SLAB.get());
                        content.accept(GeneralBlocks.Rubber_BUTTON.get());
                        content.accept(GeneralBlocks.Rubber_PRESSURE_PLATE.get());
                        content.accept(GeneralBlocks.Rubber_FENCE.get());
                        content.accept(GeneralBlocks.Rubber_FENCE_GATE.get());
                        content.accept(GeneralBlocks.Rubber_LOG.get());
                        content.accept(GeneralBlocks.Rubber_WOOD.get());
                        content.accept(GeneralBlocks.STRIPPED_Rubber_LOG.get());
                        content.accept(GeneralBlocks.STRIPPED_Rubber_WOOD.get());
                        content.accept(GeneralBlocks.Rubber_DOOR.get());
                        content.accept(GeneralBlocks.Rubber_TRAPDOOR.get());
                        content.accept(GeneralBlocks.Rubber_SAPLING.get());
                        content.accept(GeneralBlocks.Rubber_LEAVES.get());
                        content.accept(GeneralBlocks.Rubber_Rubber_LOG.get());
                        content.accept(GeneralBlocks.Rubber_SHELF.get());
                        content.accept(GeneralBlocks.COBBLESTONE_STAIRS.get());
                        content.accept(GrowableOresItems.Sticky_Resin.get());
                        content.accept(MapleArmorItems.Rubber_SIGN.get());
                        content.accept(MapleArmorItems.Rubber_HANGING_SIGN.get());
                        content.accept(GrowableOresItems.RUBBER_BOAT.get());
                        content.accept(GrowableOresItems.RUBBER_CHEST_BOAT.get());
                        content.accept(GeneralBlocks.Machine.get());
                        content.accept(GeneralBlocks.Advanced_Machine.get());
                        content.accept(GeneralBlocks.Super_Machine.get());
                        content.accept(GrowableOresBlocks.FLUID_TANK_BLOCK.get());
                        content.accept(GeneralBlocks.Reinforced_Stone.get());
                        content.accept(GeneralBlocks.Reinforced_Glass.get());
                        content.accept(GeneralBlocks.Reinforced_DOOR.get());

                        //PLASTER
                        content.accept(GeneralBlocks.GREEN_PLASTER.get());
                        content.accept(GeneralBlocks.PLASTER.get());
                        content.accept(GeneralBlocks.ORANGE_PLASTER.get());
                        content.accept(GeneralBlocks.MAGENTA_PLASTER.get());
                        content.accept(GeneralBlocks.LIGHT_BLUE_PLASTER.get());
                        content.accept(GeneralBlocks.YELLOW_PLASTER.get());
                        content.accept(GeneralBlocks.LIME_PLASTER.get());
                        content.accept(GeneralBlocks.PINK_PLASTER.get());
                        content.accept(GeneralBlocks.GRAY_PLASTER.get());
                        content.accept(GeneralBlocks.LIGHT_GRAY_PLASTER.get());
                        content.accept(GeneralBlocks.CYAN_PLASTER.get());
                        content.accept(GeneralBlocks.PURPLE_PLASTER.get());
                        content.accept(GeneralBlocks.BLUE_PLASTER.get());
                        content.accept(GeneralBlocks.BROWN_PLASTER.get());
                        content.accept(GeneralBlocks.RED_PLASTER.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> Generators_And_Wiring = CREATIVE_MODE_TABS.register("generators_and_wiring",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GrowableOresBlocks.COAL_GENERATOR.get().asItem()))
                    .title(Component.translatable("itemGroup.industrial_elixir.generators_and_wiring"))
                    .displayItems((pParameters, content) -> {
                        content.accept(GrowableOresBlocks.COPPER_CABLE.get());
                        content.accept(GrowableOresBlocks.TIN_CABLE.get());
                        content.accept(GrowableOresBlocks.GOLD_CABLE.get());
                        content.accept(GrowableOresBlocks.HV_CABLE.get());
                        content.accept(GrowableOresBlocks.GLASSFIBER_CABLE.get());
                        content.accept(GrowableOresBlocks.INSULATED_COPPER_CABLE.get());
                        content.accept(GrowableOresBlocks.INSULATED_GOLD_CABLE.get());
                        content.accept(GrowableOresBlocks.INSULATED_HV_CABLE.get());

                        content.accept(GrowableOresBlocks.EnergyBox.get());
                        content.accept(GrowableOresBlocks.ChargePad.get());
                        content.accept(GrowableOresBlocks.CESU.get());
                        content.accept(GrowableOresBlocks.MFE.get());
                        content.accept(GrowableOresBlocks.MFSU.get());
                        content.accept(GrowableOresBlocks.ChargePad.get());
                        content.accept(GrowableOresBlocks.CESU_CHARGE_PAD.get());
                        content.accept(GrowableOresBlocks.MFE_CHARGE_PAD.get());
                        content.accept(GrowableOresBlocks.MFSU_CHARGE_PAD.get());
                        content.accept(GrowableOresBlocks.LV_TRANSFORMER.get());
                        content.accept(GrowableOresBlocks.MV_TRANSFORMER.get());
                        content.accept(GrowableOresBlocks.HV_TRANSFORMER.get());
                        content.accept(GrowableOresBlocks.EV_TRANSFORMER.get());
                        content.accept(GrowableOresBlocks.Pipe_Wooden_Fluid_Block.get());
                        content.accept(GrowableOresBlocks.Pipe_Stone_Fluid_Block.get());
                        content.accept(GrowableOresBlocks.COAL_GENERATOR.get());
                        content.accept(GrowableOresBlocks.FLUID_GENERATOR.get());
                        content.accept(GrowableOresBlocks.GENERATOR_Wind_Mill.get());
                        content.accept(GrowableOresBlocks.GENERATOR_SolarPanel.get());
                        content.accept(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL.get());
                        content.accept(GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL.get());
                        content.accept(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL.get());
                        content.accept(GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL.get());
                        content.accept(GrowableOresBlocks.Electric_Heater_Block.get());
                        content.accept(GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> Reactor = CREATIVE_MODE_TABS.register("reactor",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GrowableOresBlocks.SACRED_GENERATOR.get().asItem()))
                    .title(Component.translatable("itemGroup.industrial_elixir.reactor"))
                    .displayItems((pParameters, content) -> {
                        content.accept(GrowableOresItems.SACRED_ESSENCE.get());
                        content.accept(GrowableOresItems.SACRED_SHARD.get());
                        content.accept(GrowableOresItems.SACRED_CORE.get());
                        content.accept(GrowableOresItems.COOLANT_CELL_10K.get());
                        content.accept(GrowableOresItems.COOLANT_CELL_30K.get());
                        content.accept(GrowableOresItems.COOLANT_CELL_60K.get());
                        content.accept(GrowableOresItems.HEAT_VENT.get());
                        content.accept(GrowableOresItems.ADVANCED_HEAT_VENT.get());
                        content.accept(GrowableOresItems.OVERCLOCKED_HEAT_VENT.get());
                        content.accept(GrowableOresItems.EMPTY_VESSEL.get());
                        //content.accept(GrowableOresItems.REACTOR_PLATING.get());
                        //content.accept(GrowableOresItems.NEUTRON_REFLECTOR.get());
                        //content.accept(GrowableOresItems.HEAT_EXCHANGER.get());
                        content.accept(GrowableOresBlocks.SACRED_GENERATOR.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> Machine = CREATIVE_MODE_TABS.register("machine",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GrowableOresBlocks.Macerator_Block.get().asItem()))
                    .title(Component.translatable("itemGroup.industrial_elixir.machine"))
                    .displayItems((pParameters, content) -> {
                        content.accept(GrowableOresBlocks.Macerator_Block.get());
                        content.accept(GrowableOresBlocks.Compressor_Block.get());
                        content.accept(GrowableOresBlocks.MetalFormerBlock.get());
                        content.accept(GrowableOresBlocks.Extractor_Block.get());

                        content.accept(GrowableOresBlocks.MolecularTransformerBlock.get());

                        content.accept(GrowableOresBlocks.Iron_Furnace_Block.get());
                        content.accept(GrowableOresBlocks.ElectricFurnace_Block.get());
                        content.accept(GrowableOresBlocks.INDUCTION_FURNACE.get());
                        content.accept(GrowableOresBlocks.CUTTING_Block.get());
                        content.accept(GrowableOresBlocks.RECYCLER_Block.get());
                        content.accept(GrowableOresBlocks.Ore_Washing_Block.get());
                        content.accept(GrowableOresBlocks.GrowableOres_Block.get());
                        content.accept(GrowableOresBlocks.Brew_Reactor_BLOCK.get());
                        content.accept(GrowableOresBlocks.BLAST_FURNACE_BLOCK.get());
                        content.accept(GrowableOresBlocks.HEAT_CENTRIFUGE.get());
                        content.accept(GrowableOresBlocks.MATTER_GENERATOR.get());
                        content.accept(GrowableOresBlocks.Replicator.get());
                        content.accept(GrowableOresBlocks.PATTERN_STORAGE.get());
                        content.accept(GrowableOresBlocks.CHUNK_LOADER.get());
                        content.accept(GrowableOresBlocks.VENDOR_MACHINE_Block.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> Tool_And_Utilities = CREATIVE_MODE_TABS.register("tool_and_utilities",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GrowableOresItems.ENERGY_STORAGE.get()))
                    .title(Component.translatable("itemGroup.industrial_elixir.tool_and_utilities"))
                    .displayItems((pParameters, content) -> {
                        content.accept(IndustrialElixirFluidItems.Fluid_UU_BUCKET.get());
                        content.accept(IndustrialElixirFluidItems.Fluid_AIR_BUCKET.get());
                        content.accept(IndustrialElixirFluidItems.Hot_Spring_BUCKET.get());
                        content.accept(GrowableOresItems.ENERGY_STORAGE.get());
                        content.accept(GrowableOresItems.OVERCLOCKER.get());
                        content.accept(GrowableOresItems.TRANSFORMER.get());
                        content.accept(GrowableOresItems.REDSTONE_INVERTER.get());

                        content.accept(MapleArmorItems.ROLLING.get());
                        content.accept(MapleArmorItems.CUTTING.get());

                        content.accept(GrowableOresItems.RE_BATTERY.get());
                        addFullEnergyItem(content, GrowableOresItems.RE_BATTERY.get());
                        content.accept(GrowableOresItems.ADVANCED_RE_BATTERY.get());
                        addFullEnergyItem(content, GrowableOresItems.ADVANCED_RE_BATTERY.get());
                        content.accept(GrowableOresItems.ENERGY_CRYSTAL.get());
                        addFullEnergyItem(content, GrowableOresItems.ENERGY_CRYSTAL.get());
                        content.accept(GrowableOresItems.LAPOTRON_CRYSTAL.get());
                        addFullEnergyItem(content, GrowableOresItems.LAPOTRON_CRYSTAL.get());

                        content.accept(GrowableOresItems.EMPTY_CELL.get());
                        content.accept(GrowableOresItems.WATER_CELL.get());
                        content.accept(GrowableOresItems.LAVA_CELL.get());
                        content.accept(IndustrialElixirFluidItems.UU_CELL.get());
                        content.accept(IndustrialElixirFluidItems.AIR_CELL.get());
                        content.accept(IndustrialElixirFluidItems.Hot_Spring_CELL.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> Combat = CREATIVE_MODE_TABS.register("combat",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(MapleArmorItems.Quantum_HELMET.get()))
                    .title(Component.translatable("itemGroup.industrial_elixir.combat"))
                    .displayItems((pParameters, content) -> {
                        content.accept(MapleArmorItems.Quantum_HELMET.get());
                        addFullEnergyItem(content, MapleArmorItems.Quantum_HELMET.get());
                        content.accept(MapleArmorItems.Quantum_CHESTPLATE.get());
                        addFullEnergyItem(content, MapleArmorItems.Quantum_CHESTPLATE.get());
                        content.accept(MapleArmorItems.Quantum_LEGGINGS.get());
                        addFullEnergyItem(content, MapleArmorItems.Quantum_LEGGINGS.get());
                        content.accept(MapleArmorItems.Quantum_BOOTS.get());
                        addFullEnergyItem(content, MapleArmorItems.Quantum_BOOTS.get());
                        content.accept(MapleArmorItems.Electric_Jetpack.get());
                        addFullEnergyItem(content, MapleArmorItems.Electric_Jetpack.get());
                        content.accept(MapleArmorItems.BRONZE_BOOTS.get());
                        content.accept(MapleArmorItems.BRONZE_CHESTPLATE.get());
                        content.accept(MapleArmorItems.BRONZE_HELMET.get());
                        content.accept(MapleArmorItems.BRONZE_LEGGINGS.get());

                        content.accept(MapleArmorItems.BRONZE_PICKAXE.get());
                        content.accept(MapleArmorItems.BRONZE_AXE.get());
                        content.accept(MapleArmorItems.BRONZE_SHOVEL.get());
                        content.accept(MapleArmorItems.BRONZE_SWORD.get());
                        content.accept(MapleArmorItems.BRONZE_HOE.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> Materials = CREATIVE_MODE_TABS.register("materials",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GrowableOresItems.Rubber.get()))
                    .title(Component.translatable("itemGroup.industrial_elixir.materials"))
                    .displayItems((pParameters, content) -> {
                        content.accept(GrowableOresItems.Raw_Lead.get());
                        content.accept(GrowableOresItems.Raw_Tin.get());
                        content.accept(GrowableOresItems.Raw_SACRED.get());

                        content.accept(GrowableOresItems.CRUSHED_COPPER.get());
                        content.accept(GrowableOresItems.CRUSHED_GOLD.get());
                        content.accept(GrowableOresItems.CRUSHED_IRON.get());
                        content.accept(GrowableOresItems.CRUSHED_LEAD.get());
                        content.accept(GrowableOresItems.CRUSHED_SILVER.get());
                        content.accept(GrowableOresItems.CRUSHED_TIN.get());
                        content.accept(GrowableOresItems.CRUSHED_SACRED.get());

                        content.accept(GrowableOresItems.PURIFIED_COPPER.get());
                        content.accept(GrowableOresItems.PURIFIED_GOLD.get());
                        content.accept(GrowableOresItems.PURIFIED_IRON.get());
                        content.accept(GrowableOresItems.PURIFIED_LEAD.get());
                        content.accept(GrowableOresItems.PURIFIED_SILVER.get());
                        content.accept(GrowableOresItems.PURIFIED_TIN.get());
                        content.accept(GrowableOresItems.PURIFIED_SACRED.get());

                        content.accept(GrowableOresItems.BRONZE_DUST.get());
                        content.accept(GrowableOresItems.CLAY_DUST.get());
                        content.accept(GrowableOresItems.COAL_DUST.get());
                        content.accept(GrowableOresItems.COPPER_DUST.get());
                        content.accept(GrowableOresItems.DIAMOND_DUST.get());
                        content.accept(GrowableOresItems.ENERGIUM_DUST.get());
                        content.accept(GrowableOresItems.GOLD_DUST.get());
                        content.accept(GrowableOresItems.IRON_DUST.get());
                        content.accept(GrowableOresItems.LAPIS_DUST.get());
                        content.accept(GrowableOresItems.LEAD_DUST.get());
                        content.accept(GrowableOresItems.LITHIUM_DUST.get());
                        content.accept(GrowableOresItems.OBSIDIAN_DUST.get());
                        content.accept(GrowableOresItems.SILICON_DIOXIDE_DUST.get());
                        content.accept(GrowableOresItems.SILVER_DUST.get());
                        content.accept(GrowableOresItems.STONE_DUST.get());
                        content.accept(GrowableOresItems.SULFUR_DUST.get());
                        content.accept(GrowableOresItems.TIN_DUST.get());

                        content.accept(GrowableOresItems.SMALL_BRONZE_DUST.get());
                        content.accept(GrowableOresItems.SMALL_COPPER_DUST.get());
                        content.accept(GrowableOresItems.SMALL_GOLD_DUST.get());
                        content.accept(GrowableOresItems.SMALL_IRON_DUST.get());
                        content.accept(GrowableOresItems.SMALL_LAPIS_DUST.get());
                        content.accept(GrowableOresItems.SMALL_LEAD_DUST.get());
                        content.accept(GrowableOresItems.SMALL_LITHIUM_DUST.get());
                        content.accept(GrowableOresItems.SMALL_OBSIDIAN_DUST.get());
                        content.accept(GrowableOresItems.SMALL_SILVER_DUST.get());
                        content.accept(GrowableOresItems.SMALL_SULFUR_DUST.get());
                        content.accept(GrowableOresItems.SMALL_TIN_DUST.get());

                        content.accept(GrowableOresItems.BRONZE_INGOT.get());
                        content.accept(GrowableOresItems.LEAD_INGOT.get());
                        content.accept(GrowableOresItems.SILVER_INGOT.get());
                        content.accept(GrowableOresItems.STEEL_INGOT.get());
                        content.accept(GrowableOresItems.TIN_INGOT.get());
                        content.accept(GrowableOresItems.SACRED_INGOT.get());
                        content.accept(GrowableOresItems.IMPURE_SACRED_STONE.get());

                        content.accept(GrowableOresItems.BRONZE_PLATE.get());
                        content.accept(GrowableOresItems.COPPER_PLATE.get());
                        content.accept(GrowableOresItems.GOLD_PLATE.get());
                        content.accept(GrowableOresItems.IRON_PLATE.get());
                        content.accept(GrowableOresItems.LAPIS_PLATE.get());
                        content.accept(GrowableOresItems.LEAD_PLATE.get());
                        content.accept(GrowableOresItems.OBSIDIAN_PLATE.get());
                        content.accept(GrowableOresItems.STEEL_PLATE.get());
                        content.accept(GrowableOresItems.TIN_PLATE.get());

                        content.accept(GrowableOresItems.DENSE_BRONZE_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_COPPER_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_GOLD_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_IRON_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_LAPIS_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_LEAD_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_OBSIDIAN_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_STEEL_PLATE.get());
                        content.accept(GrowableOresItems.DENSE_TIN_PLATE.get());

                        content.accept(GrowableOresItems.BRONZE_CASING.get());
                        content.accept(GrowableOresItems.COPPER_CASING.get());
                        content.accept(GrowableOresItems.GOLD_CASING.get());
                        content.accept(GrowableOresItems.IRON_CASING.get());
                        content.accept(GrowableOresItems.LEAD_CASING.get());
                        content.accept(GrowableOresItems.STEEL_CASING.get());
                        content.accept(GrowableOresItems.TIN_CASING.get());

                        content.accept(GrowableOresItems.Rubber.get());
                        content.accept(GrowableOresItems.CARBON_FIBRE.get());
                        content.accept(GrowableOresItems.CARBON_MESH.get());
                        content.accept(GrowableOresItems.CARBON_PLATE.get());
                        content.accept(GrowableOresItems.COAL_BALL.get());
                        content.accept(GrowableOresItems.COAL_BLOCK.get());
                        content.accept(GrowableOresItems.COAL_CHUNK.get());
                        content.accept(GrowableOresItems.INDUSTRIAL_DIAMOND.get());
                        content.accept(GrowableOresItems.IRIDIUM_SHARD.get());
                        content.accept(GrowableOresItems.IRIDIUM_ORE.get());
                        content.accept(GrowableOresItems.IRIDIUM_PLATE.get());
                        content.accept(GrowableOresItems.ALLOY_INGOT.get());
                        content.accept(GrowableOresItems.ALLOY_PLATE.get());
                        content.accept(GrowableOresItems.Circuit.get());
                        content.accept(GrowableOresItems.Advanced_Circuit.get());
                        content.accept(GrowableOresItems.Electric_Motor.get());
                        content.accept(GrowableOresItems.Scrap.get());
                        content.accept(GrowableOresItems.Scrap_box.get());
                        content.accept(GrowableOresItems.Coil.get());
                        content.accept(GrowableOresItems.SLAG.get());
                        content.accept(GrowableOresItems.ASHES.get());
                        content.accept(GrowableOresItems.PATTERN_STORAGE_CRYSTAL.get());
                        content.accept(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL.get());
                        content.accept(GrowableOresItems.HEAT_CONDUCTOR.get());

                        content.accept(AdvancedItems.IRRADIANT_SACRED_INGOT.get());
                        content.accept(AdvancedItems.IRRADIANT_GLASS_PANE.get());
                        content.accept(AdvancedItems.LUMINITE.get());
                        content.accept(AdvancedItems.ENRICHED_LUMINITE.get());
                        content.accept(AdvancedItems.LUMINITE_ALLOY.get());
                        content.accept(AdvancedItems.ENRICHED_LUMINITE_ALLOY.get());
                        content.accept(AdvancedItems.IRIDIUM_AMETHYST_PLATE.get());
                        content.accept(AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE.get());
                        content.accept(AdvancedItems.IRRADIANT_REINFORCED_PLATE.get());
                        content.accept(AdvancedItems.LUMINITE_Part.get());
                        content.accept(AdvancedItems.Iridium_INGOT.get());
                        content.accept(AdvancedItems.Quantum_Core.get());
                        content.accept(AdvancedItems.MT_Core.get());

                        content.accept(GrowableICOresBlocks.IER_Bronze_Cane.get());
                        content.accept(GrowableICOresBlocks.IER_silver_Cane.get());
                        content.accept(GrowableICOresBlocks.IER_Tin_Cane.get());
                        content.accept(GrowableICOresBlocks.IER_SACRED_Cane.get());
                        content.accept(GrowableICOresBlocks.IER_steel_Cane.get());
                        content.accept(GrowableICOresBlocks.IER_LEAD_Cane.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> AGRICULTURE = CREATIVE_MODE_TABS.register("agriculture",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(MapleFoodComponents.Coffee_Black.get()))
                    .title(Component.translatable("itemGroup.industrial_elixir.agriculture"))
                    .displayItems((pParameters, content) -> {
                        content.accept(GrowableOresBlocks.COFFEE_MACHINE_Block.get());
                        content.accept(GrowableOresBlocks.CROP_FARM_Block.get());
                        content.accept(MapleFoodComponents.Coffee_Beans.get());
                        content.accept(MapleFoodComponents.Coffee_Black.get());
                        content.accept(MapleFoodComponents.Cappuccino.get());
                        content.accept(MapleFoodComponents.Latte.get());
                        content.accept(MapleFoodComponents.Mocha.get());
                        content.accept(MapleFoodComponents.Hot_Cocoa.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static void addFullEnergyItem(CreativeModeTab.Output content, Item item) {
        ItemStack stack = new ItemStack(item);
        stack.setDamageValue(0);
        if (stack.getItem() instanceof SimpleEnergyItem battery) {
            battery.setStoredEnergy(stack, battery.getEnergyCapacity(stack));
        }
        content.accept(stack);
    }
}
