package com.skniro.industrial_elixir.compat.rei;


import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.rei.category.*;
import com.skniro.industrial_elixir.compat.rei.display.*;
import com.skniro.industrial_elixir.screen.ingame.machine.*;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.BrewReactorScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.OreWashingScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.heat.ModBlastFurnaceScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

public class IndustrialModREIClient implements REIClientPlugin {

    @Override
    public String getPluginProviderName() {
        return "IndustrialElixirClient";
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MaceratorCategory());
        registry.add(new CompressorCategory());
        registry.add(new MetalFormerRollingCategory());
        registry.add(new MetalFormerCuttingCategory());
        registry.add(new MetalFormerExtrudingCategory());
        registry.add(new MolecularTransformerCategory());
        registry.add(new ExtractorCategory());
        registry.add(new GrowableOresCategory());
        registry.add(new RecyclerCategory());
        registry.add(new BlockCutterCategory());
        registry.add(new BrewReactorCategory());
        registry.add(new HeatCentrifugeCategory());
        registry.add(new OreWashingCategory());
        registry.add(new BlastFurnaceCategory());
        registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(GrowableOresBlocks.ElectricFurnace_Block));
        registry.addWorkstations(MaceratorDisplay.MACERATOR, EntryStacks.of(GrowableOresBlocks.Macerator_Block));
        registry.addWorkstations(CompressorDisplay.Compressor, EntryStacks.of(GrowableOresBlocks.Compressor_Block));
        registry.addWorkstations(MetalFormerRollingDisplay.MetalFormerRolling, EntryStacks.of(GrowableOresBlocks.MetalFormerBlock));
        registry.addWorkstations(MetalFormerCuttingDisplay.MetalFormerCutting, EntryStacks.of(GrowableOresBlocks.MetalFormerBlock));
        registry.addWorkstations(MetalFormerExtrudingDisplay.MetalFormerExtruding, EntryStacks.of(GrowableOresBlocks.MetalFormerBlock));
        registry.addWorkstations(ExtractorDisplay.Extractor, EntryStacks.of(GrowableOresBlocks.Extractor_Block));
        registry.addWorkstations(MolecularTransformerDisplay.MolecularTransformer, EntryStacks.of(GrowableOresBlocks.MolecularTransformerBlock));
        registry.addWorkstations(GrowableOresDisplay.GrowableOres, EntryStacks.of(GrowableOresBlocks.GrowableOres_Block));
        registry.addWorkstations(RecyclerDisplay.Recycler, EntryStacks.of(GrowableOresBlocks.RECYCLER_Block));
        registry.addWorkstations(BlockCutterDisplay.BlockCutter, EntryStacks.of(GrowableOresBlocks.CUTTING_Block));
        registry.addWorkstations(BrewReactorDisplay.BREW_REACTOR, EntryStacks.of(GrowableOresBlocks.Brew_Reactor_BLOCK));
        registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(GrowableOresBlocks.INDUCTION_FURNACE));
        registry.addWorkstations(HeatCentrifugeDisplay.HEAT_CENTRIFUGE, EntryStacks.of(GrowableOresBlocks.HEAT_CENTRIFUGE));
        registry.addWorkstations(OreWashingDisplay.ORE_WASHING, EntryStacks.of(GrowableOresBlocks.Ore_Washing_Block));
        registry.addWorkstations(BlastFurnaceDisplay.BLAST_FURNACE, EntryStacks.of(GrowableOresBlocks.BLAST_FURNACE_BLOCK));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 76,
                ((screen.height - 166) / 2) + 35, 23, 12), MaceratorBlockScreen.class,
                MaceratorDisplay.MACERATOR);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 72,
                        ((screen.height - 166) / 2) + 33, 23, 14), CompressorBlockScreen.class,
                CompressorDisplay.Compressor);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 70,
                        ((screen.height - 166) / 2) + 34, 48, 12), MetalFormerBlockScreen.class,
                MetalFormerRollingDisplay.MetalFormerRolling);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 70,
                        ((screen.height - 166) / 2) + 34, 48, 12), MetalFormerBlockScreen.class,
                MetalFormerCuttingDisplay.MetalFormerCutting);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 70,
                        ((screen.height - 166) / 2) + 34, 48, 12), MetalFormerBlockScreen.class,
                MetalFormerExtrudingDisplay.MetalFormerExtruding);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 74,
                        ((screen.height - 166) / 2) + 37, 23, 12), MolecularTransformerBlockScreen.class,
                MolecularTransformerDisplay.MolecularTransformer);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 75,
                        ((screen.height - 166) / 2) + 35, 23, 14), ExtractorBlockScreen.class,
                ExtractorDisplay.Extractor);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 73,
                        ((screen.height - 166) / 2) + 34, 28, 14), AlchemyBlockScreen.class,
                GrowableOresDisplay.GrowableOres);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 72,
                        ((screen.height - 166) / 2) + 34, 23, 16), RecyclerBlockScreen.class,
                RecyclerDisplay.Recycler);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 70,
                        ((screen.height - 166) / 2) + 33, 48, 19), CuttingBlockScreen.class,
                BlockCutterDisplay.BlockCutter);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 71,
                        ((screen.height - 166) / 2) + 34, 22, 15), ElectricFurnaceBlockScreen.class,
                BuiltinPlugin.SMELTING);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 79,
                        ((screen.height - 166) / 2) + 32, 11, 16), BrewReactorScreen.class,
                BrewReactorDisplay.BREW_REACTOR);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 71,
                        ((screen.height - 166) / 2) + 34, 21, 15), InductionFurnaceBlockScreen.class,
                BuiltinPlugin.SMELTING);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 79,
                        ((screen.height - 166) / 2) + 32, 21, 16), HeatCentrifugeBlockScreen.class,
                HeatCentrifugeDisplay.HEAT_CENTRIFUGE);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 79,
                        ((screen.height - 166) / 2) + 32, 11, 16), OreWashingScreen.class,
                OreWashingDisplay.ORE_WASHING);

        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 73,
                        ((screen.height - 166) / 2) + 34, 21, 16), ModBlastFurnaceScreen.class,
                BlastFurnaceDisplay.BLAST_FURNACE);
    }
}