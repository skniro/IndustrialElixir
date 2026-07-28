package com.skniro.industrial_elixir.energy.heat.impl;


import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.DelegatingHeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatItem;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
/**
 * Note: instances of this class do not perform any context validation,
 * that is handled by the DelegatingHeatStorage they are wrapped behind.
 */
@ApiStatus.Internal
public class SimpleItemHeatStorageImpl implements HeatStorage {
	public static HeatStorage createSimpleStorage(ContainerItemContext ctx, long capacity, long maxInsert, long maxExtract) {
		StoragePreconditions.notNegative(capacity);
		StoragePreconditions.notNegative(maxInsert);
		StoragePreconditions.notNegative(maxExtract);

		Item startingItem = ctx.getItemVariant().getItem();

		return new DelegatingHeatStorage(
				new SimpleItemHeatStorageImpl(ctx, capacity, maxInsert, maxExtract),
				() -> ctx.getItemVariant().isOf(startingItem) && ctx.getAmount() > 0
		);
	}

	private final ContainerItemContext ctx;
	private final long capacity;
	private final long maxInsert, maxExtract;

	private SimpleItemHeatStorageImpl(ContainerItemContext ctx, long capacity, long maxInsert, long maxExtract) {
		this.ctx = ctx;
		this.capacity = capacity;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	/**
	 * Try to set the Heat of the stack to {@code HeatAmountPerCount}, return true if success.
	 */
	private boolean trySetHeat(long HeatAmountPerCount, long count, TransactionContext transaction) {
		ItemStack newStack = ctx.getItemVariant().toStack();
		SimpleHeatItem.setStoredHeatUnchecked(newStack, HeatAmountPerCount);
		ItemVariant newVariant = ItemVariant.of(newStack);

		// Try to convert exactly `count` items.
		try (Transaction nested = transaction.openNested()) {
			if (ctx.extract(ctx.getItemVariant(), count, nested) == count && ctx.insert(newVariant, count, nested) == count) {
				nested.commit();
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean supportsInsertion() {
		return maxInsert > 0;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		long count = ctx.getAmount();

		long maxAmountPerCount = maxAmount / count;
		long currentAmountPerCount = getAmount() / count;
		long insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

		if (insertedPerCount > 0) {
			if (trySetHeat(currentAmountPerCount + insertedPerCount, count, transaction)) {
				return insertedPerCount * count;
			}
		}

		return 0;
	}

	@Override
	public boolean supportsExtraction() {
		return maxExtract > 0;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		long count = ctx.getAmount();

		long maxAmountPerCount = maxAmount / count;
		long currentAmountPerCount = getAmount() / count;
		long extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

		if (extractedPerCount > 0) {
			if (trySetHeat(currentAmountPerCount - extractedPerCount, count, transaction)) {
				return extractedPerCount * count;
			}
		}

		return 0;
	}

	@Override
	public long getAmount() {
		return ctx.getAmount() * SimpleHeatItem.getStoredHeatUnchecked(ctx.getItemVariant().getComponents());
	}

	@Override
	public long getCapacity() {
		return ctx.getAmount() * capacity;
	}
}

