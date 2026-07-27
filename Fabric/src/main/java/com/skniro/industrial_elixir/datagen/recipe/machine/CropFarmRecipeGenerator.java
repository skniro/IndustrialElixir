package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.growableoresir.block.GrowableICOresBlocks;
import com.skniro.industrial_elixir.api.data.recipe.CraftingDataHelper;
import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CropFarmRecipeGenerator extends CraftingDataHelper {
    public CropFarmRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                // 小麦
                createCropFarm(Items.WHEAT,3)
                        .input(Items.WHEAT_SEEDS)
                        .output2(Items.WHEAT_SEEDS)
                        .processTime(600)
                        .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                        .save(output, "wheat_seeds_to_wheat");


                // 胡萝卜
                createCropFarm(Items.CARROT,3)
                        .input(Items.CARROT)
                        .processTime(600)
                        .unlockedBy("has_carrot", has(Items.CARROT))
                        .save(output, "carrot_to_carrot");


                // 马铃薯
                createCropFarm(Items.POTATO,3)
                        .input(Items.POTATO)
                        .processTime(600)
                        .unlockedBy("has_potato", has(Items.POTATO))
                        .save(output, "potato_to_potato");


                // 甜菜根
                createCropFarm(Items.BEETROOT,3)
                        .input(Items.BEETROOT_SEEDS)
                        .output2(Items.BEETROOT_SEEDS)
                        .processTime(600)
                        .unlockedBy("has_beetroot_seeds", has(Items.BEETROOT_SEEDS))
                        .save(output, "beetroot_seeds_to_beetroot");


                // 甘蔗
                createCropFarm(Items.SUGAR_CANE,2)
                        .input(Items.SUGAR_CANE)
                        .processTime(2400)
                        .unlockedBy("has_sugar_cane", has(Items.SUGAR_CANE))
                        .save(output, "sugar_cane_to_sugar_cane");


                // 仙人掌
                createCropFarm(Items.CACTUS)
                        .input(Items.CACTUS)
                        .processTime(2400)
                        .unlockedBy("has_cactus", has(Items.CACTUS))
                        .save(output, "cactus_to_cactus");


                // 竹子
                createCropFarm(Items.BAMBOO,9)
                        .input(Items.BAMBOO)
                        .processTime(1800)
                        .unlockedBy("has_bamboo", has(Items.BAMBOO))
                        .save(output, "bamboo_to_bamboo");


                // 可可豆
                createCropFarm(Items.COCOA_BEANS,3)
                        .input(Items.COCOA_BEANS)
                        .processTime(800)
                        .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS))
                        .save(output, "cocoa_beans_to_cocoa_beans");


                // 南瓜
                createCropFarm(Items.PUMPKIN)
                        .input(Items.PUMPKIN_SEEDS)
                        .output2(Items.PUMPKIN_SEEDS)
                        .processTime(1200)
                        .unlockedBy("has_pumpkin_seeds", has(Items.PUMPKIN_SEEDS))
                        .save(output, "pumpkin_seeds_to_pumpkin");


                // 西瓜
                createCropFarm(Items.MELON_SEEDS)
                        .input(Items.MELON_SEEDS)
                        .output2(Items.MELON_SLICE, 9)
                        .processTime(1200)
                        .unlockedBy("has_melon_seeds", has(Items.MELON_SEEDS))
                        .save(output, "melon_seeds_to_melon_slice");


                // 下界疣
                createCropFarm(Items.NETHER_WART, 3)
                        .input(Items.NETHER_WART)
                        .processTime(800)
                        .unlockedBy("has_nether_wart", has(Items.NETHER_WART))
                        .save(output, "nether_wart_to_nether_wart");

                createCropFarm(GrowableICOresBlocks.IER_Bronze_Cane)
                        .input(GrowableICOresBlocks.IER_Bronze_Cane)
                        .processTime(2400)
                        .unlockedBy("has_sugar_cane", has(GrowableICOresBlocks.IER_Bronze_Cane))
                        .save(output, "ier_bronze_ore_cane_to_ier_bronze_ore_cane");

                // Steel Ore Cane
                createCropFarm(GrowableICOresBlocks.IER_steel_Cane)
                        .input(GrowableICOresBlocks.IER_steel_Cane)
                        .processTime(3000)
                        .unlockedBy("has_steel_ore_cane", has(GrowableICOresBlocks.IER_steel_Cane))
                        .save(output, "ier_steel_ore_cane_to_ier_steel_ore_cane");


                // Silver Ore Cane
                createCropFarm(GrowableICOresBlocks.IER_silver_Cane)
                        .input(GrowableICOresBlocks.IER_silver_Cane)
                        .processTime(2400)
                        .unlockedBy("has_silver_ore_cane", has(GrowableICOresBlocks.IER_silver_Cane))
                        .save(output, "ier_silver_ore_cane_to_ier_silver_ore_cane");


                // Tin Ore Cane
                createCropFarm(GrowableICOresBlocks.IER_Tin_Cane)
                        .input(GrowableICOresBlocks.IER_Tin_Cane)
                        .processTime(2400)
                        .unlockedBy("has_tin_ore_cane", has(GrowableICOresBlocks.IER_Tin_Cane))
                        .save(output, "ier_tin_ore_cane_to_ier_tin_ore_cane");


                // Sacred Ore Cane
                createCropFarm(GrowableICOresBlocks.IER_SACRED_Cane)
                        .input(GrowableICOresBlocks.IER_SACRED_Cane)
                        .processTime(2400)
                        .unlockedBy("has_sacred_ore_cane", has(GrowableICOresBlocks.IER_SACRED_Cane))
                        .save(output, "ier_sacred_ore_cane_to_ier_sacred_ore_cane");


                // Lead Ore Cane
                createCropFarm(GrowableICOresBlocks.IER_LEAD_Cane)
                        .input(GrowableICOresBlocks.IER_LEAD_Cane)
                        .processTime(2400)
                        .unlockedBy("has_lead_ore_cane", has(GrowableICOresBlocks.IER_LEAD_Cane))
                        .save(output, "ier_lead_ore_cane_to_ier_lead_ore_cane");


               // 橡树
                createCropFarm(Items.OAK_SAPLING)
                        .input(Items.OAK_SAPLING)
                        .output2(Items.OAK_LOG, 5)
                        .output3(Items.APPLE, 1)
                        .processTime(1600)
                        .unlockedBy("has_oak_sapling", has(Items.OAK_SAPLING))
                        .save(output, "oak_sapling_to_oak_log");

                //苍白橡树
                createCropFarm(Items.PALE_OAK_SAPLING)
                        .input(Items.PALE_OAK_SAPLING)
                        .output2(Items.PALE_OAK_LOG, 5)
                        .output3(Items.RESIN_CLUMP, 1)
                        .processTime(1600)
                        .unlockedBy("has_pale_oak_sapling", has(Items.PALE_OAK_SAPLING))
                        .save(output, "pale_oak_sapling_to_oak_log");


               // 云杉
                createCropFarm(Items.SPRUCE_SAPLING)
                        .input(Items.SPRUCE_SAPLING)
                        .output2(Items.SPRUCE_LOG, 6)
                        .processTime(1600)
                        .unlockedBy("has_spruce_sapling", has(Items.SPRUCE_SAPLING))
                        .save(output, "spruce_sapling_to_spruce_log");


               // 白桦
                createCropFarm(Items.BIRCH_SAPLING)
                        .input(Items.BIRCH_SAPLING)
                        .output2(Items.BIRCH_LOG, 5)
                        .processTime(1600)
                        .unlockedBy("has_birch_sapling", has(Items.BIRCH_SAPLING))
                        .save(output, "birch_sapling_to_birch_log");


               // 丛林木
                createCropFarm(Items.JUNGLE_SAPLING)
                        .input(Items.JUNGLE_SAPLING)
                        .output2(Items.JUNGLE_LOG, 6)
                        .processTime(1000)
                        .unlockedBy("has_jungle_sapling", has(Items.JUNGLE_SAPLING))
                        .save(output, "jungle_sapling_to_jungle_log");


               // 金合欢
                createCropFarm(Items.ACACIA_SAPLING)
                        .input(Items.ACACIA_SAPLING)
                        .output2(Items.ACACIA_LOG, 5)
                        .processTime(1000)
                        .unlockedBy("has_acacia_sapling", has(Items.ACACIA_SAPLING))
                        .save(output, "acacia_sapling_to_acacia_log");


               // 深色橡木
                createCropFarm(Items.DARK_OAK_SAPLING,2)
                        .input(Items.DARK_OAK_SAPLING)
                        .output2(Items.DARK_OAK_LOG, 10)
                        .processTime(1000)
                        .unlockedBy("has_dark_oak_sapling", has(Items.DARK_OAK_SAPLING))
                        .save(output, "dark_oak_saplings_to_dark_oak_log");


               // 红树
                createCropFarm(Items.MANGROVE_PROPAGULE)
                        .input(Items.MANGROVE_PROPAGULE)
                        .output2(Items.MANGROVE_LOG, 6)
                        .processTime(1000)
                        .unlockedBy("has_mangrove_propagule", has(Items.MANGROVE_PROPAGULE))
                        .save(output, "mangrove_propagule_to_mangrove_log");


               // 樱花
                createCropFarm(Items.CHERRY_SAPLING)
                        .input(Items.CHERRY_SAPLING)
                        .output2(Items.CHERRY_LOG, 6)
                        .processTime(1000)
                        .unlockedBy("has_cherry_sapling", has(Items.CHERRY_SAPLING))
                        .save(output, "cherry_sapling_to_cherry_log");

               // 绯红菌树
                createCropFarm(Items.CRIMSON_FUNGUS)
                        .input(Items.CRIMSON_FUNGUS)
                        .output2(Items.CRIMSON_STEM, 8)
                        .processTime(1000)
                        .unlockedBy("has_crimson_fungus", has(Items.CRIMSON_FUNGUS))
                        .save(output, "crimson_fungus_to_crimson_stem");


               // 诡异菌树
                createCropFarm(Items.WARPED_FUNGUS)
                        .input(Items.WARPED_FUNGUS)
                        .output2(Items.WARPED_STEM, 8)
                        .processTime(1000)
                        .unlockedBy("has_warped_fungus", has(Items.WARPED_FUNGUS))
                        .save(output, "warped_fungus_to_warped_stem");

                // 紫颂果
                createCropFarm(Items.CHORUS_FLOWER)
                        .input(Items.CHORUS_FLOWER)
                        .output2(Items.CHORUS_FRUIT, 4)
                        .processTime(1200)
                        .unlockedBy("has_chorus_flower", has(Items.CHORUS_FLOWER))
                        .save(output, "chorus_flower_to_chorus_fruit");

                // 火把花
                createCropFarm(Items.TORCHFLOWER_SEEDS)
                        .input(Items.TORCHFLOWER_SEEDS)
                        .output2(Items.TORCHFLOWER, 1)
                        .processTime(600)
                        .unlockedBy("has_torchflower_seeds", has(Items.TORCHFLOWER_SEEDS))
                        .save(output, "torchflower_seeds_to_torchflower");


                // 瓶子草
                createCropFarm(Items.PITCHER_POD)
                        .input(Items.PITCHER_POD)
                        .output2(Items.PITCHER_PLANT, 1)
                        .processTime(600)
                        .unlockedBy("has_pitcher_pod", has(Items.PITCHER_POD))
                        .save(output, "pitcher_pod_to_pitcher_plant");

                createCropFarm(GrowableOresItems.Rubber, 3)
                        .input(GeneralBlocks.Rubber_SAPLING)
                        .output2(GeneralBlocks.Rubber_LOG, 6)
                        .output3(GeneralBlocks.Rubber_SAPLING)
                        .processTime(600)
                        .unlockedBy("has_rubber_sapling", has(GeneralBlocks.Rubber_SAPLING))
                        .save(output, "rubber_sapling_to_rubber");
            }
        };
    }

    @Override
    public String getName() {
        return "CropFarm";
    }
}
