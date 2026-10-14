package com.skniro.industrial_elixir.api.fluid.item;

import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Fluid capability of a fluid cell.
 *
 * <p>A cell with a fluid contains exactly {@link FluidCellItem#CAPACITY_MB} of it and can be drained
 * into an empty cell, and an empty cell can be filled into the matching fluid cell.
 *
 * <p>As the cell item changes, the resulting stack is exposed through {@link #getContainer()}.
 */
public class FullFluidCellHandler implements IFluidHandlerItem {

    private final FluidCellItem cell;
    private ItemStack container;

    public FullFluidCellHandler(ItemStack container, FluidCellItem cell) {
        this.container = container;
        this.cell = cell;
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }

    private boolean isFilled() {
        return cell.getFluid() != Fluids.EMPTY;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return isFilled() ? new FluidStack(cell.getFluid(), FluidCellItem.CAPACITY_MB) : FluidStack.EMPTY;
    }

    public int getAmount() {
        return isFilled() ? FluidCellItem.CAPACITY_MB : 0;
    }

    public int getCapacity() {
        return FluidCellItem.CAPACITY_MB;
    }

    @Override
    public int getTankCapacity(int tank) {
        return getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return !isFilled() || stack.getFluid() == cell.getFluid();
    }

    /**
     * Filling an empty cell converts it into the matching filled cell.
     */
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (isFilled() || resource.isEmpty()) return 0;

        Item filledItem = FluidCellItem.getFilledCell(resource.getFluid());

        if (filledItem == GrowableOresItems.EMPTY_CELL.get()) return 0;
        if (resource.getAmount() < FluidCellItem.CAPACITY_MB) return 0;

        if (action.execute()) {
            container = new ItemStack(filledItem);
        }

        return FluidCellItem.CAPACITY_MB;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack contained = getFluidInTank(0);

        if (contained.isEmpty() || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(contained, resource)) {
            return FluidStack.EMPTY;
        }

        return drain(resource.getAmount(), action);
    }

    /**
     * Draining a filled cell converts it back into an empty cell.
     */
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (!isFilled() || maxDrain < FluidCellItem.CAPACITY_MB) return FluidStack.EMPTY;

        FluidStack drained = getFluidInTank(0);

        if (action.execute()) {
            container = new ItemStack(GrowableOresItems.EMPTY_CELL.get());
        }

        return drained;
    }
}
