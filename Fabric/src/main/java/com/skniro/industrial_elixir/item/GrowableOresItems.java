package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.entity.MapleEntityType;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.item.init.BatteryItem;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.item.init.ItemUpgradeModule;
import com.skniro.industrial_elixir.item.init.ReactorComponentItem;
import com.skniro.industrial_elixir.item.init.ScrapboxItem;
import com.skniro.industrial_elixir.item.init.PatternStorageCrystalItem;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluids;

public class GrowableOresItems {

    public static final Item RE_BATTERY = registerItem("re_battery",       s -> new BatteryItem(s, EnergyTier.TIER1, 10000), new Item.Properties().stacksTo(1));
    public static final Item ADVANCED_RE_BATTERY = registerItem("advanced_re_battery",    s -> new BatteryItem(s, EnergyTier.TIER2, 100000), new Item.Properties().stacksTo(1));
    public static final Item ENERGY_CRYSTAL = registerItem("energy_crystal",         s -> new BatteryItem(s, EnergyTier.TIER3, 1000000), new Item.Properties().stacksTo(1));
    public static final Item LAPOTRON_CRYSTAL = registerItem("lapotron_crystal",       s -> new BatteryItem(s, EnergyTier.TIER4, 10000000), new Item.Properties().stacksTo(1));

    public static final Item Rubber = registerItem("rubber", Item::new, new Item.Properties());


    public static final Item RUBBER_BOAT = registerItem("rubber_boat", (settings) -> new BoatItem(MapleEntityType.RUBBER_BOAT, settings), new Item.Properties().stacksTo(1));

    public static final Item RUBBER_CHEST_BOAT = registerItem("rubber_chest_boat", (settings) -> new BoatItem(MapleEntityType.RUBBER_CHEST_BOAT, settings), new Item.Properties().stacksTo(1));

    public static final Item OVERCLOCKER = registerItem("overclocker", (settings) -> new ItemUpgradeModule(settings, ItemUpgradeModule.UpgradeType.OVERCLOCKER), new Item.Properties());
    public static final Item ENERGY_STORAGE = registerItem("energy_storage", (settings) -> new ItemUpgradeModule(settings, ItemUpgradeModule.UpgradeType.ENERGY_STORAGE), new Item.Properties());
    public static final Item TRANSFORMER = registerItem("transformer", (settings) -> new ItemUpgradeModule(settings, ItemUpgradeModule.UpgradeType.TRANSFORMER), new Item.Properties());
    public static final Item REDSTONE_INVERTER = registerItem("redstone_inverter", (settings) -> new ItemUpgradeModule(settings, ItemUpgradeModule.UpgradeType.REDSTONE_INVERTER), new Item.Properties());

    public static final Item SACRED_ESSENCE = registerItem("sacred_essence", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.SACRED_ESSENCE), new Item.Properties().stacksTo(1).durability(20000));
    public static final Item SACRED_SHARD = registerItem("sacred_shard", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.SACRED_SHARD), new Item.Properties().stacksTo(1).durability(20000));
    public static final Item SACRED_CORE = registerItem("sacred_core", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.SACRED_CORE), new Item.Properties().stacksTo(1).durability(20000));
    public static final Item COOLANT_CELL_10K = registerItem("coolant_cell_10k", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.COOLANT_CELL), new Item.Properties().stacksTo(16).durability(10000));
    public static final Item COOLANT_CELL_30K = registerItem("coolant_cell_30k", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.COOLANT_CELL), new Item.Properties().stacksTo(16).durability(30000));
    public static final Item COOLANT_CELL_60K = registerItem("coolant_cell_60k", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.COOLANT_CELL), new Item.Properties().stacksTo(16).durability(60000));
    public static final Item HEAT_VENT = registerItem("heat_vent", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.HEAT_VENT), new Item.Properties().stacksTo(16).durability(1000));
    public static final Item ADVANCED_HEAT_VENT = registerItem("advanced_heat_vent", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.ADVANCED_HEAT_VENT), new Item.Properties().stacksTo(16).durability(1000));
    public static final Item OVERCLOCKED_HEAT_VENT = registerItem("overclocked_heat_vent", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.OVERCLOCKED_HEAT_VENT), new Item.Properties().stacksTo(16).durability(1000));
    public static final Item REACTOR_PLATING = registerItem("reactor_plating", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.REACTOR_PLATING), new Item.Properties().stacksTo(16));
    public static final Item NEUTRON_REFLECTOR = registerItem("neutron_reflector", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.NEUTRON_REFLECTOR), new Item.Properties().stacksTo(1).durability(10000));
    public static final Item HEAT_EXCHANGER = registerItem("heat_exchanger", (settings) -> new ReactorComponentItem(settings, ReactorComponentItem.ComponentType.HEAT_EXCHANGER), new Item.Properties().stacksTo(1).durability(2500));

    public static final Item Raw_Lead = registerItem("raw_lead", Item::new, new Item.Properties());
    public static final Item Raw_Tin = registerItem("raw_tin", Item::new, new Item.Properties());
    public static final Item Raw_SACRED = registerItem("raw_sacred", Item::new, new Item.Properties());

    public static final Item CRUSHED_COPPER = registerItem("crushed_copper", Item::new, new Item.Properties());
    public static final Item CRUSHED_GOLD = registerItem("crushed_gold", Item::new, new Item.Properties());
    public static final Item CRUSHED_IRON = registerItem("crushed_iron", Item::new, new Item.Properties());
    public static final Item CRUSHED_LEAD = registerItem("crushed_lead", Item::new, new Item.Properties());
    public static final Item CRUSHED_SILVER = registerItem("crushed_silver", Item::new, new Item.Properties());
    public static final Item CRUSHED_TIN = registerItem("crushed_tin", Item::new, new Item.Properties());
    public static final Item CRUSHED_SACRED = registerItem("crushed_sacred", Item::new, new Item.Properties());

    public static final Item PURIFIED_COPPER = registerItem("purified_copper", Item::new, new Item.Properties());
    public static final Item PURIFIED_GOLD = registerItem("purified_gold", Item::new, new Item.Properties());
    public static final Item PURIFIED_IRON = registerItem("purified_iron", Item::new, new Item.Properties());
    public static final Item PURIFIED_LEAD = registerItem("purified_lead", Item::new, new Item.Properties());
    public static final Item PURIFIED_SILVER = registerItem("purified_silver", Item::new, new Item.Properties());
    public static final Item PURIFIED_TIN = registerItem("purified_tin", Item::new, new Item.Properties());
    public static final Item PURIFIED_SACRED = registerItem("purified_sacred", Item::new, new Item.Properties());

    public static final Item BRONZE_DUST = registerItem("bronze_dust", Item::new, new Item.Properties());
    public static final Item CLAY_DUST = registerItem("clay_dust", Item::new, new Item.Properties());
    public static final Item COAL_DUST = registerItem("coal_dust", Item::new, new Item.Properties());
    public static final Item COPPER_DUST = registerItem("copper_dust", Item::new, new Item.Properties());
    public static final Item DIAMOND_DUST = registerItem("diamond_dust", Item::new, new Item.Properties());
    public static final Item ENERGIUM_DUST = registerItem("energium_dust", Item::new, new Item.Properties());
    public static final Item GOLD_DUST = registerItem("gold_dust", Item::new, new Item.Properties());
    public static final Item IRON_DUST = registerItem("iron_dust", Item::new, new Item.Properties());
    public static final Item LAPIS_DUST = registerItem("lapis_dust", Item::new, new Item.Properties());
    public static final Item LEAD_DUST = registerItem("lead_dust", Item::new, new Item.Properties());
    public static final Item LITHIUM_DUST = registerItem("lithium_dust", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_DUST = registerItem("obsidian_dust", Item::new, new Item.Properties());
    public static final Item SILICON_DIOXIDE_DUST = registerItem("silicon_dioxide_dust", Item::new, new Item.Properties());
    public static final Item SILVER_DUST = registerItem("silver_dust", Item::new, new Item.Properties());
    public static final Item STONE_DUST = registerItem("stone_dust", Item::new, new Item.Properties());
    public static final Item SULFUR_DUST = registerItem("sulfur_dust", Item::new, new Item.Properties());
    public static final Item TIN_DUST = registerItem("tin_dust", Item::new, new Item.Properties());

    public static final Item SMALL_BRONZE_DUST = registerItem("small_bronze_dust", Item::new, new Item.Properties());
    public static final Item SMALL_COPPER_DUST = registerItem("small_copper_dust", Item::new, new Item.Properties());
    public static final Item SMALL_GOLD_DUST = registerItem("small_gold_dust", Item::new, new Item.Properties());
    public static final Item SMALL_IRON_DUST = registerItem("small_iron_dust", Item::new, new Item.Properties());
    public static final Item SMALL_LAPIS_DUST = registerItem("small_lapis_dust", Item::new, new Item.Properties());
    public static final Item SMALL_LEAD_DUST = registerItem("small_lead_dust", Item::new, new Item.Properties());
    public static final Item SMALL_LITHIUM_DUST = registerItem("small_lithium_dust", Item::new, new Item.Properties());
    public static final Item SMALL_OBSIDIAN_DUST = registerItem("small_obsidian_dust", Item::new, new Item.Properties());
    public static final Item SMALL_SILVER_DUST = registerItem("small_silver_dust", Item::new, new Item.Properties());
    public static final Item SMALL_SULFUR_DUST = registerItem("small_sulfur_dust", Item::new, new Item.Properties());
    public static final Item SMALL_TIN_DUST = registerItem("small_tin_dust", Item::new, new Item.Properties());

    public static final Item BRONZE_INGOT = registerItem("bronze_ingot", Item::new, new Item.Properties());
    public static final Item LEAD_INGOT = registerItem("lead_ingot", Item::new, new Item.Properties());
    public static final Item SILVER_INGOT = registerItem("silver_ingot", Item::new, new Item.Properties());
    public static final Item STEEL_INGOT = registerItem("steel_ingot", Item::new, new Item.Properties());
    public static final Item TIN_INGOT = registerItem("tin_ingot", Item::new, new Item.Properties());
    public static final Item SACRED_INGOT = registerItem("sacred_ingot", Item::new, new Item.Properties());

    public static final Item BRONZE_PLATE = registerItem("bronze_plate", Item::new, new Item.Properties());
    public static final Item COPPER_PLATE = registerItem("copper_plate", Item::new, new Item.Properties());
    public static final Item GOLD_PLATE = registerItem("gold_plate", Item::new, new Item.Properties());
    public static final Item IRON_PLATE = registerItem("iron_plate", Item::new, new Item.Properties());
    public static final Item LAPIS_PLATE = registerItem("lapis_plate", Item::new, new Item.Properties());
    public static final Item LEAD_PLATE = registerItem("lead_plate", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_PLATE = registerItem("obsidian_plate", Item::new, new Item.Properties());
    public static final Item STEEL_PLATE = registerItem("steel_plate", Item::new, new Item.Properties());
    public static final Item TIN_PLATE = registerItem("tin_plate", Item::new, new Item.Properties());

    public static final Item DENSE_BRONZE_PLATE = registerItem("dense_bronze_plate", Item::new, new Item.Properties());
    public static final Item DENSE_COPPER_PLATE = registerItem("dense_copper_plate", Item::new, new Item.Properties());
    public static final Item DENSE_GOLD_PLATE = registerItem("dense_gold_plate", Item::new, new Item.Properties());
    public static final Item DENSE_IRON_PLATE = registerItem("dense_iron_plate", Item::new, new Item.Properties());
    public static final Item DENSE_LAPIS_PLATE = registerItem("dense_lapis_plate", Item::new, new Item.Properties());
    public static final Item DENSE_LEAD_PLATE = registerItem("dense_lead_plate", Item::new, new Item.Properties());
    public static final Item DENSE_OBSIDIAN_PLATE = registerItem("dense_obsidian_plate", Item::new, new Item.Properties());
    public static final Item DENSE_STEEL_PLATE = registerItem("dense_steel_plate", Item::new, new Item.Properties());
    public static final Item DENSE_TIN_PLATE = registerItem("dense_tin_plate", Item::new, new Item.Properties());

    public static final Item BRONZE_CASING = registerItem("bronze_casing", Item::new, new Item.Properties());
    public static final Item COPPER_CASING = registerItem("copper_casing", Item::new, new Item.Properties());
    public static final Item GOLD_CASING = registerItem("gold_casing", Item::new, new Item.Properties());
    public static final Item IRON_CASING = registerItem("iron_casing", Item::new, new Item.Properties());
    public static final Item LEAD_CASING = registerItem("lead_casing", Item::new, new Item.Properties());
    public static final Item STEEL_CASING = registerItem("steel_casing", Item::new, new Item.Properties());
    public static final Item TIN_CASING = registerItem("tin_casing", Item::new, new Item.Properties());

    public static final Item CARBON_FIBRE = registerItem("carbon_fibre", Item::new, new Item.Properties());
    public static final Item CARBON_MESH = registerItem("carbon_mesh", Item::new, new Item.Properties());
    public static final Item CARBON_PLATE = registerItem("carbon_plate", Item::new, new Item.Properties());
    public static final Item COAL_BALL = registerItem("coal_ball", Item::new, new Item.Properties());
    public static final Item COAL_BLOCK = registerItem("coal_block", Item::new, new Item.Properties());
    public static final Item COAL_CHUNK = registerItem("coal_chunk", Item::new, new Item.Properties());
    public static final Item INDUSTRIAL_DIAMOND = registerItem("industrial_diamond", Item::new, new Item.Properties());

    public static final Item IRIDIUM_SHARD = registerItem("iridium_shard", Item::new, new Item.Properties().rarity(Rarity.RARE));
    public static final Item IRIDIUM_ORE = registerItem("iridium_ore", Item::new, new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item IRIDIUM_PLATE = registerItem("iridium_plate", Item::new, new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item ALLOY_INGOT = registerItem("alloy_ingot", Item::new, new Item.Properties());
    public static final Item ALLOY_PLATE = registerItem("alloy_plate", Item::new, new Item.Properties());
    public static final Item Sticky_Resin = registerItem("sticky_resin", Item::new, new Item.Properties());

    public static final Item Circuit = registerItem("circuit", Item::new, new Item.Properties());
    public static final Item Advanced_Circuit = registerItem("advanced_circuit", Item::new, new Item.Properties());

    public static final Item Coil = registerItem("coil", Item::new, new Item.Properties());
    public static final Item Electric_Motor = registerItem("electric_motor", Item::new, new Item.Properties());
    public static final Item Scrap = registerItem("scrap", Item::new, new Item.Properties());
    public static final Item Scrap_box = registerItem("scrap_box", ScrapboxItem::new, new Item.Properties());

    public static final Item EMPTY_CELL = registerItem("empty_cell",(properties)-> new FluidCellItem(properties, Fluids.EMPTY), new Item.Properties().stacksTo(16));
    public static final Item WATER_CELL = registerItem("water_cell",(properties)-> new FluidCellItem(properties, Fluids.WATER), new Item.Properties().stacksTo(16));
    public static final Item LAVA_CELL = registerItem("lava_cell",(properties)-> new FluidCellItem(properties, Fluids.LAVA), new Item.Properties().stacksTo(16));
    public static final Item EMPTY_VESSEL = registerItem("empty_vessel", Item::new, new Item.Properties().stacksTo(16));
    public static final Item IMPURE_SACRED_STONE = registerItem("impure_sacred_stone", Item::new, new Item.Properties());
    public static final Item SLAG = registerItem("slag", Item::new, new Item.Properties());
    public static final Item ASHES = registerItem("ashes", Item::new, new Item.Properties());
    public static final Item PATTERN_STORAGE_CRYSTAL = registerItem("pattern_storage_crystal", PatternStorageCrystalItem::new, new Item.Properties().stacksTo(1));
    public static final Item RAW_PATTERN_STORAGE_CRYSTAL = registerItem("raw_pattern_storage_crystal", Item::new, new Item.Properties().stacksTo(1));
    public static final Item HEAT_CONDUCTOR = registerItem("heat_conductor", Item::new, new Item.Properties());

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name))));
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)), item);
    }

    public static void shield_item(){
      IndustrialElixir.LOGGER.debug("register shield item.");
    }
}
