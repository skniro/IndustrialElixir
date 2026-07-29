package com.skniro.industrial_elixir.energy.api.base;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * An energy storage that can't accept energy, but will allow extracting any amount of energy.
 * Creative batteries are a possible use case.
 * {@link #INSTANCE} can be used instead of creating a new object every time.
 */
public class InfiniteEnergyStorage implements EnergyStorage {
	public static final InfiniteEnergyStorage INSTANCE = new InfiniteEnergyStorage();

	@Override
	public boolean supportsInsertion() {
		return false;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		return 0;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		return maxAmount;
	}

	@Override
	public long getAmount() {
		return Long.MAX_VALUE;
	}

	@Override
	public long getCapacity() {
		return Long.MAX_VALUE;
	}
}