package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.fluid.init.MapleHotSpringFluidBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class IndustrialElixirFluidBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, IndustrialElixir.MOD_ID);

    public static final Supplier<Block> Hot_Spring_BLOCK = registerBlockWithoutItem( "hot_spring_block",
                                                         (settings)-> new MapleHotSpringFluidBlock(IndustrialElixirFluids.STILL_Hot_Spring.get(), settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel((state)->{return 7;}));

    public static final Supplier<Block> Fluid_UU_BLOCK = registerBlockWithoutItem( "fluid_uu_block",
            (settings)-> new MapleHotSpringFluidBlock(IndustrialElixirFluids.STILL_Fluid_UU.get(), settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER));

    public static <B extends Block> Supplier<Block> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        return BLOCKS.register(name, () -> {
            return (Block)func.apply(props.setId(ResourceKey.create(Registries.BLOCK, Helper.id(name))));
        });
    }

    private static <B extends Block> Supplier<Block> registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, ? extends B> block, BlockBehaviour.Properties properties) {
        Supplier<Block> register = register(name, block, properties.setId(ResourceKey.create(Registries.BLOCK, Helper.id(name))));
        return register;
    }

    public static void registerFluidBlocks(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering Industrial Elixir Fluid Blocks for " + IndustrialElixir.MOD_ID);
        BLOCKS.register(eventBus);
    }
}
