package com.skniro.industrial_elixir.api.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Objects;

public abstract class SingleFluidStorage extends SnapshotParticipant<ResourceAmount<FluidVariant>> implements SingleSlotStorage<FluidVariant> {
	/**
	 * Create a fluid storage with a fixed capacity and a change handler.
	 *
	 * @param capacity Fixed capacity of the fluid storage. Must be non-negative.
	 * @param onChange Change handler, generally for {@code setChanged()} or similar calls. May not be null.
	 */
    public FluidVariant variant = getBlankVariant();
    public long amount = 0;

    protected final FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    protected abstract long getCapacity(FluidVariant variant);

    /**
     * @return {@code true} if the passed non-blank variant can be inserted, {@code false} otherwise.
     */
    protected boolean canInsert(FluidVariant variant) {
        return true;
    }

    /**
     * @return {@code true} if the passed non-blank variant can be extracted, {@code false} otherwise.
     */
    protected boolean canExtract(FluidVariant variant) {
        return true;
    }

    @Override
    public long insert(FluidVariant insertedVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);

        if ((insertedVariant.equals(variant) || variant.isBlank()) && canInsert(insertedVariant)) {
            long insertedAmount = Math.min(maxAmount, getCapacity(insertedVariant) - amount);

            if (insertedAmount > 0) {
                updateSnapshots(transaction);

                if (variant.isBlank()) {
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
    public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);

        if (extractedVariant.equals(variant) && canExtract(extractedVariant)) {
            long extractedAmount = Math.min(maxAmount, amount);

            if (extractedAmount > 0) {
                updateSnapshots(transaction);
                amount -= extractedAmount;

                if (amount == 0) {
                    variant = getBlankVariant();
                }

                return extractedAmount;
            }
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return variant.isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return variant;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return getCapacity(variant);
    }

    @Override
    protected ResourceAmount<FluidVariant> createSnapshot() {
        return new ResourceAmount<>(variant, amount);
    }

    @Override
    protected void readSnapshot(ResourceAmount<FluidVariant> snapshot) {
        variant = snapshot.resource();
        amount = snapshot.amount();
    }

    @Override
    public String toString() {
        return "SingleVariantStorage[%d %s]".formatted(amount, variant);
    }



    public void readValue(ValueInput value) {
        this.variant = value.read("variant", FluidVariant.CODEC).orElseGet(FluidVariant::blank);
        this.amount = value.getLongOr("amount", 0L);
    }

    void writeValue(ValueOutput value) {
        value.store("variant", FluidVariant.CODEC, this.variant);
        value.putLong("amount", this.amount);
    }

    public static SingleFluidStorage withFixedCapacity(long capacity, Runnable onChange) {
        StoragePreconditions.notNegative(capacity);
        Objects.requireNonNull(onChange, "onChange may not be null");

        return new SingleFluidStorage() {
            @Override
            protected long getCapacity(FluidVariant variant) {
                return capacity;
            }

            @Override
            protected void onFinalCommit() {
                onChange.run();
            }
        };
    }
}