package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class MetalFormerRecipeGenerator extends FabricRecipeProvider {
    public MetalFormerRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                // Bronze
                createMetalFormerRolling(GrowableOresItems.BRONZE_INGOT, GrowableOresItems.BRONZE_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.BRONZE_PLATE, GrowableOresItems.BRONZE_CASING,2).save(output);

                // Iron
                createMetalFormerRolling(Items.IRON_INGOT, GrowableOresItems.IRON_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.IRON_PLATE, GrowableOresItems.IRON_CASING,2).save(output);

                // Gold
                createMetalFormerRolling(Items.GOLD_INGOT, GrowableOresItems.GOLD_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.GOLD_PLATE, GrowableOresItems.GOLD_CASING,2).save(output);

                // Copper
                createMetalFormerRolling(Items.COPPER_INGOT, GrowableOresItems.COPPER_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.COPPER_PLATE, GrowableOresItems.COPPER_CASING, 2).save(output);

                // Steel
                createMetalFormerRolling(GrowableOresItems.STEEL_INGOT, GrowableOresItems.STEEL_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.STEEL_PLATE, GrowableOresItems.STEEL_CASING,2).save(output);

                // Lead
                createMetalFormerRolling(GrowableOresItems.LEAD_INGOT, GrowableOresItems.LEAD_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.LEAD_PLATE, GrowableOresItems.LEAD_CASING,2).save(output);

                // Tin
                createMetalFormerRolling(GrowableOresItems.TIN_INGOT, GrowableOresItems.TIN_PLATE).save(output);
                createMetalFormerRolling(GrowableOresItems.TIN_PLATE, GrowableOresItems.TIN_CASING,2).save(output);

                createMetalFormerCutting(GrowableOresItems.TIN_PLATE, GrowableOresBlocks.TIN_CABLE,3).save(output);
                createMetalFormerCutting(GrowableOresItems.IRON_PLATE, GrowableOresBlocks.HV_CABLE,4).save(output);
                createMetalFormerCutting(GrowableOresItems.GOLD_PLATE, GrowableOresBlocks.GOLD_CABLE,4).save(output);
                createMetalFormerCutting(GrowableOresItems.COPPER_PLATE, GrowableOresBlocks.COPPER_CABLE,2).save(output);

                createMetalFormerExtruding(Items.COPPER_INGOT, GrowableOresBlocks.COPPER_CABLE,3).save(output);
                createMetalFormerExtruding(GrowableOresItems.TIN_INGOT, GrowableOresBlocks.TIN_CABLE,3).save(output);
                createMetalFormerExtruding(Items.GOLD_INGOT, GrowableOresBlocks.GOLD_CABLE,4).save(output);
                createMetalFormerExtruding(Items.IRON_INGOT, GrowableOresBlocks.HV_CABLE,4).save(output);
                createMetalFormerExtruding(GrowableOresItems.IRIDIUM_PLATE, AdvancedItems.Iridium_INGOT).save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "MetalFormer";
    }
}
