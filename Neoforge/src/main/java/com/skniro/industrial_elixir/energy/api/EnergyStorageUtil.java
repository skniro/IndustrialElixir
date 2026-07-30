package com.skniro.industrial_elixir.energy.api;

import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * Helper functions to work with {@link EnergyStorage}s.
 */
@SuppressWarnings({"unused"})
public class EnergyStorageUtil {
	/**
	 * Move energy between two energy storages, and return the amount that was successfully moved.
	 *
	 * @param from The source storage. May be null.
	 * @param to The target storage. May be null.
	 * @param maxAmount The maximum amount that may be moved.
	 * @param transaction The transaction this transfer is part of,
	 *                    or {@code null} if a transaction should be opened just for this transfer.
	 * @return The amount of energy that was successfully moved.
	 */
	public static long move(@Nullable EnergyStorage from, @Nullable EnergyStorage to, long maxAmount, @Nullable TransactionContext transaction) {
		if (from == null || to == null) return 0;

		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative");

		// Simulate extraction first.
		long maxExtracted;

		try (Transaction extractionTestTransaction = Transaction.open(transaction)) {
			maxExtracted = from.extract(maxAmount, extractionTestTransaction);
		}

		try (Transaction moveTransaction = Transaction.open(transaction)) {
			// Then insert what can be extracted.
			long accepted = to.insert(maxExtracted, moveTransaction);

			// Extract for real.
			if (from.extract(accepted, moveTransaction) == accepted) {
				// Commit if the amounts match.
				moveTransaction.commit();
				return accepted;
			}
		}

		return 0;
	}

	/**
	 * Return true if the passed stack offers an energy storage through {@link EnergyStorage#ITEM}.
	 * This can typically be used for inventories or slots that want to accept energy storages only.
	 */
	public static boolean isEnergyStorage(ItemStack stack) {
		return stack.getCapability(EnergyStorage.ITEM, ItemAccess.forStack(stack)) != null;
	}

	private EnergyStorageUtil() {
	}
}
