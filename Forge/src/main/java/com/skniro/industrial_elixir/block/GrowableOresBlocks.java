package com.skniro.industrial_elixir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.ModContent;
import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.init.*;
import com.skniro.industrial_elixir.block.init.energybox.ChargePadBlock;
import com.skniro.industrial_elixir.block.init.energybox.EnergyBoxBlock;
import com.skniro.industrial_elixir.block.init.generator.init.ElectricHeaterBlock;
import com.skniro.industrial_elixir.block.init.generator.init.SolidFuelHeaterBlock;
import com.skniro.industrial_elixir.block.init.machine.fluid.BrewReactorBlock;
import com.skniro.industrial_elixir.block.init.container.fluid.FluidTankBlock;
import com.skniro.industrial_elixir.block.init.generator.CoalGeneratorBlock;
import com.skniro.industrial_elixir.block.init.generator.FluidGeneratorBlock;
import com.skniro.industrial_elixir.block.init.generator.SacredGeneratorBlock;
import com.skniro.industrial_elixir.block.init.generator.GeneratorSolarPanelBlock;
import com.skniro.industrial_elixir.block.init.generator.GeneratorWindMillBlock;
import com.skniro.industrial_elixir.block.init.generator.NuclearReactorBlock;
import com.skniro.industrial_elixir.block.init.machine.*;
import com.skniro.industrial_elixir.block.init.machine.MatterGeneratorBlock;
import com.skniro.industrial_elixir.block.init.machine.PatternStorageBlock;
import com.skniro.industrial_elixir.block.init.machine.fluid.CoffeeMachineBlock;
import com.skniro.industrial_elixir.block.init.machine.fluid.OreWashingBlock;
import com.skniro.industrial_elixir.block.init.machine.fluid.ReplicatorBlock;
import com.skniro.industrial_elixir.block.init.machine.heat.ModBlastFurnaceBlock;
import com.skniro.industrial_elixir.block.init.pipe.StoneFluidPipeBlock;
import com.skniro.industrial_elixir.block.init.pipe.WoodFluidPipeBlock;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowableOresBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, IndustrialElixir.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    public static final Supplier<Block> GrowableOres_Block =registerBlock("growableores_block", Alchemyblock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> Macerator_Block =registerBlock("macerator", MaceratorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> Compressor_Block =registerBlock("compressor", CompressorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> MATTER_GENERATOR = registerBlock("matter_generator", MatterGeneratorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> FLUID_GENERATOR = registerBlock("fluid_generator", FluidGeneratorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> MetalFormerBlock =registerBlock("metalformer", MetalFormerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final Supplier<Block> COPPER_CABLE = registerBlock("copper_cable", (settings)-> new CableBlock(settings, ModContent.Cables.COPPER, "copper"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> TIN_CABLE = registerBlock("tin_cable", (settings)-> new CableBlock(settings, ModContent.Cables.TIN, "tin"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> GOLD_CABLE = registerBlock("gold_cable", (settings)-> new CableBlock(settings, ModContent.Cables.GOLD, "gold"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> HV_CABLE = registerBlock("hv_cable", (settings)-> new CableBlock(settings, ModContent.Cables.HV, "hv"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> GLASSFIBER_CABLE = registerBlock("glassfiber_cable", (settings)-> new CableBlock(settings, ModContent.Cables.GLASSFIBER, "glassfiber"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> INSULATED_TIN_CABLE = registerBlock("insulated_tin_cable", (settings)-> new CableBlock(settings, ModContent.Cables.INSULATED_TIN, "insulated_tin"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> INSULATED_COPPER_CABLE = registerBlock("insulated_copper_cable", (settings)-> new CableBlock(settings, ModContent.Cables.INSULATED_COPPER, "insulated_copper"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> INSULATED_GOLD_CABLE = registerBlock("insulated_gold_cable", (settings)-> new CableBlock(settings, ModContent.Cables.INSULATED_GOLD, "insulated_gold"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));
    public static final Supplier<Block> INSULATED_HV_CABLE = registerBlock("insulated_hv_cable", (settings)-> new CableBlock(settings, ModContent.Cables.INSULATED_HV, "insulated_hv"), BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 8.0F));

    public static final Supplier<Block> COAL_GENERATOR = registerBlock("coal_generator", CoalGeneratorBlock::new, (BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> NUCLEAR_REACTOR = registerBlock("nuclear_reactor", NuclearReactorBlock::new, (BlockBehaviour.Properties.of().strength(5.0F, 10.0F).requiresCorrectToolForDrops().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> SACRED_GENERATOR = registerBlock("sacred_generator", (settings) -> new SacredGeneratorBlock(settings, EnergyTier.TIER3, 4096), (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final Supplier<Block> GENERATOR_Wind_Mill =registerBlock("generator_wind_mill", GeneratorWindMillBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> EnergyBox =registerBlock("energy_box", (settings)-> new EnergyBoxBlock(settings, 40000, EnergyTier.TIER1), (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> ChargePad =registerBlock("charge_pad", (settings)-> new ChargePadBlock(settings, 40000, EnergyTier.TIER1), (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> CESU = registerBlock("cesu", s -> new EnergyBoxBlock(s, 300000, EnergyTier.TIER2), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    public static final Supplier<Block> MFE = registerBlock("mfe", s -> new EnergyBoxBlock(s, 4000000, EnergyTier.TIER3), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    public static final Supplier<Block> MFSU = registerBlock("mfsu", s -> new EnergyBoxBlock(s, 40000000, EnergyTier.TIER4), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    public static final Supplier<Block> CESU_CHARGE_PAD = registerBlock("charge_pad_cesu", s -> new ChargePadBlock(s, 300000, EnergyTier.TIER2), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    public static final Supplier<Block> MFE_CHARGE_PAD = registerBlock("charge_pad_mfe", s -> new ChargePadBlock(s, 4000000, EnergyTier.TIER3), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    public static final Supplier<Block> MFSU_CHARGE_PAD = registerBlock("charge_pad_mfsu", s -> new ChargePadBlock(s, 40000000, EnergyTier.TIER4), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F));

    public static final Supplier<Block> MolecularTransformerBlock =registerBlock("molecular_transformer_block", MolecularTransformerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> GENERATOR_SolarPanel = registerSolar("generator_solar_panel", EnergyTier.TIER1, 1, 0, 1000);
    public static final Supplier<Block> GENERATOR_ADVANCED_SOLAR_PANEL = registerSolar("generator_advanced_solar_panel", EnergyTier.TIER2, 8, 1, 10000);
    public static final Supplier<Block> GENERATOR_HYBRID_SOLAR_PANEL = registerSolar("generator_hybrid_solar_panel", EnergyTier.TIER3, 64, 8, 100000);
    public static final Supplier<Block> GENERATOR_ULTIMATE_SOLAR_PANEL = registerSolar("generator_ultimate_solar_panel", EnergyTier.TIER4, 512, 64, 1000000);
    public static final Supplier<Block> GENERATOR_QUANTUM_SOLAR_PANEL = registerSolar("generator_quantum_solar_panel", EnergyTier.TIER5, 4096, 2048, 10000000);

    public static final Supplier<Block> Extractor_Block =registerBlock("extractor", ExtractorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> EV_TRANSFORMER =registerBlock("ev_transformer", TransformerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> HV_TRANSFORMER =registerBlock("hv_transformer", TransformerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> MV_TRANSFORMER =registerBlock("mv_transformer", TransformerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> LV_TRANSFORMER =registerBlock("lv_transformer", TransformerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> Iron_Furnace_Block =registerBlock("iron_furnace", IronFurnaceBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> ElectricFurnace_Block =registerBlock("electric_furnace", ElectricFurnaceBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> INDUCTION_FURNACE = registerBlock("induction_furnace", InductionFurnaceBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 4.0F)));
    public static final Supplier<Block> HEAT_CENTRIFUGE = registerBlock("heat_centrifuge", HeatCentrifugeBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 4.0F)));
    public static final Supplier<Block> PATTERN_STORAGE = registerBlock("pattern_storage", PatternStorageBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> CUTTING_Block =registerBlock("cutting_block", CuttingBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> RECYCLER_Block =registerBlock("recycler_block", RecyclerBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> Pipe_Wooden_Fluid_Block =registerBlock("pipe_wooden_fluid", WoodFluidPipeBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> Pipe_Stone_Fluid_Block =registerBlock("pipe_stone_fluid", StoneFluidPipeBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> FLUID_TANK_BLOCK =registerBlock("fluid_tank", FluidTankBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5F).noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> Brew_Reactor_BLOCK =registerBlock("brew_reactor_tank", BrewReactorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> Ore_Washing_Block =registerBlock("ore_washing_block", OreWashingBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> Replicator =registerBlock("replicator", ReplicatorBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(4.0F, 5.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final Supplier<Block> CHUNK_LOADER = registerBlock("chunk_loader", ChunkLoaderBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final Supplier<Block> Electric_Heater_Block =registerBlock("electric_heater", ElectricHeaterBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> SOLID_FUEL_HEATER_GENERATOR = registerBlock("solid_fuel_heater_generator", (properties)-> new SolidFuelHeaterBlock(properties,1000), (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> BLAST_FURNACE_BLOCK =registerBlock("blast_furnace", ModBlastFurnaceBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> CROP_FARM_Block =registerBlock("crop_farm", CropFarmBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> VENDOR_MACHINE_Block =registerBlock("vendor_machine", VendorMachineBlock::new, (BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final Supplier<Block> COFFEE_MACHINE_Block =registerBlock("coffee_machine_block", CoffeeMachineBlock::new, BlockBehaviour.Properties.of().noOcclusion().requiresCorrectToolForDrops().strength(3.0F, 3.0F));

    private static Supplier<Block> registerSolar(String name, EnergyTier tier, int DayPower, int NightPower, long capacity) {
        return registerBlock(name, (settings) -> new GeneratorSolarPanelBlock(settings, tier, DayPower, NightPower, capacity),
                BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.METAL).mapColor(MapColor.METAL));
    }

    public static <B extends Block> Supplier<Block> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        return BLOCKS.register(name, () -> {
            return (Block)func.apply(props.setId(ResourceKey.create(Registries.BLOCK, Helper.id(name))));
        });
    }

    private static <B extends Block> Supplier<Block> registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, ? extends B> block, BlockBehaviour.Properties properties) {
        Supplier<Block> register = register(name, block, properties.setId(ResourceKey.create(Registries.BLOCK, Helper.id(name))));
        return register;
    }

    private static <B extends Block> Supplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> block, BlockBehaviour.Properties properties) {
        Supplier<Block> bSupplier = registerBlockWithoutItem(name, block, properties);
        registerBlockItem(name, bSupplier);
        return bSupplier;
    }

    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, Helper.id(name)))));
    }

    public static void registerGrowableOresBlocks(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering GrowableOres Blocks for " + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
    }
}
