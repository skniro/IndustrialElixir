package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class OreWashingRecipeGenerator extends FabricRecipeProvider {
    public OreWashingRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                oreWashing(GrowableOresItems.CRUSHED_COPPER, GrowableOresItems.PURIFIED_COPPER, GrowableOresItems.SMALL_COPPER_DUST, "crushed_copper_to_purified_copper");
                oreWashing(GrowableOresItems.CRUSHED_GOLD, GrowableOresItems.PURIFIED_GOLD, GrowableOresItems.SMALL_GOLD_DUST, "crushed_gold_to_purified_gold");
                oreWashing(GrowableOresItems.CRUSHED_IRON, GrowableOresItems.PURIFIED_IRON, GrowableOresItems.SMALL_IRON_DUST, "crushed_iron_to_purified_iron");
                oreWashing(GrowableOresItems.CRUSHED_LEAD, GrowableOresItems.PURIFIED_LEAD, GrowableOresItems.SMALL_LEAD_DUST, "crushed_lead_to_purified_lead");
                oreWashing(GrowableOresItems.CRUSHED_SILVER, GrowableOresItems.PURIFIED_SILVER, GrowableOresItems.SMALL_SILVER_DUST, "crushed_silver_to_purified_silver");
                oreWashing(GrowableOresItems.CRUSHED_TIN, GrowableOresItems.PURIFIED_TIN, GrowableOresItems.SMALL_TIN_DUST, "crushed_tin_to_purified_tin");
                oreWashing(GrowableOresItems.CRUSHED_SACRED, GrowableOresItems.PURIFIED_SACRED, null, "crushed_septrin_to_purified_septrin");
            }

            private void oreWashing(ItemLike input, ItemLike primaryOutput, Item secondaryOutput, String name) {
                var builder = createOreWashing(primaryOutput)
                        .fluid(Fluids.WATER, 1000)
                        .input(input)
                        .output3(GrowableOresItems.STONE_DUST)
                        .unlockedBy("has_base_item", has(input));

                if (secondaryOutput != null) {
                    builder.output2(secondaryOutput);
                }

                builder.save(output, name);
            }
        };
    }

    @Override
    public String getName() {
        return "OreWashing";
    }
}
