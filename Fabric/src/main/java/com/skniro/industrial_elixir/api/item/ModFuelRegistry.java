package com.skniro.industrial_elixir.api.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModFuelRegistry {
    private static final Map<Item, Integer> MOD_FUEL = new HashMap<>();
    private static final Map<TagKey<Item>, Integer> MOD_FUEL_TAGS = new HashMap<>();

    public static void registerFuel(Item item, int burnTime) {
        MOD_FUEL.put(item, burnTime);
    }

    public static void registerFuel(TagKey<Item> tag, int burnTime) {
        MOD_FUEL_TAGS.put(tag, burnTime);
    }

    public static Map<Item, Integer> getModFuel() {
        return Collections.unmodifiableMap(MOD_FUEL);
    }

    public static Map<TagKey<Item>, Integer> getModFuelTags() {
        return Collections.unmodifiableMap(MOD_FUEL_TAGS);
    }
}
