package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.ExtractorCraftingRecipe;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class ExtractorRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private final List<Ingredient> ingredient = new ArrayList<>();
    private final ItemStackTemplate result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Nullable
    private String group;

    private ExtractorRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStackTemplate result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static ExtractorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count) {
        return new ExtractorRecipeJsonBuilder(itemLookup, new ItemStackTemplate(output.asItem(), count));
    }

    public static ExtractorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output) {
        return create(itemLookup, output, 1);
    }

    public ExtractorRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public ExtractorRecipeJsonBuilder input(ItemLike item, int amount) {
        for (int i = 0; i < amount; i++) {
            this.ingredient.add(Ingredient.of(item));
        }
        return this;
    }
    public ExtractorRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public ExtractorRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        Ingredient ing = Ingredient.of(itemLookup.getOrThrow(tag));
        for (int i = 0; i < amount; i++) {
            this.ingredient.add(ing);
        }
        return this;
    }


    public ExtractorRecipeJsonBuilder unlockedBy(String name,
                                                Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public ExtractorRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput exporter, ResourceKey<Recipe<?>> recipeKey) {
        ResourceKey<Recipe<?>> finalKey = modifyRecipeKey(recipeKey, exporter);
        ExtractorCraftingRecipe recipe = new ExtractorCraftingRecipe(ingredient.getFirst(), result);
        if (criteria.isEmpty()) {
            exporter.accept(finalKey, recipe, null);
            return;
        }

        Advancement.Builder advancementBuilder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        exporter.accept(finalKey, recipe, advancementBuilder.build(finalKey.identifier().withPrefix("recipes/")));

    }

    private ResourceKey<Recipe<?>> modifyRecipeKey(ResourceKey<Recipe<?>> recipeKey, RecipeOutput exporter) {
        return ResourceKey.create(recipeKey.registryKey(), exporter.getRecipeIdentifier(recipeKey.identifier().withPrefix("extractor/")));
    }
}