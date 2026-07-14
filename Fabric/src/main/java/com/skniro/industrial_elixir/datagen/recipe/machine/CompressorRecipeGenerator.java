package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class CompressorRecipeGenerator extends FabricRecipeProvider {
    public CompressorRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                createCompressor(Items.IRON_BLOCK).input(Items.IRON_INGOT, 9)
                        .unlockedBy("has_base_item", has(Items.IRON_INGOT)).save(output);
                createCompressor(GrowableOresItems.IRON_PLATE).input(GrowableOresItems.IRON_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.IRON_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_IRON_PLATE).input(GrowableOresItems.IRON_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.IRON_PLATE)).save(output);
                createCompressor(GrowableOresItems.IRON_DUST).input(GrowableOresItems.SMALL_IRON_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_IRON_DUST)).save(output);

                createCompressor(GeneralBlocks.Tin_Block).input(GrowableOresItems.TIN_INGOT, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.TIN_INGOT)).save(output);
                createCompressor(GrowableOresItems.TIN_PLATE).input(GrowableOresItems.TIN_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.TIN_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_TIN_PLATE).input(GrowableOresItems.TIN_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.TIN_PLATE)).save(output);
                createCompressor(GrowableOresItems.TIN_DUST).input(GrowableOresItems.SMALL_TIN_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_TIN_DUST)).save(output);

                createCompressor(Items.COPPER_BLOCK).input(Items.COPPER_INGOT, 9)
                        .unlockedBy("has_base_item", has(Items.COPPER_INGOT)).save(output);
                createCompressor(GrowableOresItems.COPPER_PLATE).input(GrowableOresItems.COPPER_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.COPPER_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_COPPER_PLATE).input(GrowableOresItems.COPPER_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.COPPER_PLATE)).save(output);
                createCompressor(GrowableOresItems.COPPER_DUST).input(GrowableOresItems.SMALL_COPPER_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_COPPER_DUST)).save(output);

                createCompressor(Items.GOLD_BLOCK).input(Items.GOLD_INGOT, 9)
                        .unlockedBy("has_base_item", has(Items.GOLD_INGOT)).save(output);
                createCompressor(GrowableOresItems.GOLD_PLATE).input(GrowableOresItems.GOLD_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.GOLD_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_GOLD_PLATE).input(GrowableOresItems.GOLD_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.GOLD_PLATE)).save(output);
                createCompressor(GrowableOresItems.GOLD_DUST).input(GrowableOresItems.SMALL_GOLD_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_GOLD_DUST)).save(output);

                createCompressor(GeneralBlocks.Silver_Block).input(GrowableOresItems.SILVER_INGOT, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SILVER_INGOT)).save(output);
                createCompressor(GrowableOresItems.SILVER_DUST).input(GrowableOresItems.SMALL_SILVER_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_SILVER_DUST)).save(output);

                createCompressor(GeneralBlocks.Lead_Block).input(GrowableOresItems.LEAD_INGOT, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LEAD_INGOT)).save(output);
                createCompressor(GrowableOresItems.LEAD_PLATE).input(GrowableOresItems.LEAD_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LEAD_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_LEAD_PLATE).input(GrowableOresItems.LEAD_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LEAD_PLATE)).save(output);
                createCompressor(GrowableOresItems.LEAD_DUST).input(GrowableOresItems.SMALL_LEAD_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_LEAD_DUST)).save(output);

                createCompressor(GeneralBlocks.Bronze_Block).input(GrowableOresItems.BRONZE_INGOT, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.BRONZE_INGOT)).save(output);
                createCompressor(GrowableOresItems.BRONZE_PLATE).input(GrowableOresItems.BRONZE_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.BRONZE_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_BRONZE_PLATE).input(GrowableOresItems.BRONZE_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.BRONZE_PLATE)).save(output);
                createCompressor(GrowableOresItems.BRONZE_DUST).input(GrowableOresItems.SMALL_BRONZE_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_BRONZE_DUST)).save(output);

                createCompressor(GeneralBlocks.Steel_Block).input(GrowableOresItems.STEEL_INGOT, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.STEEL_INGOT)).save(output);
                createCompressor(GrowableOresItems.DENSE_STEEL_PLATE).input(GrowableOresItems.STEEL_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.STEEL_PLATE)).save(output);

                createCompressor(Items.REDSTONE_BLOCK).input(Items.REDSTONE, 9)
                        .unlockedBy("has_base_item", has(Items.REDSTONE)).save(output);

                createCompressor(Items.LAPIS_BLOCK).input(Items.LAPIS_LAZULI, 9)
                        .unlockedBy("has_base_item", has(Items.LAPIS_LAZULI)).save(output);
                createCompressor(GrowableOresItems.LAPIS_PLATE).input(GrowableOresItems.LAPIS_DUST, 1)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LAPIS_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_LAPIS_PLATE).input(GrowableOresItems.LAPIS_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LAPIS_PLATE)).save(output);
                createCompressor(GrowableOresItems.LAPIS_DUST).input(GrowableOresItems.SMALL_LAPIS_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_LAPIS_DUST)).save(output);

                createCompressor(GrowableOresItems.ALLOY_PLATE).input(GrowableOresItems.ALLOY_INGOT, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.ALLOY_INGOT)).save(output);

                createCompressor(GrowableOresItems.ENERGY_CRYSTAL).input(GrowableOresItems.ENERGIUM_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.ENERGIUM_DUST)).save(output);
                createCompressor(GrowableOresItems.COAL_BLOCK).input(GrowableOresItems.COAL_BALL)
                        .unlockedBy("has_base_item", has(GrowableOresItems.COAL_BALL)).save(output);
                createCompressor(GrowableOresItems.CARBON_PLATE).input(GrowableOresItems.CARBON_MESH)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CARBON_MESH)).save(output);


                createCompressor(Items.NETHER_BRICKS).input(Items.NETHER_BRICK, 4)
                        .unlockedBy("has_base_item", has(Items.NETHER_BRICK)).save(output);
                createCompressor(GrowableOresItems.OBSIDIAN_DUST).input(GrowableOresItems.SMALL_OBSIDIAN_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_OBSIDIAN_DUST)).save(output);
                createCompressor(GrowableOresItems.OBSIDIAN_PLATE).input(GrowableOresItems.OBSIDIAN_DUST)
                        .unlockedBy("has_base_item", has(GrowableOresItems.OBSIDIAN_DUST)).save(output);
                createCompressor(GrowableOresItems.DENSE_OBSIDIAN_PLATE).input(GrowableOresItems.OBSIDIAN_PLATE, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.OBSIDIAN_PLATE)).save(output);
                createCompressor(Items.BRICKS).input(Items.BRICK, 4)
                        .unlockedBy("has_base_item", has(Items.NETHER_BRICK)).save(output);
                createCompressor(Items.SANDSTONE).input(Items.SAND, 4)
                        .unlockedBy("has_base_item", has(Items.SAND)).save(output);
                createCompressor(Items.BLAZE_ROD).input(Items.BLAZE_POWDER, 5)
                        .unlockedBy("has_base_item", has(Items.BLAZE_POWDER)).save(output);
                createCompressor(Items.GLOWSTONE).input(Items.GLOWSTONE_DUST, 4)
                        .unlockedBy("has_base_item", has(Items.GLOWSTONE_DUST)).save(output);
                createCompressor(Items.SNOW_BLOCK).input(Items.SNOWBALL, 4)
                        .unlockedBy("has_base_item", has(Items.SNOWBALL)).save(output);
                createCompressor(Items.DIAMOND).input(GrowableOresItems.COAL_CHUNK)
                        .unlockedBy("has_base_item", has(GrowableOresItems.COAL_CHUNK)).save(output);
                createCompressor(Items.ICE).input(Items.SNOW_BLOCK)
                        .unlockedBy("has_base_item", has(Items.SNOW_BLOCK)).save(output);
                createCompressor(Items.PACKED_ICE).input(Items.ICE,2)
                        .unlockedBy("has_base_item", has(Items.ICE)).save(output);
                createCompressor(Items.CLAY).input(Items.CLAY_BALL,4)
                        .unlockedBy("has_base_item", has(Items.CLAY_BALL)).save(output);

                createCompressor(GrowableOresItems.IRIDIUM_ORE).input(GrowableOresItems.IRIDIUM_SHARD, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.IRIDIUM_SHARD)).save(output);
                createCompressor(AdvancedItems.Iridium_INGOT).input(GrowableOresItems.IRIDIUM_ORE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.IRIDIUM_ORE)).save(output);

                createCompressor(GrowableOresItems.LITHIUM_DUST).input(GrowableOresItems.SMALL_LITHIUM_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_LITHIUM_DUST)).save(output);

                createCompressor(GrowableOresItems.SULFUR_DUST).input(GrowableOresItems.SMALL_SULFUR_DUST, 9)
                        .unlockedBy("has_base_item", has(GrowableOresItems.SMALL_SULFUR_DUST)).save(output);

                createCompressor(GrowableOresItems.SACRED_INGOT).input(GrowableOresItems.CRUSHED_SACRED)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_SACRED)).save(output);
                createCompressor(GrowableOresItems.SACRED_INGOT).input(GrowableOresItems.CRUSHED_SACRED)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_SACRED)).save(output,"crushed_sacred_to_sacred_ingot");
                createCompressor(IndustrialElixirFluidItems.AIR_CELL).input(GrowableOresItems.EMPTY_CELL)
                        .unlockedBy("has_base_item", has(GrowableOresItems.EMPTY_CELL)).save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "Compressor";
    }
}
