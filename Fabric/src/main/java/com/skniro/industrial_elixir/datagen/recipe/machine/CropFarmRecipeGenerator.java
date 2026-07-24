package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.CraftingDataHelper;
import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CropFarmRecipeGenerator extends CraftingDataHelper {
    public CropFarmRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                // Example: Wheat Seeds -> Wheat + extra seeds
                createCropFarm(Items.WHEAT)
                        .input(Items.WHEAT_SEEDS, 1)
                        .output2(Items.WHEAT_SEEDS, 1)
                        .processTime(200)
                        .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                        .save(output, "wheat_seeds_to_wheat");

                createCropFarm(Items.CARROT)
                        .input(Items.CARROT, 1)
                        .output2(Items.CARROT, 1)
                        .processTime(200)
                        .unlockedBy("has_carrot", has(Items.CARROT))
                        .save(output, "carrot_to_carrot");

                createCropFarm(Items.POTATO)
                        .input(Items.POTATO, 1)
                        .output2(Items.POTATO, 1)
                        .processTime(200)
                        .unlockedBy("has_potato", has(Items.POTATO))
                        .save(output, "potato_to_potato");

                createCropFarm(Items.BEETROOT)
                        .input(Items.BEETROOT_SEEDS, 1)
                        .output2(Items.BEETROOT_SEEDS, 1)
                        .processTime(200)
                        .unlockedBy("has_beetroot_seeds", has(Items.BEETROOT_SEEDS))
                        .save(output, "beetroot_seeds_to_beetroot");
            }
        };
    }

    @Override
    public String getName() {
        return "CropFarm";
    }
}
