package com.skniro.industrial_elixir.client;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.data.model.BatteryLevelProperty;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.renderer.AlchemyblockentityRenderer;
import com.skniro.industrial_elixir.client.particle.MapleCampfireSmokeParticle;
import com.skniro.industrial_elixir.client.particle.MapleParticleTypes;
import com.skniro.industrial_elixir.compat.jei.IndustrialElixirJEIPlugin;
import com.skniro.industrial_elixir.compat.jei.IndustrialElixirJEIUtils;
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
import com.skniro.industrial_elixir.entity.MapleEntityType;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.growableoresir.client.GrowableOresClienttwo;
import com.skniro.industrial_elixir.screen.ingame.machine.heat.ModBlastFurnaceScreen;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@EventBusSubscriber(modid = IndustrialElixir.MOD_ID, value = Dist.CLIENT)
public class IndustrialElixirOresClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModKeyMappings.registerKeys();
        ClientTickEvents.END_CLIENT_TICK.register(ModClientEvents::onEndTick);

        ChunkSectionLayer renderLayer2 = ChunkSectionLayer.CUTOUT;
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_SAPLING.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_LEAVES.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.GLASSFIBER_CABLE.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Item_Block.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Iten_Block.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Fluid_Block.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Fluid_Block.get(), renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Reinforced_Glass.get(), renderLayer2);

        ChunkSectionLayer renderLayer3 = ChunkSectionLayer.TRANSLUCENT;
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.FLUID_TANK_BLOCK.get(), renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Hot_Spring.get(), renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Hot_Spring.get(), renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Fluid_AIR.get(), renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Fluid_AIR.get(), renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.FLOWING_Fluid_UU.get(), renderLayer3);
        ModItemBlockRenderTypes.setRenderLayer(IndustrialElixirFluids.STILL_Fluid_UU.get(), renderLayer3);


        BlockEntityRenderers.register(AlchemyBlockEntityType.ALCHEMY_BLOCK_ENTITY.get(), AlchemyblockentityRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Wooden_BLOCK_ENTITY, WoodenPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Stone_BLOCK_ENTITY, WoodenPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Wooden_Fluid_BLOCK_ENTITY, FluidPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Stone_Fluid_BLOCK_ENTITY, FluidPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.FLUID_TANK_BLOCK_ENTITY, FluidTankRenderer::new);

        registerClientEntityRenderer();

        HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "water"), (context, deltaTracker)->  WindowsWatermarkRenderer.render(context));
        
        ClientRecipeSynchronizedEvent.EVENT.register((minecraft, synchronizedRecipes) -> {
            if (IndustrialElixirJEIUtils.isJEIAvailable()) {
                IndustrialElixirJEIPlugin.recipeMap = synchronizedRecipes;
                try {
                    IndustrialElixirJEIPlugin.pushRecipesToJei();
                } catch (Throwable ignored) {
                }
            }
        });

        FluidRenderingRegistry.register(IndustrialElixirFluids.STILL_Hot_Spring.get(), IndustrialElixirFluids.FLOWING_Hot_Spring.get(),
                new FluidModel.Unbaked(
                        new Material(Identifier.parse("industrial_elixir:block/spring_still")),
                        new Material(Identifier.parse("industrial_elixir:block/spring_flow")),
                        null,
                        _ -> 0xEDDBDBDB
                ));

        final FluidModel.Unbaked Fluid_UU_MODEL = new FluidModel.Unbaked(
                new Material(Identifier.withDefaultNamespace("block/water_still")),
                new Material(Identifier.withDefaultNamespace("block/water_flow")),
                new Material(Identifier.withDefaultNamespace("block/water_overlay")), _ -> 0xA1BFBFBF);

        final FluidModel.Unbaked Fluid_AIR_MODEL = new FluidModel.Unbaked(
                new Material(Identifier.withDefaultNamespace("block/water_still")),
                new Material(Identifier.withDefaultNamespace("block/water_flow")),
                new Material(Identifier.withDefaultNamespace("block/water_overlay")), _ -> 0xA1C64CEB);


        ParticleProviderRegistry.getInstance().register(MapleParticleTypes.HOT_SPRING.get(), MapleCampfireSmokeParticle.CosySmokeFactory::new);

        FluidRenderingRegistry.register(IndustrialElixirFluids.STILL_Fluid_UU.get(), IndustrialElixirFluids.FLOWING_Fluid_UU.get(), Fluid_UU_MODEL);
        FluidRenderingRegistry.register(IndustrialElixirFluids.STILL_Fluid_AIR.get(), IndustrialElixirFluids.FLOWING_Fluid_AIR.get(), Fluid_AIR_MODEL);
    }

    public static void registerClientEntityRenderer() {

        var rubber_boat = new ModelLayerLocation(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "boat/rubber"), "main");
        ModelLayerRegistry.registerModelLayer(rubber_boat, BoatModel::createBoatModel);
        EntityRendererRegistry.register(MapleEntityType.RUBBER_BOAT.get(), (dispatcher) -> new BoatRenderer(dispatcher, rubber_boat));

        var rubber_chest_boat = new ModelLayerLocation(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "chest_boat/rubber"), "main");
        ModelLayerRegistry.registerModelLayer(rubber_chest_boat, BoatModel::createChestBoatModel);
        EntityRendererRegistry.register(MapleEntityType.RUBBER_CHEST_BOAT.get(), (dispatcher) -> new BoatRenderer(dispatcher, rubber_chest_boat));
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

    @SubscribeEvent
    public static void registerSelectItemModel(RegisterSelectItemModelPropertyEvent event) {
        event.register(BatteryLevelProperty.ID, BatteryLevelProperty.TYPE);
    }
}
