package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.recipe.machine.MolecularTransformerCraftingRecipe;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

public class MolecularTransformerRecipeJsonBuilder implements RecipeBuilder {
    private final HolderGetter<Item> itemLookup;
    private final List<Ingredient> ingredient = new ArrayList<>();
    private final ItemStack result;
    private long requiredEnergy;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Nullable
    private String group;

    private MolecularTransformerRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStack result, long requiredEnergy) {
        this.itemLookup = itemLookup;
        this.result = result;
        this.requiredEnergy = requiredEnergy;
    }

    public static MolecularTransformerRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, int count, long requiredEnergy) {
        return new MolecularTransformerRecipeJsonBuilder(itemLookup, new ItemStack(output.asItem(), count), requiredEnergy);
    }

    public static MolecularTransformerRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike output, long requiredEnergy) {
        return create(itemLookup, output, 1, requiredEnergy);
    }

    public MolecularTransformerRecipeJsonBuilder input(ItemLike item) {
        return input(item, 1);
    }

    public MolecularTransformerRecipeJsonBuilder input(ItemLike item, int amount) {
        for (int i = 0; i < amount; i++) {
            this.ingredient.add(Ingredient.of(item));
        }
        return this;
    }
    public MolecularTransformerRecipeJsonBuilder input(TagKey<Item> tag) {
        return input(tag, 1);
    }

    public MolecularTransformerRecipeJsonBuilder input(TagKey<Item> tag, int amount) {
        Ingredient ing = Ingredient.of(tag);
        for (int i = 0; i < amount; i++) {
            this.ingredient.add(ing);
        }
        return this;
    }


    public MolecularTransformerRecipeJsonBuilder unlockedBy(String name,
                                                Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public MolecularTransformerRecipeJsonBuilder group(@Nullable String group) {
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
        MolecularTransformerCraftingRecipe recipe = new MolecularTransformerCraftingRecipe(ingredient.getFirst(), result, requiredEnergy);
        if (criteria.isEmpty()) {
            exporter.accept(finalRecipeId, recipe, null);
            return;
        }

        Advancement.Builder advancementBuilder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        exporter.accept(finalRecipeId, recipe, advancementBuilder.build(finalRecipeId.withPrefix("recipes/")));

    }

    private ResourceLocation modifyRecipeKey(ResourceLocation recipeId, RecipeOutput exporter) {
        return exporter.getRecipeIdentifier(recipeId.withPrefix("molecular_transformer/"));
    }
}