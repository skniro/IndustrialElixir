package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class MaceratorRecipeGenerator extends FabricRecipeProvider {
    public MaceratorRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {

                createMacerator(Items.STRING, 2).input(Items.WHITE_WOOL)
                        .unlockedBy("has_base_item", has(Items.WHITE_WOOL)).save(output);
                createMacerator(GrowableOresItems.COAL_DUST).input(Items.COAL)
                        .unlockedBy("has_base_item", has(Items.COAL)).save(output);
                createMacerator(GrowableOresItems.COAL_DUST,9).input(Items.COAL_BLOCK)
                        .unlockedBy("has_base_item", has(Items.COAL_BLOCK)).save(output,"coal_block_to_coal_dust");
                createMacerator(Items.REDSTONE,9).input(Items.REDSTONE_BLOCK)
                        .unlockedBy("has_base_item", has(Items.REDSTONE_BLOCK)).save(output);

                createMacerator(Items.FLINT).input(Items.GRAVEL)
                        .unlockedBy("has_base_item", has(Items.GRAVEL)).save(output);

                createMacerator(Items.GLOWSTONE_DUST, 9).input(Items.GLOWSTONE)
                        .unlockedBy("has_base_item", has(Items.GLOWSTONE)).save(output);

                createMacerator(Items.QUARTZ, 4).input(Items.QUARTZ_BLOCK)
                        .unlockedBy("has_base_item", has(Items.QUARTZ_BLOCK)).save(output);

                createMacerator(Items.SNOWBALL, 1).input(Items.ICE)
                        .unlockedBy("has_base_item", has(Items.ICE)).save(output);

                createMacerator(Items.BONE_MEAL, 4).input(Items.BONE)
                        .unlockedBy("has_base_item", has(Items.BONE)).save(output);

                createMacerator(GrowableOresItems.CLAY_DUST, 2).input(Items.CLAY)
                        .unlockedBy("has_base_item", has(Items.CLAY)).save(output);

                createMacerator(GrowableOresItems.IRON_DUST, 2).input(GrowableOresItems.STEEL_INGOT)
                        .unlockedBy("has_base_item", has(GrowableOresItems.STEEL_INGOT)).save(output,"steel_ingot_to_iron_dust");

                createMacerator(Items.SAND, 2).input(Items.SANDSTONE)
                        .unlockedBy("has_base_item", has(Items.SANDSTONE)).save(output);

                createMacerator(Items.SAND, 2).input(Items.COBBLESTONE)
                        .unlockedBy("has_base_item", has(Items.STONE)).save(output,"stone_to_sand");

                createMacerator(Items.COBBLESTONE, 2).input(Items.STONE)
                        .unlockedBy("has_base_item", has(Items.STONE)).save(output);

                createMacerator(Items.BLAZE_POWDER, 5).input(Items.BLAZE_ROD)
                        .unlockedBy("has_base_item", has(Items.BLAZE_ROD)).save(output);

       /*         createMacerator(GrowableOresItems.ENERGIUM_DUST, 2).input(Items.ENDER_EYE)
                        .criterion("has_base_item", conditionsFromItem(Items.SANDSTONE)).offerTo(exporter,"steel_ingot_to_iron_dust");
*/
                createMacerator(GrowableOresItems.CRUSHED_IRON,2).input(Items.RAW_IRON)
                        .unlockedBy("has_base_item", has(Items.RAW_IRON)).save(output,"raw_iron_to_crushed_iron");
                createMacerator(GrowableOresItems.CRUSHED_IRON,2).input(ConventionalItemTags.IRON_ORES)
                        .unlockedBy("has_base_item", has(ConventionalItemTags.IRON_ORES)).save(output);
                createMacerator(GrowableOresItems.IRON_DUST,9).input(GrowableOresItems.DENSE_IRON_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.DENSE_IRON_PLATE)).save(output,"dense_iron_plate_to_iron_dust");
                createMacerator(GrowableOresItems.IRON_DUST).input(GrowableOresItems.IRON_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.IRON_PLATE)).save(output,"iron_plate_to_iron_dust");
                createMacerator(GrowableOresItems.IRON_DUST).input(GrowableOresItems.PURIFIED_IRON)
                        .unlockedBy("has_base_item", has(GrowableOresItems.PURIFIED_IRON)).save(output,"purified_iron_to_iron_dust");
                createMacerator(GrowableOresItems.IRON_DUST).input(GrowableOresItems.CRUSHED_IRON)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_IRON)).save(output,"crushed_iron_to_iron_dust");
                createMacerator(GrowableOresItems.IRON_DUST).input(Items.IRON_INGOT)
                        .unlockedBy("has_base_item", has(Items.IRON_INGOT)).save(output,"iron_ingot_to_iron_dust");

                createMacerator(GrowableOresItems.CRUSHED_GOLD,2).input(Items.RAW_GOLD)
                        .unlockedBy("has_base_item", has(Items.RAW_GOLD)).save(output,"raw_gold_to_crushed_gold");
                createMacerator(GrowableOresItems.CRUSHED_GOLD,2).input(ConventionalItemTags.GOLD_ORES)
                        .unlockedBy("has_base_item", has(ConventionalItemTags.GOLD_ORES)).save(output);
                createMacerator(GrowableOresItems.GOLD_DUST,9).input(GrowableOresItems.DENSE_GOLD_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.DENSE_GOLD_PLATE)).save(output,"dense_gold_plate_to_gold_dust");
                createMacerator(GrowableOresItems.GOLD_DUST).input(GrowableOresItems.GOLD_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.GOLD_PLATE)).save(output,"gold_plate_to_gold_dust");
                createMacerator(GrowableOresItems.GOLD_DUST).input(GrowableOresItems.PURIFIED_GOLD)
                        .unlockedBy("has_base_item", has(GrowableOresItems.PURIFIED_GOLD)).save(output,"purified_gold_to_gold_dust");
                createMacerator(GrowableOresItems.GOLD_DUST).input(GrowableOresItems.CRUSHED_GOLD)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_GOLD)).save(output,"crushed_gold_to_gold_dust");
                createMacerator(GrowableOresItems.GOLD_DUST).input(Items.GOLD_INGOT)
                        .unlockedBy("has_base_item", has(Items.GOLD_INGOT)).save(output,"gold_ingot_to_gold_dust");

                createMacerator(GrowableOresItems.BRONZE_DUST,9).input(GrowableOresItems.DENSE_BRONZE_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.DENSE_BRONZE_PLATE)).save(output,"dense_bronze_plate_to_bronze_dust");
                createMacerator(GrowableOresItems.BRONZE_DUST).input(GrowableOresItems.BRONZE_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.BRONZE_PLATE)).save(output,"bronze_plate_to_bronze_dust");
                createMacerator(GrowableOresItems.BRONZE_DUST).input(GrowableOresItems.BRONZE_INGOT)
                        .unlockedBy("has_base_item", has(GrowableOresItems.BRONZE_INGOT)).save(output,"bronze_ingot_to_bronze_dust");

                createMacerator(GrowableOresItems.DIAMOND_DUST).input(Items.DIAMOND)
                        .unlockedBy("has_base_item", has(Items.DIAMOND)).save(output,"bronze_ingot_to_diamond_dust");

                createMacerator(GrowableOresItems.CRUSHED_COPPER,2).input(Items.RAW_COPPER)
                        .unlockedBy("has_base_item", has(Items.RAW_COPPER)).save(output,"raw_copper_to_crushed_copper");
                createMacerator(GrowableOresItems.CRUSHED_COPPER,2).input(ConventionalItemTags.COPPER_ORES)
                        .unlockedBy("has_base_item", has(ConventionalItemTags.COPPER_ORES)).save(output);
                createMacerator(GrowableOresItems.COPPER_DUST,9).input(GrowableOresItems.DENSE_COPPER_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.DENSE_COPPER_PLATE)).save(output,"dense_copper_plate_to_copper_dust");
                createMacerator(GrowableOresItems.COPPER_DUST).input(GrowableOresItems.COPPER_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.COPPER_PLATE)).save(output,"copper_plate_to_copper_dust");
                createMacerator(GrowableOresItems.COPPER_DUST).input(GrowableOresItems.PURIFIED_COPPER)
                        .unlockedBy("has_base_item", has(GrowableOresItems.PURIFIED_COPPER)).save(output,"purified_copper_to_copper_dust");
                createMacerator(GrowableOresItems.COPPER_DUST).input(GrowableOresItems.CRUSHED_COPPER)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_COPPER)).save(output,"crushed_copper_to_copper_dust");
                createMacerator(GrowableOresItems.COPPER_DUST).input(Items.COPPER_INGOT)
                        .unlockedBy("has_base_item", has(Items.COPPER_INGOT)).save(output,"copper_ingot_to_copper_dust");

                createMacerator(GrowableOresItems.CRUSHED_SACRED,2).input(GrowableOresItems.Raw_SACRED)
                        .unlockedBy("has_base_item", has(GrowableOresItems.Raw_SACRED)).save(output,"raw_septrin_to_crushed_septrin");
                createMacerator(GrowableOresItems.CRUSHED_SACRED,2).input(ModItemTags.SACRED_ORES)
                        .unlockedBy("has_base_item", has(ModItemTags.SACRED_ORES)).save(output);

                createMacerator(GrowableOresItems.CRUSHED_LEAD,2).input(GrowableOresItems.Raw_Lead)
                        .unlockedBy("has_base_item", has(GrowableOresItems.Raw_Lead)).save(output,"raw_lead_to_crushed_lead");
                createMacerator(GrowableOresItems.CRUSHED_LEAD,2).input(ModItemTags.LEAD_ORES)
                        .unlockedBy("has_base_item", has(ModItemTags.LEAD_ORES)).save(output);
                createMacerator(GrowableOresItems.LEAD_DUST,9).input(GrowableOresItems.DENSE_LEAD_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.DENSE_LEAD_PLATE)).save(output,"dense_lead_plate_to_lead_dust");
                createMacerator(GrowableOresItems.LEAD_DUST).input(GrowableOresItems.LEAD_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LEAD_PLATE)).save(output,"lead_plate_to_lead_dust");
                createMacerator(GrowableOresItems.LEAD_DUST).input(GrowableOresItems.PURIFIED_LEAD)
                        .unlockedBy("has_base_item", has(GrowableOresItems.PURIFIED_LEAD)).save(output,"purified_lead_to_lead_dust");
                createMacerator(GrowableOresItems.LEAD_DUST).input(GrowableOresItems.CRUSHED_LEAD)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_LEAD)).save(output,"crushed_lead_to_lead_dust");
                createMacerator(GrowableOresItems.LEAD_DUST).input(GrowableOresItems.LEAD_INGOT)
                        .unlockedBy("has_base_item", has(GrowableOresItems.LEAD_INGOT)).save(output,"lead_ingot_to_lead_dust");

                createMacerator(GrowableOresItems.CRUSHED_TIN,2).input(GrowableOresItems.Raw_Tin)
                        .unlockedBy("has_base_item", has(GrowableOresItems.Raw_Tin)).save(output,"raw_tin_to_crushed_tin");
                createMacerator(GrowableOresItems.CRUSHED_TIN,2).input(ModItemTags.TIN_ORES)
                        .unlockedBy("has_base_item", has(ModItemTags.TIN_ORES)).save(output);
                createMacerator(GrowableOresItems.TIN_DUST,9).input(GrowableOresItems.DENSE_TIN_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.DENSE_TIN_PLATE)).save(output,"dense_tin_plate_to_tin_dust");
                createMacerator(GrowableOresItems.TIN_DUST).input(GrowableOresItems.TIN_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.TIN_PLATE)).save(output,"tin_plate_to_tin_dust");
                createMacerator(GrowableOresItems.TIN_DUST).input(GrowableOresItems.PURIFIED_TIN)
                        .unlockedBy("has_base_item", has(GrowableOresItems.PURIFIED_TIN)).save(output,"purified_tin_to_tin_dust");
                createMacerator(GrowableOresItems.TIN_DUST).input(GrowableOresItems.CRUSHED_TIN)
                        .unlockedBy("has_base_item", has(GrowableOresItems.CRUSHED_TIN)).save(output,"crushed_tin_to_tin_dust");
                createMacerator(GrowableOresItems.TIN_DUST).input(GrowableOresItems.TIN_INGOT)
                        .unlockedBy("has_base_item", has(GrowableOresItems.TIN_INGOT)).save(output,"tin_ingot_to_tin_dust");

                createMacerator(GrowableOresItems.LAPIS_DUST).input(Items.LAPIS_LAZULI)
                        .unlockedBy("has_base_item", has(Items.LAPIS_LAZULI)).save(output,"lapis_to_lapis_dust");
                createMacerator(GrowableOresItems.LAPIS_DUST, 9).input(Items.LAPIS_BLOCK)
                        .unlockedBy("has_base_item", has(Items.LAPIS_BLOCK)).save(output,"lapis_block_to_lapis_dust");

                createMacerator(GrowableOresItems.OBSIDIAN_DUST).input(Items.OBSIDIAN)
                        .unlockedBy("has_base_item", has(Items.OBSIDIAN)).save(output,"obsidian_to_obsidian_dust");
                createMacerator(GrowableOresItems.OBSIDIAN_DUST).input(GrowableOresItems.OBSIDIAN_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.OBSIDIAN_PLATE)).save(output,"obsidian_plate_to_obsidian_dust");
                createMacerator(GrowableOresItems.OBSIDIAN_DUST,9).input(GrowableOresItems.DENSE_OBSIDIAN_PLATE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.OBSIDIAN_PLATE)).save(output,"dense_obsidian_plate_to_obsidian_dust");
                createMacerator(GrowableOresItems.IRIDIUM_SHARD,9).input(GrowableOresItems.IRIDIUM_ORE)
                        .unlockedBy("has_base_item", has(GrowableOresItems.IRIDIUM_ORE)).save(output);
            }



            public void offerCampfireCooking(ItemLike inputs, ItemLike output, float experience, int cookingTime) {
                this.simpleCookingRecipe("campfire_cooking", CampfireCookingRecipe::new, cookingTime, inputs, output, experience);
            }

            public void offerSmoking(ItemLike inputs, ItemLike output, float experience, int cookingTime) {
                this.simpleCookingRecipe("smoking", SmokingRecipe::new, cookingTime, inputs, output, experience);
            }

            public void offerSmoking(TagKey<Item> inputs, ItemLike output, float experience, int cookingTime) {
                this.simpleCookingRecipeTag("smoking", SmokingRecipe::new, cookingTime, inputs, output, experience);
            }

            public void offerCampfireCooking(TagKey<Item> inputs, ItemLike output, float experience, int cookingTime) {
                this.simpleCookingRecipeTag("campfire_cooking", CampfireCookingRecipe::new, cookingTime, inputs, output, experience);
            }

            public <T extends AbstractCookingRecipe> void simpleCookingRecipeTag(final String source, final AbstractCookingRecipe.Factory<T> factory, final int cookingTime, final TagKey<Item> baseTag, final ItemLike result, final float experience) {
                SimpleCookingRecipeBuilder.generic(this.tag(baseTag), RecipeCategory.FOOD, CookingBookCategory.FOOD, result, experience, cookingTime, factory).unlockedBy("has_" + baseTag.location().getPath(), this.has(baseTag)).save(this.output, getItemName(result) + "_from_" + baseTag.location().getPath() + "_" + source);
            }

        };
    }

    @Override
    public String getName() {
        return "Macerator";
    }
}
