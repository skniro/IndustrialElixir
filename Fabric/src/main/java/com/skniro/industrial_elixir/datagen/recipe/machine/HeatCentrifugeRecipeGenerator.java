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

public class HeatCentrifugeRecipeGenerator extends FabricRecipeProvider {
    public HeatCentrifugeRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                heatCentrifuge(GrowableOresItems.CRUSHED_COPPER, GrowableOresItems.COPPER_DUST, GrowableOresItems.SMALL_TIN_DUST, GrowableOresItems.STONE_DUST, "crushed_copper_to_purified_copper");
                heatCentrifuge(GrowableOresItems.CRUSHED_GOLD, GrowableOresItems.GOLD_DUST, GrowableOresItems.SMALL_SILVER_DUST, GrowableOresItems.STONE_DUST,"crushed_gold_to_purified_gold");
                heatCentrifuge(GrowableOresItems.CRUSHED_IRON, GrowableOresItems.IRON_DUST, GrowableOresItems.SMALL_GOLD_DUST, GrowableOresItems.STONE_DUST,"crushed_iron_to_purified_iron");
                heatCentrifuge(GrowableOresItems.CRUSHED_LEAD, GrowableOresItems.LEAD_DUST, GrowableOresItems.SMALL_COPPER_DUST, GrowableOresItems.STONE_DUST,"crushed_lead_to_purified_lead");
                heatCentrifuge(GrowableOresItems.CRUSHED_SILVER, GrowableOresItems.SILVER_DUST, GrowableOresItems.SMALL_SILVER_DUST,GrowableOresItems.STONE_DUST, "crushed_silver_to_purified_silver");
                heatCentrifuge(GrowableOresItems.CRUSHED_TIN, GrowableOresItems.TIN_DUST, GrowableOresItems.SMALL_IRON_DUST, GrowableOresItems.STONE_DUST,"crushed_tin_to_purified_tin");
                heatCentrifuge(GrowableOresItems.CRUSHED_SACRED, GrowableOresItems.IMPURE_SACRED_STONE, GrowableOresItems.STONE_DUST,null, "crushed_septrin_to_purified_septrin");
                heatCentrifuge(GrowableOresItems.PURIFIED_COPPER, GrowableOresItems.COPPER_DUST, GrowableOresItems.SMALL_TIN_DUST, GrowableOresItems.STONE_DUST, "purified_copper_to_purified_copper");
                heatCentrifuge(GrowableOresItems.PURIFIED_GOLD, GrowableOresItems.GOLD_DUST, GrowableOresItems.SMALL_SILVER_DUST, GrowableOresItems.STONE_DUST,"purified_gold_to_purified_gold");
                heatCentrifuge(GrowableOresItems.PURIFIED_IRON, GrowableOresItems.IRON_DUST, GrowableOresItems.SMALL_GOLD_DUST, GrowableOresItems.STONE_DUST,"purified_iron_to_purified_iron");
                heatCentrifuge(GrowableOresItems.PURIFIED_LEAD, GrowableOresItems.LEAD_DUST, GrowableOresItems.SMALL_COPPER_DUST, GrowableOresItems.STONE_DUST,"purified_lead_to_purified_lead");
                heatCentrifuge(GrowableOresItems.PURIFIED_SILVER, GrowableOresItems.SILVER_DUST, GrowableOresItems.SMALL_SILVER_DUST,GrowableOresItems.STONE_DUST, "purified_silver_to_purified_silver");
                heatCentrifuge(GrowableOresItems.PURIFIED_TIN, GrowableOresItems.TIN_DUST, GrowableOresItems.SMALL_IRON_DUST, GrowableOresItems.STONE_DUST,"purified_tin_to_purified_tin");
                heatCentrifuge(GrowableOresItems.PURIFIED_SACRED, GrowableOresItems.IMPURE_SACRED_STONE, GrowableOresItems.STONE_DUST,null, "purified_septrin_to_purified_septrin");
                createHeatCentrifuge(GrowableOresItems.COAL_DUST,5).input(GrowableOresItems.SLAG).output2(GrowableOresItems.SMALL_GOLD_DUST).save(output, "slag_to_small_gold_dust");
                createHeatCentrifuge(GrowableOresItems.SILICON_DIOXIDE_DUST,5).input(GrowableOresItems.CLAY_DUST,4).save(output, "clay_dust_to_silicon_dioxide_dust");
            }

            private void heatCentrifuge(ItemLike input, ItemLike primaryOutput, Item secondaryOutput, Item thirdOutput, String name) {
                var builder = createHeatCentrifuge(primaryOutput)
                        .input(input)
                        .unlockedBy("has_base_item", has(input));

                if (secondaryOutput != null) {
                    builder.output2(secondaryOutput);
                }

                if (thirdOutput != null) {
                    builder.output3(thirdOutput);
                }

                builder.save(output, name);
            }
        };
    }

    @Override
    public String getName() {
        return "heatCentrifuge";
    }
}
