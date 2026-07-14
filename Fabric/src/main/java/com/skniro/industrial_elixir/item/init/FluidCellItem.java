package com.skniro.industrial_elixir.item.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidCellItem extends Item {
    public static final long CAPACITY_MB = 1000;
    private static final Map<Fluid, Item> FILLED_CELLS = new HashMap<>();
    private final Fluid fluid;

    public FluidCellItem(Properties properties, Fluid fluid) {
        super(properties);
        this.fluid = fluid;
        registerStorage();
    }

    private void registerStorage() {
        if (fluid == Fluids.EMPTY) {
            FluidStorage.combinedItemApiProvider(this).register(this::createFillStorage);
        } else {
            FluidStorage.combinedItemApiProvider(this).register(this::createDrainStorage);
        }
    }

    public static void registerFluidCell(Fluid fluid, Item item) {
        FILLED_CELLS.put(fluid, item);
    }


    private EmptyItemFluidStorage createFillStorage(ContainerItemContext context) {
        Item filled = getFilledCell(fluid);

        if (filled == GrowableOresItems.EMPTY_CELL) {
            return null;
        }

        return new EmptyItemFluidStorage(context, empty -> ItemVariant.of(filled), fluid, CAPACITY_MB);
    }

    private FullItemFluidStorage createDrainStorage(ContainerItemContext context) {
        return new FullItemFluidStorage(context, full -> ItemVariant.of(GrowableOresItems.EMPTY_CELL), FluidVariant.of(fluid), CAPACITY_MB);
    }

    public static Item getFilledCell(Fluid fluid) {
        return FILLED_CELLS.getOrDefault(fluid, GrowableOresItems.EMPTY_CELL);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (fluid == Fluids.EMPTY) {
            return super.getName(stack);
        }
        return Component.literal(FluidVariantAttributes.getName(FluidVariant.of(fluid)).getString()).append(Component.translatable(FurnitureStrings.Fluid_Cell));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag flag) {
        if (fluid == Fluids.EMPTY) {
            textConsumer.accept(Component.literal("Capacity: 1000 mB").withStyle(ChatFormatting.GRAY));
        }

        if (fluid != Fluids.EMPTY) {
            textConsumer.accept(FluidVariantAttributes.getName(FluidVariant.of(fluid)).copy().withStyle(ChatFormatting.GRAY));
            textConsumer.accept(Component.literal("Stored: 1000 / 1000 mB").withStyle(ChatFormatting.GRAY));
        }
    }
}