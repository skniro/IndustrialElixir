package com.skniro.growableoresir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class GrowableOresItems {
    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name), item);
    }

    public static void shield_item(){
        IndustrialElixir.LOGGER.debug("register shield item.");
    }
}
