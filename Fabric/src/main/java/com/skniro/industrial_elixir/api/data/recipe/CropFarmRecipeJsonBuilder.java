package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.CropFarmCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CropFarmRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private Ingredient ingredient;
    private final ItemStackTemplate result;
    private ItemStackTemplate result2;
    private ItemStackTemplate result3;
    private int requiredCount = 1;
    private int processTime = 200;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Nullable
    private String group;

    private CropFarmRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStackTemplate result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static CropFarmRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count) {
        return new CropFarmRecipeJsonBuilder(itemLookup, new ItemStackTemplate(output.asItem(), count));
    }

    public static CropFarmRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output) {
        return create(itemLookup, output, 1);
    }

    public CropFarmRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public CropFarmRecipeJsonBuilder input(ItemLike item, int amount) {
        this.ingredient = Ingredient.of(item);
        this.requiredCount = amount;
        return this;
    }

    public CropFarmRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public CropFarmRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        this.ingredient = Ingredient.of(itemLookup.getOrThrow(tag));
        this.requiredCount = amount;
        return this;
    }

    public CropFarmRecipeJsonBuilder output2(ItemLike item) {
        return output2(item, 1);
    }

    public CropFarmRecipeJsonBuilder output2(ItemLike item, int count) {
        this.result2 = new ItemStackTemplate(item.asItem(), count);
        return this;
    }

    public CropFarmRecipeJsonBuilder output3(ItemLike item) {
        return output3(item, 1);
    }

    public CropFarmRecipeJsonBuilder output3(ItemLike item, int count) {
        this.result3 = new ItemStackTemplate(item.asItem(), count);
        return this;
    }

    public CropFarmRecipeJsonBuilder processTime(int ticks) {
        this.processTime = ticks;
        return this;
    }

    public CropFarmRecipeJsonBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public CropFarmRecipeJsonBuilder group(@Nullable String group) {
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
        CropFarmCraftingRecipe recipe = new CropFarmCraftingRecipe(
                ingredient,
                requiredCount,
                result,
                Optional.ofNullable(result2),
                Optional.ofNullable(result3),
                processTime
        );

        if (criteria.isEmpty()) {
            exporter.accept(finalKey, recipe, null);
            return;
        }

        Advancement.Builder advancementBuilder = exporter.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        exporter.accept(finalKey, recipe,
                advancementBuilder.build(finalKey.identifier().withPrefix("recipes/")));
    }

    private ResourceKey<Recipe<?>> modifyRecipeKey(ResourceKey<Recipe<?>> recipeKey, RecipeOutput exporter) {
        return ResourceKey.create(recipeKey.registryKey(),
                exporter.getRecipeIdentifier(recipeKey.identifier().withPrefix("crop_farm/")));
    }
}
