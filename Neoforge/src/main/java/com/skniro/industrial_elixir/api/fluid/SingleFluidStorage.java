package com.skniro.industrial_elixir.api.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A fluid storage with a fixed capacity, a per-variant capacity and change hooks.
 *
 * <p>This is the 1.21.1 counterpart of the 26.1 transfer-API based {@code SingleFluidStorage}:
 * the transaction machinery ({@code SnapshotJournal}, {@code TransactionContext}) is replaced by an
 * explicit {@code simulate} flag, and {@link #onFinalCommit()} is invoked directly after a
 * successful non-simulated modification.
 *
 * <p>It implements {@link IFluidHandler} with a single tank, so instances can be returned straight
 * from {@code Capabilities.FluidHandler.BLOCK}.
 */
public abstract class SingleFluidStorage implements IFluidHandler {
	/** NBT keys used by {@link #readValue} / {@link #writeValue}. */
	private static final String FLUID_KEY = "variant";
	private static final String AMOUNT_KEY = "amount";

	/**
	 * The stored fluid. Never contains an amount that differs from {@link #amount}.
	 * Use {@link #setFluid(FluidStack)} or {@link #insert}/{@link #extract} to modify it.
	 */
	public FluidStack variant = getBlankVariant();
	/**
	 * The amount of {@link #variant} that is currently stored.
	 */
	public int amount = 0;

	protected FluidStack getBlankVariant() {
		return FluidStack.EMPTY;
	}

	/**
	 * @param variant The fluid that the capacity is queried for.
	 * @return The capacity of this storage for the passed fluid.
	 */
	protected abstract int getCapacity(FluidStack variant);

	/**
	 * @return {@code true} if the passed non-blank variant can be inserted, {@code false} otherwise.
	 */
	protected boolean canInsert(FluidStack variant) {
		return true;
	}

	/**
	 * @return {@code true} if the passed non-blank variant can be extracted, {@code false} otherwise.
	 */
	protected boolean canExtract(FluidStack variant) {
		return true;
	}

	/**
	 * Called after a successful non-simulated insertion or extraction,
	 * generally for {@code setChanged()} or similar calls.
	 */
	protected void onFinalCommit() {

	}

	/**
	 * Try to insert up to {@code maxAmount} of the passed fluid into this storage.
	 *
	 * @param insertedVariant The fluid to insert. Its amount is ignored, {@code maxAmount} is authoritative.
	 * @param maxAmount The maximum amount to insert. May not be negative.
	 * @param simulate If {@code true}, the storage is not modified and the returned amount is only an estimate.
	 * @return The amount that was inserted.
	 */
	public int insert(FluidStack insertedVariant, int maxAmount, boolean simulate) {
		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative: " + maxAmount);
		if (insertedVariant.isEmpty()) return 0;

		boolean sameFluid = !variant.isEmpty() && FluidStack.isSameFluidSameComponents(variant, insertedVariant);

		if ((sameFluid || variant.isEmpty()) && canInsert(insertedVariant)) {
			int insertedAmount = Math.min(maxAmount, getCapacity(insertedVariant) - amount);

			if (insertedAmount > 0) {
				if (!simulate) {
					setFluid(sameFluid ? variant.copyWithAmount(amount + insertedAmount) : insertedVariant.copyWithAmount(insertedAmount));
					onFinalCommit();
				}

				return insertedAmount;
			}
		}

		return 0;
	}

	/**
	 * Try to extract up to {@code maxAmount} of the passed fluid from this storage.
	 *
	 * @param extractedVariant The fluid to extract. Its amount is ignored, {@code maxAmount} is authoritative.
	 * @param maxAmount The maximum amount to extract. May not be negative.
	 * @param simulate If {@code true}, the storage is not modified and the returned amount is only an estimate.
	 * @return The amount that was extracted.
	 */
	public int extract(FluidStack extractedVariant, int maxAmount, boolean simulate) {
		if (maxAmount < 0) throw new IllegalArgumentException("Amount must not be negative: " + maxAmount);
		if (extractedVariant.isEmpty()) return 0;

		if (!variant.isEmpty() && FluidStack.isSameFluidSameComponents(variant, extractedVariant) && canExtract(extractedVariant)) {
			int extractedAmount = Math.min(maxAmount, amount);

			if (extractedAmount > 0) {
				if (!simulate) {
					setFluid(extractedAmount >= amount ? getBlankVariant() : variant.copyWithAmount(amount - extractedAmount));
					onFinalCommit();
				}

				return extractedAmount;
			}
		}

		return 0;
	}

	/**
	 * Directly set the contents of this storage. The amount is taken from the passed stack.
	 */
	public void setFluid(FluidStack stack) {
		if (stack.isEmpty() || stack.getAmount() <= 0) {
			this.variant = getBlankVariant();
			this.amount = 0;
		} else {
			this.variant = stack.copy();
			this.amount = stack.getAmount();
		}
	}

	/**
	 * @return The stored fluid, including its amount.
	 */
	public FluidStack getFluid() {
		return variant;
	}

	/**
	 * @return The stored fluid. Kept for compatibility with the 26.1 transfer-API accessor.
	 */
	public FluidStack getResource(int index) {
		return variant;
	}

	public int getAmount() {
		return amount;
	}

	public int getCapacity() {
		return getCapacity(variant);
	}

	public boolean isEmpty() {
		return variant.isEmpty();
	}

	public boolean isResourceBlank() {
		return variant.isEmpty();
	}

	// ======================
	// ==== IFluidHandler ====
	// ======================

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return variant;
	}

	@Override
	public int getTankCapacity(int tank) {
		return getCapacity(variant);
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return canInsert(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return insert(resource, resource.getAmount(), action.simulate());
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || variant.isEmpty() || !FluidStack.isSameFluidSameComponents(variant, resource)) {
			return FluidStack.EMPTY;
		}

		return drain(resource.getAmount(), action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain <= 0 || variant.isEmpty()) return FluidStack.EMPTY;

		// Compute the drained stack before the extraction, as it may empty the tank.
		FluidStack drained = variant.copyWithAmount(Math.min(maxDrain, amount));

		if (extract(variant, maxDrain, action.simulate()) <= 0) return FluidStack.EMPTY;

		return drained;
	}

	// =============
	// ==== NBT ====
	// =============

	/**
	 * Read the contents of the passed storage from the {@code variant} (fluid id) and {@code amount} keys,
	 * which is the same layout the 26.1 code used, and the one the Fabric half of this mod uses for its tanks.
	 */
	public static void readValue(SingleFluidStorage singleFluidStorage, CompoundTag tag) {
		Fluid fluid = tag.contains(FLUID_KEY) ? BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(tag.getString(FLUID_KEY))) : null;
		long amount = tag.getLong(AMOUNT_KEY);

		if (fluid == null || fluid == Fluids.EMPTY || amount <= 0) {
			singleFluidStorage.setFluid(FluidStack.EMPTY);
		} else {
			singleFluidStorage.setFluid(new FluidStack(fluid, (int) Math.min(Integer.MAX_VALUE, amount)));
		}
	}

	public static void writeValue(SingleFluidStorage singleFluidStorage, CompoundTag tag) {
		if (singleFluidStorage.variant.isEmpty()) {
			tag.remove(FLUID_KEY);
			tag.remove(AMOUNT_KEY);
		} else {
			tag.putString(FLUID_KEY, BuiltInRegistries.FLUID.getKey(singleFluidStorage.variant.getFluid()).toString());
			tag.putLong(AMOUNT_KEY, singleFluidStorage.amount);
		}
	}

	@Override
	public String toString() {
		return "SingleVariantStorage[%d %s]".formatted(amount, variant);
	}

	public static SingleFluidStorage withFixedCapacity(int capacity, @Nullable Runnable onChange) {
		if (capacity < 0) throw new IllegalArgumentException("Capacity must not be negative: " + capacity);
		Objects.requireNonNull(onChange, "onChange may not be null");

		return new SingleFluidStorage() {
			@Override
			protected int getCapacity(FluidStack variant) {
				return capacity;
			}

			@Override
			protected void onFinalCommit() {
				onChange.run();
			}
		};
	}
}
