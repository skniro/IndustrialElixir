package com.skniro.industrial_elixir.energy.api;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.energy.impl.EmptyEnergyStorage;
import com.skniro.industrial_elixir.energy.impl.EnergyImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;


import java.util.Objects;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * An object that can store energy.
 *
 * <p><ul>
 *     <li>{@link #supportsInsertion} and {@link #supportsExtraction} can be used to tell if insertion and extraction
 *     functionality are possibly supported by this storage.</li>
 *     <li>{@link #insert} and {@link #extract} can be used to insert or extract resources from this storage.
 *     Both take a {@code simulate} flag, which replaces the transactions of the original TechReborn API.</li>
 *     <li>{@link #getAmount} and {@link #getCapacity} can be used to query the current amount and capacity of this storage.
 *     There is no guarantee that the current amount of energy can be extracted,
 *     nor that something can be inserted if capacity > amount.
 *     If you want to know, you can simulate the operation with {@link #insert} and {@link #extract}.
 *     </li>
 * </ul>
 *
 * <p>This also implements NeoForge's {@link IEnergyStorage} so the storages of this mod can be consumed by
 * (and can consume) any other energy implementation of the 1.21.1 ecosystem.
 */
@SuppressWarnings({"unused"})
public interface EnergyStorage extends IEnergyStorage {
	/**
	 * Sided block access to energy storages.
	 * The {@code Direction} parameter may be null, meaning that the full storage (ignoring side restrictions) should be queried.
	 *
	 * <p>The system is push based. That means that power sources are responsible for pushing power to nearby machines.
	 * Machines and wires should NOT pull power from other sources.
	 *
	 * <p>{@link com.skniro.industrial_elixir.energy.api.base.SimpleEnergyStorage} and {@link com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer} are provided as base implementations.
	 *
	 * <p>When the operations supported by an energy storage change,
	 * that is if the return value of {@link EnergyStorage#supportsInsertion} or {@link EnergyStorage#supportsExtraction} changes,
	 * the storage should notify its neighbors with a block update so that they can refresh their connections if necessary.
	 *
	 * <p>This may be queried safely both on the logical server and on the logical client threads.
	 * On the server thread (i.e. with a server world), all transfer functionality is always supported.
	 * On the client thread (i.e. with a client world), contents of queried EnergyStorages are unreliable and should not be modified.
	 */
	BlockCapability<EnergyStorage, @Nullable Direction> SIDED =
			BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "sided_energy"), EnergyStorage.class);

	/**
	 * Item access to energy storages.
	 *
	 * <p>{@link com.skniro.industrial_elixir.energy.impl.SimpleItemEnergyStorageImpl} is provided as an implementation example.
	 * Instances of it can be optained through {@link com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem#createStorage}.
	 * Custom implementations should always check the current item of the queried stack before any operation,
	 * like {@code SimpleItemEnergyStorageImpl} does it.
	 * The check can be handled by {@link com.skniro.industrial_elixir.energy.api.base.DelegatingEnergyStorage}.
	 *
	 * <p>This may be queried both client-side and server-side.
	 * Returned APIs should behave the same regardless of the logical side.
	 */
	ItemCapability<EnergyStorage, Void> ITEM =
			ItemCapability.createVoid(ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "energy"), EnergyStorage.class);

	/**
	 * Always empty energy storage.
	 */
	EnergyStorage EMPTY = Objects.requireNonNull(EmptyEnergyStorage.EMPTY);

	/**
	 * Stock data component type for energy.
	 *
	 * <p><b>This component should only be used on item stacks from your mod.</b>
	 * Otherwise, do not query it or assume it exists.
	 * Inter-mod energy interactions should happen using {@link #ITEM}.</b>
	 */
	DataComponentType<Long> ENERGY_COMPONENT = Objects.requireNonNull(EnergyImpl.ENERGY_COMPONENT);

	/**
	 * Return false if calling {@link #insert} will absolutely always return 0, or true otherwise or in doubt.
	 *
	 * <p>Note: This function is meant to be used by cables or other devices that can transfer energy to know if
	 * they should interact with this storage at all.
	 */
	default boolean supportsInsertion() {
		return true;
	}

	/**
	 * Try to insert up to some amount of energy into this storage.
	 *
	 * @param maxAmount The maximum amount of energy to insert. May not be negative.
	 * @param simulate If {@code true}, the storage is not modified and the returned amount is only an estimate.
	 * @return A nonnegative integer not greater than maxAmount: the amount that was inserted.
	 */
	long insert(long maxAmount, boolean simulate);

	/**
	 * Return false if calling {@link #extract} will absolutely always return 0, or true otherwise or in doubt.
	 *
	 * <p>Note: This function is meant to be used by cables or other devices that can transfer energy to know if
	 * they should interact with this storage at all.
	 */
	default boolean supportsExtraction() {
		return true;
	}

	/**
	 * Try to extract up to some amount of energy from this storage.
	 *
	 * @param maxAmount The maximum amount of energy to extract. May not be negative.
	 * @param simulate If {@code true}, the storage is not modified and the returned amount is only an estimate.
	 * @return A nonnegative integer not greater than maxAmount: the amount that was extracted.
	 */
	long extract(long maxAmount, boolean simulate);

	/**
	 * Return the current amount of energy that is stored.
	 */
	long getAmount();

	/**
	 * Return the maximum amount of energy that could be stored.
	 */
	long getCapacity();

	// ==========================================
	// ==== IEnergyStorage (integer) bridge  ====
	// ==========================================

	@Override
	default int receiveEnergy(int toReceive, boolean simulate) {
		return clampToInt(insert(toReceive, simulate));
	}

	@Override
	default int extractEnergy(int toExtract, boolean simulate) {
		return clampToInt(extract(toExtract, simulate));
	}

	@Override
	default int getEnergyStored() {
		return clampToInt(getAmount());
	}

	@Override
	default int getMaxEnergyStored() {
		return clampToInt(getCapacity());
	}

	@Override
	default boolean canReceive() {
		return supportsInsertion();
	}

	@Override
	default boolean canExtract() {
		return supportsExtraction();
	}

	private static int clampToInt(long amount) {
		return (int) Math.min(Integer.MAX_VALUE, Math.max(0L, amount));
	}
}
