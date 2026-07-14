package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class AdvancedItems {
    public static final Item IRRADIANT_SEPTRIN_INGOT = registerItem("irradiant_septrin", Item::new, new Item.Properties());
    public static final Item IRRADIANT_GLASS_PANE = registerItem("irradiant_glass_pane", Item::new, new Item.Properties());
    public static final Item LUMINITE = registerItem("luminite", Item::new, new Item.Properties());
    public static final Item ENRICHED_LUMINITE = registerItem("enriched_luminite", Item::new, new Item.Properties());
    public static final Item LUMINITE_ALLOY = registerItem("luminite_alloy", Item::new, new Item.Properties());
    public static final Item ENRICHED_LUMINITE_ALLOY = registerItem("enriched_luminite_alloy", Item::new, new Item.Properties());
    public static final Item IRIDIUM_AMETHYST_PLATE = registerItem("iridium_amethyst_plate", Item::new, new Item.Properties());
    public static final Item REINFORCED_IRIDIUM_AMETHYST_PLATE = registerItem("reinforced_iridium_amethyst_plate", Item::new, new Item.Properties());
    public static final Item IRRADIANT_REINFORCED_PLATE = registerItem("irradiant_reinforced_plate", Item::new, new Item.Properties());
    public static final Item LUMINITE_Part = registerItem("luminite_part", Item::new, new Item.Properties());
    public static final Item Iridium_INGOT = registerItem("iridium_ingot", Item::new, new Item.Properties());
    public static final Item Quantum_Core = registerItem("quantum_core", Item::new, new Item.Properties());
    public static final Item MT_Core = registerItem("mt_core", Item::new, new Item.Properties());

    public static final Item PIPE_PLUG = registerItem("pipe_plug", Item::new, new Item.Properties());

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name))));
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)), item);
    }

    public static void registerAdvancedItem(){
        IndustrialElixir.LOGGER.debug("register Advanced Item.");
    }
}
