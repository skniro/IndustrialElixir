package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.fluid.init.MapleHotSpringFluidBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Function;

public class IndustrialElixirFluidBlocks {
    public static final Block Fluid_UU_BLOCK = registerBlockWithoutItem( "fluid_uu_block",
            (settings)-> new MapleHotSpringFluidBlock(IndustrialElixirFluids.STILL_Fluid_UU, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER));

    private static Block registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        Block block = (Block)factory.apply(settings.setId(keyOf(name)));
        return Registry.register(BuiltInRegistries.BLOCK, keyOf(name), block);
    }

    private static ResourceKey<Block> keyOf(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }

    public static void registerFluidBlocks() {
        IndustrialElixir.LOGGER.info("Registering Industrial Elixir Fluid Blocks for " + IndustrialElixir.MOD_ID);
    }
}
