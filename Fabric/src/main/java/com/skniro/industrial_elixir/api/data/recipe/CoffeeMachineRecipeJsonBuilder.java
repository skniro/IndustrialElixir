package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.CoffeeMachineCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CoffeeMachineRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private Ingredient ingredient;
    private Ingredient ingredient2;
    private Ingredient ingredient3;
    private final ItemStackTemplate result;
    private int requiredCount;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private Identifier fluidId;
    private int fluidAmount;

    @Nullable
    private String group;

    private CoffeeMachineRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStackTemplate result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static CoffeeMachineRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count) {
        return new CoffeeMachineRecipeJsonBuilder(itemLookup, new ItemStackTemplate(output.asItem(), count));
    }

    public static CoffeeMachineRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output) {
        return create(itemLookup, output, 1);
    }

    public static CoffeeMachineRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemStackTemplate output) {
        return new CoffeeMachineRecipeJsonBuilder(itemLookup, output);
    }

    public CoffeeMachineRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public CoffeeMachineRecipeJsonBuilder input(ItemLike item, int amount) {
        this.ingredient = Ingredient.of(item);
        this.requiredCount = amount;
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public CoffeeMachineRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        this.ingredient = Ingredient.of(itemLookup.getOrThrow(tag));
        this.requiredCount = amount;
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder input2(ItemLike item) {
        this.ingredient2 = Ingredient.of(item);
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder input2(TagKey<Item> tag) {
        this.ingredient2 = Ingredient.of(itemLookup.getOrThrow(tag));
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder input3(ItemLike item) {
        this.ingredient3 = Ingredient.of(item);
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder input3(TagKey<Item> tag) {
        this.ingredient3 = Ingredient.of(itemLookup.getOrThrow(tag));
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder fluid(Identifier fluidId, int amount) {
        this.fluidId = fluidId;
        this.fluidAmount = amount;
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder fluid(Fluid fluid, int amount) {
        return fluid(BuiltInRegistries.FLUID.getKey(fluid), amount);
    }

    public CoffeeMachineRecipeJsonBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public CoffeeMachineRecipeJsonBuilder group(@Nullable String group) {
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
        CoffeeMachineCraftingRecipe recipe = new CoffeeMachineCraftingRecipe(
                ingredient,
                Optional.ofNullable(ingredient2),
                Optional.ofNullable(ingredient3),
                requiredCount,
                result,
                fluidId,
                fluidAmount
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
                exporter.getRecipeIdentifier(recipeKey.identifier().withPrefix("coffee_machine/")));
    }
}
