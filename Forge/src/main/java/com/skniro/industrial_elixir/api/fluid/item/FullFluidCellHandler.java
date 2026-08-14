package com.skniro.industrial_elixir.api.fluid.item;

import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class FullFluidCellHandler implements ResourceHandler<FluidResource> {

    private final ItemAccess access;
    private final Fluid fluid;

    public FullFluidCellHandler(ItemAccess access, FluidCellItem cell) {
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
    public int size() {
        return 1;
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
        return resource.getFluid() == fluid;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.getFluid() != fluid) return 0;
        if (amount < FluidCellItem.CAPACITY_MB) return 0;

        ItemStack emptyCell = new ItemStack(GrowableOresItems.EMPTY_CELL.get());
        ItemResource emptyResource = ItemResource.of(emptyCell);

        try (Transaction nested = Transaction.open(transaction)) {
            if (access.extract(access.getResource(), 1, nested) == 1
                    && access.insert(emptyResource, 1, nested) == 1) {
                nested.commit();
                return FluidCellItem.CAPACITY_MB;
            }
        }
        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.getFluid() != fluid) return 0;
        if (amount != FluidCellItem.CAPACITY_MB) return 0;

        ItemStack emptyCell = new ItemStack(GrowableOresItems.EMPTY_CELL.get());
        ItemResource emptyResource = ItemResource.of(emptyCell);

        try (Transaction nested = Transaction.open(transaction)) {
            if (access.extract(access.getResource(), 1, nested) == 1
                    && access.insert(emptyResource, 1, nested) == 1) {
                nested.commit();
                return FluidCellItem.CAPACITY_MB;
            }
        }
        return 0;
    }
}
