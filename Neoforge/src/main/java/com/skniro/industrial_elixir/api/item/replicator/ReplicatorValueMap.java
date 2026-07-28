package com.skniro.industrial_elixir.api.item.replicator;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for per-item energy and UU matter costs used by the Pattern Storage scanner
 * and Replicator. Each entry maps an Item to its replication cost.
 * <p>
 * Call {@link #register(Item, int, long)} to add or override cost values.
 * The map is queried by {@link com.skniro.industrial_elixir.block.entity.machine.PatternStorageBlockEntity}
 * when scanning items.
 */
public class ReplicatorValueMap {
    private static final Map<Item, ReplicatorCost> COSTS = new HashMap<>();

    /**
     * Register a custom replication cost for an item.
     *
     * @param item       the item to register
     * @param uuCost     UU matter cost in mB per replication
     * @param energyCost energy cost in EU per replication
     */
    public static void register(Item item, int uuCost, long energyCost) {
        COSTS.put(item, new ReplicatorCost(uuCost, energyCost));
    }

    /**
     * Get the registered replication cost for an item, or null if not registered.
     */
    public static ReplicatorCost get(Item item) {
        return COSTS.get(item);
    }

    /**
     * Check if an item has a registered replication cost.
     */
    public static boolean contains(Item item) {
        return COSTS.containsKey(item);
    }

    /**
     * Get an unmodifiable view of all registered costs.
     */
    public static Map<Item, ReplicatorCost> getAll() {
        return Collections.unmodifiableMap(COSTS);
    }

    /**
     * Register default vanilla item costs.
     * Called during mod initialization.
     */
    public static void registerDefaults() {
        // --- Common resources ---
        register(Items.DIRT, 0,7000);
        register(Items.COBBLESTONE, 0, 500);
        register(Items.STONE, 0, 6000);
        register(Items.SAND, 0, 7000);
        register(Items.GRAVEL, 0, 15000);
        register(Items.GRASS_BLOCK, 26, 0);
        register(Items.GLASS, 0, 10000);
        // --- Wood ---
        register(Items.OAK_PLANKS, 12, 0);
        register(Items.SPRUCE_PLANKS, 12, 0);
        register(Items.BIRCH_PLANKS, 12, 0);
        register(Items.JUNGLE_PLANKS, 12, 0);
        register(Items.ACACIA_PLANKS, 12, 0);
        register(Items.DARK_OAK_PLANKS, 12, 0);
        register(Items.MANGROVE_PLANKS, 12, 0);
        register(Items.CHERRY_PLANKS, 12, 0);
        register(Items.BAMBOO_PLANKS, 12, 0);
        register(Items.CRIMSON_PLANKS, 12, 0);
        register(Items.WARPED_PLANKS, 12, 0);
        register(Items.PALE_OAK_PLANKS, 12, 0);
        register(Items.SPRUCE_LOG, 44, 0);
        register(Items.BIRCH_LOG, 44, 0);
        register(Items.JUNGLE_LOG, 44, 0);
        register(Items.ACACIA_LOG, 44, 0);
        register(Items.DARK_OAK_LOG, 44, 0);
        register(Items.MANGROVE_LOG, 44, 0);
        register(Items.CHERRY_LOG, 44, 0);
        register(Items.CRIMSON_STEM, 44, 0);
        register(Items.WARPED_STEM, 44, 0);
        register(Items.OAK_SAPLING, 80, 0);
        register(Items.SPRUCE_SAPLING, 80, 0);
        register(Items.BIRCH_SAPLING, 80, 0);
        register(Items.JUNGLE_SAPLING, 80, 0);
        register(Items.ACACIA_SAPLING, 80, 0);
        register(Items.DARK_OAK_SAPLING, 80, 0);
        register(Items.MANGROVE_PROPAGULE, 80, 0);
        register(Items.CHERRY_SAPLING, 80, 0);
        register(Items.PALE_OAK_SAPLING, 80, 0);
        register(Items.CRIMSON_FUNGUS, 80, 0);
        register(Items.WARPED_FUNGUS, 80, 0);

        register(Items.DANDELION, 370, 0);
        register(Items.POPPY, 370, 0);
        register(Items.BLUE_ORCHID, 370, 0);
        register(Items.ALLIUM, 370, 0);
        register(Items.AZURE_BLUET, 370, 0);
        register(Items.RED_TULIP, 370, 0);
        register(Items.ORANGE_TULIP, 370, 0);
        register(Items.WHITE_TULIP, 370, 0);
        register(Items.PINK_TULIP, 370, 0);
        register(Items.OXEYE_DAISY, 370, 0);
        register(Items.CORNFLOWER, 370, 0);
        register(Items.LILY_OF_THE_VALLEY, 370, 0);
        register(Items.WITHER_ROSE, 370, 0);
        register(Items.TORCHFLOWER, 370, 0);
        register(Items.OPEN_EYEBLOSSOM, 370, 0);
        register(Items.CLOSED_EYEBLOSSOM, 370, 0);

        register(Items.SUNFLOWER, 370, 0);
        register(Items.LILAC, 370, 0);
        register(Items.ROSE_BUSH, 370, 0);
        register(Items.PEONY, 370, 0);

        register(Items.PINK_PETALS, 370, 0);
        register(Items.WILDFLOWERS, 370, 0);

        // --- Ores ---
        register(Items.COAL, 0, 50000);
        register(Items.IRON_ORE, 1, 0);
        register(Items.GOLD_ORE, 10, 0);
        register(Items.COPPER_ORE, 5, 0);
        register(Items.DIAMOND, 24, 0);
        register(Items.DIAMOND_BLOCK, 216, 0);
        register(Items.EMERALD, 40, 0);
        register(Items.LAPIS_LAZULI, 8, 0);
        register(Items.LAPIS_BLOCK, 68, 0);
        register(Items.REDSTONE, 2, 0);
        register(Items.REDSTONE_BLOCK, 11, 0);
        register(Items.QUARTZ, 2, 0);
        register(Items.NETHERITE_INGOT, 400, 0);
        register(Items.NETHERITE_SCRAP, 150, 0);
        register(Items.ANCIENT_DEBRIS, 100, 0);
        register(Items.COAL_BLOCK, 0, 400000);

        register(Items.IRON_INGOT, 2, 0);
        register(Items.IRON_NUGGET, 0, 0);
        register(Items.IRON_BLOCK, 18, 0);
        register(Items.RAW_IRON, 1, 0);
        register(Items.RAW_IRON_BLOCK, 9, 0);
        register(Items.IRON_ORE, 1, 0);
        register(Items.DEEPSLATE_IRON_ORE, 2, 0);

        register(Items.GOLD_INGOT, 20, 0);
        register(Items.GOLD_NUGGET, 2, 0);
        register(Items.GOLD_BLOCK, 180, 0);
        register(Items.RAW_GOLD, 10, 0);
        register(Items.RAW_GOLD_BLOCK, 90, 0);
        register(Items.DEEPSLATE_GOLD_ORE, 20, 0);
        register(Items.NETHER_GOLD_ORE, 5, 0);

        register(Items.COPPER_INGOT, 10, 0);
        register(Items.COPPER_BLOCK, 90, 0);
        register(Items.RAW_COPPER, 5, 0);
        register(Items.RAW_COPPER_BLOCK, 45, 0);
        register(Items.DEEPSLATE_COPPER_ORE, 10, 0);

        register(Items.DEEPSLATE_COAL_ORE, 0, 80000);

        register(Items.DEEPSLATE_DIAMOND_ORE, 48, 0);

        register(Items.EMERALD_BLOCK, 360, 0);
        register(Items.DEEPSLATE_EMERALD_ORE, 80, 0);

        register(Items.DEEPSLATE_LAPIS_ORE, 136, 0);

        register(Items.DEEPSLATE_REDSTONE_ORE, 4, 0);

        register(Items.QUARTZ_BLOCK, 8, 0);
        register(Items.SMOOTH_QUARTZ, 8, 0);
        register(Items.SMOOTH_QUARTZ_STAIRS, 6, 0);
        register(Items.SMOOTH_QUARTZ_SLAB, 4, 0);
        register(Items.CHISELED_QUARTZ_BLOCK, 8, 0);
        register(Items.QUARTZ_BRICKS, 8, 0);
        register(Items.QUARTZ_PILLAR, 8, 0);

        register(Items.AMETHYST_SHARD, 32, 0);
        register(Items.AMETHYST_BLOCK, 128, 0);
        register(Items.BUDDING_AMETHYST, 0, 5000000);
        register(Items.SMALL_AMETHYST_BUD, 40, 0);
        register(Items.MEDIUM_AMETHYST_BUD, 64, 0);
        register(Items.LARGE_AMETHYST_BUD, 96, 0);
        register(Items.AMETHYST_CLUSTER, 128, 0);

        register(Items.PRISMARINE_CRYSTALS, 24, 0);
        register(Items.PRISMARINE_SHARD, 12, 0);

        register(Items.ECHO_SHARD, 0, 3000000);

        register(Items.RESIN_CLUMP, 8, 0);
        register(Items.RESIN_BRICK, 8, 0);

        // --- Food ---
        register(Items.APPLE, 10, 0);
        register(Items.BREAD, 20, 0);
        register(Items.COOKED_BEEF, 40, 0);
        register(Items.GOLDEN_APPLE, 180, 0);
        register(Items.ENCHANTED_GOLDEN_APPLE, 288, 0);

        // 生肉
        register(Items.BEEF, 20, 0);
        register(Items.PORKCHOP, 16, 0);
        register(Items.CHICKEN, 12, 0);
        register(Items.MUTTON, 14, 0);
        register(Items.RABBIT, 12, 0);
        register(Items.COD, 12, 0);
        register(Items.SALMON, 14, 0);
        register(Items.TROPICAL_FISH, 16, 0);
        register(Items.PUFFERFISH, 20, 0);

// 熟肉
        register(Items.COOKED_PORKCHOP, 32, 0);
        register(Items.COOKED_CHICKEN, 24, 0);
        register(Items.COOKED_MUTTON, 28, 0);
        register(Items.COOKED_RABBIT, 24, 0);
        register(Items.COOKED_COD, 24, 0);
        register(Items.COOKED_SALMON, 28, 0);

// 蔬菜水果
        register(Items.POTATO, 8, 0);
        register(Items.BAKED_POTATO, 18, 0);
        register(Items.POISONOUS_POTATO, 4, 0);
        register(Items.CARROT, 8, 0);
        register(Items.GOLDEN_CARROT, 120, 0);
        register(Items.BEETROOT, 6, 0);
        register(Items.MELON_SLICE, 4, 0);
        register(Items.SWEET_BERRIES, 6, 0);
        register(Items.GLOW_BERRIES, 8, 0);
        register(Items.CHORUS_FRUIT, 40, 0);

// 作物
        register(Items.WHEAT, 8, 0);
        register(Items.DRIED_KELP, 4, 0);
        register(Items.KELP, 2, 0);

// 汤和炖菜
        register(Items.MUSHROOM_STEW, 40, 0);
        register(Items.BEETROOT_SOUP, 36, 0);
        register(Items.RABBIT_STEW, 80, 0);
        register(Items.SUSPICIOUS_STEW, 120, 0);

// 点心
        register(Items.COOKIE, 8, 0);
        register(Items.CAKE, 120, 0);
        register(Items.PUMPKIN_PIE, 48, 0);

// 蜂蜜
        register(Items.HONEY_BOTTLE, 24, 0);
        register(Items.HONEYCOMB, 16, 0);

// 其他
        register(Items.ROTTEN_FLESH, 2, 0);
        register(Items.SPIDER_EYE, 8, 0);

// 种子（可食用）
        register(Items.MELON_SEEDS, 2, 0);
        register(Items.PUMPKIN_SEEDS, 2, 0);

        register(GrowableOresItems.IRIDIUM_SHARD.get(), 13, 0);
        register(GrowableOresItems.IRIDIUM_ORE.get(), 120, 0);
        register(GrowableOresItems.IRIDIUM_PLATE.get(), 520, 0);

        register(GrowableOresItems.IMPURE_SACRED_STONE.get(), 31, 0);

        register(Items.SLIME_BALL, 133, 0);


        register(Item.byBlock(GrowableOresBlocks.MFSU.get()), 11630, 0);;

        register(Items.PAPER, 3074, 0);

        register(GrowableOresItems.Scrap.get(), 1, 0);;

        register(GrowableOresItems.Sticky_Resin.get(), 6331, 0);
        register(GrowableOresItems.Rubber.get(), 101, 0);
        register(Item.byBlock(GeneralBlocks.Rubber_SAPLING.get()), 3572, 0);

        register(GrowableOresItems.Raw_SACRED.get(), 16, 0);

        register(GrowableOresItems.SACRED_ESSENCE.get(), 98, 0);


        // --- Obsidian / Bedrock ---
        register(Items.OBSIDIAN, 64, 0);
        register(Items.CRYING_OBSIDIAN, 96, 0);
        register(Items.RESPAWN_ANCHOR, 768, 0);
        register(Items.END_CRYSTAL, 256, 0);
        register(Items.ENDER_CHEST, 320, 0);

        // --- Nether ---
        register(Items.BLAZE_ROD, 64, 300000);
        register(Items.BLAZE_POWDER, 32, 0);

        register(Items.GHAST_TEAR, 256, 1200000);

        register(Items.NETHER_WART, 24, 0);

        register(Items.MAGMA_CREAM, 534, 0);
        register(Items.MAGMA_BLOCK, 384, 0);

        register(Items.GLOWSTONE_DUST, 39, 0);
        register(Items.GLOWSTONE, 159, 0);

        register(Items.SHROOMLIGHT, 64, 0);

        register(Items.SOUL_SAND, 12, 0);
        register(Items.SOUL_SOIL, 8, 0);

        register(Items.BLACKSTONE, 4, 0);
        register(Items.GILDED_BLACKSTONE, 32, 500000);

        register(Items.CRIMSON_ROOTS, 8, 0);
        register(Items.WARPED_ROOTS, 8, 0);

        register(Items.NETHER_SPROUTS, 4, 0);
        register(Items.WEEPING_VINES, 8, 0);
        register(Items.TWISTING_VINES, 8, 0);

        // --- The End ---
        register(Items.ENDER_PEARL, 64, 300000);
        register(Items.ENDER_EYE, 96, 0);

        register(Items.END_STONE, 8, 0);
        register(Items.END_STONE_BRICKS, 32, 0);

        register(Items.POPPED_CHORUS_FRUIT, 32, 0);

        register(Items.CHORUS_FLOWER, 48, 0);
        register(Items.CHORUS_PLANT, 24, 0);

        register(Items.PURPUR_BLOCK, 128, 0);
        register(Items.PURPUR_PILLAR, 128, 0);
        register(Items.PURPUR_STAIRS, 96, 0);
        register(Items.PURPUR_SLAB, 64, 0);

        register(Items.SHULKER_SHELL, 256, 1200000);
        register(Items.SHULKER_BOX, 576, 0);

        register(Items.DRAGON_BREATH, 50, 800000);

        register(Items.ELYTRA, 30, 8000000);

        register(Items.DRAGON_HEAD, 200, 15000000);
        register(Items.DRAGON_EGG, 1000, 50000000);

        register(Items.END_ROD, 80, 0);
    }
}
