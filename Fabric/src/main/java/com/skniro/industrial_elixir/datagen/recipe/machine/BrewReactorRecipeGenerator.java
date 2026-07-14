package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.growableoresir.GrowableOres;
import com.skniro.industrial_elixir.api.data.recipe.CraftingDataHelper;
import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.alchemy.IndustrialElixirPotions;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class BrewReactorRecipeGenerator extends CraftingDataHelper {
    public BrewReactorRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_LEAPING)).fluid(Fluids.WATER, 1000).input(Items.RABBIT_FOOT, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.RABBIT_FOOT)).save(output, "rabbit_foot_to_super_leaping");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_SWIFTNESS)).fluid(Fluids.WATER, 1000).input(Items.SUGAR, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.SUGAR)).save(output, "sugar_to_super_swiftness");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_SLOWNESS)).fluid(Fluids.WATER, 1000).input(Items.RABBIT_FOOT, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.RABBIT_FOOT)).save(output, "rabbit_foot_to_super_slowness");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_TURTLE_MASTER)).fluid(Fluids.WATER, 1000).input(Items.TURTLE_HELMET, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.TURTLE_HELMET)).save(output, "turtle_helmet_to_super_turtle_master");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_HEALING)).fluid(Fluids.WATER, 1000).input(Items.FERMENTED_SPIDER_EYE, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.GLISTERING_MELON_SLICE)).save(output, "fermented_spider_eye_to_super_healing");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_HARMING)).fluid(Fluids.WATER, 1000).input(Items.GLISTERING_MELON_SLICE, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.GLISTERING_MELON_SLICE)).save(output, "glistering_melon_slice_to_super_harming");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_POISON)).fluid(Fluids.WATER, 1000).input(Items.SPIDER_EYE, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.SPIDER_EYE)).save(output, "spider_eye_to_super_poison");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_REGENERATION)).fluid(Fluids.WATER, 1000).input(Items.GHAST_TEAR, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.GHAST_TEAR)).save(output, "ghast_tear_to_super_regeneration");
                createBrewReactor(potionTemplate(wrapperLookup, IndustrialElixirPotions.SUPER_STRENGTH)).fluid(Fluids.WATER, 1000).input(Items.BLAZE_POWDER, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.BLAZE_POWDER)).save(output, "blaze_powder_to_super_strength");
                createBrewReactor(potionTemplate(wrapperLookup, Potions.WIND_CHARGED)).fluid(Fluids.WATER, 1000).input(Items.BREEZE_ROD, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.BREEZE_ROD)).save(output, "breeze_rod_to_wind_charged");
                createBrewReactor(potionTemplate(wrapperLookup, Potions.WEAVING)).fluid(Fluids.WATER, 1000).input(Items.COBWEB, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.COBWEB)).save(output, "cobweb_to_weaving");
                createBrewReactor(potionTemplate(wrapperLookup, Potions.OOZING)).fluid(Fluids.WATER, 1000).input(Items.SLIME_BLOCK, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.SLIME_BLOCK)).save(output, "slime_block_to_oozing");
                createBrewReactor(potionTemplate(wrapperLookup, Potions.INFESTED)).fluid(Fluids.WATER, 1000).input(Items.STONE, 1).input2(Items.GLASS_BOTTLE).unlockedBy("has_base_item", has(Items.STONE)).save(output, "stone_to_infested");
                createBrewReactor(GrowableOresItems.SACRED_ESSENCE).input(GrowableOresItems.IMPURE_SACRED_STONE, 1).input2(GrowableOresItems.EMPTY_VESSEL).unlockedBy("has_base_item", has(GrowableOresItems.EMPTY_VESSEL)).save(output, "sacred_essence");
            }
        };
    }

    @Override
    public String getName() {
        return "BrewReactor";
    }
}
