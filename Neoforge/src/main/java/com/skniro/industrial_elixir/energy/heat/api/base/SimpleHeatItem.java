package com.skniro.industrial_elixir.energy.heat.api.base;



import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.impl.SimpleItemHeatStorageImpl;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * Simple battery-like Heat containing item. If this is implemented on an item:
 * <ul>
 *     <li>The Heat will directly be stored in the components.</li>
 *     <li>Helper functions in this class to work with the stored Heat can be used.</li>
 *     <li>An HeatStorage will automatically be provided for queries through {@link HeatStorage#ITEM}.</li>
 * </ul>
 */
// TODO: Consider adding a tooltip and a recipe input -> output Heat transfer handler like RC has.
public interface SimpleHeatItem {

	/**
	 * Return a base Heat storage implementation for items, with fixed capacity, and per-operation insertion and extraction limits.
	 * This is used internally for items that implement SimpleHeatItem, but it may also be used outside of that.
	 * The Heat is stored in the {@link HeatStorage#Heat_COMPONENT} of the stacks.
	 *
	 * <p>Stackable Heat containers are supported just fine, and they will distribute Heat evenly.
	 * For example, insertion of 3 units of Heat into a stack of 2 items using this class will either insert 0 or 2 depending on the remaining capacity.
	 */
	static HeatStorage createStorage(ItemAccess ctx, long capacity, long maxInsert, long maxExtract) {
		return SimpleItemHeatStorageImpl.createSimpleStorage(ctx, capacity, maxInsert, maxExtract);
	}

	/**
	 * @param stack Current stack.
	 * @return The max Heat that can be stored in this item stack (ignoring current stack size).
	 */
	long getHeatCapacity(ItemStack stack);

	/**
	 * @param stack Current stack.
	 * @return The max amount of Heat that can be inserted in this item stack (ignoring current stack size) in a single operation.
	 */
	long getHeatMaxInput(ItemStack stack);

	/**
	 * @param stack Current stack.
	 * @return The max amount of Heat that can be extracted from this item stack (ignoring current stack size) in a single operation.
	 */
	long getHeatMaxOutput(ItemStack stack);

	/**
	 * @return The Heat stored in the stack. Count is ignored.
	 */
	default long getStoredHeat(ItemStack stack) {
		return getStoredHeatUnchecked(stack);
	}

	/**
	 * Directly set the Heat stored in the stack. Count is ignored.
	 * It's up to callers to ensure that the new amount is >= 0 and <= capacity.
	 */
	default void setStoredHeat(ItemStack stack, long newAmount) {
		setStoredHeatUnchecked(stack, newAmount);
	}

	/**
	 * Try to use exactly {@code amount} Heat if there is enough available and return true if successful,
	 * otherwise do nothing and return false.
	 * @throws IllegalArgumentException If the count of the stack is not exactly 1!
	 */
	default boolean tryUseHeat(ItemStack stack, long amount) {
		if (stack.getCount() != 1) {
			throw new IllegalArgumentException("Invalid count: " + stack.getCount());
		}

		long newAmount = getStoredHeat(stack) - amount;

		if (newAmount < 0) {
			return false;
		} else {
			setStoredHeat(stack, newAmount);
			return true;
		}
	}

	/**
	 * @return The currently stored Heat, ignoring the count and without checking the current item.
	 */
	static long getStoredHeatUnchecked(ItemStack stack) {
		return stack.getOrDefault(HeatStorage.Heat_COMPONENT, 0L);
	}

	static long getStoredHeatUnchecked(ItemVariant variant) {
		return getStoredHeatUnchecked(variant.getComponents());
	}

	static long getStoredHeatUnchecked(DataComponentMap components) {
		return components.getOrDefault(HeatStorage.Heat_COMPONENT, 0L);
	}

	/**
	 * Set the Heat, ignoring the count and without checking the current item.
	 */
	static void setStoredHeatUnchecked(ItemStack stack, long newAmount) {
		if (newAmount <= 0) {
			// Make sure newly crafted Heat containers stack with emptied ones.
			stack.remove(HeatStorage.Heat_COMPONENT);
		} else {
			stack.set(HeatStorage.Heat_COMPONENT, newAmount);
		}
	}
}
