package com.skniro.industrial_elixir.api.data.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public abstract class CraftingDataHelper extends FabricRecipeProvider {

    public CraftingDataHelper(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public final Ingredient potion1(Potion potion) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Potion", BuiltInRegistries.POTION.getKey(potion).toString());
        return DefaultCustomIngredients.customData(Ingredient.of(Items.POTION), nbt);
    }

    public final ItemStack potion(HolderLookup.Provider registries, Holder<Potion> potion) {
        Holder<Potion> boundPotion;
        if (potion.unwrapKey().isPresent()) {
            boundPotion = registries.lookupOrThrow(Registries.POTION).getOrThrow(potion.unwrapKey().get());
        } else {
            boundPotion = potion;
        }
        return PotionContents.createItemStack(Items.POTION, boundPotion);
    }

    public final ItemStackTemplate potionTemplate(HolderLookup.Provider registries, Holder<Potion> potion, int count) {
        Holder<Potion> boundPotion;
        if (potion.unwrapKey().isPresent()) {
            boundPotion = registries.lookupOrThrow(Registries.POTION).getOrThrow(potion.unwrapKey().get());
        } else {
            boundPotion = potion;
        }

        HolderLookup.RegistryLookup<net.minecraft.world.item.Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
        Holder<net.minecraft.world.item.Item> potionItem = itemLookup.getOrThrow(Items.POTION.builtInRegistryHolder().key());
        DataComponentPatch patch = DataComponentPatch.builder()
                .set(DataComponents.POTION_CONTENTS, new PotionContents(boundPotion))
                .build();
        return new ItemStackTemplate(potionItem, count, patch);
    }

    public final ItemStackTemplate potionTemplate(HolderLookup.Provider registries, Holder<Potion> potion) {
        return potionTemplate(registries, potion, 1);
    }

}
