package com.skniro.industrial_elixir.energy.heat.impl;

import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.ApiStatus;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
@ApiStatus.Internal
public final class EmptyHeatStorage implements HeatStorage {
	public static final HeatStorage EMPTY = new EmptyHeatStorage();

	private EmptyHeatStorage() {
	}

	@Override
	public boolean supportsInsertion() {
		return false;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		return 0;
	}

	@Override
	public boolean supportsExtraction() {
		return false;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		return 0;
	}

	@Override
	public long getAmount() {
		return 0;
	}

	@Override
	public long getCapacity() {
		return 0;
	}
}
