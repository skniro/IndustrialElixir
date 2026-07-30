package com.skniro.industrial_elixir.energy.api.base;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;

import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * A base energy storage implementation with fixed capacity, and per-operation insertion and extraction limits.
 * Make sure to override {@link #onRootCommit} to call {@code markDirty} and similar functions.
 */
@SuppressWarnings({"unused"})
public class SimpleEnergyStorage extends SnapshotJournal<Long> implements EnergyStorage {
	public long amount = 0;
	public final long capacity;
	public final long maxInsert, maxExtract;

	public SimpleEnergyStorage(long capacity, long maxInsert, long maxExtract) {
		TransferPreconditions.checkNonNegative((int) capacity);
		TransferPreconditions.checkNonNegative((int) maxInsert);
		TransferPreconditions.checkNonNegative((int) maxExtract);

		this.capacity = capacity;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	@Override
	protected Long createSnapshot() {
		return amount;
	}

	@Override
	protected void revertToSnapshot(Long snapshot) {
		readSnapshot(snapshot);
	}

	protected void readSnapshot(Long snapshot) {
		amount = snapshot;
	}

	@Override
	protected void onRootCommit(Long snapshot) {
		onFinalCommit();
	}

	protected void onFinalCommit() {

	}

	@Override
	public boolean supportsInsertion() {
		return maxInsert > 0;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative");

		long inserted = Math.min(maxInsert, Math.min(maxAmount, capacity - amount));

		if (inserted > 0) {
			updateSnapshots(transaction);
			amount += inserted;
			return inserted;
		}

		return 0;
	}

	@Override
	public boolean supportsExtraction() {
		return maxExtract > 0;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative");

		long extracted = Math.min(maxExtract, Math.min(maxAmount, amount));

		if (extracted > 0) {
			updateSnapshots(transaction);
			amount -= extracted;
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
