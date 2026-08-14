package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import java.util.function.Function;
import java.util.function.Supplier;

import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AdvancedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    public static final Supplier<Item> IRRADIANT_SACRED_INGOT = registerItem("irradiant_sacred", Item::new, new Item.Properties());
    public static final Supplier<Item> IRRADIANT_GLASS_PANE = registerItem("irradiant_glass_pane", Item::new, new Item.Properties());
    public static final Supplier<Item> LUMINITE = registerItem("luminite", Item::new, new Item.Properties());
    public static final Supplier<Item> ENRICHED_LUMINITE = registerItem("enriched_luminite", Item::new, new Item.Properties());
    public static final Supplier<Item> LUMINITE_ALLOY = registerItem("luminite_alloy", Item::new, new Item.Properties());
    public static final Supplier<Item> ENRICHED_LUMINITE_ALLOY = registerItem("enriched_luminite_alloy", Item::new, new Item.Properties());
    public static final Supplier<Item> IRIDIUM_AMETHYST_PLATE = registerItem("iridium_amethyst_plate", Item::new, new Item.Properties());
    public static final Supplier<Item> REINFORCED_IRIDIUM_AMETHYST_PLATE = registerItem("reinforced_iridium_amethyst_plate", Item::new, new Item.Properties());
    public static final Supplier<Item> IRRADIANT_REINFORCED_PLATE = registerItem("irradiant_reinforced_plate", Item::new, new Item.Properties());
    public static final Supplier<Item> LUMINITE_Part = registerItem("luminite_part", Item::new, new Item.Properties());
    public static final Supplier<Item> Iridium_INGOT = registerItem("iridium_ingot", Item::new, new Item.Properties());
    public static final Supplier<Item> Quantum_Core = registerItem("quantum_core", Item::new, new Item.Properties());
    public static final Supplier<Item> MT_Core = registerItem("mt_core", Item::new, new Item.Properties());

    public static final Supplier<Item> PIPE_PLUG = registerItem("pipe_plug", Item::new, new Item.Properties());

    public static <B extends Item> Supplier<Item> register(String name, Function<Item.Properties, ? extends B> func, Item.Properties props) {
        return ITEMS.register(name, () -> {
            return (Item)func.apply(props.setId(ResourceKey.create(Registries.ITEM, Helper.id(name))));
        });
    }

    private static <T extends Item> Supplier<Item> registerItem(String name, Function<Item.Properties, ? extends T> item, Item.Properties properties) {
        Supplier<Item> toReturn = register(name, item, properties.setId(ResourceKey.create(Registries.ITEM, Helper.id(name))));
        return toReturn;
    }

    public static void registerAdvancedItem(IEventBus eventBus){
        IndustrialElixir.LOGGER.debug("register Advanced Item.");
        ITEMS.register(eventBus);
    }
}
