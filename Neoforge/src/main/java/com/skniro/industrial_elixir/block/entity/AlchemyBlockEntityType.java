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
import com.skniro.industrial_elixir.block.entity.pipe.StonePipeBlockEntity;
import com.skniro.industrial_elixir.block.entity.pipe.WoodFluidPipeBlockEntity;
import com.skniro.industrial_elixir.block.entity.pipe.WoodPipeBlockEntity;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class AlchemyBlockEntityType {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, IndustrialElixir.MOD_ID);

    public static final Supplier<BlockEntityType<Alchemyblockentity>> ALCHEMY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("alchemy_block",
                    () -> new BlockEntityType<>(Alchemyblockentity::new, GrowableOresBlocks.GrowableOres_Block.get()));

    public static final Supplier<BlockEntityType<CableBlockEntity>> CABLE =
            BLOCK_ENTITIES.register("cable",
                    () -> new BlockEntityType<>(CableBlockEntity::new,
                            GrowableOresBlocks.COPPER_CABLE.get(),
                            GrowableOresBlocks.TIN_CABLE.get(),
                            GrowableOresBlocks.GOLD_CABLE.get(),
                            GrowableOresBlocks.HV_CABLE.get(),
                            GrowableOresBlocks.GLASSFIBER_CABLE.get(),
                            GrowableOresBlocks.INSULATED_TIN_CABLE.get(),
                            GrowableOresBlocks.INSULATED_COPPER_CABLE.get(),
                            GrowableOresBlocks.INSULATED_GOLD_CABLE.get(),
                            GrowableOresBlocks.INSULATED_HV_CABLE.get()));

    public static final Supplier<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR_BE =
            BLOCK_ENTITIES.register("coal_generator_be",
                    () -> new BlockEntityType<>(CoalGeneratorBlockEntity::new, GrowableOresBlocks.COAL_GENERATOR.get()));

    public static final Supplier<BlockEntityType<NuclearReactorBlockEntity>> NUCLEAR_REACTOR_BE =
            BLOCK_ENTITIES.register("nuclear_reactor_be",
                    () -> new BlockEntityType<>(NuclearReactorBlockEntity::new, GrowableOresBlocks.NUCLEAR_REACTOR.get()));

    public static final Supplier<BlockEntityType<MaceratorEntity>> Macerator_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("macerator_block",
                    () -> new BlockEntityType<>(MaceratorEntity::new, GrowableOresBlocks.Macerator_Block.get()));

    public static final Supplier<BlockEntityType<CompressorEntity>> Compressor_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("compressor_block",
                    () -> new BlockEntityType<>(CompressorEntity::new, GrowableOresBlocks.Compressor_Block.get()));

    public static final Supplier<BlockEntityType<GeneratorWindMillBlockEntity>> GENERATOR_WIND_MILL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("generator_wind_mill_be",
                    () -> new BlockEntityType<>(GeneratorWindMillBlockEntity::new, GrowableOresBlocks.GENERATOR_Wind_Mill.get()));

    public static final Supplier<BlockEntityType<GeneratorSolarPanelBlockEntity>> GENERATOR_SOLAR_PANEL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("generator_solar_panel_be",
                    () -> new BlockEntityType<>(GeneratorSolarPanelBlockEntity::new,
                            GrowableOresBlocks.GENERATOR_SolarPanel.get(),
                            GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL.get(),
                            GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL.get(),
                            GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL.get(),
                            GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL.get()));

    public static final Supplier<BlockEntityType<MetalFormerBlockEntity>> MetalFormer_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("generator_metal_former_be",
                    () -> new BlockEntityType<>(MetalFormerBlockEntity::new, GrowableOresBlocks.MetalFormerBlock.get()));

    public static final Supplier<BlockEntityType<ChargePadBlockEntity>> ChargePad_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("charge_pad_be",
                    () -> new BlockEntityType<>(ChargePadBlockEntity::new,
                            GrowableOresBlocks.ChargePad.get(),
                            GrowableOresBlocks.CESU_CHARGE_PAD.get(),
                            GrowableOresBlocks.MFE_CHARGE_PAD.get(),
                            GrowableOresBlocks.MFSU_CHARGE_PAD.get()));

    public static final Supplier<BlockEntityType<EnergyBoxBlockEntity>> EnergyBox_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("energy_box_former_be",
                    () -> new BlockEntityType<>(EnergyBoxBlockEntity::new,
                            GrowableOresBlocks.EnergyBox.get(),
                            GrowableOresBlocks.CESU.get(),
                            GrowableOresBlocks.MFE.get(),
                            GrowableOresBlocks.MFSU.get()));

    public static final Supplier<BlockEntityType<MolecularTransformerBlockEntity>> MolecularTransformer_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("molecular_transformer_former_be",
                    () -> new BlockEntityType<>(MolecularTransformerBlockEntity::new, GrowableOresBlocks.MolecularTransformerBlock.get()));

    public static final Supplier<BlockEntityType<ExtractorEntity>> Extractor_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("extractor_be",
                    () -> new BlockEntityType<>(ExtractorEntity::new, GrowableOresBlocks.Extractor_Block.get()));

    public static final Supplier<BlockEntityType<IronFurnaceBlockEntity>> Iron_Furnace_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("iron_furnace_be",
                    () -> new BlockEntityType<>(IronFurnaceBlockEntity::new, GrowableOresBlocks.Iron_Furnace_Block.get()));

    public static final Supplier<BlockEntityType<ElectricFurnaceEntity>> Electric_Furnace_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("electric_furnace_be",
                    () -> new BlockEntityType<>(ElectricFurnaceEntity::new, GrowableOresBlocks.ElectricFurnace_Block.get()));

    public static final Supplier<BlockEntityType<InductionFurnaceEntity>> INDUCTION_FURNACE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("induction_furnace_be",
                    () -> new BlockEntityType<>(InductionFurnaceEntity::new, GrowableOresBlocks.INDUCTION_FURNACE.get()));

    public static final Supplier<BlockEntityType<CuttingEntity>> Cutting_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cutting_be",
                    () -> new BlockEntityType<>(CuttingEntity::new, GrowableOresBlocks.CUTTING_Block.get()));

    public static final Supplier<BlockEntityType<RecyclerEntity>> Recycler_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("recycler_be",
                    () -> new BlockEntityType<>(RecyclerEntity::new, GrowableOresBlocks.RECYCLER_Block.get()));

    public static final Supplier<BlockEntityType<StonePipeBlockEntity>> PIPE_Stone_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pipe_stone_be",
                    () -> new BlockEntityType<>(StonePipeBlockEntity::new, GrowableOresBlocks.Pipe_Stone_Item_Block.get()));

    public static final Supplier<BlockEntityType<WoodPipeBlockEntity>> PIPE_Wooden_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pipe_wooden_be",
                    () -> new BlockEntityType<>(WoodPipeBlockEntity::new, GrowableOresBlocks.Pipe_Wooden_Iten_Block.get()));

    public static final Supplier<BlockEntityType<StoneFluidPipeBlockEntity>> PIPE_Stone_Fluid_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pipe_stone_fluid_be",
                    () -> new BlockEntityType<>(StoneFluidPipeBlockEntity::new, GrowableOresBlocks.Pipe_Stone_Fluid_Block.get()));

    public static final Supplier<BlockEntityType<WoodFluidPipeBlockEntity>> PIPE_Wooden_Fluid_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pipe_wooden_fluid_be",
                    () -> new BlockEntityType<>(WoodFluidPipeBlockEntity::new, GrowableOresBlocks.Pipe_Wooden_Fluid_Block.get()));

    public static final Supplier<BlockEntityType<FluidTankBlockEntity>> FLUID_TANK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fluid_tank_be",
                    () -> new BlockEntityType<>(FluidTankBlockEntity::new, GrowableOresBlocks.FLUID_TANK_BLOCK.get()));

    public static final Supplier<BlockEntityType<BrewReactorBlockEntity>> Brew_Reactor_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("brew_reactor_tank_be",
                    () -> new BlockEntityType<>(BrewReactorBlockEntity::new, GrowableOresBlocks.Brew_Reactor_BLOCK.get()));

    public static final Supplier<BlockEntityType<OreWashingBlockEntity>> Ore_Washing_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ore_washing_block_entity",
                    () -> new BlockEntityType<>(OreWashingBlockEntity::new, GrowableOresBlocks.Ore_Washing_Block.get()));

    public static final Supplier<BlockEntityType<ElectricHeaterBlockEntity>> ELECTRIC_HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("electric_heater_block_entity",
                    () -> new BlockEntityType<>(ElectricHeaterBlockEntity::new, GrowableOresBlocks.Electric_Heater_Block.get()));

    public static final Supplier<BlockEntityType<SolidFuelHeaterEntity>> SOLID_FUEL_HEATER_BE =
            BLOCK_ENTITIES.register("solid_fuel_heater_be",
                    () -> new BlockEntityType<>(SolidFuelHeaterEntity::new, GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR.get()));

    public static final Supplier<BlockEntityType<MatterGeneratorEntity>> MATTER_GENERATOR_BE =
            BLOCK_ENTITIES.register("matter_generator_be",
                    () -> new BlockEntityType<>(MatterGeneratorEntity::new, GrowableOresBlocks.MATTER_GENERATOR.get()));

    public static final Supplier<BlockEntityType<ModBlastFurnaceBlockEntity>> BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITIES.register("blast_furnace_block_entity",
                    () -> new BlockEntityType<>(ModBlastFurnaceBlockEntity::new, GrowableOresBlocks.BLAST_FURNACE_BLOCK.get()));

    public static final Supplier<BlockEntityType<SacredGeneratorBlockEntity>> SACRED_GENERATOR_BE =
            BLOCK_ENTITIES.register("sacred_generator_be",
                    () -> new BlockEntityType<>(SacredGeneratorBlockEntity::new, GrowableOresBlocks.SACRED_GENERATOR.get()));

    public static final Supplier<BlockEntityType<HeatCentrifugeEntity>> HEAT_CENTRIFUGE_BE =
            BLOCK_ENTITIES.register("heat_centrifuge_be",
                    () -> new BlockEntityType<>(HeatCentrifugeEntity::new, GrowableOresBlocks.HEAT_CENTRIFUGE.get()));

    public static final Supplier<BlockEntityType<PatternStorageBlockEntity>> PATTERN_STORAGE_BE =
            BLOCK_ENTITIES.register("pattern_storage_be",
                    () -> new BlockEntityType<>(PatternStorageBlockEntity::new, GrowableOresBlocks.PATTERN_STORAGE.get()));

    public static final Supplier<BlockEntityType<ReplicatorBlockEntity>> REPLICATOR_BE =
            BLOCK_ENTITIES.register("replicator_be",
                    () -> new BlockEntityType<>(ReplicatorBlockEntity::new, GrowableOresBlocks.Replicator.get()));

    public static final Supplier<BlockEntityType<FluidGeneratorEntity>> FLUID_GENERATOR_BE =
            BLOCK_ENTITIES.register("fluid_generator_be",
                    () -> new BlockEntityType<>(FluidGeneratorEntity::new, GrowableOresBlocks.FLUID_GENERATOR.get()));

    public static final Supplier<BlockEntityType<ChunkLoaderEntity>> CHUNK_LOADER_BE =
            BLOCK_ENTITIES.register("chunk_loader_be",
                    () -> new BlockEntityType<>(ChunkLoaderEntity::new, GrowableOresBlocks.CHUNK_LOADER.get()));

    public static final Supplier<BlockEntityType<CoffeeMachineBlockEntity>> COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITIES.register("coffee_machine_block_entity",
                    () -> new BlockEntityType<>(CoffeeMachineBlockEntity::new, GrowableOresBlocks.COFFEE_MACHINE_Block.get()));

    public static final Supplier<BlockEntityType<CropFarmBlockEntity>> CROP_FARM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crop_farm_block_entity",
                    () -> new BlockEntityType<>(CropFarmBlockEntity::new, GrowableOresBlocks.CROP_FARM_Block.get()));

    public static final Supplier<BlockEntityType<VendorMachineBlockEntity>> VENDOR_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("vendor_machine_block_entity",
                    () -> new BlockEntityType<>(VendorMachineBlockEntity::new, GrowableOresBlocks.VENDOR_MACHINE_Block.get()));

    public static void registerMachineEnergyEntity() {
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), ALCHEMY_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), COAL_GENERATOR_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), NUCLEAR_REACTOR_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Macerator_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Compressor_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), GENERATOR_WIND_MILL_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), GENERATOR_SOLAR_PANEL_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), MetalFormer_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), EnergyBox_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), ChargePad_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), MolecularTransformer_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Extractor_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Electric_Furnace_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), INDUCTION_FURNACE_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Cutting_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Recycler_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Brew_Reactor_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Ore_Washing_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), ELECTRIC_HEATER_BLOCK_ENTITY.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), MATTER_GENERATOR_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), SACRED_GENERATOR_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), HEAT_CENTRIFUGE_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), PATTERN_STORAGE_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), REPLICATOR_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), FLUID_GENERATOR_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), CHUNK_LOADER_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), SOLID_FUEL_HEATER_BE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), CROP_FARM_BLOCK_ENTITY.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, FLUID_TANK_BLOCK_ENTITY.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, Brew_Reactor_BLOCK_ENTITY.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, Ore_Washing_BLOCK_ENTITY.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, MATTER_GENERATOR_BE.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, REPLICATOR_BE.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, FLUID_GENERATOR_BE.get());
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get());
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), ELECTRIC_HEATER_BLOCK_ENTITY.get());
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), SOLID_FUEL_HEATER_BE.get());
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get());
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), SACRED_GENERATOR_BE.get());
    }

    public static void registerMapleBlockEntityType(IEventBus eventBus) {
        IndustrialElixir.LOGGER.debug("Registering MapleBlockEntityType for " + IndustrialElixir.MOD_ID);
        BLOCK_ENTITIES.register(eventBus);
    }
}
