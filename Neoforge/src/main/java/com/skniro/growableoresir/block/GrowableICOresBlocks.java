package com.skniro.growableoresir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.growableoresir.block.init.GrowableOreCaneBlock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowableICOresBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, IndustrialElixir.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    //Industrial Elixir
    public static final Supplier<Block> IER_steel_Cane =registerBlock("ier_steel_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> IER_silver_Cane =registerBlock("ier_silver_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> IER_Tin_Cane =registerBlock("ier_tin_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> IER_SACRED_Cane =registerBlock("ier_sacred_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> IER_Bronze_Cane =registerBlock("ier_bronze_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> IER_LEAD_Cane =registerBlock("ier_lead_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));


    public static <B extends Block> Supplier<Block> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        return BLOCKS.register(name, () -> (Block) func.apply(props));
    }

    private static <B extends Block> Supplier<Block> registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, ? extends B> block, BlockBehaviour.Properties properties) {
        return register(name, block, properties);
    }

    private static <B extends Block> Supplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> block, BlockBehaviour.Properties properties) {
        Supplier<Block> bSupplier = registerBlockWithoutItem(name, block, properties);
        registerBlockItem(name, bSupplier);
        return bSupplier;
    }

    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void registerModBlocks(IEventBus eventBus){
        Logger.getLogger("register mod blocks" + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
    }
}

