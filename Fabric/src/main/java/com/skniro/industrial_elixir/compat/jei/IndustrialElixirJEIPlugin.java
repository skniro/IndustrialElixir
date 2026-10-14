package com.skniro.industrial_elixir.compat.jei;

import com.skniro.growableoresir.GrowableOres;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.jei.category.*;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.MaceratorCraftingRecipe;
import com.skniro.industrial_elixir.screen.ingame.machine.*;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.BrewReactorScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.CoffeeMachineScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.OreWashingScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.heat.ModBlastFurnaceScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;

public class IndustrialElixirJEIPlugin implements IModPlugin {
    private static IJeiRuntime jeiRuntime = null;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID,"jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new MaceratorCategory(registration.getJeiHelpers().getGuiHelper()),
                new CompressorCategory(registration.getJeiHelpers().getGuiHelper()),
                new MetalFormerRollingCategory(registration.getJeiHelpers().getGuiHelper()),
                new MetalFormerCuttingCategory(registration.getJeiHelpers().getGuiHelper()),
                new MetalFormerExtrudingCategory(registration.getJeiHelpers().getGuiHelper()),
                new MolecularTransformerCategory(registration.getJeiHelpers().getGuiHelper()),
                new ExtractorCategory(registration.getJeiHelpers().getGuiHelper()),
                new GrowableOresCategory(registration.getJeiHelpers().getGuiHelper()),
                new RecyclerCategory(registration.getJeiHelpers().getGuiHelper()),
                new BlockCutterCategory(registration.getJeiHelpers().getGuiHelper()),
                new BrewReactorCategory(registration.getJeiHelpers().getGuiHelper()),
                new HeatCentrifugeCategory(registration.getJeiHelpers().getGuiHelper()),
                new OreWashingCategory(registration.getJeiHelpers().getGuiHelper()),
                new BlastFurnaceCategory(registration.getJeiHelpers().getGuiHelper()),
                new CoffeeMachineCategory(registration.getJeiHelpers().getGuiHelper()),
                new CropFarmCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = getClientRecipeManager();
        if (recipeManager == null) {
            IndustrialElixir.LOGGER.info("JEI recipe registration: client level is not available yet; deferring recipes until the JEI runtime is ready.");
            return;
        }

        registration.addRecipes(
                MaceratorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MACERATOR.type))
        );

        registration.addRecipes(
                CompressorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COMPRESSOR.type))
        );

        registration.addRecipes(
                MetalFormerRollingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_ROLLING.type))
        );

        registration.addRecipes(
                MetalFormerCuttingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_CUTTING.type))
        );

        registration.addRecipes(
                MetalFormerExtrudingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_EXTRUDING.type))
        );

        registration.addRecipes(
                MolecularTransformerCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type))
        );

        registration.addRecipes(
                ExtractorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.EXTRACTOR.type))
        );

        registration.addRecipes(
                GrowableOresCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CANE_CONVERTER.type))
        );

        registration.addRecipes(
                RecyclerCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.RECYCLER.type))
        );

        registration.addRecipes(
                BlockCutterCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CUTTING.type))
        );

        registration.addRecipes(
                BrewReactorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.BREW_REACTOR.type))
        );

        registration.addRecipes(
                HeatCentrifugeCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.HEAT_CENTRIFUGE.type))
        );

        registration.addRecipes(
                OreWashingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.ORE_WASHING.type))
        );

        registration.addRecipes(
                BlastFurnaceCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOD_BLAST_FURNACE.type))
        );

        registration.addRecipes(
                CoffeeMachineCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COFFEE_MACHINE.type))
        );

        registration.addRecipes(
                CropFarmCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CROP_FARM.type))
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.Iron_Furnace_Block));
        registration.addRecipeCatalysts(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.INDUCTION_FURNACE));
        registration.addRecipeCatalysts(MaceratorCategory.TYPE, new ItemStack(GrowableOresBlocks.Macerator_Block));
        registration.addRecipeCatalysts(CompressorCategory.TYPE, new ItemStack(GrowableOresBlocks.Compressor_Block));
        registration.addRecipeCatalysts(MetalFormerRollingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock));
        registration.addRecipeCatalysts(MetalFormerCuttingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock));
        registration.addRecipeCatalysts(MetalFormerExtrudingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock));
        registration.addRecipeCatalysts(ExtractorCategory.TYPE, new ItemStack(GrowableOresBlocks.Extractor_Block));
        registration.addRecipeCatalysts(MolecularTransformerCategory.TYPE, new ItemStack(GrowableOresBlocks.MolecularTransformerBlock));
        registration.addRecipeCatalysts(GrowableOresCategory.TYPE, new ItemStack(GrowableOresBlocks.GrowableOres_Block));
        registration.addRecipeCatalysts(RecyclerCategory.TYPE, new ItemStack(GrowableOresBlocks.RECYCLER_Block));
        registration.addRecipeCatalysts(BlockCutterCategory.TYPE, new ItemStack(GrowableOresBlocks.CUTTING_Block));
        registration.addRecipeCatalysts(BrewReactorCategory.TYPE, new ItemStack(GrowableOresBlocks.Brew_Reactor_BLOCK));
        registration.addRecipeCatalysts(HeatCentrifugeCategory.TYPE, new ItemStack(GrowableOresBlocks.HEAT_CENTRIFUGE));
        registration.addRecipeCatalysts(OreWashingCategory.TYPE, new ItemStack(GrowableOresBlocks.Ore_Washing_Block));
        registration.addRecipeCatalysts(BlastFurnaceCategory.TYPE, new ItemStack(GrowableOresBlocks.BLAST_FURNACE_BLOCK));
        registration.addRecipeCatalysts(CoffeeMachineCategory.TYPE, new ItemStack(GrowableOresBlocks.COFFEE_MACHINE_Block));
        registration.addRecipeCatalysts(CropFarmCategory.TYPE, new ItemStack(GrowableOresBlocks.CROP_FARM_Block));

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MaceratorBlockScreen.class, 76, 35, 23, 12, MaceratorCategory.TYPE);
        registration.addRecipeClickArea(CompressorBlockScreen.class, 72, 33, 23, 14, CompressorCategory.TYPE);
        registration.addRecipeClickArea(MetalFormerBlockScreen.class, 70, 34, 48, 12, MetalFormerRollingCategory.TYPE);
        registration.addRecipeClickArea(MetalFormerBlockScreen.class, 70, 34, 48, 12, MetalFormerCuttingCategory.TYPE);
        registration.addRecipeClickArea(MetalFormerBlockScreen.class, 70, 34, 48, 12, MetalFormerExtrudingCategory.TYPE);
        registration.addRecipeClickArea(MolecularTransformerBlockScreen.class, 74, 37, 23, 12, MolecularTransformerCategory.TYPE);
        registration.addRecipeClickArea(ExtractorBlockScreen.class, 75, 35, 23, 14, ExtractorCategory.TYPE);
        registration.addRecipeClickArea(AlchemyBlockScreen.class, 73, 34, 28, 14, GrowableOresCategory.TYPE);
        registration.addRecipeClickArea(RecyclerBlockScreen.class, 72, 34, 23, 16, RecyclerCategory.TYPE);
        registration.addRecipeClickArea(CuttingBlockScreen.class, 70, 33, 48, 19, BlockCutterCategory.TYPE);
        registration.addRecipeClickArea(BrewReactorScreen.class, 79, 32, 11, 16, BrewReactorCategory.TYPE);
        registration.addRecipeClickArea(ElectricFurnaceBlockScreen.class, 71, 34, 22, 15, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(InductionFurnaceBlockScreen.class, 71, 34, 21, 15, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(HeatCentrifugeBlockScreen.class, 79, 32, 21, 16, HeatCentrifugeCategory.TYPE);
        registration.addRecipeClickArea(OreWashingScreen.class, 79, 32, 11, 16, OreWashingCategory.TYPE);
        registration.addRecipeClickArea(ModBlastFurnaceScreen.class, 73, 34, 21, 16, BlastFurnaceCategory.TYPE);
        registration.addRecipeClickArea(CoffeeMachineScreen.class, 70, 34, 24, 16, CoffeeMachineCategory.TYPE);
        registration.addRecipeClickArea(CropFarmBlockScreen.class, 70, 34, 24, 16, CropFarmCategory.TYPE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
        // push the recipes that are already synchronized to the client into the JEI runtime
        pushRecipesToJei();
    }

    private static RecipeManager getClientRecipeManager() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.level == null ? null : minecraft.level.getRecipeManager();
    }

    public static void pushRecipesToJei() {
        if (jeiRuntime == null) return;

        RecipeManager recipeManager = getClientRecipeManager();
        if (recipeManager == null) return;

        try {
            var rm = jeiRuntime.getRecipeManager();
            try {
                rm.addRecipes(
                        MaceratorCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MACERATOR.type))
                );

                rm.addRecipes(
                        CompressorCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COMPRESSOR.type))
                );

                rm.addRecipes(
                        MetalFormerRollingCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_ROLLING.type))
                );

                rm.addRecipes(
                        MetalFormerCuttingCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_CUTTING.type))
                );

                rm.addRecipes(
                        MetalFormerExtrudingCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_EXTRUDING.type))
                );

                rm.addRecipes(
                        MolecularTransformerCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type))
                );

                rm.addRecipes(
                        ExtractorCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.EXTRACTOR.type))
                );

                rm.addRecipes(
                        GrowableOresCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CANE_CONVERTER.type))
                );

                rm.addRecipes(
                        RecyclerCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.RECYCLER.type))
                );

                rm.addRecipes(
                        BlockCutterCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CUTTING.type))
                );

                rm.addRecipes(
                        BrewReactorCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.BREW_REACTOR.type))
                );

                rm.addRecipes(
                        HeatCentrifugeCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.HEAT_CENTRIFUGE.type))
                );

                rm.addRecipes(
                        OreWashingCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.ORE_WASHING.type))
                );

                rm.addRecipes(
                        BlastFurnaceCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOD_BLAST_FURNACE.type))
                );

                rm.addRecipes(
                        CoffeeMachineCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COFFEE_MACHINE.type))
                );

                rm.addRecipes(
                        CropFarmCategory.TYPE,
                        new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CROP_FARM.type))
                );
            } catch (Throwable t) {
                IndustrialElixir.LOGGER.warn("Unable to push recipes into JEI runtime: {}", t.toString());
            }
        } catch (Throwable t) {
            IndustrialElixir.LOGGER.warn("Error getting JEI recipe manager: {}", t.toString());
        }
    }
}
