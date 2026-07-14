package com.skniro.industrial_elixir.screen;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.energybox.ChargePadScreenHandler;
import com.skniro.industrial_elixir.screen.handler.energybox.EnergyBoxScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.heat.ElectricHeaterScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.heat.SolidFuelHeaterScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.*;
import com.skniro.industrial_elixir.screen.handler.machine.PatternStorageScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.BrewReactorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.MatterGeneratorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.OreWashingScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.ReplicatorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.container.fluid.FluidTankScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.ChunkLoaderScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.CoalGeneratorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.fluid.FluidGeneratorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorSolarPanelScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorWindMillScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.NuclearReactorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.SacredGeneratorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.heat.ModBlastFurnaceScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class AlchemyScreenHandlerType <T extends AbstractContainerMenu>{
    public static final MenuType<AlchemyBlockScreenHandler> ALCHEMY =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "cane_converter_screen_handler"),
                    new ExtendedMenuType<>(AlchemyBlockScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<CoalGeneratorScreenHandler> COAL_GENERATOR_SCREEN_HANDLER =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "coal_generator_screen_handler"),
                    new ExtendedMenuType<>(CoalGeneratorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<NuclearReactorScreenHandler> NuclearReactor =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "nuclear_reactor_screen_handler"),
                    new ExtendedMenuType<>(NuclearReactorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<SacredGeneratorScreenHandler> SACRED_GENERATOR =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "sacred_generator_screen_handler"),
                    new ExtendedMenuType<>(SacredGeneratorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<GeneratorWindMillScreenHandler> GENERATOR_Wind_Mill_SCREEN_HANDLER =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "generator_wind_mill_screen_handler"),
                    new ExtendedMenuType<>(GeneratorWindMillScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<GeneratorSolarPanelScreenHandler> GENERATOR_Solar_Panel_SCREEN_HANDLER =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "generator_solar_panel_screen_handler"),
                    new ExtendedMenuType<>(GeneratorSolarPanelScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<MaceratorScreenHandler> Macerator =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "macerator_screen_handler"),
                    new ExtendedMenuType<>(MaceratorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<CompressorScreenHandler> Compressor =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "compressor_screen_handler"),
                    new ExtendedMenuType<>(CompressorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<MetalFormerScreenHandler> MetalFormer =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "metal_former_screen_handler"),
                    new ExtendedMenuType<>(MetalFormerScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<EnergyBoxScreenHandler> EnergyBox =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "energy_box_screen_handler"),
                    new ExtendedMenuType<>(EnergyBoxScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ChargePadScreenHandler> ChargePad =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "charge_pad_screen_handler"),
                    new ExtendedMenuType<>(ChargePadScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<MolecularTransformerScreenHandler> MolecularTransformer =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "molecular_transformer_screen_handler"),
                    new ExtendedMenuType<>(MolecularTransformerScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ExtractorScreenHandler> Extractor =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "extractor_screen_handler"),
                    new ExtendedMenuType<>(ExtractorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ElectricFurnaceScreenHandler> ElectricFurnace =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "electric_furnace_screen_handler"),
                    new ExtendedMenuType<>(ElectricFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<InductionFurnaceScreenHandler> InductionFurnace =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "induction_furnace_screen_handler"),
                    new ExtendedMenuType<>(InductionFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<HeatCentrifugeScreenHandler> HeatCentrifuge =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "heat_centrifuge_screen_handler"),
                    new ExtendedMenuType<>(HeatCentrifugeScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<RecyclerScreenHandler> Recycler =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "recycler_screen_handler"),
                    new ExtendedMenuType<>(RecyclerScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<CuttingScreenHandler> Cutting =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "cutting_screen_handler"),
                    new ExtendedMenuType<>(CuttingScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<BrewReactorScreenHandler> BrewReactor =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "brew_reactor_screen_handler"),
                    new ExtendedMenuType<>(BrewReactorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<OreWashingScreenHandler> OreWashing =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "ore_washing_screen_handler"),
                    new ExtendedMenuType<>(OreWashingScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<FluidTankScreenHandler> FluidTank =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fluid_tank_screen_handler"),
                    new ExtendedMenuType<>(FluidTankScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ElectricHeaterScreenHandler> ElectricHeater =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "electric_heater_screen_handler"),
                    new ExtendedMenuType<>(ElectricHeaterScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<SolidFuelHeaterScreenHandler> SolidFuelHeater =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "solid_fuel_heater_screen_handler"),
                    new ExtendedMenuType<>(SolidFuelHeaterScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ModBlastFurnaceScreenHandler> ModBlastFurnace =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "mod_blast_furnace_screen_handler"),
                    new ExtendedMenuType<>(ModBlastFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<MatterGeneratorScreenHandler> MatterGenerator =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "matter_generator_screen_handler"),
                    new ExtendedMenuType<>(MatterGeneratorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<PatternStorageScreenHandler> PatternStorage =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "pattern_storage_screen_handler"),
                    new ExtendedMenuType<>(PatternStorageScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ReplicatorScreenHandler> Replicator =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "replicator_screen_handler"),
                    new ExtendedMenuType<>(ReplicatorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<FluidGeneratorScreenHandler> FluidGenerator =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fluid_generator_screen_handler"),
                    new ExtendedMenuType<>(FluidGeneratorScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ChunkLoaderScreenHandler> ChunkLoader =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "chunk_loader_screen_handler"),
                    new ExtendedMenuType<>(ChunkLoaderScreenHandler::new, BlockPos.STREAM_CODEC));

    public static void registeralchemyscreenhandlertype() {

    }
}
