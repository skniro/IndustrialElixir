package com.skniro.industrial_elixir.compat.rei;

import com.skniro.industrial_elixir.compat.rei.category.*;
import com.skniro.industrial_elixir.compat.rei.display.*;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipe;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.*;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;
import me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore;

@REIPluginCommon
@REIPluginCompatIgnore
public class IndustrialModREICommon implements REIServerPlugin {

    @Override
    public String getPluginProviderName() {
        return "IndustrialElixir";
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MaceratorDisplay.MACERATOR, MaceratorDisplay.SERIALIZER);
        registry.register(CompressorDisplay.Compressor, CompressorDisplay.SERIALIZER);
        registry.register(MetalFormerExtrudingDisplay.MetalFormerExtruding, MetalFormerExtrudingDisplay.SERIALIZER);
        registry.register(MetalFormerCuttingDisplay.MetalFormerCutting, MetalFormerCuttingDisplay.SERIALIZER);
        registry.register(MetalFormerRollingDisplay.MetalFormerRolling, MetalFormerRollingDisplay.SERIALIZER);
        registry.register(MolecularTransformerDisplay.MolecularTransformer, MolecularTransformerDisplay.SERIALIZER);
        registry.register(ExtractorDisplay.Extractor, ExtractorDisplay.SERIALIZER);
        registry.register(GrowableOresDisplay.GrowableOres, GrowableOresDisplay.SERIALIZER);
        registry.register(RecyclerDisplay.Recycler, RecyclerDisplay.SERIALIZER);
        registry.register(BlockCutterDisplay.BlockCutter, BlockCutterDisplay.SERIALIZER);
        registry.register(BrewReactorDisplay.BREW_REACTOR, BrewReactorDisplay.SERIALIZER);
        registry.register(HeatCentrifugeDisplay.HEAT_CENTRIFUGE, HeatCentrifugeDisplay.SERIALIZER);
        registry.register(OreWashingDisplay.ORE_WASHING, OreWashingDisplay.SERIALIZER);
        registry.register(BlastFurnaceDisplay.BLAST_FURNACE, BlastFurnaceDisplay.SERIALIZER);
        registry.register(CoffeeMachineDisplay.COFFEE_MACHINE, CoffeeMachineDisplay.SERIALIZER);
        registry.register(CropFarmDisplay.CROP_FARM, CropFarmDisplay.SERIALIZER);
    }
}
