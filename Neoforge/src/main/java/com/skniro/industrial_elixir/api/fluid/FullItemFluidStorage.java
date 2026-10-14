package com.skniro.industrial_elixir.api.fluid;

import java.util.function.Function;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * The fluid view of an already filled item, e.g. a fluid cell.
 *
 * <p>It can only be drained, and draining it replaces the container by the "empty" item that is
 * produced by {@code fullToEmptyMapping}. As 1.21.1 item capabilities give access to the queried
 * stack only, the replacement is exposed through {@link #getContainer()}.
 */
public final class FullItemFluidStorage implements IFluidHandlerItem {
	private final Function<ItemStack, ItemStack> fullToEmptyMapping;
	private final FluidStack containedFluid;
	private final Item fullItem;
	private ItemStack container;

	public FullItemFluidStorage(ItemStack container, Item emptyItem, FluidStack containedFluid) {
		this(container, full -> new ItemStack(emptyItem), containedFluid);
	}

	public FullItemFluidStorage(ItemStack container, Function<ItemStack, ItemStack> fullToEmptyMapping, FluidStack containedFluid) {
		if (containedFluid.isEmpty()) throw new IllegalArgumentException("The contained fluid must not be empty");

		this.container = container;
		this.fullItem = container.getItem();
		this.fullToEmptyMapping = fullToEmptyMapping;
		this.containedFluid = containedFluid.copy();
	}

	@Override
	public ItemStack getContainer() {
		return container;
	}

	private boolean hasFluid() {
		return !container.isEmpty() && container.getItem() == fullItem;
	}

	public FluidStack getFluid() {
		return hasFluid() ? containedFluid : FluidStack.EMPTY;
	}

	public int getAmount() {
		return hasFluid() ? containedFluid.getAmount() : 0;
	}

	public int getCapacity() {
		return getAmount();
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return false;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (!hasFluid() || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(containedFluid, resource)) {
			return FluidStack.EMPTY;
		}

		return drain(resource.getAmount(), action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (!hasFluid() || maxDrain < containedFluid.getAmount()) return FluidStack.EMPTY;

		FluidStack drained = containedFluid.copy();

		if (action.execute()) {
			container = fullToEmptyMapping.apply(container);
		}

		return drained;
	}

	@Override
	public String toString() {
		return "FullItemFluidStorage[fluid=%s, amount=%d]".formatted(containedFluid, getAmount());
	}
}
