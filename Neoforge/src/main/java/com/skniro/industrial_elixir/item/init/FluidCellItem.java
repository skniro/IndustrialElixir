package com.skniro.industrial_elixir.item.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.DelegatingHeatStorage;
import com.skniro.industrial_elixir.energy.heat.impl.SimpleItemHeatStorageImpl;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class FluidCellItem extends Item {
    public static final int CAPACITY_MB = 1000;
    private static final Map<Fluid, Item> FILLED_CELLS = new HashMap<>();
    private final Fluid fluid;

    public FluidCellItem(Properties properties, Fluid fluid) {
        super(properties);
        this.fluid = fluid;
    }

/*    private void registerStorage() {
        if (fluid == Fluids.EMPTY) {
            FluidStorage.combinedItemApiProvider(this).register(this::createFillStorage);
        } else {
            FluidStorage.combinedItemApiProvider(this).register(this::createDrainStorage);
        }
    }*/

    public static void registerFluidCell(Fluid fluid, Item item) {
        FILLED_CELLS.put(fluid, item);
    }


/*
    private EmptyItemFluidStorage createFillStorage(ItemAccess context) {
        Item filled = getFilledCell(fluid);

        if (filled == GrowableOresItems.EMPTY_CELL) {
            return null;
        }

        return new EmptyItemFluidStorage(context, empty -> ItemVariant.of(filled), fluid, CAPACITY_MB);
    }

    private FullItemFluidStorage createDrainStorage(ContainerItemContext context) {
        return new FullItemFluidStorage(context, full -> ItemVariant.of(GrowableOresItems.EMPTY_CELL.get()), FluidVariant.of(fluid), CAPACITY_MB);
    }
*/

    public static Item getFilledCell(Fluid fluid) {
        return FILLED_CELLS.getOrDefault(fluid, GrowableOresItems.EMPTY_CELL.get());
    }

    @Override
    public Component getName(ItemStack stack) {
        if (fluid == Fluids.EMPTY) {
            return super.getName(stack);
        }
        return Component.literal(FluidResource.of(fluid).toString()).append(Component.translatable(FurnitureStrings.Fluid_Cell));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag flag) {
        if (fluid == Fluids.EMPTY) {
            textConsumer.accept(Component.literal("Capacity: 1000 mB").withStyle(ChatFormatting.GRAY));
        }

        if (fluid != Fluids.EMPTY) {
            textConsumer.accept(FluidResource.of(fluid).getHoverName().copy().withStyle(ChatFormatting.GRAY));
            textConsumer.accept(Component.literal("Stored: 1000 / 1000 mB").withStyle(ChatFormatting.GRAY));
        }
    }

    public Fluid getFluid(){
        return fluid;
    }
}