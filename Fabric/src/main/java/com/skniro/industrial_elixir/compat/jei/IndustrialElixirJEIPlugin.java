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
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;

public class IndustrialElixirJEIPlugin implements IModPlugin {
    public static SynchronizedRecipes recipeMap = null;
    private static IJeiRuntime jeiRuntime = null;

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID,"jei_plugin");
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
        if (recipeMap != null) {
            registration.addRecipes(
                    MaceratorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MACERATOR.type))
            );

            registration.addRecipes(
                    CompressorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COMPRESSOR.type))
            );

            registration.addRecipes(
                    MetalFormerRollingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_ROLLING.type))
            );

            registration.addRecipes(
                    MetalFormerCuttingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_CUTTING.type))
            );

            registration.addRecipes(
                    MetalFormerExtrudingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_EXTRUDING.type))
            );

            registration.addRecipes(
                    MolecularTransformerCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type))
            );

            registration.addRecipes(
                    ExtractorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.EXTRACTOR.type))
            );

            registration.addRecipes(
                    GrowableOresCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CANE_CONVERTER.type))
            );

            registration.addRecipes(
                    RecyclerCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.RECYCLER.type))
            );

            registration.addRecipes(
                    BlockCutterCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CUTTING.type))
            );

            registration.addRecipes(
                    BrewReactorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.BREW_REACTOR.type))
            );

            registration.addRecipes(
                    HeatCentrifugeCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.HEAT_CENTRIFUGE.type))
            );

            registration.addRecipes(
                    OreWashingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.ORE_WASHING.type))
            );

            registration.addRecipes(
                    BlastFurnaceCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOD_BLAST_FURNACE.type))
            );

            registration.addRecipes(
                    CoffeeMachineCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COFFEE_MACHINE.type))
            );

            registration.addRecipes(
                    CropFarmCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CROP_FARM.type))
            );
        } else {
            IndustrialElixir.LOGGER.info("JEI recipe registration: recipeMap not available yet; deferring recipes until sync event.");
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.Iron_Furnace_Block));
        registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.INDUCTION_FURNACE));
        registration.addCraftingStation(MaceratorCategory.TYPE, new ItemStack(GrowableOresBlocks.Macerator_Block));
        registration.addCraftingStation(CompressorCategory.TYPE, new ItemStack(GrowableOresBlocks.Compressor_Block));
        registration.addCraftingStation(MetalFormerRollingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock));
        registration.addCraftingStation(MetalFormerCuttingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock));
        registration.addCraftingStation(MetalFormerExtrudingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock));
        registration.addCraftingStation(ExtractorCategory.TYPE, new ItemStack(GrowableOresBlocks.Extractor_Block));
        registration.addCraftingStation(MolecularTransformerCategory.TYPE, new ItemStack(GrowableOresBlocks.MolecularTransformerBlock));
        registration.addCraftingStation(GrowableOresCategory.TYPE, new ItemStack(GrowableOresBlocks.GrowableOres_Block));
        registration.addCraftingStation(RecyclerCategory.TYPE, new ItemStack(GrowableOresBlocks.RECYCLER_Block));
        registration.addCraftingStation(BlockCutterCategory.TYPE, new ItemStack(GrowableOresBlocks.CUTTING_Block));
        registration.addCraftingStation(BrewReactorCategory.TYPE, new ItemStack(GrowableOresBlocks.Brew_Reactor_BLOCK));
        registration.addCraftingStation(HeatCentrifugeCategory.TYPE, new ItemStack(GrowableOresBlocks.HEAT_CENTRIFUGE));
        registration.addCraftingStation(OreWashingCategory.TYPE, new ItemStack(GrowableOresBlocks.Ore_Washing_Block));
        registration.addCraftingStation(BlastFurnaceCategory.TYPE, new ItemStack(GrowableOresBlocks.BLAST_FURNACE_BLOCK));
        registration.addCraftingStation(CoffeeMachineCategory.TYPE, new ItemStack(GrowableOresBlocks.COFFEE_MACHINE_Block));
        registration.addCraftingStation(CropFarmCategory.TYPE, new ItemStack(GrowableOresBlocks.CROP_FARM_Block));

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
        if (recipeMap != null) {
            // push recipes into JEI runtime if synchronized recipes already present
            pushRecipesToJei();
        }
    }

    public static void pushRecipesToJei() {
        if (jeiRuntime == null || recipeMap == null) return;

        try {
            var rm = jeiRuntime.getRecipeManager();
            try {
                rm.addRecipes(MaceratorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MACERATOR.type)));
                rm.addRecipes(CompressorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COMPRESSOR.type)));
                rm.addRecipes(MetalFormerRollingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_ROLLING.type)));
                rm.addRecipes(MetalFormerCuttingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_CUTTING.type)));
                rm.addRecipes(MetalFormerExtrudingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_EXTRUDING.type)));
                rm.addRecipes(MolecularTransformerCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type)));
                rm.addRecipes(ExtractorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.EXTRACTOR.type)));
                rm.addRecipes(GrowableOresCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CANE_CONVERTER.type)));
                rm.addRecipes(RecyclerCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.RECYCLER.type)));
                rm.addRecipes(BlockCutterCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CUTTING.type)));
                rm.addRecipes(BrewReactorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.BREW_REACTOR.type)));
                rm.addRecipes(HeatCentrifugeCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.HEAT_CENTRIFUGE.type)));
                rm.addRecipes(OreWashingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.ORE_WASHING.type)));
                rm.addRecipes(BlastFurnaceCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOD_BLAST_FURNACE.type)));
                rm.addRecipes(CoffeeMachineCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COFFEE_MACHINE.type)));
                rm.addRecipes(CropFarmCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CROP_FARM.type)));
            } catch (Throwable t) {
                IndustrialElixir.LOGGER.warn("Unable to push recipes into JEI runtime: {}", t.toString());
            }
        } catch (Throwable t) {
            IndustrialElixir.LOGGER.warn("Error getting JEI recipe manager: {}", t.toString());
        }
    }
}
