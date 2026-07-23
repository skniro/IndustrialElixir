package com.skniro.industrial_elixir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.MapleSignTypes;
import com.skniro.industrial_elixir.block.init.LogCropBlock;
import com.skniro.industrial_elixir.block.init.MapleBlockSetType;
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
import java.util.function.Function;

public class GeneralBlocks {
    public static final Block Lead_Ore = registerBlock("lead_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
    public static final Block Tin_Ore = registerBlock("tin_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
    public static final Block SACRED_Ore = registerBlock("sacred_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
    public static final Block Deepslate_Lead_Ore = registerBlock("deepslate_lead_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
    public static final Block Deepslate_Tin_Ore = registerBlock("deepslate_tin_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));
    public static final Block Deepslate_SACRED_Ore = registerBlock("deepslate_sacred_ore", (settings)-> new DropExperienceBlock(UniformInt.of(2, 4), settings),BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
    public static final Block Raw_Lead_Block = registerBlock("raw_lead_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Raw_Tin_Block = registerBlock("raw_tin_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Raw_SACRED_Block = registerBlock("raw_sacred_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Lead_Block = registerBlock("lead_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Tin_Block = registerBlock("tin_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block SACRED_Block = registerBlock("sacred_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Silver_Block = registerBlock("silver_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Bronze_Block = registerBlock("bronze_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Steel_Block = registerBlock("steel_block", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Advanced_Machine = registerBlock("advanced_machine", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Machine = registerBlock("machine", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));
    public static final Block Super_Machine = registerBlock("super_machine", Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F));

    public static final Block Rubber_SAPLING = registerBlock("rubber_sapling",
            (settings)-> new SaplingBlock(RubberSaplingGenerator.RubberSapling, settings), BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SAPLING));

    public static final Block POTTED_Rubber_SAPLING = registerBlockWithoutItem("potted_rubber_sapling",
            (settings)-> new FlowerPotBlock(Rubber_SAPLING, settings), BlockBehaviour.Properties.of().instabreak().noOcclusion());

    public static final Block Rubber_LEAVES =registerBlock("rubber_leaves",
            (settings)-> new TintedParticleLeavesBlock(0.1f, settings), (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.TERRACOTTA_GREEN)));

    public static final Block Rubber_Rubber_LOG =registerBlock("rubber_rubber_log",
            (settings)-> new LogCropBlock(settings, GrowableOresItems.Sticky_Resin), BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.NETHER));

    public static final Block Rubber_LOG = registerBlock("rubber_log",RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_BROWN)));
    public static final Block STRIPPED_Rubber_LOG = registerBlock("stripped_rubber_log",
            RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.COLOR_BROWN)));
    public static final Block STRIPPED_Rubber_WOOD = registerBlock("stripped_rubber_wood",
            RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.COLOR_BROWN)));
    public static final Block Rubber_WOOD = registerBlock("rubber_wood",
            RotatedPillarBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BROWN)));

    public static final Block Rubber_PLANKS = registerBlock("rubber_planks",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.COLOR_BROWN)));
    public static final Block Rubber_BUTTON = registerBlock("rubber_button",
            (settings)-> new ButtonBlock(MapleBlockSetType.Rubber,30, settings), Blocks.buttonProperties());
    public static final Block Rubber_STAIRS = registerBlock("rubber_stairs",
            (settings)-> new StairBlock(Blocks.OAK_PLANKS.defaultBlockState(), settings), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final Block Rubber_SLAB = registerBlock("rubber_slab",
            SlabBlock::new, (BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Block Rubber_FENCE_GATE = registerBlock("rubber_fence_gate",
            (settings)-> new FenceGateBlock(MapleSignTypes.Rubber, settings),  BlockBehaviour.Properties.of().mapColor(Rubber_PLANKS.defaultMapColor()).strength(2.0F, 3.0F));
    public static final Block Rubber_FENCE = registerBlock("rubber_fence",
            FenceBlock::new, (BlockBehaviour.Properties.of().mapColor(Rubber_PLANKS.defaultMapColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Block Rubber_DOOR = registerBlock("rubber_door",
            (settings)-> new DoorBlock(BlockSetType.CHERRY, settings), BlockBehaviour.Properties.of().mapColor(Rubber_PLANKS.defaultMapColor()).strength(3.0f).sound(SoundType.WOOD).noOcclusion());
    public static final Block Rubber_TRAPDOOR = registerBlock("rubber_trapdoor",
            (settings)-> new TrapDoorBlock(MapleBlockSetType.Rubber, settings),BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).strength(3.0F).noOcclusion());
    public static final Block Rubber_PRESSURE_PLATE = registerBlock("rubber_pressure_plate",
            (settings)-> new PressurePlateBlock(MapleBlockSetType.Rubber, settings), BlockBehaviour.Properties.of().mapColor(
                    Rubber_PLANKS.defaultMapColor()).noCollision().strength(0.5F).ignitedByLava().instrument(NoteBlockInstrument.BASS).pushReaction(PushReaction.DESTROY));

    public static final Block Rubber_SHELF = registerBlock("rubber_shelf",
            ShelfBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF).ignitedByLava().strength(2.0F, 3.0F));

    public static final Block Reinforced_Stone = registerBlock("reinforced_stone",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)).mapColor(MapColor.TERRACOTTA_GRAY).strength(100.0F, 1000.0F));
    public static final Block Reinforced_Glass = registerBlock("reinforced_glass",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.TERRACOTTA_GRAY).strength(80.0F, 800.0F)));

    public static final Block Reinforced_DOOR = registerBlock("reinforced_door",
            (settings)-> new DoorBlock(BlockSetType.IRON, settings), BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).strength(80.0F, 1000.0F).noOcclusion().pushReaction(PushReaction.IGNORE));

    //PLASTER
    public static final Block GREEN_PLASTER =registerBlock("green_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE)));
    public static final Block PLASTER =registerBlock("plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final Block ORANGE_PLASTER =registerBlock("orange_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_CONCRETE)));
    public static final Block MAGENTA_PLASTER =registerBlock("magenta_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CONCRETE)));
    public static final Block LIGHT_BLUE_PLASTER =registerBlock("light_blue_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE)));
    public static final Block YELLOW_PLASTER =registerBlock("yellow_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE)));
    public static final Block LIME_PLASTER =registerBlock("lime_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_CONCRETE)));
    public static final Block PINK_PLASTER =registerBlock("pink_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_CONCRETE)));
    public static final Block GRAY_PLASTER =registerBlock("gray_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE)));
    public static final Block LIGHT_GRAY_PLASTER =registerBlock("light_gray_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_CONCRETE)));
    public static final Block CYAN_PLASTER =registerBlock("cyan_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_CONCRETE)));
    public static final Block PURPLE_PLASTER =registerBlock("purple_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_CONCRETE)));
    public static final Block BLUE_PLASTER =registerBlock("blue_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CONCRETE)));
    public static final Block BROWN_PLASTER =registerBlock("brown_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE)));
    public static final Block RED_PLASTER =registerBlock("red_plaster",
            Block::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE)));

    //Coffee
    public static final Block Coffee_Block = registerBlock("coffee_block", (properties)-> new LogCropBlock(properties, MapleFoodComponents.Coffee_Beans),BlockBehaviour.Properties.of().noOcclusion().requiresCorrectToolForDrops().strength(3.0F, 3.0F));


    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        Block block = (Block)factory.apply(settings.setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name))));
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)))));
    }

    private static Block registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        Block block = (Block)factory.apply(settings.setId(keyOf(name)));
        return Registry.register(BuiltInRegistries.BLOCK, keyOf(name), block);
    }

    private static ResourceKey<Block> keyOf(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }

    public static void registerNetherOresBlock() {
        IndustrialElixir.LOGGER.info("register Mod Nether Ores Blocks"+ IndustrialElixir.MOD_ID);
    }
}
