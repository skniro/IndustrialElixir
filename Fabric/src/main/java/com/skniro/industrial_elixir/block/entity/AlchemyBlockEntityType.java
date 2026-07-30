package com.skniro.industrial_elixir.block.entity;

import com.mojang.datafixers.types.Type;
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
import com.skniro.industrial_elixir.block.entity.pipe.*;
import com.skniro.industrial_elixir.block.init.pipe.PipeBlock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;


public class AlchemyBlockEntityType {
    public static final BlockEntityType<Alchemyblockentity> ALCHEMY_BLOCK_ENTITY;
    public static final BlockEntityType<CableBlockEntity> CABLE;
    public static final BlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR_BE;
    public static final BlockEntityType<NuclearReactorBlockEntity> NUCLEAR_REACTOR_BE;
    public static final BlockEntityType<MaceratorEntity> Macerator_BLOCK_ENTITY;
    public static final BlockEntityType<CompressorEntity> Compressor_BLOCK_ENTITY;
    public static final BlockEntityType<GeneratorWindMillBlockEntity> GENERATOR_WIND_MILL_BLOCK_ENTITY;
    public static final BlockEntityType<GeneratorSolarPanelBlockEntity> GENERATOR_SOLAR_PANEL_BLOCK_ENTITY;
    public static final BlockEntityType<MetalFormerBlockEntity> MetalFormer_BLOCK_ENTITY;
    public static final BlockEntityType<ChargePadBlockEntity> ChargePad_BLOCK_ENTITY;
    public static final BlockEntityType<EnergyBoxBlockEntity> EnergyBox_BLOCK_ENTITY;
    public static final BlockEntityType<MolecularTransformerBlockEntity> MolecularTransformer_BLOCK_ENTITY;
    public static final BlockEntityType<ExtractorEntity> Extractor_BLOCK_ENTITY;
    public static final BlockEntityType<IronFurnaceBlockEntity> Iron_Furnace_BLOCK_ENTITY;
    public static final BlockEntityType<ElectricFurnaceEntity> Electric_Furnace_BLOCK_ENTITY;
    public static final BlockEntityType<InductionFurnaceEntity> INDUCTION_FURNACE_BLOCK_ENTITY;
    public static final BlockEntityType<CuttingEntity> Cutting_BLOCK_ENTITY;
    public static final BlockEntityType<RecyclerEntity> Recycler_BLOCK_ENTITY;
    public static final BlockEntityType<StonePipeBlockEntity> PIPE_Stone_BLOCK_ENTITY;
    public static final BlockEntityType<WoodPipeBlockEntity> PIPE_Wooden_BLOCK_ENTITY;
    public static final BlockEntityType<StoneFluidPipeBlockEntity> PIPE_Stone_Fluid_BLOCK_ENTITY;
    public static final BlockEntityType<WoodFluidPipeBlockEntity> PIPE_Wooden_Fluid_BLOCK_ENTITY;
    public static final BlockEntityType<FluidTankBlockEntity> FLUID_TANK_BLOCK_ENTITY;
    public static final BlockEntityType<BrewReactorBlockEntity> Brew_Reactor_BLOCK_ENTITY;
    public static final BlockEntityType<OreWashingBlockEntity> Ore_Washing_BLOCK_ENTITY;
    public static final BlockEntityType<ElectricHeaterBlockEntity> ELECTRIC_HEATER_BLOCK_ENTITY;
    public static final BlockEntityType<SolidFuelHeaterEntity> SOLID_FUEL_HEATER_BE;
    public static final BlockEntityType<MatterGeneratorEntity> MATTER_GENERATOR_BE;
    public static final BlockEntityType<ModBlastFurnaceBlockEntity> BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE;
    public static final BlockEntityType<SacredGeneratorBlockEntity> SACRED_GENERATOR_BE;
    public static final BlockEntityType<HeatCentrifugeEntity> HEAT_CENTRIFUGE_BE;
    public static final BlockEntityType<PatternStorageBlockEntity> PATTERN_STORAGE_BE;
    public static final BlockEntityType<ReplicatorBlockEntity> REPLICATOR_BE;
    public static final BlockEntityType<FluidGeneratorEntity> FLUID_GENERATOR_BE;
    public static final BlockEntityType<ChunkLoaderEntity> CHUNK_LOADER_BE;
    public static final BlockEntityType<CoffeeMachineBlockEntity> COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE;
    public static final BlockEntityType<CropFarmBlockEntity> CROP_FARM_BLOCK_ENTITY;
    public static final BlockEntityType<VendorMachineBlockEntity> VENDOR_MACHINE_BLOCK_ENTITY;


    static {
        ALCHEMY_BLOCK_ENTITY = create("alchemy_block", FabricBlockEntityTypeBuilder.create(Alchemyblockentity::new, GrowableOresBlocks.GrowableOres_Block));
        CABLE = create("cable", FabricBlockEntityTypeBuilder.create(CableBlockEntity::new,
                        GrowableOresBlocks.COPPER_CABLE,
                        GrowableOresBlocks.TIN_CABLE,
                        GrowableOresBlocks.GOLD_CABLE,
                        GrowableOresBlocks.HV_CABLE,
                        GrowableOresBlocks.GLASSFIBER_CABLE,
                        GrowableOresBlocks.INSULATED_TIN_CABLE,
                        GrowableOresBlocks.INSULATED_COPPER_CABLE,
                        GrowableOresBlocks.INSULATED_GOLD_CABLE,
                        GrowableOresBlocks.INSULATED_HV_CABLE
                )
        );
        COAL_GENERATOR_BE = create("coal_generator_be", FabricBlockEntityTypeBuilder.create(CoalGeneratorBlockEntity::new , GrowableOresBlocks.COAL_GENERATOR));
        NUCLEAR_REACTOR_BE = create("nuclear_reactor_be", FabricBlockEntityTypeBuilder.create(NuclearReactorBlockEntity::new, GrowableOresBlocks.NUCLEAR_REACTOR));
        Macerator_BLOCK_ENTITY = create("macerator_block", FabricBlockEntityTypeBuilder.create(MaceratorEntity::new, GrowableOresBlocks.Macerator_Block));
        Compressor_BLOCK_ENTITY = create("compressor_block", FabricBlockEntityTypeBuilder.create(CompressorEntity::new, GrowableOresBlocks.Compressor_Block));
        GENERATOR_WIND_MILL_BLOCK_ENTITY = create("generator_wind_mill_be", FabricBlockEntityTypeBuilder.create(GeneratorWindMillBlockEntity::new, GrowableOresBlocks.GENERATOR_Wind_Mill));

        GENERATOR_SOLAR_PANEL_BLOCK_ENTITY = create("generator_solar_panel_be", FabricBlockEntityTypeBuilder.create(GeneratorSolarPanelBlockEntity::new,
                GrowableOresBlocks.GENERATOR_SolarPanel,
                GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL,
                GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL,
                GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL,
                GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL
        ));

        MetalFormer_BLOCK_ENTITY = create("generator_metal_former_be", FabricBlockEntityTypeBuilder.create(MetalFormerBlockEntity::new, GrowableOresBlocks.MetalFormerBlock));
        ChargePad_BLOCK_ENTITY = create("charge_pad_be", FabricBlockEntityTypeBuilder.create(ChargePadBlockEntity::new,
                GrowableOresBlocks.ChargePad,
                GrowableOresBlocks.CESU_CHARGE_PAD,
                GrowableOresBlocks.MFE_CHARGE_PAD,
                GrowableOresBlocks.MFSU_CHARGE_PAD));

        EnergyBox_BLOCK_ENTITY = create("energy_box_former_be", FabricBlockEntityTypeBuilder.create(EnergyBoxBlockEntity::new,
                GrowableOresBlocks.EnergyBox,
                GrowableOresBlocks.CESU,
                GrowableOresBlocks.MFE,
                GrowableOresBlocks.MFSU));

        MolecularTransformer_BLOCK_ENTITY = create("molecular_transformer_former_be", FabricBlockEntityTypeBuilder.create(MolecularTransformerBlockEntity::new, GrowableOresBlocks.MolecularTransformerBlock));
        Extractor_BLOCK_ENTITY = create("extractor_be", FabricBlockEntityTypeBuilder.create(ExtractorEntity::new, GrowableOresBlocks.Extractor_Block));
        Iron_Furnace_BLOCK_ENTITY = create("iron_furnace_be", FabricBlockEntityTypeBuilder.create(IronFurnaceBlockEntity::new, GrowableOresBlocks.Iron_Furnace_Block));
        Electric_Furnace_BLOCK_ENTITY = create("electric_furnace_be", FabricBlockEntityTypeBuilder.create(ElectricFurnaceEntity::new, GrowableOresBlocks.ElectricFurnace_Block));
        INDUCTION_FURNACE_BLOCK_ENTITY = create("induction_furnace_be", FabricBlockEntityTypeBuilder.create(InductionFurnaceEntity::new, GrowableOresBlocks.INDUCTION_FURNACE));
        Cutting_BLOCK_ENTITY = create("cutting_be", FabricBlockEntityTypeBuilder.create(CuttingEntity::new, GrowableOresBlocks.CUTTING_Block));
        Recycler_BLOCK_ENTITY = create("recycler_be", FabricBlockEntityTypeBuilder.create(RecyclerEntity::new, GrowableOresBlocks.RECYCLER_Block));
        PIPE_Wooden_BLOCK_ENTITY = create("pipe_wooden_be", FabricBlockEntityTypeBuilder.create(WoodPipeBlockEntity::new, GrowableOresBlocks.Pipe_Wooden_Iten_Block));
        PIPE_Stone_BLOCK_ENTITY = create("pipe_stone_be", FabricBlockEntityTypeBuilder.create(StonePipeBlockEntity::new, GrowableOresBlocks.Pipe_Stone_Item_Block));
        PIPE_Wooden_Fluid_BLOCK_ENTITY = create("pipe_wooden_fluid_be", FabricBlockEntityTypeBuilder.create(WoodFluidPipeBlockEntity::new, GrowableOresBlocks.Pipe_Wooden_Fluid_Block));
        PIPE_Stone_Fluid_BLOCK_ENTITY = create("pipe_stone_fluid_be", FabricBlockEntityTypeBuilder.create(StoneFluidPipeBlockEntity::new, GrowableOresBlocks.Pipe_Stone_Fluid_Block));
        FLUID_TANK_BLOCK_ENTITY = create("fluid_tank_be", FabricBlockEntityTypeBuilder.create(FluidTankBlockEntity::new, GrowableOresBlocks.FLUID_TANK_BLOCK));
        Brew_Reactor_BLOCK_ENTITY = create("brew_reactor_tank_be", FabricBlockEntityTypeBuilder.create(BrewReactorBlockEntity::new, GrowableOresBlocks.Brew_Reactor_BLOCK));
        Ore_Washing_BLOCK_ENTITY = create("ore_washing_block_entity", FabricBlockEntityTypeBuilder.create(OreWashingBlockEntity::new, GrowableOresBlocks.Ore_Washing_Block));
        ELECTRIC_HEATER_BLOCK_ENTITY = create("electric_heater_block_entity", FabricBlockEntityTypeBuilder.create(ElectricHeaterBlockEntity::new, GrowableOresBlocks.Electric_Heater_Block));
        SOLID_FUEL_HEATER_BE = create("solid_fuel_heater_be", FabricBlockEntityTypeBuilder.create(SolidFuelHeaterEntity::new, GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR));
        MATTER_GENERATOR_BE = create("matter_generator_be", FabricBlockEntityTypeBuilder.create(MatterGeneratorEntity::new, GrowableOresBlocks.MATTER_GENERATOR));
        BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE = create("blast_furnace_block_entity", FabricBlockEntityTypeBuilder.create(ModBlastFurnaceBlockEntity::new, GrowableOresBlocks.BLAST_FURNACE_BLOCK));
        SACRED_GENERATOR_BE = create("sacred_generator_be", FabricBlockEntityTypeBuilder.create(SacredGeneratorBlockEntity::new, GrowableOresBlocks.SACRED_GENERATOR));
        HEAT_CENTRIFUGE_BE = create("heat_centrifuge_be", FabricBlockEntityTypeBuilder.create(com.skniro.industrial_elixir.block.entity.machine.HeatCentrifugeEntity::new, GrowableOresBlocks.HEAT_CENTRIFUGE));
        PATTERN_STORAGE_BE = create("pattern_storage_be", FabricBlockEntityTypeBuilder.create(PatternStorageBlockEntity::new, GrowableOresBlocks.PATTERN_STORAGE));
        REPLICATOR_BE = create("replicator_be", FabricBlockEntityTypeBuilder.create(ReplicatorBlockEntity::new, GrowableOresBlocks.Replicator));
        FLUID_GENERATOR_BE = create("fluid_generator_be", FabricBlockEntityTypeBuilder.create(FluidGeneratorEntity::new, GrowableOresBlocks.FLUID_GENERATOR));
        CHUNK_LOADER_BE = create("chunk_loader_be", FabricBlockEntityTypeBuilder.create(ChunkLoaderEntity::new, GrowableOresBlocks.CHUNK_LOADER));
        COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE= create("coffee_machine_block_entity", FabricBlockEntityTypeBuilder.create(CoffeeMachineBlockEntity::new, GrowableOresBlocks.COFFEE_MACHINE_Block));
        CROP_FARM_BLOCK_ENTITY = create("crop_farm_block_entity", FabricBlockEntityTypeBuilder.create(CropFarmBlockEntity::new, GrowableOresBlocks.CROP_FARM_Block));
        VENDOR_MACHINE_BLOCK_ENTITY = create("vendor_machine_block_entity", FabricBlockEntityTypeBuilder.create(VendorMachineBlockEntity::new, GrowableOresBlocks.VENDOR_MACHINE_Block));
    }



    private static <T extends BlockEntity> BlockEntityType create(String id, FabricBlockEntityTypeBuilder<T> builder) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, id);
        return (BlockEntityType) Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID,id), builder.build(null));
    }

    public static void registerMachineEnergyEntity() {
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), CABLE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), ALCHEMY_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), COAL_GENERATOR_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), NUCLEAR_REACTOR_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Macerator_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Compressor_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), GENERATOR_WIND_MILL_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), GENERATOR_SOLAR_PANEL_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), MetalFormer_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), EnergyBox_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), ChargePad_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), MolecularTransformer_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Extractor_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Electric_Furnace_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), INDUCTION_FURNACE_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Cutting_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Recycler_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Brew_Reactor_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), Ore_Washing_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), ELECTRIC_HEATER_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), MATTER_GENERATOR_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), SACRED_GENERATOR_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), HEAT_CENTRIFUGE_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), PATTERN_STORAGE_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), REPLICATOR_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), FLUID_GENERATOR_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), CHUNK_LOADER_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), SOLID_FUEL_HEATER_BE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyContainer.getSideStorage(direction), CROP_FARM_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, FLUID_TANK_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, Brew_Reactor_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, Ore_Washing_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, MATTER_GENERATOR_BE);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, REPLICATOR_BE);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, FLUID_GENERATOR_BE);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidContainer, COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE);
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), ELECTRIC_HEATER_BLOCK_ENTITY);
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), SOLID_FUEL_HEATER_BE);
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE);
        HeatStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.heatContainer.getSideStorage(direction), SACRED_GENERATOR_BE);
    }

    public static void registerMapleBlockEntityType() {
        IndustrialElixir.LOGGER.debug("Registering MapleBlockEntityType for " + IndustrialElixir.MOD_ID);
    }

    public static class PipeBlockEntity extends BlockEntity implements ItemOwner, PipeExtractDirectionController {

        private final List<com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem> items = new ArrayList<>();
        private final EnumSet<Direction> blocked = EnumSet.noneOf(Direction.class);
        private @Nullable Direction preferredExtractDirection;
        private @Nullable Direction currentExtractingFrom;

        public PipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        public boolean isBlocked(Direction dir) {
            return blocked.contains(dir);
        }

        public void setBlocked(Direction dir, boolean value) {
            if (value) {
                blocked.add(dir);
            } else {
                blocked.remove(dir);
            }
            setChanged();
        }

        @Override
        public @Nullable Direction getPreferredExtractDirection() {
            return preferredExtractDirection;
        }

        @Override
        public boolean setPreferredExtractDirection(@Nullable Direction direction) {
            if (preferredExtractDirection == direction) {
                return false;
            }

            preferredExtractDirection = direction;
            setChanged();

            if (level != null && !level.isClientSide()) {
                onExtractDirectionChanged(getDisplayExtractDirection(level, worldPosition));
            }

            return true;
        }

        public void tick(Level world, BlockPos pos, BlockState state) {
            updateConnections();
        }

        public List<com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem> getItems() {
            return new ArrayList<>(items);
        }

        public void neighborUpdate() {
            updateConnections();
            if (level != null && !level.isClientSide()) {
                onExtractDirectionChanged(getDisplayExtractDirection(level, worldPosition));
            }
        }

        @Override
        protected void loadAdditional(ValueInput nbt) {
            preferredExtractDirection = readDirection(nbt.getIntOr("pipe.preferred_extract_side", -1));
            blocked.clear();
            for (Direction dir : Direction.values()) {
                if (nbt.getIntOr("pipe.blocked_" + dir.name(), 0) == 1) {
                    blocked.add(dir);
                }
            }
            super.loadAdditional(nbt);
        }

        @Override
        protected void saveAdditional(ValueOutput nbt) {
            super.saveAdditional(nbt);
            nbt.putInt("pipe.preferred_extract_side",
                    preferredExtractDirection == null ? -1 : preferredExtractDirection.get3DDataValue());
            for (Direction dir : Direction.values()) {
                nbt.putInt("pipe.blocked_" + dir.name(), blocked.contains(dir) ? 1 : 0);
            }
        }

        protected void updateConnections() {
            if (level == null || level.isClientSide()) {
                return;
            }

            BlockState state = getBlockState();
            BlockState newState = state;

            for (Direction dir : Direction.values()) {
                boolean connected = isConnectable(dir);
                newState = newState.setValue(PipeBlock.PROPERTY_MAP.get(dir), connected);
            }

            if (newState != state) {
                level.setBlockAndUpdate(worldPosition, newState);
            }
        }

        protected boolean isConnectable(Direction dir) {
            if (isBlocked(dir)) {
                return false;
            }
            BlockPos target = worldPosition.relative(dir);
            BlockState neighborState = level.getBlockState(target);
            Block neighborBlock = neighborState.getBlock();

            if (neighborBlock instanceof PipeBlock) {
                return true;
            }

            var storage = ItemStorage.SIDED.find(level, target, dir.getOpposite());

            return storage != null;
        }

        void moveItems(Level world, BlockPos pos, BlockState state) {
            Iterator<com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem> it = items.iterator();

            while (it.hasNext()) {
                com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem item = it.next();

                if (isBlocked(item.direction)) {
                    continue;
                }

                if (item.variant.isBlank() || item.amount <= 0) {
                    it.remove();
                    continue;
                }

                item.progress += 0.1;

                if (item.progress >= 1.0) {
                    BlockPos target = pos.relative(item.direction);

                    if (!tryMoveToNext(world, target, item)
                            && !tryInsert(world, target, item)) {

                        item.progress = 1.0;
                        continue;
                    }

                    it.remove();
                }
            }

            if (items.isEmpty()) {
                currentExtractingFrom = null;
            }
        }

        private boolean tryMoveToNext(Level world, BlockPos pos, com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem item) {
            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity pipe) {
                if (isBlocked(item.direction)) {
                    return false;
                }

                Direction fromDir = item.direction.getOpposite();
                if (pipe.getCurrentExtractingFrom() == fromDir) {
                    return false;
                }

                Direction newDir = getNextDirection(world, pos, item);

                com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem newItem = new com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem(item.variant, item.amount, newDir);
                newItem.lastDirection = item.direction;
                // 只有目标管道items容量未超限时才转移，否则不转移
                if (pipe.items.size() < 64) { // 假定最大堆叠数64，可根据实际调整
                    pipe.items.add(newItem);
                    return true;
                } else {
                    // 目标管道已满，转移失败，物品保留在当前管道
                    return false;
                }
            }

            return false;
        }

        private Direction getNextDirection(Level world, BlockPos pos, Direction from) {
            List<Direction> possible = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                if (dir == from.getOpposite()) {
                    continue;
                }
                if (isBlocked(dir)) {
                    continue;
                }
                BlockState state = world.getBlockState(pos);
                if (state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) {
                    possible.add(dir);
                }
            }

            if (possible.isEmpty()) {
                return from;
            }

            return possible.get(world.getRandom().nextInt(possible.size()));
        }

        private boolean tryInsert(Level world, BlockPos pos, com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem item) {
            if (item.variant.isBlank() || item.amount <= 0) {
                return false;
            }

            if (isBlocked(item.direction)) {
                return false;
            }
            Storage<ItemVariant> storage =
                    ItemStorage.SIDED.find(world, pos, item.direction.getOpposite());

            if (storage == null) {
                return false;
            }

            // 禁止向木头管道的pull面插入物品
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof WoodPipeBlockEntity) {
                BlockState state = world.getBlockState(pos);
                Direction insertDir = item.direction.getOpposite();
                BooleanProperty pullProp = com.skniro.industrial_elixir.block.init.pipe.WoodPipeBlock.PULL_PROPERTY_MAP.get(insertDir);
                if (pullProp != null && state.getValue(pullProp)) {
                    return false;
                }
            }
            try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                long inserted = storage.insert(item.variant, item.amount, tx);

                if (inserted > 0) {
                    item.amount -= inserted;
                    if (item.amount <= 0) {
                        item.variant = ItemVariant.blank();
                    }
                    tx.commit();
                    return true;
                } else {
                    // 插入失败，物品保留在当前管道，等待下次tick
                    return false;
                }
            }

        }

        private Direction getNextDirection(Level world, BlockPos pos, com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem item) {
            List<Direction> possible = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                if (isBlocked(dir)) {
                    continue;
                }
                if (dir == item.lastDirection.getOpposite()) {
                    continue;
                }

                BlockState state = world.getBlockState(pos);
                if (state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) {
                    possible.add(dir);
                }
            }

            if (possible.isEmpty()) {
                return item.lastDirection.getOpposite();
            }

            return possible.get(world.getRandom().nextInt(possible.size()));
        }

        void extractItems(Level world, BlockPos pos, BlockState state) {
            if (!items.isEmpty()) {
                onExtractDirectionChanged(getDisplayExtractDirection(world, pos));
                return;
            }

            Direction preferredDir = getActivePreferredExtractDirection(world, pos);
            if (preferredDir != null) {
                onExtractDirectionChanged(preferredDir);
                tryExtractFromSide(world, pos, preferredDir);
                return;
            }

            Direction extractedDir = tryExtractFromAuto(world, pos);
            if (extractedDir != null) {
                onExtractDirectionChanged(extractedDir);
                return;
            }

            onExtractDirectionChanged(findFirstConnectedStorageSide(world, pos));
        }

        protected void onExtractDirectionChanged(@Nullable Direction direction) {
        }

        private @Nullable Direction getDisplayExtractDirection(Level world, BlockPos pos) {
            Direction preferredDir = getActivePreferredExtractDirection(world, pos);
            if (preferredDir != null) {
                return preferredDir;
            }
            return findFirstConnectedStorageSide(world, pos);
        }

        private @Nullable Direction getActivePreferredExtractDirection(Level world, BlockPos pos) {
            if (preferredExtractDirection == null) {
                return null;
            }

            if (isBlocked(preferredExtractDirection)) {
                return null;
            }

            return findStorage(world, pos, preferredExtractDirection) == null ? null : preferredExtractDirection;
        }

        private @Nullable Direction findFirstConnectedStorageSide(Level world, BlockPos pos) {
            for (Direction dir : Direction.values()) {
                if (isBlocked(dir)) {
                    continue;
                }
                if (findStorage(world, pos, dir) != null) {
                    return dir;
                }
            }
            return null;
        }

        private @Nullable Direction tryExtractFromAuto(Level world, BlockPos pos) {
            for (Direction dir : Direction.values()) {
                if (tryExtractFromSide(world, pos, dir)) {
                    return dir;
                }
            }
            return null;
        }

        private boolean tryExtractFromSide(Level world, BlockPos pos, Direction dir) {
            if (isBlocked(dir)) {
                return false;
            }

            Storage<ItemVariant> storage = findStorage(world, pos, dir);
            if (storage == null) {
                return false;
            }

            try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                StorageView<ItemVariant> view = null;

                for (StorageView<ItemVariant> next : storage) {
                    if (!next.isResourceBlank()) {
                        view = next;
                        break;
                    }
                }

                if (view == null) {
                    return false;
                }

                long extracted = storage.extract(view.getResource(), 1, tx);
                if (extracted <= 0) {
                    return false;
                }

                currentExtractingFrom = dir;
                Direction outDir = dir.getOpposite();
                items.add(new com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem(view.getResource(), extracted, outDir));
                tx.commit();
                return true;
            }
        }

        public @Nullable Direction getCurrentExtractingFrom() {
            return currentExtractingFrom;
        }

        private @Nullable ResourceHandler<ItemResource> findStorage(Level world, BlockPos pos, Direction dir) {
            BlockPos target = pos.relative(dir);
            return ItemStorage.SIDED.find(world, target, dir.getOpposite());
        }

        private static @Nullable Direction readDirection(int value) {
            if (value < 0 || value >= Direction.values().length) {
                return null;
            }
            return Direction.from3DDataValue(value);
        }

        @Override
        public Level level() {
            return this.level;
        }

        @Override
        public Vec3 position() {
            return this.getBlockPos().getCenter();
        }

        @Override
        public float getVisualRotationYInDegrees() {
            return 0;
        }

        public class PipeItem {
            public ItemResource variant;
            public int amount;

            public Direction direction;
            public Direction lastDirection;

            public double progress;

            public PipeItem(ItemResource variant, int amount, Direction dir) {
                this.variant = variant;
                this.amount = amount;
                this.direction = dir;
                this.lastDirection = dir;
                this.progress = 0.0;
            }
        }
    }
}
