package com.skniro.industrial_elixir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.MapleSignTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import java.util.function.Function;

public class MapleSignBlocks {

    public static final Block Rubber_SIGN = registerBlockWithoutItem("rubber_sign",(settings)-> new StandingSignBlock(MapleSignTypes.Rubber, settings),BlockBehaviour.Properties.of().mapColor(GeneralBlocks.Rubber_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
    public static final Block Rubber_WALL_SIGN = registerBlockWithoutItem("rubber_wall_sign",(settings)-> new WallSignBlock(MapleSignTypes.Rubber, settings),BlockBehaviour.Properties.of().mapColor(GeneralBlocks.Rubber_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava().overrideLootTable((Rubber_SIGN.getLootTable())));
    public static final Block Rubber_HANGING_SIGN = registerBlockWithoutItem("rubber_hanging_sign",(settings)-> new CeilingHangingSignBlock(MapleSignTypes.Rubber, settings),BlockBehaviour.Properties.of().mapColor(GeneralBlocks.Rubber_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
    public static final Block Rubber_WALL_HANGING_SIGN = registerBlockWithoutItem("rubber_wall_hanging_sign",(settings)->  new WallHangingSignBlock(MapleSignTypes.Rubber, settings),BlockBehaviour.Properties.of().mapColor(GeneralBlocks.Rubber_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava().overrideLootTable((Rubber_HANGING_SIGN.getLootTable())));


    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        Block block = (Block)factory.apply(settings.setId(keyOf(name)));
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, keyOf(name), block);
    }


    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)),
                new BlockItem(block, new Item.Properties().stacksTo(16).useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)))));
    }

    private static Block registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        Block block = (Block)factory.apply(settings.setId(keyOf(name)));
        return Registry.register(BuiltInRegistries.BLOCK, keyOf(name), block);
    }

    private static ResourceKey<Block> keyOf(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }

    public static void registerMapleSignBlocks() {
        IndustrialElixir.LOGGER.debug("Registering MapleSignBlocks for " + IndustrialElixir.MOD_ID);
    }
}
