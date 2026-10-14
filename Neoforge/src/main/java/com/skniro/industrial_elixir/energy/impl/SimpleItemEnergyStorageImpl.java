package com.skniro.industrial_elixir.energy.impl;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.DelegatingEnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * Energy storage backed by the {@link EnergyStorage#ENERGY_COMPONENT} of an item stack.
 *
 * <p>The stack is the one that was queried through the {@link EnergyStorage#ITEM} capability,
 * so the changes are applied to the queried stack directly and only when not simulating.
 *
 * <p>Note: instances of this class do not perform any context validation,
 * that is handled by the DelegatingEnergyStorage they are wrapped behind.
 */
@ApiStatus.Internal
public class SimpleItemEnergyStorageImpl implements EnergyStorage {
	public static EnergyStorage createSimpleStorage(ItemStack stack, long capacity, long maxInsert, long maxExtract) {
		if (capacity < 0 || maxInsert < 0 || maxExtract < 0) throw new IllegalArgumentException("Values must not be negative");

		Item startingItem = stack.getItem();

		return new DelegatingEnergyStorage(
				new SimpleItemEnergyStorageImpl(stack, capacity, maxInsert, maxExtract),
				() -> stack.getItem() == startingItem && !stack.isEmpty()
		);
	}

	private final ItemStack stack;
	private final long capacity;
	private final long maxInsert, maxExtract;

	private SimpleItemEnergyStorageImpl(ItemStack stack, long capacity, long maxInsert, long maxExtract) {
		this.stack = stack;
		this.capacity = capacity;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	@Override
	public boolean supportsInsertion() {
		return maxInsert > 0;
	}

	@Override
	public long insert(long maxAmount, boolean simulate) {
		long count = stack.getCount();

		if (count <= 0) return 0;

		long maxAmountPerCount = maxAmount / count;
		long currentAmountPerCount = getAmount() / count;
		long insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

		if (insertedPerCount > 0) {
			if (!simulate) {
				SimpleEnergyItem.setStoredEnergyUnchecked(stack, currentAmountPerCount + insertedPerCount);
			}

			return insertedPerCount * count;
		}

		return 0;
	}

	@Override
	public boolean supportsExtraction() {
		return maxExtract > 0;
	}

	@Override
	public long extract(long maxAmount, boolean simulate) {
		long count = stack.getCount();

		if (count <= 0) return 0;

		long maxAmountPerCount = maxAmount / count;
		long currentAmountPerCount = getAmount() / count;
		long extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

		if (extractedPerCount > 0) {
			if (!simulate) {
				SimpleEnergyItem.setStoredEnergyUnchecked(stack, currentAmountPerCount - extractedPerCount);
			}

			return extractedPerCount * count;
		}

		return 0;
	}

	@Override
	public long getAmount() {
		return (long) stack.getCount() * SimpleEnergyItem.getStoredEnergyUnchecked(stack);
	}

	@Override
	public long getCapacity() {
		return (long) stack.getCount() * capacity;
	}
}
