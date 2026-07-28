package com.skniro.industrial_elixir.compat.rei;

import com.skniro.industrial_elixir.compat.rei.category.*;
import com.skniro.industrial_elixir.compat.rei.display.*;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipe;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.*;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;

public class IndustrialModREICommon implements REICommonPlugin {

    @Override
    public String getPluginProviderName() {
        return "IndustrialElixir";
    }

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(MaceratorCraftingRecipe.class)
                .filterType(AlchemyRecipeType.MACERATOR.type.get())
                .fill(MaceratorDisplay::new);
        registry.beginRecipeFiller(CompressorCraftingRecipe.class)
                .filterType(AlchemyRecipeType.COMPRESSOR.type.get())
                .fill(CompressorDisplay::new);
        registry.beginRecipeFiller(MetalFormerExtrudingCraftingRecipe.class)
                .filterType(AlchemyRecipeType.METALFORMER_EXTRUDING.type.get())
                .fill(MetalFormerExtrudingDisplay::new);
        registry.beginRecipeFiller(MetalFormerRollingCraftingRecipe.class)
                .filterType(AlchemyRecipeType.METALFORMER_ROLLING.type.get())
                .fill(MetalFormerRollingDisplay::new);
        registry.beginRecipeFiller(MetalFormerCuttingCraftingRecipe.class)
                .filterType(AlchemyRecipeType.METALFORMER_CUTTING.type.get())
                .fill(MetalFormerCuttingDisplay::new);
        registry.beginRecipeFiller(MolecularTransformerCraftingRecipe.class)
                .filterType(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get())
                .fill(MolecularTransformerDisplay::new);
        registry.beginRecipeFiller(ExtractorCraftingRecipe.class)
                .filterType(AlchemyRecipeType.EXTRACTOR.type.get())
                .fill(ExtractorDisplay::new);
        registry.beginRecipeFiller(AlchemyCraftingRecipe.class)
                .filterType(AlchemyRecipeType.CANE_CONVERTER.type.get())
                .fill(GrowableOresDisplay::new);
        registry.beginRecipeFiller(RecyclerCraftingRecipe.class)
                .filterType(AlchemyRecipeType.RECYCLER.type.get())
                .fill(RecyclerDisplay::new);
        registry.beginRecipeFiller(CuttingCraftingRecipe.class)
                .filterType(AlchemyRecipeType.CUTTING.type.get())
                .fill(BlockCutterDisplay::new);
        registry.beginRecipeFiller(BrewReactorCraftingRecipe.class)
                .filterType(AlchemyRecipeType.BREW_REACTOR.type.get())
                .fill(BrewReactorDisplay::new);
        registry.beginRecipeFiller(HeatCentrifugeCraftingRecipe.class)
                .filterType(AlchemyRecipeType.HEAT_CENTRIFUGE.type.get())
                .fill(HeatCentrifugeDisplay::new);
        registry.beginRecipeFiller(OreWashingCraftingRecipe.class)
                .filterType(AlchemyRecipeType.ORE_WASHING.type.get())
                .fill(OreWashingDisplay::new);
        registry.beginRecipeFiller(ModBlastFurnaceCraftingRecipe.class)
                .filterType(AlchemyRecipeType.MOD_BLAST_FURNACE.type.get())
                .fill(BlastFurnaceDisplay::new);
        registry.beginRecipeFiller(CoffeeMachineCraftingRecipe.class)
                .filterType(AlchemyRecipeType.COFFEE_MACHINE.type.get())
                .fill(CoffeeMachineDisplay::new);
        registry.beginRecipeFiller(CropFarmCraftingRecipe.class)
                .filterType(AlchemyRecipeType.CROP_FARM.type.get())
                .fill(CropFarmDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MaceratorDisplay.MACERATOR.getIdentifier(), MaceratorDisplay.SERIALIZER);
        registry.register(CompressorDisplay.Compressor.getIdentifier(), CompressorDisplay.SERIALIZER);
        registry.register(MetalFormerExtrudingDisplay.MetalFormerExtruding.getIdentifier(), MetalFormerExtrudingDisplay.SERIALIZER);
        registry.register(MetalFormerCuttingDisplay.MetalFormerCutting.getIdentifier(), MetalFormerCuttingDisplay.SERIALIZER);
        registry.register(MetalFormerRollingDisplay.MetalFormerRolling.getIdentifier(), MetalFormerRollingDisplay.SERIALIZER);
        registry.register(MolecularTransformerDisplay.MolecularTransformer.getIdentifier(), MolecularTransformerDisplay.SERIALIZER);
        registry.register(ExtractorDisplay.Extractor.getIdentifier(), ExtractorDisplay.SERIALIZER);
        registry.register(GrowableOresDisplay.GrowableOres.getIdentifier(), GrowableOresDisplay.SERIALIZER);
        registry.register(RecyclerDisplay.Recycler.getIdentifier(), RecyclerDisplay.SERIALIZER);
        registry.register(BlockCutterDisplay.BlockCutter.getIdentifier(), BlockCutterDisplay.SERIALIZER);
        registry.register(BrewReactorDisplay.BREW_REACTOR.getIdentifier(), BrewReactorDisplay.SERIALIZER);
        registry.register(HeatCentrifugeDisplay.HEAT_CENTRIFUGE.getIdentifier(), HeatCentrifugeDisplay.SERIALIZER);
        registry.register(OreWashingDisplay.ORE_WASHING.getIdentifier(), OreWashingDisplay.SERIALIZER);
        registry.register(BlastFurnaceDisplay.BLAST_FURNACE.getIdentifier(), BlastFurnaceDisplay.SERIALIZER);
        registry.register(CoffeeMachineDisplay.COFFEE_MACHINE.getIdentifier(), CoffeeMachineDisplay.SERIALIZER);
        registry.register(CropFarmDisplay.CROP_FARM.getIdentifier(), CropFarmDisplay.SERIALIZER);
    }
}