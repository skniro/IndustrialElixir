package com.skniro.industrial_elixir.datagen.recipe.base;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ArmorRecipeGenerator extends FabricRecipeProvider {
    public ArmorRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.TOOLS, MapleArmorItems.BRONZE_PICKAXE)
                        .pattern("###")
                        .pattern(" I ")
                        .pattern(" I ")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .define('I', Items.STICK)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.BRONZE_SWORD)
                        .pattern("#")
                        .pattern("#")
                        .pattern("|")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .define('|', Items.STICK)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.TOOLS, MapleArmorItems.BRONZE_AXE)
                        .pattern("##")
                        .pattern("#|")
                        .pattern(" |")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .define('|', Items.STICK)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.TOOLS, MapleArmorItems.BRONZE_SHOVEL)
                        .pattern("#")
                        .pattern("|")
                        .pattern("|")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .define('|', Items.STICK)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.TOOLS, MapleArmorItems.BRONZE_HOE)
                        .pattern("##")
                        .pattern(" |")
                        .pattern(" |")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .define('|', Items.STICK)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.BRONZE_HELMET)
                        .pattern("###")
                        .pattern("# #")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.BRONZE_CHESTPLATE)
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.BRONZE_LEGGINGS)
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.BRONZE_BOOTS)
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', GrowableOresItems.BRONZE_INGOT)
                        .unlockedBy(getHasName(GrowableOresItems.BRONZE_INGOT),
                                has(GrowableOresItems.BRONZE_INGOT))
                        .save(output);

                SimpleCookingRecipeBuilder.smelting(
                                Ingredient.of(
                                        MapleArmorItems.BRONZE_PICKAXE,
                                        MapleArmorItems.BRONZE_AXE,
                                        MapleArmorItems.BRONZE_SHOVEL,
                                        MapleArmorItems.BRONZE_SWORD,
                                        MapleArmorItems.BRONZE_HOE,
                                        MapleArmorItems.BRONZE_HELMET,
                                        MapleArmorItems.BRONZE_CHESTPLATE,
                                        MapleArmorItems.BRONZE_LEGGINGS,
                                        MapleArmorItems.BRONZE_BOOTS
                                ),
                                RecipeCategory.MISC, CookingBookCategory.MISC, GrowableOresItems.BRONZE_INGOT, 0.1F, 200)
                        .unlockedBy("has_bronze_pickaxe", this.has(MapleArmorItems.BRONZE_PICKAXE))
                        .unlockedBy("has_bronze_axe", this.has(MapleArmorItems.BRONZE_AXE))
                        .unlockedBy("has_bronze_shovel", this.has(MapleArmorItems.BRONZE_SHOVEL))
                        .unlockedBy("has_bronze_sword", this.has(MapleArmorItems.BRONZE_SWORD))
                        .unlockedBy("has_bronze_hoe", this.has(MapleArmorItems.BRONZE_HOE))
                        .unlockedBy("has_bronze_helmet", this.has(MapleArmorItems.BRONZE_HELMET))
                        .unlockedBy("has_bronze_chestplate", this.has(MapleArmorItems.BRONZE_CHESTPLATE))
                        .unlockedBy("has_bronze_leggings", this.has(MapleArmorItems.BRONZE_LEGGINGS))
                        .unlockedBy("has_bronze_boots", this.has(MapleArmorItems.BRONZE_BOOTS))
                        .save(this.output, getSmeltingRecipeName(GrowableOresItems.BRONZE_INGOT));

                shaped(RecipeCategory.COMBAT, MapleArmorItems.Quantum_HELMET)
                        .pattern("CCC")
                        .pattern("#L#")
                        .pattern("ARA")
                        .define('C', GrowableOresItems.CARBON_PLATE)
                        .define('#', GrowableOresItems.IRIDIUM_PLATE)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .define('A', GrowableOresItems.Advanced_Circuit)
                        .define('R', GeneralBlocks.Reinforced_Glass)
                        .unlockedBy(getHasName(GrowableOresItems.Advanced_Circuit),
                                has(GrowableOresItems.Advanced_Circuit))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.Quantum_CHESTPLATE)
                        .pattern("CCC")
                        .pattern("#L#")
                        .pattern("#A#")
                        .define('C', GrowableOresItems.CARBON_PLATE)
                        .define('#', GrowableOresItems.IRIDIUM_PLATE)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .define('A', GrowableOresItems.ALLOY_PLATE)
                        .unlockedBy(getHasName(GrowableOresItems.CARBON_PLATE),
                                has(GrowableOresItems.CARBON_PLATE))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.Quantum_LEGGINGS)
                        .pattern("MLM")
                        .pattern("#C#")
                        .pattern("GCG")
                        .define('M', GeneralBlocks.Machine)
                        .define('G', Items.GLOWSTONE_DUST)
                        .define('C', GrowableOresItems.CARBON_PLATE)
                        .define('#', GrowableOresItems.IRIDIUM_PLATE)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .unlockedBy(getHasName(GrowableOresItems.CARBON_PLATE),
                                has(GrowableOresItems.CARBON_PLATE))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.Quantum_BOOTS)
                        .pattern("CCC")
                        .pattern("#R#")
                        .pattern("RLR")
                        .define('R', GrowableOresItems.Rubber)
                        .define('C', GrowableOresItems.CARBON_PLATE)
                        .define('#', GrowableOresItems.IRIDIUM_PLATE)
                        .define('L', GrowableOresItems.LAPOTRON_CRYSTAL)
                        .unlockedBy(getHasName(GrowableOresItems.CARBON_PLATE),
                                has(GrowableOresItems.CARBON_PLATE))
                        .save(output);

                shaped(RecipeCategory.COMBAT, MapleArmorItems.Electric_Jetpack)
                        .pattern("I#I")
                        .pattern("ILI")
                        .pattern("G G")
                        .define('I', GrowableOresItems.IRON_CASING)
                        .define('#', GrowableOresItems.Advanced_Circuit)
                        .define('L', GrowableOresBlocks.EnergyBox)
                        .define('G', Items.GLOWSTONE_DUST)
                        .unlockedBy(getHasName(GrowableOresItems.IRON_CASING),
                                has(GrowableOresItems.IRON_CASING))
                        .save(output);
            }
        };
    }


    @Override
    public String getName() {
        return "Armor";
    }
}