package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.fluid.init.MapleHotSpringFluidBlock;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.item.init.ModBucketItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class IndustrialElixirFluidItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, IndustrialElixir.MOD_ID);

    public static final Supplier<Item> Fluid_UU_BUCKET = registerItem("fluid_uu_bucket",
            (settings)->  new BucketItem(IndustrialElixirFluids.STILL_Fluid_UU.get(), settings), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Supplier<Item> Fluid_AIR_BUCKET = registerItem("fluid_air_bucket",
            (settings)->  new BucketItem(IndustrialElixirFluids.STILL_Fluid_AIR.get(), settings), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Supplier<Item> UU_CELL = registerItem("uu_cell",
            (properties)-> new FluidCellItem(properties, IndustrialElixirFluids.STILL_Fluid_UU.get()), new Item.Properties().craftRemainder(GrowableOresItems.EMPTY_CELL.get()).stacksTo(16));
    public static final Supplier<Item> AIR_CELL = registerItem("air_cell",
            (properties)-> new FluidCellItem(properties, IndustrialElixirFluids.STILL_Fluid_AIR.get()), new Item.Properties().craftRemainder(GrowableOresItems.EMPTY_CELL.get()).stacksTo(16));
    public static final Supplier<Item> Hot_Spring_BUCKET = registerItem("hot_spring_bucket",
            (settings)->  new ModBucketItem(IndustrialElixirFluids.STILL_Hot_Spring.get(), settings), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Supplier<Item> Hot_Spring_CELL = registerItem("hot_spring_cell",
            (settings)->  new FluidCellItem(settings, IndustrialElixirFluids.STILL_Hot_Spring.get()), new Item.Properties().craftRemainder(GrowableOresItems.EMPTY_CELL.get()).stacksTo(16));

    public static <B extends Item> Supplier<Item> register(String name, Function<Item.Properties, ? extends B> func, Item.Properties props) {
        return ITEMS.register(name, () -> {
            return (Item)func.apply(props.setId(ResourceKey.create(Registries.ITEM, Helper.id(name))));
        });
    }

    private static <T extends Item> Supplier<Item> registerItem(String name, Function<Item.Properties, ? extends T> item, Item.Properties properties) {
        Supplier<Item> toReturn = register(name, item, properties.setId(ResourceKey.create(Registries.ITEM, Helper.id(name))));
        return toReturn;
    }

    public static void registerFluidItems(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering Industrial Elixir Fluid Items for " + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
    }
}
