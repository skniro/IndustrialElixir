package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class MapleArmorItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    //Tool
    public static final Supplier<Item> BRONZE_HELMET = registerItem("bronze_helmet", (settings) -> new Item(settings.humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.HELMET)),new Item.Properties());
    public static final Supplier<Item> BRONZE_CHESTPLATE = registerItem("bronze_chestplate", (settings) -> new Item(settings.humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.CHESTPLATE)),new Item.Properties());
    public static final Supplier<Item> BRONZE_LEGGINGS = registerItem("bronze_leggings", (settings) -> new Item(settings.humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.LEGGINGS)),new Item.Properties());
    public static final Supplier<Item> BRONZE_BOOTS = registerItem("bronze_boots", (settings) -> new Item(settings.humanoidArmor(MapleArmorMaterials.BRONZE, ArmorType.BOOTS)),new Item.Properties());

    public static final Supplier<Item> BRONZE_SWORD = registerItem("bronze_sword", (settings) -> new Item(settings.sword(MapleToolMaterials.BRONZE, 3.0F, -2.4F)),new Item.Properties());
    public static final Supplier<Item> BRONZE_SHOVEL = registerItem("bronze_shovel", (settings) -> new ShovelItem(MapleToolMaterials.BRONZE, 1.5F, -3.0F, settings), new Item.Properties());
    public static final Supplier<Item> BRONZE_PICKAXE = registerItem("bronze_pickaxe", (settings) ->new Item(settings.pickaxe(MapleToolMaterials.BRONZE, 1.0F, -2.8F)), new Item.Properties());
    public static final Supplier<Item> BRONZE_AXE = registerItem("bronze_axe", (settings) -> new AxeItem(MapleToolMaterials.BRONZE, 6.0F, -3.1F, settings), new Item.Properties());
    public static final Supplier<Item> BRONZE_HOE = registerItem("bronze_hoe", (settings) -> new HoeItem(MapleToolMaterials.BRONZE, -2.0F, -1.0F, settings), new Item.Properties());

    public static final Supplier<Item> Rubber_SIGN = registerItem("rubber_sign",
            (settings)-> new SignItem(MapleSignBlocks.Rubber_SIGN.get(), MapleSignBlocks.Rubber_WALL_SIGN.get(), settings), new Item.Properties().stacksTo(16));

    public static final Supplier<Item> Rubber_HANGING_SIGN = registerItem("rubber_hanging_sign", (settings)-> new HangingSignItem(
            MapleSignBlocks.Rubber_HANGING_SIGN.get(), MapleSignBlocks.Rubber_WALL_HANGING_SIGN.get(), settings), new Item.Properties().stacksTo(16));

    public static final Supplier<Item> ROLLING = registerItem("tool_rolling", RollingCuttingToolItem::new, new Item.Properties());
    public static final Supplier<Item> CUTTING = registerItem("tool_cutting", RollingCuttingToolItem::new, new Item.Properties());

    //Armor
    public static final Supplier<Item> Quantum_HELMET = registerItem("quantum_helmet", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.HELMET, EnergyTier.TIER5), new Item.Properties());
    public static final Supplier<Item> Quantum_CHESTPLATE = registerItem("quantum_chestplate", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.CHESTPLATE, EnergyTier.TIER5), new Item.Properties());
    public static final Supplier<Item> Quantum_LEGGINGS = registerItem("quantum_leggings", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.LEGGINGS, EnergyTier.TIER5), new Item.Properties());
    public static final Supplier<Item> Quantum_BOOTS = registerItem("quantum_boots", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorType.BOOTS, EnergyTier.TIER5), new Item.Properties());

    public static final Supplier<Item> Electric_Jetpack = registerItem("electric_jetpack", (settings)-> new ElectricJetpackItem(settings, MapleArmorMaterials.ELECTRIC_JETPACK, ArmorType.CHESTPLATE, EnergyTier.TIER3), new Item.Properties());

    public static <B extends Item> Supplier<Item> register(String name, Function<Item.Properties, ? extends B> func, Item.Properties props) {
        return ITEMS.register(name, () -> {
            return (Item)func.apply(props.setId(ResourceKey.create(Registries.ITEM, Helper.id(name))));
        });
    }

    private static <T extends Item> Supplier<Item> registerItem(String name, Function<Item.Properties, ? extends T> item, Item.Properties properties) {
        Supplier<Item> toReturn = register(name, item, properties.setId(ResourceKey.create(Registries.ITEM, Helper.id(name))));
        return toReturn;
    }

    public static void registerMapleArmorItems(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering Maple armor items for " + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
    }
}
