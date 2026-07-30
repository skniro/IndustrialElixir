package com.skniro.industrial_elixir.api.fluid;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;

public abstract class SingleFluidStorage extends SnapshotJournal<ResourceAmount<FluidResource>> implements ResourceHandler<FluidResource> {
	/**
	 * Create a fluid storage with a fixed capacity and a change handler.
	 *
	 * @param capacity Fixed capacity of the fluid storage. Must be non-negative.
	 * @param onChange Change handler, generally for {@code setChanged()} or similar calls. May not be null.
	 */

    public FluidResource variant = getBlankVariant();
    public int amount = 0;

    protected final FluidResource getBlankVariant() {
        return FluidResource.EMPTY;
    }


    protected abstract int getCapacity(FluidResource variant);

    /**
     * @return {@code true} if the passed non-blank variant can be inserted, {@code false} otherwise.
     */
    protected boolean canInsert(FluidResource variant) {
        return true;
    }

    @Override
    public int size() {
        return 1;
    }

    /**
     * @return {@code true} if the passed non-blank variant can be extracted, {@code false} otherwise.
     */
    protected boolean canExtract(FluidResource variant) {
        return true;
    }

    protected void onFinalCommit() {

    }

    @Override
    public int insert(int index, FluidResource insertedVariant, int maxAmount, TransactionContext transaction) {
        return insert(insertedVariant, maxAmount, transaction);
    }

    @Override
    public int insert( FluidResource insertedVariant, int maxAmount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(insertedVariant, maxAmount);

        if ((insertedVariant.equals(variant) || variant.isEmpty()) && canInsert(insertedVariant)) {
            int insertedAmount = Math.min(maxAmount, getCapacity(insertedVariant) - amount);

            if (insertedAmount > 0) {
                updateSnapshots(transaction);

                if (variant.isEmpty()) {
                    variant = insertedVariant;
                    amount = insertedAmount;
                } else {
                    amount += insertedAmount;
                }

                return insertedAmount;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, FluidResource extractedVariant, int maxAmount, TransactionContext transaction) {
       return extract(extractedVariant, maxAmount, transaction);
    }

    @Override
    public int extract(FluidResource extractedVariant, int maxAmount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(extractedVariant, maxAmount);

        if (extractedVariant.equals(variant) && canExtract(extractedVariant)) {
            long extractedAmount = Math.min(maxAmount, amount);

            if (extractedAmount > 0) {
                updateSnapshots(transaction);
                amount -= extractedAmount;

                if (amount == 0) {
                    variant = getBlankVariant();
                }

                return Math.toIntExact(extractedAmount);
            }
        }

        return 0;
    }

    public boolean isResourceBlank() {
        return variant.isEmpty();
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return isResourceBlank();
    }

    @Override
    public FluidResource getResource(int index) {
        return variant;
    }

    @Override
    public long getAmountAsLong(int index) {
        return getAmount();
    }


    public int getAmount() {
        return amount;
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return getCapacity();
    }

    public int getCapacity() {
        return getCapacity(variant);
    }

    @Override
    protected ResourceAmount<FluidResource> createSnapshot() {
        return new ResourceAmount<>(variant, amount);
    }

    @Override
    protected void revertToSnapshot(ResourceAmount<FluidResource> snapshot) {
        variant = snapshot.resource();
        amount = snapshot.amount();
    }

    @Override
    public String toString() {
        return "SingleVariantStorage[%d %s]".formatted(amount, variant);
    }

    @Override
    protected void onRootCommit(ResourceAmount<FluidResource> resource) {
        onFinalCommit();
    }

    public static void readValue(SingleFluidStorage singleFluidStorage, ValueInput value) {
        singleFluidStorage.variant = value.read("variant", FluidResource.CODEC).orElse(FluidResource.EMPTY);
        singleFluidStorage.amount = value.getIntOr("amount", 0);
    }

    public static void writeValue(SingleFluidStorage singleFluidStorage, ValueOutput value) {
        value.store("variant", FluidResource.CODEC, singleFluidStorage.variant);
        value.putLong("amount", singleFluidStorage.amount);
    }

    public static SingleFluidStorage withFixedCapacity(int capacity, Runnable onChange) {
        TransferPreconditions.checkNonNegative(capacity);
        Objects.requireNonNull(onChange, "onChange may not be null");

        return new SingleFluidStorage() {
            @Override
            protected int getCapacity(FluidResource variant) {
                return capacity;
            }

            @Override
            protected void onFinalCommit() {
                onChange.run();
            }
        };
    }
}