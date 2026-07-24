package com.skniro.industrial_elixir.datagen.recipe;

import com.skniro.industrial_elixir.datagen.recipe.advanced.AdvancedRecipeGenerator;
import com.skniro.industrial_elixir.datagen.recipe.base.ArmorRecipeGenerator;
import com.skniro.industrial_elixir.datagen.recipe.machine.*;
import com.skniro.industrial_elixir.datagen.recipe.material.CableUpgradeRecipeGenerator;
import com.skniro.industrial_elixir.datagen.recipe.material.MaterialRecipeGenerator;
import com.skniro.industrial_elixir.datagen.recipe.material.TreeBlockRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class RecipeDataGeneration {

    public static void onInit(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(MaterialRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(CompressorRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(MaceratorRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(TreeBlockRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(BaseMachineRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(ExtractorRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(MetalFormerRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(MolecularTransformerRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(CableUpgradeRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(AdvancedRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(ArmorRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(CuttingRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(BrewReactorRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(OreWashingRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(ModBlastFurnaceRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(HeatCentrifugeRecipeGenerator::new);
        fabricDataGenerator.createPack().addProvider(CoffeeMachineRecipeGenerator::new);
    }
}
