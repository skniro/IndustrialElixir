package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.fluid.init.MapleHotSpringFluidBlock;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
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
import java.util.function.Function;

public class IndustrialElixirFluidItems {
    public static final Item Fluid_UU_BUCKET = registerItem("fluid_uu_bucket",
            (settings)->  new BucketItem(IndustrialElixirFluids.STILL_Fluid_UU, settings), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Item Fluid_AIR_BUCKET = registerItem("fluid_air_bucket",
            (settings)->  new BucketItem(IndustrialElixirFluids.STILL_Fluid_AIR, settings), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Item UU_CELL = registerItem("uu_cell",
            (properties)-> new FluidCellItem(properties, IndustrialElixirFluids.STILL_Fluid_UU), new Item.Properties().craftRemainder(GrowableOresItems.EMPTY_CELL).stacksTo(16));
    public static final Item AIR_CELL = registerItem("air_cell",
            (properties)-> new FluidCellItem(properties, IndustrialElixirFluids.STILL_Fluid_AIR), new Item.Properties().craftRemainder(GrowableOresItems.EMPTY_CELL).stacksTo(16));
    public static final Item Hot_Spring_BUCKET = registerItem("hot_spring_bucket",
            (settings)->  new BucketItem(IndustrialElixirFluids.STILL_Hot_Spring, settings), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Item Hot_CELL = registerItem("hot_spring_cell",
            (settings)->  new FluidCellItem(settings, IndustrialElixirFluids.STILL_Hot_Spring), new Item.Properties().craftRemainder(GrowableOresItems.EMPTY_CELL).stacksTo(16));

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name))));
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)), item);
    }

    public static void registerFluidItems() {
        IndustrialElixir.LOGGER.info("Registering Industrial Elixir Fluid Items for " + IndustrialElixir.MOD_ID);
    }
}
