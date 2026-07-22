package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final ResourceKey<CreativeModeTab> General = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "general"));
    public static final ResourceKey<CreativeModeTab> Generators_And_Wiring = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "generators_and_wiring"));
    public static final ResourceKey<CreativeModeTab> Reactor = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "reactor"));
    public static final ResourceKey<CreativeModeTab> Machine = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "machine"));
    public static final ResourceKey<CreativeModeTab> Tool_And_Utilities = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "tool_and_utilities"));
    public static final ResourceKey<CreativeModeTab> Combat = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "combat"));
    public static final ResourceKey<CreativeModeTab> Materials = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "materials"));

    public static void CreativeTab() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, General, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GeneralBlocks.Machine.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.general"))
                .build()); // build() no longer registers by itself

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Generators_And_Wiring, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GrowableOresBlocks.COAL_GENERATOR.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.generators_and_wiring"))
                .build()); // build() no longer registers by itself

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Reactor, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GrowableOresBlocks.SACRED_GENERATOR.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.reactor"))
                .build()); // build() no longer registers by itself

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Machine, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GrowableOresBlocks.Macerator_Block.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.machine"))
                .build()); // build() no longer registers by itself

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Tool_And_Utilities, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GrowableOresItems.ENERGY_STORAGE.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.tool_and_utilities"))
                .build()); // build() no longer registers by itself

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Combat, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(MapleArmorItems.Quantum_HELMET.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.combat"))
                .build()); // build() no longer registers by itself

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Materials, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GrowableOresItems.Rubber.asItem()))
                .title(Component.translatable("itemGroup.industrial_elixir.materials"))
                .build()); // build() no longer registers by itself
    }

    public static void CreativeTabItem() {
        CreativeModeTabEvents.modifyOutputEvent(General).register(content -> {
            content.accept(GeneralBlocks.Lead_Ore);
            content.accept(GeneralBlocks.Tin_Ore);
            content.accept(GeneralBlocks.SACRED_Ore);
            content.accept(GeneralBlocks.Deepslate_Lead_Ore);
            content.accept(GeneralBlocks.Deepslate_Tin_Ore);
            content.accept(GeneralBlocks.Deepslate_SACRED_Ore);
            content.accept(GeneralBlocks.Raw_Lead_Block);
            content.accept(GeneralBlocks.Raw_Tin_Block);
            content.accept(GeneralBlocks.Raw_SACRED_Block);
            content.accept(GeneralBlocks.Lead_Block);
            content.accept(GeneralBlocks.Tin_Block);
            content.accept(GeneralBlocks.SACRED_Block);
            content.accept(GeneralBlocks.Silver_Block);
            content.accept(GeneralBlocks.Bronze_Block);
            content.accept(GeneralBlocks.Steel_Block);

            content.accept(GeneralBlocks.Rubber_STAIRS);
            content.accept(GeneralBlocks.Rubber_SLAB);
            content.accept(GeneralBlocks.Rubber_BUTTON);
            content.accept(GeneralBlocks.Rubber_PRESSURE_PLATE);
            content.accept(GeneralBlocks.Rubber_FENCE);
            content.accept(GeneralBlocks.Rubber_FENCE_GATE);
            content.accept(GeneralBlocks.Rubber_LOG);
            content.accept(GeneralBlocks.Rubber_LOG);
            content.accept(GeneralBlocks.Rubber_WOOD);
            content.accept(GeneralBlocks.STRIPPED_Rubber_LOG);
            content.accept(GeneralBlocks.STRIPPED_Rubber_LOG);
            content.accept(GeneralBlocks.STRIPPED_Rubber_WOOD);
            content.accept(GeneralBlocks.Rubber_DOOR);
            content.accept(GeneralBlocks.Rubber_TRAPDOOR);
            content.accept(GeneralBlocks.Rubber_SAPLING);
            content.accept(GeneralBlocks.Rubber_LEAVES);
            content.accept(GeneralBlocks.Rubber_Rubber_LOG);
            content.accept(GrowableOresItems.Sticky_Resin);
            content.accept(MapleArmorItems.Rubber_SIGN);
            content.accept(MapleArmorItems.Rubber_HANGING_SIGN);
            content.accept(GrowableOresItems.RUBBER_BOAT);
            content.accept(GrowableOresItems.RUBBER_CHEST_BOAT);
            content.accept(GeneralBlocks.Machine);
            content.accept(GeneralBlocks.Advanced_Machine);
            content.accept(GeneralBlocks.Super_Machine);
            content.accept(GrowableOresBlocks.FLUID_TANK_BLOCK);
            content.accept(GeneralBlocks.Reinforced_Stone);
            content.accept(GeneralBlocks.Reinforced_Glass);
            content.accept(GeneralBlocks.Reinforced_DOOR);

            //PLASTER
            content.accept(GeneralBlocks.GREEN_PLASTER);
            content.accept(GeneralBlocks.PLASTER);
            content.accept(GeneralBlocks.ORANGE_PLASTER);
            content.accept(GeneralBlocks.MAGENTA_PLASTER);
            content.accept(GeneralBlocks.LIGHT_BLUE_PLASTER);
            content.accept(GeneralBlocks.YELLOW_PLASTER);
            content.accept(GeneralBlocks.LIME_PLASTER);
            content.accept(GeneralBlocks.PINK_PLASTER);
            content.accept(GeneralBlocks.GRAY_PLASTER);
            content.accept(GeneralBlocks.LIGHT_GRAY_PLASTER);
            content.accept(GeneralBlocks.CYAN_PLASTER);
            content.accept(GeneralBlocks.PURPLE_PLASTER);
            content.accept(GeneralBlocks.BLUE_PLASTER);
            content.accept(GeneralBlocks.BROWN_PLASTER);
            content.accept(GeneralBlocks.RED_PLASTER);
        });

        CreativeModeTabEvents.modifyOutputEvent(Generators_And_Wiring).register(content -> {
            content.accept(GrowableOresBlocks.COPPER_CABLE);
            content.accept(GrowableOresBlocks.TIN_CABLE);
            content.accept(GrowableOresBlocks.GOLD_CABLE);
            content.accept(GrowableOresBlocks.HV_CABLE);
            content.accept(GrowableOresBlocks.GLASSFIBER_CABLE);
            content.accept(GrowableOresBlocks.INSULATED_COPPER_CABLE);
            content.accept(GrowableOresBlocks.INSULATED_GOLD_CABLE);
            content.accept(GrowableOresBlocks.INSULATED_HV_CABLE);

            content.accept(GrowableOresBlocks.EnergyBox);
            content.accept(GrowableOresBlocks.ChargePad);
            content.accept(GrowableOresBlocks.CESU);
            content.accept(GrowableOresBlocks.MFE);
            content.accept(GrowableOresBlocks.MFSU);
            content.accept(GrowableOresBlocks.ChargePad);
            content.accept(GrowableOresBlocks.CESU_CHARGE_PAD);
            content.accept(GrowableOresBlocks.MFE_CHARGE_PAD);
            content.accept(GrowableOresBlocks.MFSU_CHARGE_PAD);
            content.accept(GrowableOresBlocks.LV_TRANSFORMER);
            content.accept(GrowableOresBlocks.MV_TRANSFORMER);
            content.accept(GrowableOresBlocks.HV_TRANSFORMER);
            content.accept(GrowableOresBlocks.EV_TRANSFORMER);
            content.accept(GrowableOresBlocks.Pipe_Wooden_Fluid_Block);
            content.accept(GrowableOresBlocks.Pipe_Stone_Fluid_Block);
            content.accept(GrowableOresBlocks.COAL_GENERATOR);
            content.accept(GrowableOresBlocks.FLUID_GENERATOR);
            content.accept(GrowableOresBlocks.GENERATOR_Wind_Mill);
            content.accept(GrowableOresBlocks.GENERATOR_SolarPanel);
            content.accept(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL);
            content.accept(GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL);
            content.accept(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL);
            content.accept(GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL);
            content.accept(GrowableOresBlocks.Electric_Heater_Block);
            content.accept(GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR);
        });

        CreativeModeTabEvents.modifyOutputEvent(Reactor).register(content -> {
            content.accept(GrowableOresItems.SACRED_ESSENCE);
            content.accept(GrowableOresItems.SACRED_SHARD);
            content.accept(GrowableOresItems.SACRED_CORE);
            content.accept(GrowableOresItems.COOLANT_CELL_10K);
            content.accept(GrowableOresItems.COOLANT_CELL_30K);
            content.accept(GrowableOresItems.COOLANT_CELL_60K);
            content.accept(GrowableOresItems.HEAT_VENT);
            content.accept(GrowableOresItems.ADVANCED_HEAT_VENT);
            content.accept(GrowableOresItems.OVERCLOCKED_HEAT_VENT);
            content.accept(GrowableOresItems.EMPTY_VESSEL);
            //content.accept(GrowableOresItems.REACTOR_PLATING);
            //content.accept(GrowableOresItems.NEUTRON_REFLECTOR);
            //content.accept(GrowableOresItems.HEAT_EXCHANGER);
            content.accept(GrowableOresBlocks.SACRED_GENERATOR);

        });

        CreativeModeTabEvents.modifyOutputEvent(Machine).register(content -> {
            content.accept(GrowableOresBlocks.Macerator_Block);
            content.accept(GrowableOresBlocks.Compressor_Block);
            content.accept(GrowableOresBlocks.MetalFormerBlock);
            content.accept(GrowableOresBlocks.Extractor_Block);

            content.accept(GrowableOresBlocks.MolecularTransformerBlock);

            content.accept(GrowableOresBlocks.Iron_Furnace_Block);
            content.accept(GrowableOresBlocks.ElectricFurnace_Block);
            content.accept(GrowableOresBlocks.INDUCTION_FURNACE);
            content.accept(GrowableOresBlocks.CUTTING_Block);
            content.accept(GrowableOresBlocks.RECYCLER_Block);
            content.accept(GrowableOresBlocks.Ore_Washing_Block);
            content.accept(GrowableOresBlocks.GrowableOres_Block);
            content.accept(GrowableOresBlocks.Brew_Reactor_BLOCK);
            content.accept(GrowableOresBlocks.Ore_Washing_Block);
            content.accept(GrowableOresBlocks.BLAST_FURNACE_BLOCK);
            content.accept(GrowableOresBlocks.HEAT_CENTRIFUGE);
            content.accept(GrowableOresBlocks.MATTER_GENERATOR);
            content.accept(GrowableOresBlocks.Replicator);
            content.accept(GrowableOresBlocks.PATTERN_STORAGE);
            content.accept(GrowableOresBlocks.CHUNK_LOADER);
        });

        CreativeModeTabEvents.modifyOutputEvent(Tool_And_Utilities).register(content -> {
            content.accept(IndustrialElixirFluidItems.Fluid_UU_BUCKET);
            content.accept(IndustrialElixirFluidItems.Fluid_AIR_BUCKET);
            content.accept(GrowableOresItems.ENERGY_STORAGE);
            content.accept(GrowableOresItems.OVERCLOCKER);
            content.accept(GrowableOresItems.TRANSFORMER);
            content.accept(GrowableOresItems.REDSTONE_INVERTER);

            content.accept(MapleArmorItems.ROLLING);
            content.accept(MapleArmorItems.CUTTING);

            content.accept(GrowableOresItems.RE_BATTERY);
            addFullEnergyItem(content, GrowableOresItems.RE_BATTERY);
            content.accept(GrowableOresItems.ADVANCED_RE_BATTERY);
            addFullEnergyItem(content, GrowableOresItems.ADVANCED_RE_BATTERY);
            content.accept(GrowableOresItems.ENERGY_CRYSTAL);
            addFullEnergyItem(content, GrowableOresItems.ENERGY_CRYSTAL);
            content.accept(GrowableOresItems.LAPOTRON_CRYSTAL);
            addFullEnergyItem(content, GrowableOresItems.LAPOTRON_CRYSTAL);

            content.accept(GrowableOresItems.EMPTY_CELL);
            content.accept(GrowableOresItems.WATER_CELL);
            content.accept(GrowableOresItems.LAVA_CELL);
            content.accept(IndustrialElixirFluidItems.UU_CELL);
            content.accept(IndustrialElixirFluidItems.AIR_CELL);
        });

        CreativeModeTabEvents.modifyOutputEvent(Combat).register(content -> {
            content.accept(MapleArmorItems.Quantum_HELMET);
            addFullEnergyItem(content, MapleArmorItems.Quantum_HELMET);
            content.accept(MapleArmorItems.Quantum_CHESTPLATE);
            addFullEnergyItem(content, MapleArmorItems.Quantum_CHESTPLATE);
            content.accept(MapleArmorItems.Quantum_LEGGINGS);
            addFullEnergyItem(content, MapleArmorItems.Quantum_LEGGINGS);
            content.accept(MapleArmorItems.Quantum_BOOTS);
            addFullEnergyItem(content, MapleArmorItems.Quantum_BOOTS);
            content.accept(MapleArmorItems.Electric_Jetpack);
            addFullEnergyItem(content, MapleArmorItems.Electric_Jetpack);
            content.accept(MapleArmorItems.BRONZE_BOOTS);
            content.accept(MapleArmorItems.BRONZE_CHESTPLATE);
            content.accept(MapleArmorItems.BRONZE_HELMET);
            content.accept(MapleArmorItems.BRONZE_LEGGINGS);

            content.accept(MapleArmorItems.BRONZE_PICKAXE);
            content.accept(MapleArmorItems.BRONZE_AXE);
            content.accept(MapleArmorItems.BRONZE_SHOVEL);
            content.accept(MapleArmorItems.BRONZE_SWORD);
            content.accept(MapleArmorItems.BRONZE_HOE);

            content.accept(MapleArmorItems.Electric_Jetpack);
        });

        CreativeModeTabEvents.modifyOutputEvent(Materials).register(content -> {
            content.accept(GrowableOresItems.Raw_Lead);
            content.accept(GrowableOresItems.Raw_Tin);
            content.accept(GrowableOresItems.Raw_SACRED);

            content.accept(GrowableOresItems.CRUSHED_COPPER);
            content.accept(GrowableOresItems.CRUSHED_GOLD);
            content.accept(GrowableOresItems.CRUSHED_IRON);
            content.accept(GrowableOresItems.CRUSHED_LEAD);
            content.accept(GrowableOresItems.CRUSHED_SILVER);
            content.accept(GrowableOresItems.CRUSHED_TIN);
            content.accept(GrowableOresItems.CRUSHED_SACRED);

            content.accept(GrowableOresItems.PURIFIED_COPPER);
            content.accept(GrowableOresItems.PURIFIED_GOLD);
            content.accept(GrowableOresItems.PURIFIED_IRON);
            content.accept(GrowableOresItems.PURIFIED_LEAD);
            content.accept(GrowableOresItems.PURIFIED_SILVER);
            content.accept(GrowableOresItems.PURIFIED_TIN);
            content.accept(GrowableOresItems.PURIFIED_SACRED);

            content.accept(GrowableOresItems.BRONZE_DUST);
            content.accept(GrowableOresItems.CLAY_DUST);
            content.accept(GrowableOresItems.COAL_DUST);
            content.accept(GrowableOresItems.COPPER_DUST);
            content.accept(GrowableOresItems.DIAMOND_DUST);
            content.accept(GrowableOresItems.ENERGIUM_DUST);
            content.accept(GrowableOresItems.GOLD_DUST);
            content.accept(GrowableOresItems.IRON_DUST);
            content.accept(GrowableOresItems.LAPIS_DUST);
            content.accept(GrowableOresItems.LEAD_DUST);
            content.accept(GrowableOresItems.LITHIUM_DUST);
            content.accept(GrowableOresItems.OBSIDIAN_DUST);
            content.accept(GrowableOresItems.SILICON_DIOXIDE_DUST);
            content.accept(GrowableOresItems.SILVER_DUST);
            content.accept(GrowableOresItems.STONE_DUST);
            content.accept(GrowableOresItems.SULFUR_DUST);
            content.accept(GrowableOresItems.TIN_DUST);

            content.accept(GrowableOresItems.SMALL_BRONZE_DUST);
            content.accept(GrowableOresItems.SMALL_COPPER_DUST);
            content.accept(GrowableOresItems.SMALL_GOLD_DUST);
            content.accept(GrowableOresItems.SMALL_IRON_DUST);
            content.accept(GrowableOresItems.SMALL_LAPIS_DUST);
            content.accept(GrowableOresItems.SMALL_LEAD_DUST);
            content.accept(GrowableOresItems.SMALL_LITHIUM_DUST);
            content.accept(GrowableOresItems.SMALL_OBSIDIAN_DUST);
            content.accept(GrowableOresItems.SMALL_SILVER_DUST);
            content.accept(GrowableOresItems.SMALL_SULFUR_DUST);
            content.accept(GrowableOresItems.SMALL_TIN_DUST);

            content.accept(GrowableOresItems.BRONZE_INGOT);
            content.accept(GrowableOresItems.LEAD_INGOT);
            content.accept(GrowableOresItems.SILVER_INGOT);
            content.accept(GrowableOresItems.STEEL_INGOT);
            content.accept(GrowableOresItems.TIN_INGOT);
            content.accept(GrowableOresItems.SACRED_INGOT);
            content.accept(GrowableOresItems.IMPURE_SACRED_STONE);

            content.accept(GrowableOresItems.BRONZE_PLATE);
            content.accept(GrowableOresItems.COPPER_PLATE);
            content.accept(GrowableOresItems.GOLD_PLATE);
            content.accept(GrowableOresItems.IRON_PLATE);
            content.accept(GrowableOresItems.LAPIS_PLATE);
            content.accept(GrowableOresItems.LEAD_PLATE);
            content.accept(GrowableOresItems.OBSIDIAN_PLATE);
            content.accept(GrowableOresItems.STEEL_PLATE);
            content.accept(GrowableOresItems.TIN_PLATE);

            content.accept(GrowableOresItems.DENSE_BRONZE_PLATE);
            content.accept(GrowableOresItems.DENSE_COPPER_PLATE);
            content.accept(GrowableOresItems.DENSE_GOLD_PLATE);
            content.accept(GrowableOresItems.DENSE_IRON_PLATE);
            content.accept(GrowableOresItems.DENSE_LAPIS_PLATE);
            content.accept(GrowableOresItems.DENSE_LEAD_PLATE);
            content.accept(GrowableOresItems.DENSE_OBSIDIAN_PLATE);
            content.accept(GrowableOresItems.DENSE_STEEL_PLATE);
            content.accept(GrowableOresItems.DENSE_TIN_PLATE);

            content.accept(GrowableOresItems.BRONZE_CASING);
            content.accept(GrowableOresItems.COPPER_CASING);
            content.accept(GrowableOresItems.GOLD_CASING);
            content.accept(GrowableOresItems.IRON_CASING);
            content.accept(GrowableOresItems.LEAD_CASING);
            content.accept(GrowableOresItems.STEEL_CASING);
            content.accept(GrowableOresItems.TIN_CASING);

            content.accept(GrowableOresItems.Rubber);
            content.accept(GrowableOresItems.CARBON_FIBRE);
            content.accept(GrowableOresItems.CARBON_MESH);
            content.accept(GrowableOresItems.CARBON_PLATE);
            content.accept(GrowableOresItems.COAL_BALL);
            content.accept(GrowableOresItems.COAL_BLOCK);
            content.accept(GrowableOresItems.COAL_CHUNK);
            content.accept(GrowableOresItems.INDUSTRIAL_DIAMOND);
            content.accept(GrowableOresItems.IRIDIUM_SHARD);
            content.accept(GrowableOresItems.IRIDIUM_ORE);
            content.accept(GrowableOresItems.IRIDIUM_PLATE);
            content.accept(GrowableOresItems.ALLOY_INGOT);
            content.accept(GrowableOresItems.ALLOY_PLATE);
            content.accept(GrowableOresItems.Circuit);
            content.accept(GrowableOresItems.Advanced_Circuit);
            content.accept(GrowableOresItems.Electric_Motor);
            content.accept(GrowableOresItems.Scrap);
            content.accept(GrowableOresItems.Scrap_box);
            content.accept(GrowableOresItems.Coil);
            content.accept(GrowableOresItems.SLAG);
            content.accept(GrowableOresItems.ASHES);
            content.accept(GrowableOresItems.PATTERN_STORAGE_CRYSTAL);
            content.accept(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL);
            content.accept(GrowableOresItems.HEAT_CONDUCTOR);

            content.accept(AdvancedItems.IRRADIANT_SACRED_INGOT);
            content.accept(AdvancedItems.IRRADIANT_GLASS_PANE);
            content.accept(AdvancedItems.LUMINITE);
            content.accept(AdvancedItems.ENRICHED_LUMINITE);
            content.accept(AdvancedItems.LUMINITE_ALLOY);
            content.accept(AdvancedItems.ENRICHED_LUMINITE_ALLOY);
            content.accept(AdvancedItems.IRIDIUM_AMETHYST_PLATE);
            content.accept(AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE);
            content.accept(AdvancedItems.IRRADIANT_REINFORCED_PLATE);
            content.accept(AdvancedItems.LUMINITE_Part);
            content.accept(AdvancedItems.Iridium_INGOT);
            content.accept(AdvancedItems.Quantum_Core);
            content.accept(AdvancedItems.MT_Core);
        });
    }

    public static void addFullEnergyItem(FabricCreativeModeTabOutput content, Item item) {
        ItemStack stack = new ItemStack(item);
        stack.setDamageValue(0);
        if (stack.getItem() instanceof SimpleEnergyItem battery) {
            battery.setStoredEnergy(stack, battery.getEnergyCapacity(stack));
        }
        content.accept(stack);
    }

}
