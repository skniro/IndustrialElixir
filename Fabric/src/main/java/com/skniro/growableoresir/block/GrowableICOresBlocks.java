package com.skniro.growableoresir.block;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.growableoresir.block.init.GrowableOreCaneBlock;
import java.util.function.Function;
import java.util.logging.Logger;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class GrowableICOresBlocks {
    //Industrial Elixir
    public static final Block IER_steel_Cane =registerBlock("ier_steel_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Block IER_silver_Cane =registerBlock("ier_silver_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Block IER_Tin_Cane =registerBlock("ier_tin_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Block IER_SACRED_Cane =registerBlock("ier_sacred_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Block IER_Bronze_Cane =registerBlock("ier_bronze_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    public static final Block IER_LEAD_Cane =registerBlock("ier_lead_ore_cane", GrowableOreCaneBlock::new ,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));


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
    public static void registerModBlocks(){
        Logger.getLogger("register mod blocks" + IndustrialElixir.MOD_ID);
    }
}

