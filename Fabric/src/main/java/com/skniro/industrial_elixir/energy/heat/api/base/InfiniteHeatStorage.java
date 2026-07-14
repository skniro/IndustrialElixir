package com.skniro.industrial_elixir.energy.heat.api.base;


import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * An Heat storage that can't accept Heat, but will allow extracting any amount of Heat.
 * Creative batteries are a possible use case.
 * {@link #INSTANCE} can be used instead of creating a new object every time.
 */
public class InfiniteHeatStorage implements HeatStorage {
	public static final InfiniteHeatStorage INSTANCE = new InfiniteHeatStorage();

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