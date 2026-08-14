package com.skniro.industrial_elixir.screen;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.energybox.ChargePadScreenHandler;
import com.skniro.industrial_elixir.screen.handler.energybox.EnergyBoxScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.heat.ElectricHeaterScreenHandler;
import com.skniro.industrial_elixir.screen.handler.generator.heat.SolidFuelHeaterScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.*;
import com.skniro.industrial_elixir.screen.handler.machine.PatternStorageScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.BrewReactorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.CoffeeMachineScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.CropFarmScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.VendorMachineScreenHandler;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AlchemyScreenHandlerType {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, IndustrialElixir.MOD_ID);

    public static final Supplier<MenuType<AlchemyBlockScreenHandler>> ALCHEMY =
            registerBlockEntityMenu("cane_converter_screen_handler", AlchemyBlockScreenHandler::new);

    public static final Supplier<MenuType<CoalGeneratorScreenHandler>> COAL_GENERATOR_SCREEN_HANDLER =
            registerBlockEntityMenu("coal_generator_screen_handler", CoalGeneratorScreenHandler::new);

    public static final Supplier<MenuType<NuclearReactorScreenHandler>> NuclearReactor =
            registerBlockEntityMenu("nuclear_reactor_screen_handler", NuclearReactorScreenHandler::new);

    public static final Supplier<MenuType<SacredGeneratorScreenHandler>> SACRED_GENERATOR =
            registerBlockEntityMenu("sacred_generator_screen_handler", SacredGeneratorScreenHandler::new);

    public static final Supplier<MenuType<GeneratorWindMillScreenHandler>> GENERATOR_Wind_Mill_SCREEN_HANDLER =
            registerBlockEntityMenu("generator_wind_mill_screen_handler", GeneratorWindMillScreenHandler::new);

    public static final Supplier<MenuType<GeneratorSolarPanelScreenHandler>> GENERATOR_Solar_Panel_SCREEN_HANDLER =
            registerBlockEntityMenu("generator_solar_panel_screen_handler", GeneratorSolarPanelScreenHandler::new);

    public static final Supplier<MenuType<MaceratorScreenHandler>> Macerator =
            registerBlockEntityMenu("macerator_screen_handler", MaceratorScreenHandler::new);

    public static final Supplier<MenuType<CompressorScreenHandler>> Compressor =
            registerBlockEntityMenu("compressor_screen_handler", CompressorScreenHandler::new);

    public static final Supplier<MenuType<MetalFormerScreenHandler>> MetalFormer =
            registerBlockEntityMenu("metal_former_screen_handler", MetalFormerScreenHandler::new);

    public static final Supplier<MenuType<EnergyBoxScreenHandler>> EnergyBox =
            registerBlockEntityMenu("energy_box_screen_handler", EnergyBoxScreenHandler::new);

    public static final Supplier<MenuType<ChargePadScreenHandler>> ChargePad =
            registerBlockEntityMenu("charge_pad_screen_handler", ChargePadScreenHandler::new);

    public static final Supplier<MenuType<MolecularTransformerScreenHandler>> MolecularTransformer =
            registerBlockEntityMenu("molecular_transformer_screen_handler", MolecularTransformerScreenHandler::new);

    public static final Supplier<MenuType<ExtractorScreenHandler>> Extractor =
            registerBlockEntityMenu("extractor_screen_handler", ExtractorScreenHandler::new);

    public static final Supplier<MenuType<ElectricFurnaceScreenHandler>> ElectricFurnace =
            registerBlockEntityMenu("electric_furnace_screen_handler", ElectricFurnaceScreenHandler::new);

    public static final Supplier<MenuType<InductionFurnaceScreenHandler>> InductionFurnace =
            registerBlockEntityMenu("induction_furnace_screen_handler", InductionFurnaceScreenHandler::new);

    public static final Supplier<MenuType<HeatCentrifugeScreenHandler>> HeatCentrifuge =
            registerBlockEntityMenu("heat_centrifuge_screen_handler", HeatCentrifugeScreenHandler::new);

    public static final Supplier<MenuType<RecyclerScreenHandler>> Recycler =
            registerBlockEntityMenu("recycler_screen_handler", RecyclerScreenHandler::new);

    public static final Supplier<MenuType<CuttingScreenHandler>> Cutting =
            registerBlockEntityMenu("cutting_screen_handler", CuttingScreenHandler::new);

    public static final Supplier<MenuType<BrewReactorScreenHandler>> BrewReactor =
            registerBlockEntityMenu("brew_reactor_screen_handler", BrewReactorScreenHandler::new);

    public static final Supplier<MenuType<OreWashingScreenHandler>> OreWashing =
            registerBlockEntityMenu("ore_washing_screen_handler", OreWashingScreenHandler::new);

    public static final Supplier<MenuType<FluidTankScreenHandler>> FluidTank =
            registerBlockEntityMenu("fluid_tank_screen_handler", FluidTankScreenHandler::new);

    public static final Supplier<MenuType<ElectricHeaterScreenHandler>> ElectricHeater =
            registerBlockEntityMenu("electric_heater_screen_handler", ElectricHeaterScreenHandler::new);

    public static final Supplier<MenuType<SolidFuelHeaterScreenHandler>> SolidFuelHeater =
            registerBlockEntityMenu("solid_fuel_heater_screen_handler", SolidFuelHeaterScreenHandler::new);

    public static final Supplier<MenuType<ModBlastFurnaceScreenHandler>> ModBlastFurnace =
            registerBlockEntityMenu("mod_blast_furnace_screen_handler", ModBlastFurnaceScreenHandler::new);

    public static final Supplier<MenuType<MatterGeneratorScreenHandler>> MatterGenerator =
            registerBlockEntityMenu("matter_generator_screen_handler", MatterGeneratorScreenHandler::new);

    public static final Supplier<MenuType<PatternStorageScreenHandler>> PatternStorage =
            registerBlockEntityMenu("pattern_storage_screen_handler", PatternStorageScreenHandler::new);

    public static final Supplier<MenuType<ReplicatorScreenHandler>> Replicator =
            registerBlockEntityMenu("replicator_screen_handler", ReplicatorScreenHandler::new);

    public static final Supplier<MenuType<FluidGeneratorScreenHandler>> FluidGenerator =
            registerBlockEntityMenu("fluid_generator_screen_handler", FluidGeneratorScreenHandler::new);

    public static final Supplier<MenuType<ChunkLoaderScreenHandler>> ChunkLoader =
            registerBlockEntityMenu("chunk_loader_screen_handler", ChunkLoaderScreenHandler::new);

    public static final Supplier<MenuType<CoffeeMachineScreenHandler>> CoffeeMachine =
            registerBlockEntityMenu("coffee_machine_screen_handler", CoffeeMachineScreenHandler::new);

    public static final Supplier<MenuType<CropFarmScreenHandler>> CropFarm =
            registerBlockEntityMenu("crop_farm_screen_handler", CropFarmScreenHandler::new);

    public static final Supplier<MenuType<VendorMachineScreenHandler>> VendorMachine =
            registerMenuType("vendor_machine_screen_handler", VendorMachineScreenHandler::new);

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerBlockEntityMenu(String name,
                                                                                                    IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String name,
                                                                                            IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void registeralchemyscreenhandlertype(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering ScreenHandlerType for " + IndustrialElixir.MOD_ID);
        MENUS.register(eventBus);
    }
}
