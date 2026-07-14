package com.skniro.industrial_elixir.util;

import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModLootTableModifiers {
    private static final Identifier IGLOO_STRUCTURE_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/igloo_chest");
    private static final Identifier Mineshaft_STRUCTURE_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/abandoned_mineshaft");
    private static final Identifier Ancient_City_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/ancient_city");
    private static final Identifier Bastion_Bridge_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/bastion_bridge");
    private static final Identifier Bastion_Hoglin_Stable_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/bastion_hoglin_stable");
    private static final Identifier Bastion_Other_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/bastion_other");
    private static final Identifier Bastion_Treasure_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/bastion_treasure");
    private static final Identifier Buried_Treasure_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/buried_treasure");
    private static final Identifier End_City_Treasure_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/end_city_treasure");
    private static final Identifier Desert_Pyramid_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/desert_pyramid");
    private static final Identifier Jungle_Temple_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/jungle_temple");
    private static final Identifier Jungle_Temple_Dispenser_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/jungle_temple_dispenser");
    private static final Identifier Underwater_Ruin_Small_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/underwater_ruin_small");
    private static final Identifier Underwater_Ruin_Big_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/underwater_ruin_big");
    private static final Identifier Stronghold_Corridor_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/stronghold_corridor");
    private static final Identifier Stronghold_Crossing_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/stronghold_crossing");
    private static final Identifier Pillager_Outpost_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/pillager_outpost");
    private static final Identifier Ruined_Portal_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/ruined_portal");
    private static final Identifier Woodland_Mansion_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/woodland_mansion");
    private static final Identifier Simple_Dungeon_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/simple_dungeon");
    private static final Identifier Nether_Bridge_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/nether_bridge");
    private static final Identifier Shipwreck_Map_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/shipwreck_map");
    private static final Identifier Shipwreck_Supply_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/shipwreck_supply");
    private static final Identifier Shipwreck_Treasure_CHEST_ID
            = Identifier.fromNamespaceAndPath("minecraft", "chests/shipwreck_treasure");

    private static LootPool.Builder bronzeArmorPool(float chance, float minCount, float maxCount) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(MapleArmorItems.BRONZE_HELMET))
                .add(LootItem.lootTableItem(MapleArmorItems.BRONZE_CHESTPLATE))
                .add(LootItem.lootTableItem(MapleArmorItems.BRONZE_LEGGINGS))
                .add(LootItem.lootTableItem(MapleArmorItems.BRONZE_BOOTS))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)).build());
    }

    private static LootPool.Builder powderPool(float chance, float minCount, float maxCount) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(GrowableOresItems.BRONZE_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.CLAY_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.COAL_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.COPPER_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.DIAMOND_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.ENERGIUM_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.GOLD_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.IRON_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.LAPIS_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.LEAD_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.LITHIUM_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.OBSIDIAN_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.SILICON_DIOXIDE_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.SILVER_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.STONE_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.SULFUR_DUST))
                .add(LootItem.lootTableItem(GrowableOresItems.TIN_DUST))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)).build());
    }

    private static LootPool.Builder iridiumOrePool(float chance, float minCount, float maxCount) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(GrowableOresItems.IRIDIUM_ORE))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)).build());
    }

    private static LootPool.Builder iridiumShardPool(float chance, float minCount, float maxCount) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(GrowableOresItems.IRIDIUM_SHARD))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)).build());
    }

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((id, tableBuilder, source, registries) -> {
            // ========== Tier 3: High-value chests ==========
            if(Ancient_City_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.35f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.55f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.10f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumShardPool(0.05f, 1.0f, 1.0f).build());
            }
            if(Bastion_Treasure_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.35f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.55f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.10f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumShardPool(0.05f, 1.0f, 1.0f).build());
            }
            if(End_City_Treasure_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.35f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.55f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.10f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumShardPool(0.05f, 1.0f, 1.0f).build());
            }

            // ========== Tier 2.5: Upper-mid chests ==========
            if(Bastion_Bridge_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.20f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.35f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.06f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.03f, 1.0f, 1.0f).build());
            }
            if(Bastion_Other_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.20f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.35f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.06f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.03f, 1.0f, 1.0f).build());
            }
            if(Woodland_Mansion_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.25f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.35f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.06f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.03f, 1.0f, 1.0f).build());
            }
            if(Nether_Bridge_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.20f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.35f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.06f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.03f, 1.0f, 1.0f).build());
            }

            // ========== Tier 2: Mid chests ==========
            if(Bastion_Hoglin_Stable_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.30f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.05f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Stronghold_Corridor_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.30f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.05f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Stronghold_Crossing_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.30f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.05f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }

            // ========== Tier 1.5: Lower-mid chests ==========
            if(Desert_Pyramid_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.12f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.25f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.04f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Jungle_Temple_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.12f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.25f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.04f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Jungle_Temple_Dispenser_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.12f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.25f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.04f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Pillager_Outpost_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.12f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.25f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.04f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Ruined_Portal_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.12f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.25f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.04f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }
            if(Simple_Dungeon_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.12f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.25f, 1.0f, 2.0f).build());
                tableBuilder.pool(iridiumOrePool(0.04f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.02f, 1.0f, 1.0f).build());
            }

            // ========== Tier 1: Low-tier chests ==========
            if(IGLOO_STRUCTURE_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Mineshaft_STRUCTURE_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Buried_Treasure_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Underwater_Ruin_Small_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Underwater_Ruin_Big_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Shipwreck_Map_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Shipwreck_Supply_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
            if(Shipwreck_Treasure_CHEST_ID.equals(id)) {
                tableBuilder.pool(bronzeArmorPool(0.08f, 1.0f, 1.0f).build());
                tableBuilder.pool(powderPool(0.18f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumOrePool(0.03f, 1.0f, 1.0f).build());
                tableBuilder.pool(iridiumShardPool(0.01f, 1.0f, 1.0f).build());
            }
        });
    }
}