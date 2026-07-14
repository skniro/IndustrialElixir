package com.skniro.industrial_elixir.datagen.recipe.machine;

import com.skniro.industrial_elixir.api.data.recipe.ModRecipeGenerator;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class MolecularTransformerRecipeGenerator extends FabricRecipeProvider {
    public MolecularTransformerRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected ModRecipeGenerator createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new ModRecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                // Bronze
                createMolecularTransformer(GrowableOresItems.TIN_INGOT, GrowableOresItems.SILVER_INGOT, 500000).save(output);
                createMolecularTransformer(Items.WITHER_SKELETON_SKULL, Items.NETHER_STAR,250000000).save(output);
                createMolecularTransformer(Items.NETHERRACK, Items.GUNPOWDER, 2,70000).save(output);
                createMolecularTransformer(Items.GLOWSTONE_DUST, AdvancedItems.LUMINITE_Part,1000000).save(output);
                createMolecularTransformer(Items.IRON_INGOT, GrowableOresItems.IRIDIUM_ORE,9000000).save(output);
                createMolecularTransformer(Items.RED_WOOL, Items.REDSTONE_BLOCK,500000).save(output);
                createMolecularTransformer(Items.BLUE_WOOL, Items.LAPIS_BLOCK,500000).save(output);
                createMolecularTransformer(GrowableOresItems.INDUSTRIAL_DIAMOND, Items.DIAMOND,1000000).save(output);
                createMolecularTransformer(Items.CHARCOAL, Items.COAL,60000).save(output);
                createMolecularTransformer(Items.COAL, GrowableOresItems.INDUSTRIAL_DIAMOND,9000000).save(output);
                createMolecularTransformer(GrowableOresItems.SILVER_INGOT, Items.GOLD_INGOT,500000).save(output);
                createMolecularTransformer(Items.DIRT, Items.CLAY,50000).save(output);
                createMolecularTransformer(Items.SAND, Items.GRAVEL,50000).save(output);
                createMolecularTransformer(Items.YELLOW_WOOL, Items.GLOWSTONE,500000).save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "MolecularTransformer";
    }
}
