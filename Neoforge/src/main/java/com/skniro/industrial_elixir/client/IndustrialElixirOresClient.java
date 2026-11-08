package com.skniro.industrial_elixir.client;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.renderer.AlchemyblockentityRenderer;
import com.skniro.industrial_elixir.block.renderer.container.fluid.FluidTankRenderer;
import com.skniro.industrial_elixir.block.renderer.pipe.FluidPipeRenderer;
import com.skniro.industrial_elixir.client.particle.MapleCampfireSmokeParticle;
import com.skniro.industrial_elixir.client.particle.MapleParticleTypes;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.fluid.MapleFluidTypes;
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
import com.skniro.industrial_elixir.entity.MapleEntityType;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.screen.ingame.machine.heat.ModBlastFurnaceScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = IndustrialElixir.MOD_ID, value = Dist.CLIENT)
public class IndustrialElixirOresClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        RenderType renderLayer2 = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_SAPLING.get(), renderLayer2);
        ItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_LEAVES.get(), renderLayer2);
        ItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.GLASSFIBER_CABLE.get(), renderLayer2);
        ItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Fluid_Block.get(), renderLayer2);
        ItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Fluid_Block.get(), renderLayer2);
        ItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Reinforced_Glass.get(), renderLayer2);

        RenderType renderLayer3 = RenderType.translucent();
        ItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.FLUID_TANK_BLOCK.get(), renderLayer3);
        ItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Hot_Spring.get(), renderLayer3);
        ItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Hot_Spring.get(), renderLayer3);
        ItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Fluid_AIR.get(), renderLayer3);
        ItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Fluid_AIR.get(), renderLayer3);
        ItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Fluid_UU.get(), renderLayer3);
        ItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Fluid_UU.get(), renderLayer3);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AlchemyBlockEntityType.ALCHEMY_BLOCK_ENTITY.get(), AlchemyblockentityRenderer::new);
        // 1.21.1 boat models are picked from the vanilla {@code Boat.Type}; there is no data driven boat model.
        event.registerEntityRenderer(MapleEntityType.RUBBER_BOAT.get(), pContext -> new BoatRenderer(pContext, false));
        event.registerEntityRenderer(MapleEntityType.RUBBER_CHEST_BOAT.get(), pContext -> new BoatRenderer(pContext, true));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(AlchemyScreenHandlerType.ALCHEMY.get(), AlchemyBlockScreen::new);
        event.register(AlchemyScreenHandlerType.COAL_GENERATOR_SCREEN_HANDLER.get(), CoalGeneratorScreen::new);
        event.register(AlchemyScreenHandlerType.NuclearReactor.get(), NuclearReactorScreen::new);
        event.register(AlchemyScreenHandlerType.SACRED_GENERATOR.get(), SacredGeneratorScreen::new);
        event.register(AlchemyScreenHandlerType.Macerator.get(), MaceratorBlockScreen::new);
        event.register(AlchemyScreenHandlerType.Compressor.get(), CompressorBlockScreen::new);
        event.register(AlchemyScreenHandlerType.GENERATOR_Wind_Mill_SCREEN_HANDLER.get(), GeneratorWindMillScreen::new);
        event.register(AlchemyScreenHandlerType.GENERATOR_Solar_Panel_SCREEN_HANDLER.get(), GeneratorSolarPanelScreen::new);
        event.register(AlchemyScreenHandlerType.MetalFormer.get(), MetalFormerBlockScreen::new);
        event.register(AlchemyScreenHandlerType.ChargePad.get(), ChargePadBlockScreen::new);
        event.register(AlchemyScreenHandlerType.EnergyBox.get(), EnergyBoxBlockScreen::new);
        event.register(AlchemyScreenHandlerType.MolecularTransformer.get(), MolecularTransformerBlockScreen::new);
        event.register(AlchemyScreenHandlerType.Extractor.get(), ExtractorBlockScreen::new);
        event.register(AlchemyScreenHandlerType.ElectricFurnace.get(), ElectricFurnaceBlockScreen::new);
        event.register(AlchemyScreenHandlerType.InductionFurnace.get(), InductionFurnaceBlockScreen::new);
        event.register(AlchemyScreenHandlerType.Cutting.get(), CuttingBlockScreen::new);
        event.register(AlchemyScreenHandlerType.Recycler.get(), RecyclerBlockScreen::new);
        event.register(AlchemyScreenHandlerType.BrewReactor.get(), BrewReactorScreen::new);
        event.register(AlchemyScreenHandlerType.OreWashing.get(), OreWashingScreen::new);
        event.register(AlchemyScreenHandlerType.FluidTank.get(), FluidTankScreen::new);
        event.register(AlchemyScreenHandlerType.ElectricHeater.get(), ElectricHeaterBlockScreen::new);
        event.register(AlchemyScreenHandlerType.SolidFuelHeater.get(), SolidFuelHeaterScreen::new);
        event.register(AlchemyScreenHandlerType.ModBlastFurnace.get(), ModBlastFurnaceScreen::new);
        event.register(AlchemyScreenHandlerType.HeatCentrifuge.get(), HeatCentrifugeBlockScreen::new);
        event.register(AlchemyScreenHandlerType.MatterGenerator.get(), MatterGeneratorScreen::new);
        event.register(AlchemyScreenHandlerType.PatternStorage.get(), PatternStorageScreen::new);
        event.register(AlchemyScreenHandlerType.Replicator.get(), ReplicatorScreen::new);
        event.register(AlchemyScreenHandlerType.FluidGenerator.get(), FluidGeneratorScreen::new);
        event.register(AlchemyScreenHandlerType.ChunkLoader.get(), ChunkLoaderScreen::new);
        event.register(AlchemyScreenHandlerType.CoffeeMachine.get(), CoffeeMachineScreen::new);
        event.register(AlchemyScreenHandlerType.CropFarm.get(), CropFarmBlockScreen::new);
        event.register(AlchemyScreenHandlerType.VendorMachine.get(), VendorMachineScreen::new);
    }

    /**
     * 1.21.1 has no {@code FluidModel} / {@code RegisterFluidModelsEvent}; custom fluid sprites and
     * tints are supplied through the client fluid type extensions instead.
     */
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return ResourceLocation.parse("industrial_elixir:block/spring_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return ResourceLocation.parse("industrial_elixir:block/spring_flow");
            }

            @Override
            public int getTintColor() {
                return 0xEDDBDBDB;
            }
        }, MapleFluidTypes.Spring_FLUID_TYPE.get());

        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return 0xA1BFBFBF;
            }
        }, MapleFluidTypes.UU_FLUID_TYPE.get());

        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return 0xA1C64CEB;
            }
        }, MapleFluidTypes.AIR_FLUID_TYPE.get());
    }

    @SubscribeEvent
    public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MapleParticleTypes.HOT_SPRING.get(), MapleCampfireSmokeParticle.CosySmokeFactory::new);
    }
}
