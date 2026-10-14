package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.OreWashingCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class OreWashingRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private Ingredient ingredient;
    private final ItemStack result;
    private ItemStack result2;
    private ItemStack result3;
    private int requiredCount = 1;
    private ResourceLocation fluidId;
    private Integer fluidAmount;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Nullable
    private String group;

    private OreWashingRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStack result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static OreWashingRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count) {
        return new OreWashingRecipeJsonBuilder(itemLookup, new ItemStack(output.asItem(), count));
    }

    public static OreWashingRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output) {
        return create(itemLookup, output, 1);
    }

    public OreWashingRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public OreWashingRecipeJsonBuilder input(ItemLike item, int amount) {
        this.ingredient = Ingredient.of(item);
        this.requiredCount = amount;
        return this;
    }

    public OreWashingRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public OreWashingRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        this.ingredient = Ingredient.of(tag);
        this.requiredCount = amount;
        return this;
    }

    public OreWashingRecipeJsonBuilder output2(ItemLike item) {
        return output2(item, 1);
    }

    public OreWashingRecipeJsonBuilder output2(ItemLike item, int count) {
        this.result2 = new ItemStack(item.asItem(), count);
        return this;
    }

    public OreWashingRecipeJsonBuilder output3(ItemLike item) {
        return output3(item, 1);
    }

    public OreWashingRecipeJsonBuilder output3(ItemLike item, int count) {
        this.result3 = new ItemStack(item.asItem(), count);
        return this;
    }

    public OreWashingRecipeJsonBuilder fluid(ResourceLocation fluidId, int amount) {
        this.fluidId = fluidId;
        this.fluidAmount = amount;
        return this;
    }

    public OreWashingRecipeJsonBuilder fluid(Fluid fluid, int amount) {
        return fluid(BuiltInRegistries.FLUID.getKey(fluid), amount);
    }

    public OreWashingRecipeJsonBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public OreWashingRecipeJsonBuilder group(@Nullable String group) {
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
        OreWashingCraftingRecipe recipe = new OreWashingCraftingRecipe(
                ingredient,
                requiredCount,
                result,
                Optional.ofNullable(result2),
                Optional.ofNullable(result3),
                fluidId,
                fluidAmount
        );
        if (criteria.isEmpty()) {
            exporter.accept(finalRecipeId, recipe, null);
            return;
        }

        Advancement.Builder advancementBuilder = exporter.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        exporter.accept(finalRecipeId, recipe, advancementBuilder.build(finalRecipeId.withPrefix("recipes/")));
    }

    private ResourceLocation modifyRecipeKey(ResourceLocation recipeId, RecipeOutput exporter) {
        return exporter.getRecipeIdentifier(recipeId.withPrefix("ore_washing/"));
    }
}
