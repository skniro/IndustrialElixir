package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.BrewReactorCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class BrewReactorRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private Ingredient ingredient;
    private Ingredient ingredient2;
    private final ItemStack result;
    private int requiredCount;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Nullable
    private String group;

    private BrewReactorRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStack result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    private ResourceLocation fluidId = null;
    private Integer fluidAmount = null;

    public static BrewReactorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count) {
        return new BrewReactorRecipeJsonBuilder(itemLookup, new ItemStack(output.asItem(), count));
    }

    public static BrewReactorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output) {
        return create(itemLookup, output, 1);
    }

    public static BrewReactorRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemStack output) {
        return new BrewReactorRecipeJsonBuilder(itemLookup, output);
    }

    public BrewReactorRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public BrewReactorRecipeJsonBuilder input(ItemLike item, int amount) {
        this.ingredient = Ingredient.of(item);
        this.requiredCount = amount;
        return this;
    }

    public BrewReactorRecipeJsonBuilder input2(ItemLike item) {
        this.ingredient2 = Ingredient.of(item);
        return this;
    }

    public BrewReactorRecipeJsonBuilder input2(TagKey<Item> tag) {
        this.ingredient2 = Ingredient.of(tag);
        return this;
    }
    public BrewReactorRecipeJsonBuilder fluid(ResourceLocation fluidId, int amount) {
        this.fluidId = fluidId;
        this.fluidAmount = amount;
        return this;
    }

    /**
     * Convenience overload to accept a vanilla Fluid instance (eg. Fluids.WATER.getSource()).
     * The fluid's registry id will be used as the recipe's fluid identifier.
     */
    public BrewReactorRecipeJsonBuilder fluid(Fluid fluid, int amount) {
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
        this.fluidId = id;
        this.fluidAmount = amount;
        return this;
    }
    public BrewReactorRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public BrewReactorRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        this.ingredient = Ingredient.of(tag);
        this.requiredCount = amount;
        return this;
    }


    public BrewReactorRecipeJsonBuilder unlockedBy(String name,
                                                   Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public BrewReactorRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput exporter, ResourceLocation recipeId) {
        ResourceLocation finalRecipeId = modifyRecipeKey(recipeId, exporter);
        // 如果调用方通过 input2 指定了第二输入则使用，否则不设置第二输入（传入 null，会在序列化器/codec 中被视为可选并省略字段）
        Optional<Ingredient> ing2 = this.ingredient2 != null ? Optional.of(this.ingredient2) : Optional.empty();
        Optional<ResourceLocation> fluidOpt = this.fluidId != null ? Optional.of(this.fluidId) : Optional.empty();
        Optional<Integer> amtOpt = this.fluidAmount != null ? Optional.of(this.fluidAmount) : Optional.empty();
        BrewReactorCraftingRecipe recipe = new BrewReactorCraftingRecipe(ingredient, ing2, requiredCount, result, fluidOpt, amtOpt);
        if (criteria.isEmpty()) {
            exporter.accept(finalRecipeId, recipe, null);
            return;
        }

        Advancement.Builder advancementBuilder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        exporter.accept(finalRecipeId, recipe, advancementBuilder.build(finalRecipeId.withPrefix("recipes/")));

    }

    private ResourceLocation modifyRecipeKey(ResourceLocation recipeId, RecipeOutput exporter) {
        return exporter.getRecipeIdentifier(recipeId.withPrefix("brew_reactor/"));
    }
}
