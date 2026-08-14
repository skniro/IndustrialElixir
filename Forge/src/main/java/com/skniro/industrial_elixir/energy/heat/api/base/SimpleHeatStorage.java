package com.skniro.industrial_elixir.energy.heat.api.base;


import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * A base Heat storage implementation with fixed capacity, and per-operation insertion and extraction limits.
 * Make sure to override {@link #onRootCommit} to call {@code markDirty} and similar functions.
 */
@SuppressWarnings({"unused"})
public class SimpleHeatStorage extends SnapshotJournal<Long> implements HeatStorage {
	public long amount = 0;
	public final long capacity;
	public final long maxInsert, maxExtract;

	public SimpleHeatStorage(long capacity, long maxInsert, long maxExtract) {
		if (capacity < 0 || maxInsert < 0 || maxExtract < 0) throw new IllegalArgumentException("Values must not be negative");

		this.capacity = capacity;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	@Override
	protected Long createSnapshot() {
		return amount;
	}

	protected void readSnapshot(Long snapshot) {
		amount = snapshot;
	}

	@Override
	public boolean supportsInsertion() {
		return maxInsert > 0;
	}

	@Override
	protected void revertToSnapshot(Long snapshot) {
		readSnapshot(snapshot);
	}

	@Override
	protected void onRootCommit(Long snapshot) {
		onFinalCommit();
	}

	protected void onFinalCommit() {

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
