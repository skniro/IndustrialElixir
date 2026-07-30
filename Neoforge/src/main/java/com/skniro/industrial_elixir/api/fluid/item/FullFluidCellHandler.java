package com.skniro.industrial_elixir.api.fluid.item;

import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class FullFluidCellHandler implements ResourceHandler<FluidResource> {

    private final ItemStack stack;
    private final ItemAccess access;
    private final Fluid fluid;

    public FullFluidCellHandler(ItemStack stack, ItemAccess access, FluidCellItem cell) {
        this.stack = stack;
        this.access = access;
        this.fluid = cell.getFluid();
    }

    @Override
    public FluidResource getResource(int index) {
        return FluidResource.of(fluid);
    }

    public int getAmount() {
        return FluidCellItem.CAPACITY_MB;
    }

    public int getCapacity() {
        return FluidCellItem.CAPACITY_MB;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (!resource.matches(new FluidStack(fluid, amount))) {
            return 0;
        }

        if (amount < FluidCellItem.CAPACITY_MB) {
            return 0;
        }

        ItemStack emptyCell = new ItemStack(GrowableOresItems.EMPTY_CELL.get());
        return FluidCellItem.CAPACITY_MB;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public long getAmountAsLong(int index) {
        return getAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return getCapacity();
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return false;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return 0;
    }
}