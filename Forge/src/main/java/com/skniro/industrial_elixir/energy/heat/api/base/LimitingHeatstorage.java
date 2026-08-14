package com.skniro.industrial_elixir.energy.heat.api.base;

import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * An energy storage that will apply additional per-insert and per-extract limits to another storage.
 */
public class LimitingHeatstorage implements HeatStorage {
	protected final HeatStorage backingStorage;
	protected final long maxInsert, maxExtract;

	/**
	 * Create a new limiting storage.
	 * @param backingStorage Storage to delegate to.
	 * @param maxInsert The maximum amount of energy that can be inserted in one operation.
	 * @param maxExtract The maximum amount of energy that can be extracted in one operation.
	 */
	public LimitingHeatstorage(HeatStorage backingStorage, long maxInsert, long maxExtract) {
		Objects.requireNonNull(backingStorage);
		if (maxInsert < 0 || maxExtract < 0) throw new IllegalArgumentException("Values must not be negative");

		this.backingStorage = backingStorage;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	@Override
	public boolean supportsInsertion() {
		return maxInsert > 0 && backingStorage.supportsInsertion();
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		return backingStorage.insert(Math.min(maxAmount, maxInsert), transaction);
	}

	@Override
	public boolean supportsExtraction() {
		return maxExtract > 0 && backingStorage.supportsExtraction();
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		return backingStorage.extract(Math.min(maxAmount, maxExtract), transaction);
	}

	@Override
	public long getAmount() {
		return backingStorage.getAmount();
	}

	@Override
	public long getCapacity() {
		return backingStorage.getCapacity();
	}
}
