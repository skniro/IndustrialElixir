package com.skniro.industrial_elixir.registry.tag;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> BATTERY = of("battery");
    public static final TagKey<Item> Upgrade = of("upgrade");
    public static final TagKey<Item> SACRED_ORES = of("sacred_ores");
    public static final TagKey<Item> TIN_ORES = of("tin_ores");
    public static final TagKey<Item> LEAD_ORES = of("lead_ores");
    public static final TagKey<Item> DUST_TIN = of("dust_tin");
    public static final TagKey<Item> DUST_COPPER = of("dust_copper");
    public static final TagKey<Item> REPAIRS_BRONZE_ARMOR = of("repairs_bronze_armor");
    public static final TagKey<Item> BRONZE_TOOL_MATERIALS = of("bronze_tool_materials");
    public static final TagKey<Item> Diamond = of("diamond");

    private ModItemTags() {
    }

    private static TagKey<Item> of(String id) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, id));
    }
}