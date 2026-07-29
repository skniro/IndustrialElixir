package com.skniro.industrial_elixir.energy.heat.api;

import com.skniro.industrial_elixir.IndustrialElixir;

import com.skniro.industrial_elixir.energy.heat.api.base.DelegatingHeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatItem;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleSidedHeatContainer;
import com.skniro.industrial_elixir.energy.heat.impl.EmptyHeatStorage;
import com.skniro.industrial_elixir.energy.heat.impl.HeatImpl;
import com.skniro.industrial_elixir.energy.heat.impl.SimpleItemHeatStorageImpl;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;


import java.util.Objects;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * An object that can store Heat.
 *
 * <p><ul>
 *     <li>{@link #supportsInsertion} and {@link #supportsExtraction} can be used to tell if insertion and extraction
 *     functionality are possibly supported by this storage.</li>
 *     <li>{@link #insert} and {@link #extract} can be used to insert or extract resources from this storage.</li>
 *     <li>{@link #getAmount} and {@link #getCapacity} can be used to query the current amount and capacity of this storage.
 *     There is no guarantee that the current amount of Heat can be extracted,
 *     nor that something can be inserted if capacity > amount.
 *     If you want to know, you can simulate the operation with {@link #insert} and {@link #extract}.
 *     </li>
 * </ul>
 *
 * @see Transaction
 */
@SuppressWarnings({"unused"})
public interface HeatStorage {
	/**
	 * Sided block access to Heat storages.
	 * The {@code Direction} parameter may be null, meaning that the full storage (ignoring side restrictions) should be queried.
	 * Refer to {@link BlockApiLookup} for documentation on how to use this field.
	 *
	 * <p>The system is push based. That means that power sources are responsible for pushing power to nearby machines.
	 * Machines and wires should NOT pull power from other sources.
	 *
	 * <p>{@link SimpleHeatStorage} and {@link SimpleSidedHeatContainer} are provided as base implementations.
	 *
	 * <p>When the operations supported by an Heat storage change,
	 * that is if the return value of {@link HeatStorage#supportsInsertion} or {@link HeatStorage#supportsExtraction} changes,
	 * the storage should notify its neighbors with a block update so that they can refresh their connections if necessary.
	 *
	 * <p>This may be queried safely both on the logical server and on the logical client threads.
	 * On the server thread (i.e. with a server world), all transfer functionality is always supported.
	 * On the client thread (i.e. with a client world), contents of queried HeatStorages are unreliable and should not be modified.
	 */
	BlockApiLookup<HeatStorage, @Nullable Direction> SIDED =
			BlockApiLookup.get(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "sided_heat"), HeatStorage.class, Direction.class);

	/**
	 * Item access to Heat storages.
	 * Querying should always happen through {@link ContainerItemContext#find}.
	 *
	 * <p>{@link SimpleItemHeatStorageImpl} is provided as an implementation example.
	 * Instances of it can be optained through {@link SimpleHeatItem#createStorage}.
	 * Custom implementations should treat the context as a wrapper around a single slot,
	 * and always check the current item variant and amount before any operation, like {@code SimpleItemHeatStorageImpl} does it.
	 * The check can be handled by {@link DelegatingHeatStorage}.
	 *
	 * <p>This may be queried both client-side and server-side.
	 * Returned APIs should behave the same regardless of the logical side.
	 */
	ItemCapability<HeatStorage, ItemAccess> ITEM =
			ItemCapability.create(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "heat"), HeatStorage.class, ItemAccess.class);

	/**
	 * Always empty Heat storage.
	 */
	HeatStorage EMPTY = Objects.requireNonNull(EmptyHeatStorage.EMPTY);

	/**
	 * Stock data component type for Heat.
	 *
	 * <p><b>This component should only be used on item stacks from your mod.</b>
	 * Otherwise, do not query it or assume it exists.
	 * Inter-mod Heat interactions should happen using {@link #ITEM}.</b>
	 */
	DataComponentType<Long> Heat_COMPONENT = Objects.requireNonNull(HeatImpl.Heat_COMPONENT);

	/**
	 * Return false if calling {@link #insert} will absolutely always return 0, or true otherwise or in doubt.
	 *
	 * <p>Note: This function is meant to be used by cables or other devices that can transfer Heat to know if
	 * they should interact with this storage at all.
	 */
	default boolean supportsInsertion() {
		return true;
	}

	/**
	 * Try to insert up to some amount of Heat into this storage.
	 *
	 * @param maxAmount The maximum amount of Heat to insert. May not be negative.
	 * @param transaction The transaction this operation is part of.
	 * @return A nonnegative integer not greater than maxAmount: the amount that was inserted.
	 */
	long insert(long maxAmount, TransactionContext transaction);

	/**
	 * Return false if calling {@link #extract} will absolutely always return 0, or true otherwise or in doubt.
	 *
	 * <p>Note: This function is meant to be used by cables or other devices that can transfer Heat to know if
	 * they should interact with this storage at all.
	 */
	default boolean supportsExtraction() {
		return true;
	}

	/**
	 * Try to extract up to some amount of Heat from this storage.
	 *
	 * @param maxAmount The maximum amount of Heat to extract. May not be negative.
	 * @param transaction The transaction this operation is part of.
	 * @return A nonnegative integer not greater than maxAmount: the amount that was extracted.
	 */
	long extract(long maxAmount, TransactionContext transaction);

	/**
	 * Return the current amount of Heat that is stored.
	 */
	long getAmount();

	/**
	 * Return the maximum amount of Heat that could be stored.
	 */
	long getCapacity();
}
