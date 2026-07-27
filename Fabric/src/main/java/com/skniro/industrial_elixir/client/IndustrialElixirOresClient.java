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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class IndustrialElixirOresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyMappings.registerKeys();
        ClientTickEvents.END_CLIENT_TICK.register(ModClientEvents::onEndTick);
        SelectItemModelProperties.ID_MAPPER.put(BatteryLevelProperty.ID, BatteryLevelProperty.TYPE);

        ChunkSectionLayer renderLayer2 = ChunkSectionLayer.CUTOUT;
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_SAPLING, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Rubber_LEAVES, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.GLASSFIBER_CABLE, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Item_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Iten_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Stone_Fluid_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GrowableOresBlocks.Pipe_Wooden_Fluid_Block, renderLayer2);
        ModItemBlockRenderTypes.setRenderLayer(GeneralBlocks.Reinforced_Glass, renderLayer2);

        ChunkSectionLayer renderLayer3 = ChunkSectionLayer.TRANSLUCENT;
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
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Wooden_BLOCK_ENTITY, WoodenPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Stone_BLOCK_ENTITY, WoodenPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Wooden_Fluid_BLOCK_ENTITY, FluidPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.PIPE_Stone_Fluid_BLOCK_ENTITY, FluidPipeRenderer::new);
        //BlockEntityRenderers.register(AlchemyBlockEntityType.FLUID_TANK_BLOCK_ENTITY, FluidTankRenderer::new);

        registerClientEntityRenderer();

        HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "water"), this::renderHud);

        GrowableOresClienttwo.register();

        ClientRecipeSynchronizedEvent.EVENT.register((minecraft, synchronizedRecipes) -> {
            if (IndustrialElixirJEIUtils.isJEIAvailable()) {
                IndustrialElixirJEIPlugin.recipeMap = synchronizedRecipes;
                try {
                    IndustrialElixirJEIPlugin.pushRecipesToJei();
                } catch (Throwable ignored) {
                }
            }
        });

        FluidRenderingRegistry.register(IndustrialElixirFluids.STILL_Hot_Spring, IndustrialElixirFluids.FLOWING_Hot_Spring,
                new FluidModel.Unbaked(
                        new Material(Identifier.parse("industrial_elixir:block/spring_still")),
                        new Material(Identifier.parse("industrial_elixir:block/spring_flow")),
                        null,
                        _ -> 0xA1E0E7EC
                ));


        ParticleProviderRegistry.getInstance().register(MapleParticleTypes.HOT_SPRING, MapleCampfireSmokeParticle.CosySmokeFactory::new);

        FluidRenderingRegistry.register(IndustrialElixirFluids.STILL_Fluid_UU, IndustrialElixirFluids.FLOWING_Fluid_UU, IndustrialElixirFluids.Fluid_UU_MODEL);
    }

    private void renderHud(GuiGraphicsExtractor context, DeltaTracker tickCounter) {
        WindowsWatermarkRenderer.render(context);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientEntityRenderer() {

        var rubber_boat = new ModelLayerLocation(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "boat/rubber"), "main");
        ModelLayerRegistry.registerModelLayer(rubber_boat, BoatModel::createBoatModel);
        EntityRendererRegistry.register(MapleEntityType.RUBBER_BOAT, (dispatcher) -> new BoatRenderer(dispatcher, rubber_boat));

        var rubber_chest_boat = new ModelLayerLocation(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "chest_boat/rubber"), "main");
        ModelLayerRegistry.registerModelLayer(rubber_chest_boat, BoatModel::createChestBoatModel);
        EntityRendererRegistry.register(MapleEntityType.RUBBER_CHEST_BOAT, (dispatcher) -> new BoatRenderer(dispatcher, rubber_chest_boat));
    }
}
