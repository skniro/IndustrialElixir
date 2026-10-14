package com.skniro.industrial_elixir.block.entity;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.entity.cable.CableBlockEntity;
import com.skniro.industrial_elixir.block.entity.energybox.ChargePadBlockEntity;
import com.skniro.industrial_elixir.block.entity.energybox.EnergyBoxBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.heat.ElectricHeaterBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.heat.SolidFuelHeaterEntity;
import com.skniro.industrial_elixir.block.entity.machine.fluid.*;
import com.skniro.industrial_elixir.block.entity.container.fluid.FluidTankBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.CoalGeneratorBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.FluidGeneratorEntity;
import com.skniro.industrial_elixir.block.entity.generator.GeneratorSolarPanelBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.GeneratorWindMillBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.NuclearReactorBlockEntity;
import com.skniro.industrial_elixir.block.entity.generator.SacredGeneratorBlockEntity;
import com.skniro.industrial_elixir.block.entity.machine.*;
import com.skniro.industrial_elixir.block.entity.machine.CropFarmBlockEntity;
import com.skniro.industrial_elixir.block.entity.machine.PatternStorageBlockEntity;
import com.skniro.industrial_elixir.block.entity.machine.heat.ModBlastFurnaceBlockEntity;
import com.skniro.industrial_elixir.block.entity.pipe.StoneFluidPipeBlockEntity;
import com.skniro.industrial_elixir.block.entity.pipe.WoodFluidPipeBlockEntity;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class AlchemyBlockEntityType {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, IndustrialElixir.MOD_ID);

    public static final Supplier<BlockEntityType<Alchemyblockentity>> ALCHEMY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("alchemy_block",
                    () -> BlockEntityType.Builder.of(Alchemyblockentity::new, GrowableOresBlocks.GrowableOres_Block.get()).build(null));

    public static final Supplier<BlockEntityType<CableBlockEntity>> CABLE =
            BLOCK_ENTITIES.register("cable",
                    () -> BlockEntityType.Builder.of(CableBlockEntity::new,
                            GrowableOresBlocks.COPPER_CABLE.get(),
                            GrowableOresBlocks.TIN_CABLE.get(),
                            GrowableOresBlocks.GOLD_CABLE.get(),
                            GrowableOresBlocks.HV_CABLE.get(),
                            GrowableOresBlocks.GLASSFIBER_CABLE.get(),
                            GrowableOresBlocks.INSULATED_TIN_CABLE.get(),
                            GrowableOresBlocks.INSULATED_COPPER_CABLE.get(),
                            GrowableOresBlocks.INSULATED_GOLD_CABLE.get(),
                            GrowableOresBlocks.INSULATED_HV_CABLE.get()).build(null));

    public static final Supplier<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR_BE =
            BLOCK_ENTITIES.register("coal_generator_be",
                    () -> BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new, GrowableOresBlocks.COAL_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<NuclearReactorBlockEntity>> NUCLEAR_REACTOR_BE =
            BLOCK_ENTITIES.register("nuclear_reactor_be",
                    () -> BlockEntityType.Builder.of(NuclearReactorBlockEntity::new, GrowableOresBlocks.NUCLEAR_REACTOR.get()).build(null));

    public static final Supplier<BlockEntityType<MaceratorEntity>> Macerator_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("macerator_block",
                    () -> BlockEntityType.Builder.of(MaceratorEntity::new, GrowableOresBlocks.Macerator_Block.get()).build(null));

    public static final Supplier<BlockEntityType<CompressorEntity>> Compressor_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("compressor_block",
                    () -> BlockEntityType.Builder.of(CompressorEntity::new, GrowableOresBlocks.Compressor_Block.get()).build(null));

    public static final Supplier<BlockEntityType<GeneratorWindMillBlockEntity>> GENERATOR_WIND_MILL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("generator_wind_mill_be",
                    () -> BlockEntityType.Builder.of(GeneratorWindMillBlockEntity::new, GrowableOresBlocks.GENERATOR_Wind_Mill.get()).build(null));

    public static final Supplier<BlockEntityType<GeneratorSolarPanelBlockEntity>> GENERATOR_SOLAR_PANEL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("generator_solar_panel_be",
                    () -> BlockEntityType.Builder.of(GeneratorSolarPanelBlockEntity::new,
                            GrowableOresBlocks.GENERATOR_SolarPanel.get(),
                            GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL.get(),
                            GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL.get(),
                            GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL.get(),
                            GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL.get()).build(null));

    public static final Supplier<BlockEntityType<MetalFormerBlockEntity>> MetalFormer_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("generator_metal_former_be",
                    () -> BlockEntityType.Builder.of(MetalFormerBlockEntity::new, GrowableOresBlocks.MetalFormerBlock.get()).build(null));

    public static final Supplier<BlockEntityType<ChargePadBlockEntity>> ChargePad_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("charge_pad_be",
                    () -> BlockEntityType.Builder.of(ChargePadBlockEntity::new,
                            GrowableOresBlocks.ChargePad.get(),
                            GrowableOresBlocks.CESU_CHARGE_PAD.get(),
                            GrowableOresBlocks.MFE_CHARGE_PAD.get(),
                            GrowableOresBlocks.MFSU_CHARGE_PAD.get()).build(null));

    public static final Supplier<BlockEntityType<EnergyBoxBlockEntity>> EnergyBox_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("energy_box_former_be",
                    () -> BlockEntityType.Builder.of(EnergyBoxBlockEntity::new,
                            GrowableOresBlocks.EnergyBox.get(),
                            GrowableOresBlocks.CESU.get(),
                            GrowableOresBlocks.MFE.get(),
                            GrowableOresBlocks.MFSU.get()).build(null));

    public static final Supplier<BlockEntityType<MolecularTransformerBlockEntity>> MolecularTransformer_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("molecular_transformer_former_be",
                    () -> BlockEntityType.Builder.of(MolecularTransformerBlockEntity::new, GrowableOresBlocks.MolecularTransformerBlock.get()).build(null));

    public static final Supplier<BlockEntityType<ExtractorEntity>> Extractor_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("extractor_be",
                    () -> BlockEntityType.Builder.of(ExtractorEntity::new, GrowableOresBlocks.Extractor_Block.get()).build(null));

    public static final Supplier<BlockEntityType<IronFurnaceBlockEntity>> Iron_Furnace_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("iron_furnace_be",
                    () -> BlockEntityType.Builder.of(IronFurnaceBlockEntity::new, GrowableOresBlocks.Iron_Furnace_Block.get()).build(null));

    public static final Supplier<BlockEntityType<ElectricFurnaceEntity>> Electric_Furnace_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("electric_furnace_be",
                    () -> BlockEntityType.Builder.of(ElectricFurnaceEntity::new, GrowableOresBlocks.ElectricFurnace_Block.get()).build(null));

    public static final Supplier<BlockEntityType<InductionFurnaceEntity>> INDUCTION_FURNACE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("induction_furnace_be",
                    () -> BlockEntityType.Builder.of(InductionFurnaceEntity::new, GrowableOresBlocks.INDUCTION_FURNACE.get()).build(null));

    public static final Supplier<BlockEntityType<CuttingEntity>> Cutting_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cutting_be",
                    () -> BlockEntityType.Builder.of(CuttingEntity::new, GrowableOresBlocks.CUTTING_Block.get()).build(null));

    public static final Supplier<BlockEntityType<RecyclerEntity>> Recycler_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("recycler_be",
                    () -> BlockEntityType.Builder.of(RecyclerEntity::new, GrowableOresBlocks.RECYCLER_Block.get()).build(null));

    public static final Supplier<BlockEntityType<StoneFluidPipeBlockEntity>> PIPE_Stone_Fluid_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pipe_stone_fluid_be",
                    () -> BlockEntityType.Builder.of(StoneFluidPipeBlockEntity::new, GrowableOresBlocks.Pipe_Stone_Fluid_Block.get()).build(null));

    public static final Supplier<BlockEntityType<WoodFluidPipeBlockEntity>> PIPE_Wooden_Fluid_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pipe_wooden_fluid_be",
                    () -> BlockEntityType.Builder.of(WoodFluidPipeBlockEntity::new, GrowableOresBlocks.Pipe_Wooden_Fluid_Block.get()).build(null));

    public static final Supplier<BlockEntityType<FluidTankBlockEntity>> FLUID_TANK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fluid_tank_be",
                    () -> BlockEntityType.Builder.of(FluidTankBlockEntity::new, GrowableOresBlocks.FLUID_TANK_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<BrewReactorBlockEntity>> Brew_Reactor_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("brew_reactor_tank_be",
                    () -> BlockEntityType.Builder.of(BrewReactorBlockEntity::new, GrowableOresBlocks.Brew_Reactor_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<OreWashingBlockEntity>> Ore_Washing_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ore_washing_block_entity",
                    () -> BlockEntityType.Builder.of(OreWashingBlockEntity::new, GrowableOresBlocks.Ore_Washing_Block.get()).build(null));

    public static final Supplier<BlockEntityType<ElectricHeaterBlockEntity>> ELECTRIC_HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("electric_heater_block_entity",
                    () -> BlockEntityType.Builder.of(ElectricHeaterBlockEntity::new, GrowableOresBlocks.Electric_Heater_Block.get()).build(null));

    public static final Supplier<BlockEntityType<SolidFuelHeaterEntity>> SOLID_FUEL_HEATER_BE =
            BLOCK_ENTITIES.register("solid_fuel_heater_be",
                    () -> BlockEntityType.Builder.of(SolidFuelHeaterEntity::new, GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<MatterGeneratorEntity>> MATTER_GENERATOR_BE =
            BLOCK_ENTITIES.register("matter_generator_be",
                    () -> BlockEntityType.Builder.of(MatterGeneratorEntity::new, GrowableOresBlocks.MATTER_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<ModBlastFurnaceBlockEntity>> BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITIES.register("blast_furnace_block_entity",
                    () -> BlockEntityType.Builder.of(ModBlastFurnaceBlockEntity::new, GrowableOresBlocks.BLAST_FURNACE_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<SacredGeneratorBlockEntity>> SACRED_GENERATOR_BE =
            BLOCK_ENTITIES.register("sacred_generator_be",
                    () -> BlockEntityType.Builder.of(SacredGeneratorBlockEntity::new, GrowableOresBlocks.SACRED_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<HeatCentrifugeEntity>> HEAT_CENTRIFUGE_BE =
            BLOCK_ENTITIES.register("heat_centrifuge_be",
                    () -> BlockEntityType.Builder.of(HeatCentrifugeEntity::new, GrowableOresBlocks.HEAT_CENTRIFUGE.get()).build(null));

    public static final Supplier<BlockEntityType<PatternStorageBlockEntity>> PATTERN_STORAGE_BE =
            BLOCK_ENTITIES.register("pattern_storage_be",
                    () -> BlockEntityType.Builder.of(PatternStorageBlockEntity::new, GrowableOresBlocks.PATTERN_STORAGE.get()).build(null));

    public static final Supplier<BlockEntityType<ReplicatorBlockEntity>> REPLICATOR_BE =
            BLOCK_ENTITIES.register("replicator_be",
                    () -> BlockEntityType.Builder.of(ReplicatorBlockEntity::new, GrowableOresBlocks.Replicator.get()).build(null));

    public static final Supplier<BlockEntityType<FluidGeneratorEntity>> FLUID_GENERATOR_BE =
            BLOCK_ENTITIES.register("fluid_generator_be",
                    () -> BlockEntityType.Builder.of(FluidGeneratorEntity::new, GrowableOresBlocks.FLUID_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<ChunkLoaderEntity>> CHUNK_LOADER_BE =
            BLOCK_ENTITIES.register("chunk_loader_be",
                    () -> BlockEntityType.Builder.of(ChunkLoaderEntity::new, GrowableOresBlocks.CHUNK_LOADER.get()).build(null));

    public static final Supplier<BlockEntityType<CoffeeMachineBlockEntity>> COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITIES.register("coffee_machine_block_entity",
                    () -> BlockEntityType.Builder.of(CoffeeMachineBlockEntity::new, GrowableOresBlocks.COFFEE_MACHINE_Block.get()).build(null));

    public static final Supplier<BlockEntityType<CropFarmBlockEntity>> CROP_FARM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crop_farm_block_entity",
                    () -> BlockEntityType.Builder.of(CropFarmBlockEntity::new, GrowableOresBlocks.CROP_FARM_Block.get()).build(null));

    public static final Supplier<BlockEntityType<VendorMachineBlockEntity>> VENDOR_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("vendor_machine_block_entity",
                    () -> BlockEntityType.Builder.of(VendorMachineBlockEntity::new, GrowableOresBlocks.VENDOR_MACHINE_Block.get()).build(null));

    public static void registerMachineEnergyEntity(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(EnergyStorage.SIDED, ALCHEMY_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, CABLE.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, COAL_GENERATOR_BE.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, NUCLEAR_REACTOR_BE.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Macerator_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Compressor_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, GENERATOR_WIND_MILL_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, GENERATOR_SOLAR_PANEL_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, MetalFormer_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, EnergyBox_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, ChargePad_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, MolecularTransformer_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Extractor_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Electric_Furnace_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, INDUCTION_FURNACE_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Cutting_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Recycler_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Brew_Reactor_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, Ore_Washing_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, ELECTRIC_HEATER_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, SOLID_FUEL_HEATER_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, MATTER_GENERATOR_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, SACRED_GENERATOR_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, HEAT_CENTRIFUGE_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, PATTERN_STORAGE_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, REPLICATOR_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, FLUID_GENERATOR_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, CHUNK_LOADER_BE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(EnergyStorage.SIDED, CROP_FARM_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FLUID_TANK_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, Brew_Reactor_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, Ore_Washing_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MATTER_GENERATOR_BE.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, REPLICATOR_BE.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FLUID_GENERATOR_BE.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get(), (blockEntity, direction) -> blockEntity.fluidContainer);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PIPE_Stone_Fluid_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PIPE_Wooden_Fluid_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getFluidHandler());
        event.registerBlockEntity(HeatStorage.SIDED, ELECTRIC_HEATER_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction));
        event.registerBlockEntity(HeatStorage.SIDED, SOLID_FUEL_HEATER_BE.get(),(blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction));
        event.registerBlockEntity(HeatStorage.SIDED, BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get(),(blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction));
        event.registerBlockEntity(HeatStorage.SIDED, SACRED_GENERATOR_BE.get(),(blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ALCHEMY_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Macerator_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Compressor_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MetalFormer_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MolecularTransformer_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Extractor_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Electric_Furnace_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INDUCTION_FURNACE_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Cutting_BLOCK_ENTITY.get(), (blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Recycler_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Brew_Reactor_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Ore_Washing_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ELECTRIC_HEATER_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SOLID_FUEL_HEATER_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MATTER_GENERATOR_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, HEAT_CENTRIFUGE_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PATTERN_STORAGE_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, REPLICATOR_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FLUID_GENERATOR_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CHUNK_LOADER_BE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CROP_FARM_BLOCK_ENTITY.get(),(blockEntity, direction) -> blockEntity.getItemHandler(direction));
    }

    public static void registerMapleBlockEntityType(IEventBus eventBus) {
        IndustrialElixir.LOGGER.debug("Registering MapleBlockEntityType for " + IndustrialElixir.MOD_ID);
        BLOCK_ENTITIES.register(eventBus);
    }
}
