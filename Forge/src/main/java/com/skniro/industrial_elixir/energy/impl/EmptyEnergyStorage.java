package com.skniro.industrial_elixir.energy.impl;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.ApiStatus;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
@ApiStatus.Internal
public final class EmptyEnergyStorage implements EnergyStorage {
	public static final EnergyStorage EMPTY = new EmptyEnergyStorage();

	private EmptyEnergyStorage() {
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
