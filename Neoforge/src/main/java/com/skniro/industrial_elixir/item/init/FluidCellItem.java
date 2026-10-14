package com.skniro.industrial_elixir.item.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidCellItem extends Item {
    public static final int CAPACITY_MB = 1000;
    private static final Map<Fluid, Item> FILLED_CELLS = new HashMap<>();
    private final Fluid fluid;

    public FluidCellItem(Properties properties, Fluid fluid) {
        super(properties);
        this.fluid = fluid;
    }

    public static void registerFluidCell(Fluid fluid, Item item) {
        FILLED_CELLS.put(fluid, item);
    }

    public static Item getFilledCell(Fluid fluid) {
        return FILLED_CELLS.getOrDefault(fluid, GrowableOresItems.EMPTY_CELL.get());
    }

    @Override
    public Component getName(ItemStack stack) {
        if (fluid == Fluids.EMPTY) {
            return super.getName(stack);
        }
        return new FluidStack(fluid, 1).getHoverName().copy().append(Component.translatable(FurnitureStrings.Fluid_Cell));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> textConsumer, TooltipFlag flag) {
        if (fluid == Fluids.EMPTY) {
            textConsumer.add(Component.literal("Capacity: 1000 mB").withStyle(ChatFormatting.GRAY));
        }

        if (fluid != Fluids.EMPTY) {
            textConsumer.add(new FluidStack(fluid, 1).getHoverName().copy().withStyle(ChatFormatting.GRAY));
            textConsumer.add(Component.literal("Stored: 1000 / 1000 mB").withStyle(ChatFormatting.GRAY));
        }
    }

    public Fluid getFluid(){
        return fluid;
    }
}
