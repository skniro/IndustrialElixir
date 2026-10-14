package com.skniro.industrial_elixir.energy.api;

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
	 * <p>The transfer is first simulated on both sides, and only applied if both sides agree.
	 *
	 * @param from The source storage. May be null.
	 * @param to The target storage. May be null.
	 * @param maxAmount The maximum amount that may be moved.
	 * @return The amount of energy that was successfully moved.
	 */
	public static long move(@Nullable EnergyStorage from, @Nullable EnergyStorage to, long maxAmount) {
		if (from == null || to == null) return 0;

		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative");

		// Simulate extraction first.
		long maxExtracted = from.extract(maxAmount, true);

		if (maxExtracted <= 0) return 0;

		// Then insert what can be extracted.
		long accepted = to.insert(maxExtracted, true);

		if (accepted <= 0) return 0;

		// Extract for real, and insert what was actually extracted.
		long extracted = from.extract(accepted, false);

		if (extracted <= 0) return 0;

		return to.insert(extracted, false);
	}

	/**
	 * Return true if the passed stack offers an energy storage through {@link EnergyStorage#ITEM}.
	 * This can typically be used for inventories or slots that want to accept energy storages only.
	 */
	public static boolean isEnergyStorage(ItemStack stack) {
		return stack.getCapability(EnergyStorage.ITEM) != null;
	}

	private EnergyStorageUtil() {
	}
}
