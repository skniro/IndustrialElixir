package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.item.init.QuantumSuitItem;
import com.skniro.industrial_elixir.item.init.ElectricJetpackItem;
import com.skniro.industrial_elixir.item.init.RollingCuttingToolItem;
import com.skniro.industrial_elixir.item.init.equipment.MapleArmorMaterials;
import com.skniro.industrial_elixir.item.init.tool.MapleToolMaterials;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.equipment.ArmorType;
import java.util.function.Function;

public class MapleArmorItems {
    //Tool
    public static final Item BRONZE_HELMET = registerItem("bronze_helmet", Item::new,(new Item.Properties()).humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.HELMET));
    public static final Item BRONZE_CHESTPLATE = registerItem("bronze_chestplate", Item::new,(new Item.Properties()).humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.CHESTPLATE));
    public static final Item BRONZE_LEGGINGS = registerItem("bronze_leggings", Item::new,(new Item.Properties()).humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.LEGGINGS));
    public static final Item BRONZE_BOOTS = registerItem("bronze_boots", Item::new,(new Item.Properties()).humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.BOOTS));

    public static final Item BRONZE_SWORD = registerItem("bronze_sword", Item::new,(new Item.Properties()).sword(MapleToolMaterials.BRONZE, 3.0F, -2.4F));
    public static final Item BRONZE_SHOVEL = registerItem("bronze_shovel", (settings) -> new ShovelItem(MapleToolMaterials.BRONZE, 1.5F, -3.0F, settings), new Item.Properties());
    public static final Item BRONZE_PICKAXE = registerItem("bronze_pickaxe", (settings) ->new Item(settings.pickaxe(MapleToolMaterials.BRONZE, 1.0F, -2.8F)), new Item.Properties());
    public static final Item BRONZE_AXE = registerItem("bronze_axe", (settings) -> new AxeItem(MapleToolMaterials.BRONZE, 6.0F, -3.1F, settings), new Item.Properties());
    public static final Item BRONZE_HOE = registerItem("bronze_hoe", (settings) -> new HoeItem(MapleToolMaterials.BRONZE, -2.0F, -1.0F, settings), new Item.Properties());

    public static final Item Rubber_SIGN = registerItem("rubber_sign",
            (settings)-> new SignItem(MapleSignBlocks.Rubber_SIGN, MapleSignBlocks.Rubber_WALL_SIGN, settings), new Item.Properties().stacksTo(16));

    public static final Item Rubber_HANGING_SIGN = registerItem("rubber_hanging_sign", (settings)-> new HangingSignItem(
            MapleSignBlocks.Rubber_HANGING_SIGN, MapleSignBlocks.Rubber_WALL_HANGING_SIGN, settings), new Item.Properties().stacksTo(16));

    public static final Item ROLLING = registerItem("tool_rolling", RollingCuttingToolItem::new, new Item.Properties());
    public static final Item CUTTING = registerItem("tool_cutting", RollingCuttingToolItem::new, new Item.Properties());

    //Armor
    public static final Item Quantum_HELMET = registerItem("quantum_helmet", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.HELMET, EnergyTier.TIER5), new Item.Properties());
    public static final Item Quantum_CHESTPLATE = registerItem("quantum_chestplate", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.CHESTPLATE, EnergyTier.TIER5), new Item.Properties());
    public static final Item Quantum_LEGGINGS = registerItem("quantum_leggings", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.LEGGINGS, EnergyTier.TIER5), new Item.Properties());
    public static final Item Quantum_BOOTS = registerItem("quantum_boots", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.BOOTS, EnergyTier.TIER5), new Item.Properties());

    public static final Item Electric_Jetpack = registerItem("electric_jetpack", (settings)-> new ElectricJetpackItem(settings, MapleArmorMaterials.ELECTRIC_JETPACK, ArmorType.CHESTPLATE, EnergyTier.TIER3), new Item.Properties());

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name))));
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)), item);
    }

    public static void registerMapleArmorItems() {
        IndustrialElixir.LOGGER.info("Registering Maple armor items for " + IndustrialElixir.MOD_ID);
    }
}
