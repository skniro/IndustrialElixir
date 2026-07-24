package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.CraftingDataHelper;
import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.item.MapleFoodComponents;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class CoffeeMachineRecipeGenerator extends CraftingDataHelper {
    public CoffeeMachineRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                createCoffeeMachine(MapleFoodComponents.Coffee_Black)
                        .fluid(Fluids.WATER, 1000)
                        .input(MapleFoodComponents.Coffee_Beans, 1)
                        .unlockedBy("has_coffee_beans", has(MapleFoodComponents.Coffee_Beans))
                        .save(output, "coffee_beans_to_coffee_black");

                createCoffeeMachine(MapleFoodComponents.Cappuccino)
                        .fluid(Fluids.WATER, 1000)
                        .input(Items.MILK_BUCKET, 1)
                        .input(MapleFoodComponents.Coffee_Beans, 1)
                        .input(Items.SUGAR, 1)
                        .unlockedBy("has_coffee_beans", has(MapleFoodComponents.Coffee_Beans))
                        .save(output, "coffee_cappuccino");

                createCoffeeMachine(MapleFoodComponents.Latte)
                        .fluid(Fluids.WATER, 1000)
                        .input(Items.MILK_BUCKET, 1)
                        .input(MapleFoodComponents.Coffee_Beans, 1)
                        .unlockedBy("has_coffee_beans", has(MapleFoodComponents.Coffee_Beans))
                        .save(output, "coffee_latte");

                createCoffeeMachine(MapleFoodComponents.Mocha)
                        .fluid(Fluids.WATER, 1000)
                        .input(MapleFoodComponents.Coffee_Beans, 1)
                        .input(Items.COCOA_BEANS, 1)
                        .input(Items.MILK_BUCKET, 1)
                        .unlockedBy("has_coffee_beans", has(MapleFoodComponents.Coffee_Beans))
                        .save(output, "coffee_mocha");

                createCoffeeMachine(MapleFoodComponents.Hot_Cocoa)
                        .fluid(Fluids.WATER, 1000)
                        .input(Items.COCOA_BEANS, 1)
                        .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS))
                        .save(output, "cocoa_to_hot_cocoa");
            }
        };
    }

    @Override
    public String getName() {
        return "CoffeeMachine";
    }
}
