package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.item.init.QuantumSuitItem;
import com.skniro.industrial_elixir.item.init.ElectricJetpackItem;
import com.skniro.industrial_elixir.item.init.RollingCuttingToolItem;
import com.skniro.industrial_elixir.item.init.equipment.MapleArmorMaterials;
import com.skniro.industrial_elixir.item.init.tool.MapleToolMaterials;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class MapleArmorItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    //Durability multipliers for the custom armour sets (equivalent to the values 26.1 stored on ArmorMaterial).
    private static final int QUANTUM_DURABILITY = 37;
    private static final int BRONZE_DURABILITY = 15;
    private static final int ELECTRIC_JETPACK_DURABILITY = 12;

    //Tool
    public static final Supplier<Item> BRONZE_HELMET = registerItem("bronze_helmet", (settings) -> new ArmorItem(MapleArmorMaterials.BRONZE, ArmorItem.Type.HELMET, settings), new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(BRONZE_DURABILITY)));
    public static final Supplier<Item> BRONZE_CHESTPLATE = registerItem("bronze_chestplate", (settings) -> new ArmorItem(MapleArmorMaterials.BRONZE, ArmorItem.Type.CHESTPLATE, settings), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(BRONZE_DURABILITY)));
    public static final Supplier<Item> BRONZE_LEGGINGS = registerItem("bronze_leggings", (settings) -> new ArmorItem(MapleArmorMaterials.BRONZE, ArmorItem.Type.LEGGINGS, settings), new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(BRONZE_DURABILITY)));
    public static final Supplier<Item> BRONZE_BOOTS = registerItem("bronze_boots", (settings) -> new ArmorItem(MapleArmorMaterials.BRONZE, ArmorItem.Type.BOOTS, settings), new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(BRONZE_DURABILITY)));

    public static final Supplier<Item> BRONZE_SWORD = registerItem("bronze_sword", (settings) -> new SwordItem(MapleToolMaterials.BRONZE, settings), new Item.Properties());
    public static final Supplier<Item> BRONZE_SHOVEL = registerItem("bronze_shovel", (settings) -> new ShovelItem(MapleToolMaterials.BRONZE, settings), new Item.Properties());
    public static final Supplier<Item> BRONZE_PICKAXE = registerItem("bronze_pickaxe", (settings) -> new PickaxeItem(MapleToolMaterials.BRONZE, settings), new Item.Properties());
    public static final Supplier<Item> BRONZE_AXE = registerItem("bronze_axe", (settings) -> new AxeItem(MapleToolMaterials.BRONZE, settings), new Item.Properties());
    public static final Supplier<Item> BRONZE_HOE = registerItem("bronze_hoe", (settings) -> new HoeItem(MapleToolMaterials.BRONZE, settings), new Item.Properties());

    public static final Supplier<Item> Rubber_SIGN = registerItem("rubber_sign",
            (settings)-> new SignItem(settings, MapleSignBlocks.Rubber_SIGN.get(), MapleSignBlocks.Rubber_WALL_SIGN.get()), new Item.Properties().stacksTo(16));

    public static final Supplier<Item> Rubber_HANGING_SIGN = registerItem("rubber_hanging_sign", (settings)-> new HangingSignItem(
            MapleSignBlocks.Rubber_HANGING_SIGN.get(), MapleSignBlocks.Rubber_WALL_HANGING_SIGN.get(), settings), new Item.Properties().stacksTo(16));

    public static final Supplier<Item> ROLLING = registerItem("tool_rolling", RollingCuttingToolItem::new, new Item.Properties());
    public static final Supplier<Item> CUTTING = registerItem("tool_cutting", RollingCuttingToolItem::new, new Item.Properties());

    //Armor
    public static final Supplier<Item> Quantum_HELMET = registerItem("quantum_helmet", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorItem.Type.HELMET, EnergyTier.TIER5), new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(QUANTUM_DURABILITY)));
    public static final Supplier<Item> Quantum_CHESTPLATE = registerItem("quantum_chestplate", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorItem.Type.CHESTPLATE, EnergyTier.TIER5), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(QUANTUM_DURABILITY)));
    public static final Supplier<Item> Quantum_LEGGINGS = registerItem("quantum_leggings", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorItem.Type.LEGGINGS, EnergyTier.TIER5), new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(QUANTUM_DURABILITY)));
    public static final Supplier<Item> Quantum_BOOTS = registerItem("quantum_boots", (settings)-> new QuantumSuitItem(settings, MapleArmorMaterials.QUANTUM, ArmorItem.Type.BOOTS, EnergyTier.TIER5), new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(QUANTUM_DURABILITY)));

    public static final Supplier<Item> Electric_Jetpack = registerItem("electric_jetpack", (settings)-> new ElectricJetpackItem(settings, MapleArmorMaterials.ELECTRIC_JETPACK, ArmorItem.Type.CHESTPLATE, EnergyTier.TIER3), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(ELECTRIC_JETPACK_DURABILITY)));

    public static <B extends Item> Supplier<Item> register(String name, Function<Item.Properties, ? extends B> func, Item.Properties props) {
        return ITEMS.register(name, () -> (Item) func.apply(props));
    }

    private static <T extends Item> Supplier<Item> registerItem(String name, Function<Item.Properties, ? extends T> item, Item.Properties properties) {
        return register(name, item, properties);
    }

    public static void registerMapleArmorItems(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering Maple armor items for " + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
    }
}
