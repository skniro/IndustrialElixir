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
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

@JeiPlugin
@REIPluginCompatIgnore
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
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MACERATOR.type.get()))
            );

            registration.addRecipes(
                    CompressorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COMPRESSOR.type.get()))
            );

            registration.addRecipes(
                    MetalFormerRollingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_ROLLING.type.get()))
            );

            registration.addRecipes(
                    MetalFormerCuttingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_CUTTING.type.get()))
            );

            registration.addRecipes(
                    MetalFormerExtrudingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_EXTRUDING.type.get()))
            );

            registration.addRecipes(
                    MolecularTransformerCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get()))
            );

            registration.addRecipes(
                    ExtractorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.EXTRACTOR.type.get()))
            );

            registration.addRecipes(
                    GrowableOresCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CANE_CONVERTER.type.get()))
            );

            registration.addRecipes(
                    RecyclerCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.RECYCLER.type.get()))
            );

            registration.addRecipes(
                    BlockCutterCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CUTTING.type.get()))
            );

            registration.addRecipes(
                    BrewReactorCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.BREW_REACTOR.type.get()))
            );

            registration.addRecipes(
                    HeatCentrifugeCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.HEAT_CENTRIFUGE.type.get()))
            );

            registration.addRecipes(
                    OreWashingCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.ORE_WASHING.type.get()))
            );

            registration.addRecipes(
                    BlastFurnaceCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOD_BLAST_FURNACE.type.get()))
            );

            registration.addRecipes(
                    CoffeeMachineCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COFFEE_MACHINE.type.get()))
            );

            registration.addRecipes(
                    CropFarmCategory.TYPE,
                    new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CROP_FARM.type.get()))
            );
        } else {
            IndustrialElixir.LOGGER.info("JEI recipe registration: recipeMap not available yet; deferring recipes until sync event.");
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.Iron_Furnace_Block.get()));
        registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(GrowableOresBlocks.INDUCTION_FURNACE.get()));
        registration.addCraftingStation(MaceratorCategory.TYPE, new ItemStack(GrowableOresBlocks.Macerator_Block.get()));
        registration.addCraftingStation(CompressorCategory.TYPE, new ItemStack(GrowableOresBlocks.Compressor_Block.get()));
        registration.addCraftingStation(MetalFormerRollingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock.get()));
        registration.addCraftingStation(MetalFormerCuttingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock.get()));
        registration.addCraftingStation(MetalFormerExtrudingCategory.TYPE, new ItemStack(GrowableOresBlocks.MetalFormerBlock.get()));
        registration.addCraftingStation(ExtractorCategory.TYPE, new ItemStack(GrowableOresBlocks.Extractor_Block.get()));
        registration.addCraftingStation(MolecularTransformerCategory.TYPE, new ItemStack(GrowableOresBlocks.MolecularTransformerBlock.get()));
        registration.addCraftingStation(GrowableOresCategory.TYPE, new ItemStack(GrowableOresBlocks.GrowableOres_Block.get()));
        registration.addCraftingStation(RecyclerCategory.TYPE, new ItemStack(GrowableOresBlocks.RECYCLER_Block.get()));
        registration.addCraftingStation(BlockCutterCategory.TYPE, new ItemStack(GrowableOresBlocks.CUTTING_Block.get()));
        registration.addCraftingStation(BrewReactorCategory.TYPE, new ItemStack(GrowableOresBlocks.Brew_Reactor_BLOCK.get()));
        registration.addCraftingStation(HeatCentrifugeCategory.TYPE, new ItemStack(GrowableOresBlocks.HEAT_CENTRIFUGE.get()));
        registration.addCraftingStation(OreWashingCategory.TYPE, new ItemStack(GrowableOresBlocks.Ore_Washing_Block.get()));
        registration.addCraftingStation(BlastFurnaceCategory.TYPE, new ItemStack(GrowableOresBlocks.BLAST_FURNACE_BLOCK.get()));
        registration.addCraftingStation(CoffeeMachineCategory.TYPE, new ItemStack(GrowableOresBlocks.COFFEE_MACHINE_Block.get()));
        registration.addCraftingStation(CropFarmCategory.TYPE, new ItemStack(GrowableOresBlocks.CROP_FARM_Block.get()));

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
                rm.addRecipes(MaceratorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MACERATOR.type.get())));
                rm.addRecipes(CompressorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COMPRESSOR.type.get())));
                rm.addRecipes(MetalFormerRollingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_ROLLING.type.get())));
                rm.addRecipes(MetalFormerCuttingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_CUTTING.type.get())));
                rm.addRecipes(MetalFormerExtrudingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.METALFORMER_EXTRUDING.type.get())));
                rm.addRecipes(MolecularTransformerCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get())));
                rm.addRecipes(ExtractorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.EXTRACTOR.type.get())));
                rm.addRecipes(GrowableOresCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CANE_CONVERTER.type.get())));
                rm.addRecipes(RecyclerCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.RECYCLER.type.get())));
                rm.addRecipes(BlockCutterCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CUTTING.type.get())));
                rm.addRecipes(BrewReactorCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.BREW_REACTOR.type.get())));
                rm.addRecipes(HeatCentrifugeCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.HEAT_CENTRIFUGE.type.get())));
                rm.addRecipes(OreWashingCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.ORE_WASHING.type.get())));
                rm.addRecipes(BlastFurnaceCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.MOD_BLAST_FURNACE.type.get())));
                rm.addRecipes(CoffeeMachineCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.COFFEE_MACHINE.type.get())));
                rm.addRecipes(CropFarmCategory.TYPE, new ArrayList<>(recipeMap.getAllOfType(AlchemyRecipeType.CROP_FARM.type.get())));
            } catch (Throwable t) {
                IndustrialElixir.LOGGER.warn("Unable to push recipes into JEI runtime: {}", t.toString());
            }
        } catch (Throwable t) {
            IndustrialElixir.LOGGER.warn("Error getting JEI recipe manager: {}", t.toString());
        }
    }
}
