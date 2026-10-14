package com.skniro.industrial_elixir.energy.api.base;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * A base energy storage implementation with fixed capacity, and per-operation insertion and extraction limits.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 */
@SuppressWarnings({"unused"})
public class SimpleEnergyStorage implements EnergyStorage {
	public long amount = 0;
	public final long capacity;
	public final long maxInsert, maxExtract;

	public SimpleEnergyStorage(long capacity, long maxInsert, long maxExtract) {
		if (capacity < 0 || maxInsert < 0 || maxExtract < 0) throw new IllegalArgumentException("Values must not be negative");

		this.capacity = capacity;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	protected void onFinalCommit() {

	}

	@Override
	public boolean supportsInsertion() {
		return maxInsert > 0;
	}

	@Override
	public long insert(long maxAmount, boolean simulate) {
		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative");

		long inserted = Math.min(maxInsert, Math.min(maxAmount, capacity - amount));

		if (inserted > 0) {
			if (!simulate) {
				amount += inserted;
				onFinalCommit();
			}

			return inserted;
		}

		return 0;
	}

	@Override
	public boolean supportsExtraction() {
		return maxExtract > 0;
	}

	@Override
	public long extract(long maxAmount, boolean simulate) {
		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative");

		long extracted = Math.min(maxExtract, Math.min(maxAmount, amount));

		if (extracted > 0) {
			if (!simulate) {
				amount -= extracted;
				onFinalCommit();
			}

			return extracted;
		}

		return 0;
	}

	@Override
	public long getAmount() {
		return amount;
	}

	@Override
	public long getCapacity() {
		return capacity;
	}
}
