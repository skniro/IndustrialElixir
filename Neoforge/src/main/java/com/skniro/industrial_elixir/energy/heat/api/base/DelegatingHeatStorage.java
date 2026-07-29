package com.skniro.industrial_elixir.energy.heat.api.base;


import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;


import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * An Heat storage that delegates to another Heat storage,
 * with an optional boolean supplier to check that the storage is still valid.
 * This can be used for easier item Heat storage implementation, or overridden for custom delegation logic.
 */
public class DelegatingHeatStorage implements HeatStorage {
	protected final Supplier<HeatStorage> backingStorage;
	protected final BooleanSupplier validPredicate;

	/**
	 * Create a new instance.
	 * @param backingStorage Storage to delegate to.
	 * @param validPredicate A function that can return false to prevent any operation, or true to call the delegate as usual.
	 *                       {@code null} can be passed if no filtering is necessary.
	 */
	public DelegatingHeatStorage(HeatStorage backingStorage, @Nullable BooleanSupplier validPredicate) {
		this(() -> backingStorage, validPredicate);
		Objects.requireNonNull(backingStorage);
	}

	/**
	 * More general constructor that allows the backing storage to change over time.
	 */
	public DelegatingHeatStorage(Supplier<HeatStorage> backingStorage, @Nullable BooleanSupplier validPredicate) {
		this.backingStorage = Objects.requireNonNull(backingStorage);
		this.validPredicate = validPredicate == null ? () -> true : validPredicate;
	}

	@Override
	public boolean supportsInsertion() {
		return validPredicate.getAsBoolean() && backingStorage.get().supportsInsertion();
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notNegative(maxAmount);

		if (validPredicate.getAsBoolean()) {
			return backingStorage.get().insert(maxAmount, transaction);
		} else {
			return 0;
		}
	}

	@Override
	public boolean supportsExtraction() {
		return validPredicate.getAsBoolean() && backingStorage.get().supportsExtraction();
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notNegative(maxAmount);

		if (validPredicate.getAsBoolean()) {
			return backingStorage.get().extract(maxAmount, transaction);
		} else {
			return 0;
		}
	}

	@Override
	public long getAmount() {
		if (validPredicate.getAsBoolean()) {
			return backingStorage.get().getAmount();
		} else {
			return 0;
		}
	}

	@Override
	public long getCapacity() {
		if (validPredicate.getAsBoolean()) {
			return backingStorage.get().getCapacity();
		} else {
			return 0;
		}
	}
}
