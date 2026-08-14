package com.skniro.industrial_elixir.api.fluid;

import java.util.function.Function;



public final class FullItemFluidStorage extends SingleFluidStorage {
	private final ItemAccess context;
	private final ItemResource fullItem;
	private final Function<ItemResource, ItemResource> fullToEmptyMapping;
	private final FluidResource containedFluid;
	private final int containedAmount;

	public FullItemFluidStorage(ItemAccess context, ItemResource emptyItem, FluidResource containedFluid, int containedAmount) {
		this(context, fullVariant -> emptyItem, containedFluid, containedAmount);
	}

	public FullItemFluidStorage(ItemAccess context, Function<ItemResource, ItemResource> fullToEmptyMapping, FluidResource containedFluid, int containedAmount) {
		TransferPreconditions.checkNonEmptyNonNegative(containedFluid, containedAmount);

		this.context = context;
		this.fullItem = context.getResource();
		this.fullToEmptyMapping = fullToEmptyMapping;
		this.containedFluid = containedFluid;
		this.containedAmount = containedAmount;
	}

	@Override
	public int extract(FluidResource resource, int maxAmount, TransactionContext transaction) {
		TransferPreconditions.checkNonEmptyNonNegative(resource, maxAmount);

		if (!context.getResource().matches(fullItem.toStack())) return 0;

		if (resource.equals(containedFluid) && maxAmount >= containedAmount) {
			ItemResource newVariant = fullToEmptyMapping.apply(context.getResource());

			if (context.exchange(newVariant, 1, transaction) == 1) {
				return containedAmount;
			}
		}

		return 0;
	}

	@Override
	public int insert(FluidResource resource, int maxAmount, TransactionContext transaction) {
		return 0;
	}

	@Override
	protected int getCapacity(FluidResource variant) {
		return 0;
	}

	@Override
	public boolean isResourceBlank() {
		return getResource(0).isEmpty();
	}

	@Override
	public FluidResource getResource(int index) {
		if (context.getResource().matches(fullItem.toStack())) {
			return containedFluid;
		} else {
			return FluidResource.EMPTY;
		}
	}

	public FluidResource getResource() {
		return getResource(0);
	}

	@Override
	public int getAmount() {
		if (context.getResource().matches(fullItem.toStack())) {
			return containedAmount;
		} else {
			return 0;
		}
	}

	@Override
	public int getCapacity() {
		return getAmount();
	}

	@Override
	public String toString() {
		return "FullItemFluidStorage[context=%s, fluid=%s, amount=%d]"
				.formatted(context, containedFluid, containedAmount);
	}
}
