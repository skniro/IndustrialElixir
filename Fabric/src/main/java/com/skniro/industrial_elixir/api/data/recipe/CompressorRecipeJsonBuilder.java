package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.CompressorCraftingRecipe;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class CompressorRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private Ingredient ingredient;
    private final ItemStack result;
    private int requiredCount;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Nullable
    private String group;

    private CompressorRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStack result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static CompressorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count) {
        return new CompressorRecipeJsonBuilder(itemLookup, new ItemStack(output.asItem(), count));
    }

    public static CompressorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output) {
        return create(itemLookup, output, 1);
    }

    public CompressorRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public CompressorRecipeJsonBuilder input(ItemLike item, int amount) {
        this.ingredient = Ingredient.of(item);
        this.requiredCount = amount;
        return this;
    }
    public CompressorRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public CompressorRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        this.ingredient = Ingredient.of(itemLookup.getOrThrow(tag));
        this.requiredCount = amount;
        return this;
    }


    public CompressorRecipeJsonBuilder unlockedBy(String name,
                                                 Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public CompressorRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput exporter, ResourceLocation recipeKey) {
        ResourceKey<Recipe<?>> finalKey = modifyRecipeKey(recipeKey, exporter);
        CompressorCraftingRecipe recipe = new CompressorCraftingRecipe(ingredient, requiredCount, result);
        if (criteria.isEmpty()) {
            exporter.accept(finalKey, recipe, null);
            return;
        }

        Advancement.Builder advancementBuilder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        exporter.accept(finalKey, recipe, advancementBuilder.build(finalKey.location().withPrefix("recipes/")));

    }

    private ResourceLocation modifyRecipeKey(ResourceLocation recipeKey, RecipeOutput exporter) {
        return ResourceKey.create(recipeKey, exporter.getRecipeIdentifier(recipeKey.location().withPrefix("compressor/")));
    }
}