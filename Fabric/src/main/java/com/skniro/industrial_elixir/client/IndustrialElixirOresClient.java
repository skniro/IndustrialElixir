package com.skniro.industrial_elixir.client;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.renderer.AlchemyblockentityRenderer;
import com.skniro.industrial_elixir.block.renderer.container.fluid.FluidTankRenderer;
import com.skniro.industrial_elixir.block.renderer.pipe.FluidPipeRenderer;
import com.skniro.industrial_elixir.block.renderer.pipe.WoodenPipeRenderer;
import com.skniro.industrial_elixir.client.particle.MapleCampfireSmokeParticle;
import com.skniro.industrial_elixir.client.particle.MapleParticleTypes;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.keybind.ModClientEvents;
import com.skniro.industrial_elixir.keybind.ModKeyMappings;
import com.skniro.industrial_elixir.screen.ingame.energybox.ChargePadBlockScreen;
import com.skniro.industrial_elixir.screen.ingame.energybox.EnergyBoxBlockScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.heat.ElectricHeaterBlockScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.heat.SolidFuelHeaterScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.*;
import com.skniro.industrial_elixir.screen.ingame.machine.ChunkLoaderScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.BrewReactorScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.CoffeeMachineScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.CropFarmBlockScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.PatternStorageScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.MatterGeneratorScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.OreWashingScreen;
import com.skniro.industrial_elixir.screen.ingame.machine.fluid.ReplicatorScreen;
import com.skniro.industrial_elixir.screen.ingame.container.fluid.FluidTankScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.CoalGeneratorScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.fluid.FluidGeneratorScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.GeneratorSolarPanelScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.GeneratorWindMillScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.NuclearReactorScreen;
import com.skniro.industrial_elixir.screen.ingame.generator.SacredGeneratorScreen;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.growableoresir.client.GrowableOresClienttwo;
import com.skniro.industrial_elixir.screen.ingame.machine.heat.ModBlastFurnaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class IndustrialElixirOresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyMappings.registerKeys();
        ClientTickEvents.END_CLIENT_TICK.register(ModClientEvents::onEndTick);

        RenderType renderLayer2 = RenderType.cutout();
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_SAPLING, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_LEAVES, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.GLASSFIBER_CABLE, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Item_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Iten_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Fluid_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Fluid_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Reinforced_Glass, renderLayer2);

        RenderType renderLayer3 = RenderType.translucent();
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.FLUID_TANK_BLOCK, renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Hot_Spring, renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Hot_Spring, renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Fluid_AIR, renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Fluid_AIR, renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Fluid_UU, renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Fluid_UU, renderLayer3);

        MenuScreens.register(AlchemyScreenHandlerType.ALCHEMY, AlchemyBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.COAL_GENERATOR_SCREEN_HANDLER, CoalGeneratorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.NuclearReactor, NuclearReactorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.SACRED_GENERATOR, SacredGeneratorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.Macerator, MaceratorBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.Compressor, CompressorBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.GENERATOR_Wind_Mill_SCREEN_HANDLER, GeneratorWindMillScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.GENERATOR_Solar_Panel_SCREEN_HANDLER, GeneratorSolarPanelScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.MetalFormer, MetalFormerBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.ChargePad, ChargePadBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.EnergyBox, EnergyBoxBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.MolecularTransformer, MolecularTransformerBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.Extractor, ExtractorBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.ElectricFurnace, ElectricFurnaceBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.InductionFurnace, InductionFurnaceBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.Cutting, CuttingBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.Recycler, RecyclerBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.BrewReactor, BrewReactorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.OreWashing, OreWashingScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.FluidTank, FluidTankScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.ElectricHeater, ElectricHeaterBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.SolidFuelHeater, SolidFuelHeaterScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.ModBlastFurnace, ModBlastFurnaceScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.HeatCentrifuge, HeatCentrifugeBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.MatterGenerator, MatterGeneratorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.PatternStorage, PatternStorageScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.Replicator, ReplicatorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.FluidGenerator, FluidGeneratorScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.ChunkLoader, ChunkLoaderScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.CoffeeMachine, CoffeeMachineScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.CropFarm, CropFarmBlockScreen::new);
        MenuScreens.register(AlchemyScreenHandlerType.VendorMachine, VendorMachineScreen::new);

        BlockEntityRenderers.register(AlchemyBlockEntityType.ALCHEMY_BLOCK_ENTITY, AlchemyblockentityRenderer::new);
        BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Wooden_BLOCK_ENTITY, WoodenPipeRenderer::new);
        BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Stone_BLOCK_ENTITY, WoodenPipeRenderer::new);
        BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Wooden_Fluid_BLOCK_ENTITY, FluidPipeRenderer::new);
        BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Stone_Fluid_BLOCK_ENTITY, FluidPipeRenderer::new);
        BlockEntityRenderers.register(AlchemyBlockEntityType.FLUID_TANK_BLOCK_ENTITY, FluidTankRenderer::new);

        HudRenderCallback.EVENT.register(this::renderHud);

        GrowableOresClienttwo.register();

        FluidRenderHandlerRegistry.INSTANCE.register(IndustrialElixirFluids.STILL_Hot_Spring, IndustrialElixirFluids.FLOWING_Hot_Spring,
                new SimpleFluidRenderHandler(
                        ResourceLocation.parse("industrial_elixir:block/spring_still"),
                        ResourceLocation.parse("industrial_elixir:block/spring_flow"),
                        0xEDDBDBDB
                ));

        final SimpleFluidRenderHandler Fluid_UU_MODEL = new SimpleFluidRenderHandler(
                SimpleFluidRenderHandler.WATER_STILL,
                SimpleFluidRenderHandler.WATER_FLOWING,
                SimpleFluidRenderHandler.WATER_OVERLAY,
                0xA1BFBFBF);

        final SimpleFluidRenderHandler Fluid_AIR_MODEL = new SimpleFluidRenderHandler(
                SimpleFluidRenderHandler.WATER_STILL,
                SimpleFluidRenderHandler.WATER_FLOWING,
                SimpleFluidRenderHandler.WATER_OVERLAY,
                0xA1C64CEB);


        ParticleFactoryRegistry.getInstance().register(MapleParticleTypes.HOT_SPRING, MapleCampfireSmokeParticle.CosySmokeFactory::new);

        FluidRenderHandlerRegistry.INSTANCE.register(IndustrialElixirFluids.STILL_Fluid_UU, IndustrialElixirFluids.FLOWING_Fluid_UU, Fluid_UU_MODEL);
        FluidRenderHandlerRegistry.INSTANCE.register(IndustrialElixirFluids.STILL_Fluid_AIR, IndustrialElixirFluids.FLOWING_Fluid_AIR, Fluid_AIR_MODEL);
    }

    private void renderHud(GuiGraphics context, DeltaTracker tickCounter) {
        //WindowsWatermarkRenderer.render(context);
    }
}
