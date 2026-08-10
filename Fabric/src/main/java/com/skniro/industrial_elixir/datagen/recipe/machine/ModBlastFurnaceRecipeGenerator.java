package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class ModBlastFurnaceRecipeGenerator extends FabricRecipeProvider {
    public ModBlastFurnaceRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                blastFurnace(Items.IRON_INGOT, GrowableOresItems.STEEL_INGOT, GrowableOresItems.SLAG, "iron_ingot_to_stell_ingot");
                blastFurnace(Items.GLASS_PANE, GrowableOresItems.EMPTY_VESSEL, null, "glass_pane_to_empty_vessel");
            }

            private void blastFurnace(ItemLike input, ItemLike primaryOutput, Item secondaryOutput, String name) {
                var builder = createBlastFurnace(primaryOutput).fluid(IndustrialElixirFluids.STILL_Fluid_AIR, 1000)
                        .input(input).unlockedBy("has_base_item", has(input));

                if (secondaryOutput != null) {
                    builder.output2(secondaryOutput);
                }
                builder.save(output, name);
            }
        };
    }

    @Override
    public String getName() {
        return "BlastFurnace";
    }
}
