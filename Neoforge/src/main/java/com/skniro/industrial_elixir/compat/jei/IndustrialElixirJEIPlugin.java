package com.skniro.industrial_elixir.compat.jei;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.jei.category.*;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.ingame.machine.*;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.BrewReactorScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.CoffeeMachineScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.OreWashingScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.heat.ModBlastFurnaceScreen;
import me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;

@JeiPlugin
@REIPluginCompatIgnore
public class IndustrialElixirJEIPlugin implements IModPlugin {
    /**
     * Set from the client recipe-sync event; kept as a hook so the plugin can hand the
     * synchronised recipes to JEI without going through the client level again.
     */
    public static RecipeManager recipeMap = null;
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
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MACERATOR.type.get()))
        );

        registration.addRecipes(
                CompressorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COMPRESSOR.type.get()))
        );

        registration.addRecipes(
                MetalFormerRollingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_ROLLING.type.get()))
        );

        registration.addRecipes(
                MetalFormerCuttingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_CUTTING.type.get()))
        );

        registration.addRecipes(
                MetalFormerExtrudingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_EXTRUDING.type.get()))
        );

        registration.addRecipes(
                MolecularTransformerCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get()))
        );

        registration.addRecipes(
                ExtractorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.EXTRACTOR.type.get()))
        );

        registration.addRecipes(
                GrowableOresCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CANE_CONVERTER.type.get()))
        );

        registration.addRecipes(
                RecyclerCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.RECYCLER.type.get()))
        );

        registration.addRecipes(
                BlockCutterCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CUTTING.type.get()))
        );

        registration.addRecipes(
                BrewReactorCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.BREW_REACTOR.type.get()))
        );

        registration.addRecipes(
                HeatCentrifugeCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.HEAT_CENTRIFUGE.type.get()))
        );

        registration.addRecipes(
                OreWashingCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.ORE_WASHING.type.get()))
        );

        registration.addRecipes(
                BlastFurnaceCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOD_BLAST_FURNACE.type.get()))
        );

        registration.addRecipes(
                CoffeeMachineCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COFFEE_MACHINE.type.get()))
        );

        registration.addRecipes(
                CropFarmCategory.TYPE,
                new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CROP_FARM.type.get()))
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.Iron_Furnace_Block.get()));
        registration.addRecipeCatalysts(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.INDUCTION_FURNACE.get()));
        registration.addRecipeCatalysts(MaceratorCategory.TYPE, new ItemStack(GrowableOresBlocks.Macerator_Block.get()));
        registration.addRecipeCatalysts(CompressorCategory.TYPE, new ItemStack(GrowableOresBlocks.Compressor_Block.get()));
        registration.addRecipeCatalysts(MetalFormerRollingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock.get()));
        registration.addRecipeCatalysts(MetalFormerCuttingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock.get()));
        registration.addRecipeCatalysts(MetalFormerExtrudingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock.get()));
        registration.addRecipeCatalysts(ExtractorCategory.TYPE, new ItemStack(GrowableOresBlocks.Extractor_Block.get()));
        registration.addRecipeCatalysts(MolecularTransformerCategory.TYPE, new ItemStack(GrowableOresBlocks.MolecularTransformerBlock.get()));
        registration.addRecipeCatalysts(GrowableOresCategory.TYPE, new ItemStack(GrowableOresBlocks.GrowableOres_Block.get()));
        registration.addRecipeCatalysts(RecyclerCategory.TYPE, new ItemStack(GrowableOresBlocks.RECYCLER_Block.get()));
        registration.addRecipeCatalysts(BlockCutterCategory.TYPE, new ItemStack(GrowableOresBlocks.CUTTING_Block.get()));
        registration.addRecipeCatalysts(BrewReactorCategory.TYPE, new ItemStack(GrowableOresBlocks.Brew_Reactor_BLOCK.get()));
        registration.addRecipeCatalysts(HeatCentrifugeCategory.TYPE, new ItemStack(GrowableOresBlocks.HEAT_CENTRIFUGE.get()));
        registration.addRecipeCatalysts(OreWashingCategory.TYPE, new ItemStack(GrowableOresBlocks.Ore_Washing_Block.get()));
        registration.addRecipeCatalysts(BlastFurnaceCategory.TYPE, new ItemStack(GrowableOresBlocks.BLAST_FURNACE_BLOCK.get()));
        registration.addRecipeCatalysts(CoffeeMachineCategory.TYPE, new ItemStack(GrowableOresBlocks.COFFEE_MACHINE_Block.get()));
        registration.addRecipeCatalysts(CropFarmCategory.TYPE, new ItemStack(GrowableOresBlocks.CROP_FARM_Block.get()));

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
        if (recipeMap != null) {
            return recipeMap;
        }
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
                rm.addRecipes(MaceratorCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MACERATOR.type.get())));
                rm.addRecipes(CompressorCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COMPRESSOR.type.get())));
                rm.addRecipes(MetalFormerRollingCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_ROLLING.type.get())));
                rm.addRecipes(MetalFormerCuttingCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_CUTTING.type.get())));
                rm.addRecipes(MetalFormerExtrudingCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.METALFORMER_EXTRUDING.type.get())));
                rm.addRecipes(MolecularTransformerCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get())));
                rm.addRecipes(ExtractorCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.EXTRACTOR.type.get())));
                rm.addRecipes(GrowableOresCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CANE_CONVERTER.type.get())));
                rm.addRecipes(RecyclerCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.RECYCLER.type.get())));
                rm.addRecipes(BlockCutterCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CUTTING.type.get())));
                rm.addRecipes(BrewReactorCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.BREW_REACTOR.type.get())));
                rm.addRecipes(HeatCentrifugeCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.HEAT_CENTRIFUGE.type.get())));
                rm.addRecipes(OreWashingCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.ORE_WASHING.type.get())));
                rm.addRecipes(BlastFurnaceCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.MOD_BLAST_FURNACE.type.get())));
                rm.addRecipes(CoffeeMachineCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.COFFEE_MACHINE.type.get())));
                rm.addRecipes(CropFarmCategory.TYPE, new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyRecipeType.CROP_FARM.type.get())));
            } catch (Throwable t) {
                IndustrialElixir.LOGGER.warn("Unable to push recipes into JEI runtime: {}", t.toString());
            }
        } catch (Throwable t) {
            IndustrialElixir.LOGGER.warn("Error getting JEI recipe manager: {}", t.toString());
        }
    }
}
