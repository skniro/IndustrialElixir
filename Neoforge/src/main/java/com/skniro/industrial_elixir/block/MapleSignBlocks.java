package com.skniro.industrial_elixir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
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
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class MapleSignBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, IndustrialElixir.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    public static final Supplier<Block> Rubber_SIGN = registerBlockWithoutItemDeferred("rubber_sign",(settings)-> new StandingSignBlock(MapleSignTypes.Rubber, settings),() -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
    public static final Supplier<Block> Rubber_WALL_SIGN = registerBlockWithoutItemDeferred("rubber_wall_sign",(settings)-> new WallSignBlock(MapleSignTypes.Rubber, settings),() -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava().overrideLootTable((Rubber_SIGN.get().getLootTable())));
    public static final Supplier<Block> Rubber_HANGING_SIGN = registerBlockWithoutItemDeferred("rubber_hanging_sign",(settings)-> new CeilingHangingSignBlock(MapleSignTypes.Rubber, settings),() -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
    public static final Supplier<Block> Rubber_WALL_HANGING_SIGN = registerBlockWithoutItemDeferred("rubber_wall_hanging_sign",(settings)->  new WallHangingSignBlock(MapleSignTypes.Rubber, settings),() -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava().overrideLootTable((Rubber_HANGING_SIGN.get().getLootTable())));


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

    private static <B extends Block> Supplier<Block> registerBlockWithoutItemDeferred(String name, Function<BlockBehaviour.Properties, ? extends B> block, Supplier<BlockBehaviour.Properties> propertiesSupplier) {
        Supplier<Block> bSupplier = BLOCKS.register(name, () -> {
            BlockBehaviour.Properties properties = propertiesSupplier.get();
            return block.apply(properties.setId(ResourceKey.create(Registries.BLOCK, Helper.id(name))));
        });
        return bSupplier;
    }

    public static void registerMapleSignBlocks(IEventBus eventBus) {
        IndustrialElixir.LOGGER.debug("Registering MapleSignBlocks for " + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
    }
}
