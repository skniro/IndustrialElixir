package com.skniro.industrial_elixir.energy.api.base;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * A base energy storage implementation with a dynamic capacity, and per-side per-operation insertion and extraction limits.
 * {@link #getSideStorage} can be used to get an {@code EnergyStorage} implementation for a given side.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 */
@SuppressWarnings({"unused"})
public abstract class SimpleSidedEnergyContainer {
	public long amount = 0;
	private final SideStorage[] sideStorages = new SideStorage[7];

	public SimpleSidedEnergyContainer() {
		for (int i = 0; i < 7; ++i) {
			sideStorages[i] = new SideStorage(i == 6 ? null : Direction.from3DDataValue(i));
		}
	}

	/**
	 * @return The current capacity of this storage.
	 */
	public abstract long getCapacity();

	/**
	 * @return The maximum amount of energy that can be inserted in a single operation from the passed side.
	 */
	public abstract long getMaxInsert(@Nullable Direction side);

	/**
	 * @return The maximum amount of energy that can be extracted in a single operation from the passed side.
	 */
	public abstract long getMaxExtract(@Nullable Direction side);

	/**
	 * @return An {@link EnergyStorage} implementation for the passed side.
	 */
	public EnergyStorage getSideStorage(@Nullable Direction side) {
		return sideStorages[side == null ? 6 : side.get3DDataValue()];
	}

	protected void onFinalCommit() {

	}

	private class SideStorage implements EnergyStorage {
		private final Direction side;

		private SideStorage(Direction side) {
			this.side = side;
		}

		@Override
		public boolean supportsInsertion() {
			return getMaxInsert(side) > 0;
		}

		@Override
		public long insert(long maxAmount, boolean simulate) {
			if (maxAmount < 0L) {
				throw new IllegalArgumentException("Expected value to be non-negative: " + maxAmount);
			}

			long inserted = Math.min(getMaxInsert(side), Math.min(maxAmount, getCapacity() - amount));

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
			return getMaxExtract(side) > 0;
		}

		@Override
		public long extract(long maxAmount, boolean simulate) {
			if (maxAmount < 0L) {
				throw new IllegalArgumentException("Expected value to be non-negative: " + maxAmount);
			}

			long extracted = Math.min(getMaxExtract(side), Math.min(maxAmount, amount));

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
			return SimpleSidedEnergyContainer.this.getCapacity();
		}
	}
}
