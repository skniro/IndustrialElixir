package com.skniro.industrial_elixir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.entity.MapleSignTypes;
import com.skniro.industrial_elixir.block.init.*;
import com.skniro.industrial_elixir.block.init.machine.fluid.CoffeeMachineBlock;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleFoodComponents;
import com.skniro.industrial_elixir.world.Tree.RubberSaplingGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class GeneralBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, IndustrialElixir.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    public static final Supplier<Block> Lead_Ore = registerBlock("lead_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
    public static final Supplier<Block> Tin_Ore = registerBlock("tin_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
    public static final Supplier<Block> SACRED_Ore = registerBlock("sacred_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
    public static final Supplier<Block> Deepslate_Lead_Ore = registerBlock("deepslate_lead_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
    public static final Supplier<Block> Deepslate_Tin_Ore = registerBlock("deepslate_tin_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));
    public static final Supplier<Block> Deepslate_SACRED_Ore = registerBlock("deepslate_sacred_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
    public static final Supplier<Block> Raw_Lead_Block = registerBlock("raw_lead_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Raw_Tin_Block = registerBlock("raw_tin_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Raw_SACRED_Block = registerBlock("raw_sacred_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Lead_Block = registerBlock("lead_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Tin_Block = registerBlock("tin_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> SACRED_Block = registerBlock("sacred_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Silver_Block = registerBlock("silver_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Bronze_Block = registerBlock("bronze_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Steel_Block = registerBlock("steel_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Advanced_Machine = registerBlock("advanced_machine", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Machine = registerBlock("machine", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Supplier<Block> Super_Machine = registerBlock("super_machine", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));

    public static final Supplier<Block> Rubber_SAPLING = registerBlock("rubber_sapling",
            (settings)-> new SaplingBlock(RubberSaplingGenerator.RubberSapling, settings), BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SAPLING));

    public static final Supplier<Block> POTTED_Rubber_SAPLING = registerBlockWithoutItem("potted_rubber_sapling",
            (settings)-> new FlowerPotBlock(Rubber_SAPLING.get(), settings), BlockBehaviour.Properties.of().instabreak().noOcclusion());

    public static final Supplier<Block> Rubber_LEAVES =registerBlock("rubber_leaves",
            (settings)-> new TintedParticleLeavesBlock(0.1f, settings), (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.TERRACOTTA_GREEN)));

    public static final Supplier<Block> Rubber_Rubber_LOG =registerBlock("rubber_rubber_log",
            (settings)-> new LogCropBlock(settings, GrowableOresItems.Sticky_Resin), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_BROWN));

    public static final Supplier<Block> Rubber_LOG = registerBlock("rubber_log",RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> STRIPPED_Rubber_LOG = registerBlock("stripped_rubber_log",
            RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> STRIPPED_Rubber_WOOD = registerBlock("stripped_rubber_wood",
            RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> Rubber_WOOD = registerBlock("rubber_wood",
            RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BROWN)));

    public static final Supplier<Block> Rubber_PLANKS = registerBlock("rubber_planks",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> Rubber_BUTTON = registerBlock("rubber_button",
            (settings)-> new ButtonBlock(MapleBlockSetType.Rubber,30, settings), BlockBehaviour.Properties.of().noCollision().strength(0.5F).pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> Rubber_STAIRS = registerBlock("rubber_stairs",
            (settings)-> new ModStairBlock(Blocks.OAK_PLANKS.defaultBlockState(), settings), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS));
    public static final Supplier<Block> Rubber_SLAB = registerBlock("rubber_slab",
            SlabBlock::new, (BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> Rubber_FENCE_GATE = registerBlockDeferred("rubber_fence_gate",
            (settings)-> new FenceGateBlock(MapleSignTypes.Rubber, settings),  () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(2.0F, 3.0F));
    public static final Supplier<Block> Rubber_FENCE = registerBlockDeferred("rubber_fence",
            FenceBlock::new, () -> (BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Supplier<Block> Rubber_DOOR = registerBlockDeferred("rubber_door",
            (settings)-> new DoorBlock(BlockSetType.CHERRY, settings), () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(3.0f).sound(SoundType.WOOD).noOcclusion());
    public static final Supplier<Block> Rubber_TRAPDOOR = registerBlock("rubber_trapdoor",
            (settings)-> new TrapDoorBlock(MapleBlockSetType.Rubber, settings),BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).strength(3.0F).noOcclusion());
    public static final Supplier<Block> Rubber_PRESSURE_PLATE = registerBlockDeferred("rubber_pressure_plate",
            (settings)-> new PressurePlateBlock(MapleBlockSetType.Rubber, settings), () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN)
                    .noCollision().strength(0.5F).ignitedByLava().instrument(NoteBlockInstrument.BASS).pushReaction(PushReaction.DESTROY));

    public static final Supplier<Block> Rubber_SHELF = registerBlock("rubber_shelf",
            ShelfBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF).ignitedByLava().strength(2.0F, 3.0F));

    public static final Supplier<Block> Reinforced_Stone = registerBlock("reinforced_stone",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)).mapColor(MapColor.TERRACOTTA_GRAY).strength(100.0F, 1000.0F));
    public static final Supplier<Block> Reinforced_Glass = registerBlock("reinforced_glass",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.TERRACOTTA_GRAY).strength(80.0F, 800.0F)));

    public static final Supplier<Block> Reinforced_DOOR = registerBlock("reinforced_door",
            (settings)-> new DoorBlock(BlockSetType.IRON, settings), BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).strength(80.0F, 1000.0F).noOcclusion().pushReaction(PushReaction.IGNORE));

    //PLASTER
    public static final Supplier<Block> GREEN_PLASTER =registerBlock("green_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE)));
    public static final Supplier<Block> PLASTER =registerBlock("plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final Supplier<Block> ORANGE_PLASTER =registerBlock("orange_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_CONCRETE)));
    public static final Supplier<Block> MAGENTA_PLASTER =registerBlock("magenta_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CONCRETE)));
    public static final Supplier<Block> LIGHT_BLUE_PLASTER =registerBlock("light_blue_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE)));
    public static final Supplier<Block> YELLOW_PLASTER =registerBlock("yellow_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE)));
    public static final Supplier<Block> LIME_PLASTER =registerBlock("lime_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_CONCRETE)));
    public static final Supplier<Block> PINK_PLASTER =registerBlock("pink_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_CONCRETE)));
    public static final Supplier<Block> GRAY_PLASTER =registerBlock("gray_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE)));
    public static final Supplier<Block> LIGHT_GRAY_PLASTER =registerBlock("light_gray_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_CONCRETE)));
    public static final Supplier<Block> CYAN_PLASTER =registerBlock("cyan_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_CONCRETE)));
    public static final Supplier<Block> PURPLE_PLASTER =registerBlock("purple_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_CONCRETE)));
    public static final Supplier<Block> BLUE_PLASTER =registerBlock("blue_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CONCRETE)));
    public static final Supplier<Block> BROWN_PLASTER =registerBlock("brown_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE)));
    public static final Supplier<Block> RED_PLASTER =registerBlock("red_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE)));

    //Coffee
    public static final Supplier<Block> Coffee_Block = registerBlock("coffee_block", (properties)-> new CoffeeBlock(properties),BlockBehaviour.Properties.of().noOcclusion().requiresCorrectToolForDrops().strength(3.0F, 3.0F));

    public static final Supplier<Block> COFFEE_BLACK = registerBlockWithoutItem("coffee_black", CupSpecialBlock::new, BlockBehaviour.Properties.of().strength(0.5F).noOcclusion());
    public static final Supplier<Block> CAPPUCCINO = registerBlockWithoutItem("cappuccino", CupBlock::new, BlockBehaviour.Properties.of().strength(0.5F).noOcclusion());
    public static final Supplier<Block> LATTE = registerBlockWithoutItem("latte", CupBlock::new, BlockBehaviour.Properties.of().strength(0.5F).noOcclusion());
    public static final Supplier<Block> MOCHA = registerBlockWithoutItem("mocha", CupBlock::new, BlockBehaviour.Properties.of().strength(0.5F).noOcclusion());
    public static final Supplier<Block> HOT_COCOA = registerBlockWithoutItem("hot_cocoa", CupBlock::new, BlockBehaviour.Properties.of().strength(0.5F).noOcclusion());

    public static final Supplier<Block> COBBLESTONE_STAIRS = registerBlock("cobblestone_stairs",
            (settings)-> new ModStairBlock(Blocks.COBBLESTONE_STAIRS.defaultBlockState(), settings), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_STAIRS));

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

    private static <B extends Block> Supplier<Block> registerBlockDeferred(String name, Function<BlockBehaviour.Properties, ? extends B> block, Supplier<BlockBehaviour.Properties> propertiesSupplier) {
        Supplier<Block> bSupplier = BLOCKS.register(name, () -> {
            BlockBehaviour.Properties properties = propertiesSupplier.get();
            return block.apply(properties.setId(ResourceKey.create(Registries.BLOCK, Helper.id(name))));
        });
        registerBlockItem(name, bSupplier);
        return bSupplier;
    }

    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, Helper.id(name)))));
    }

    public static void registerNetherOresBlock(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("register Mod Nether Ores Blocks"+ IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
    }
}
