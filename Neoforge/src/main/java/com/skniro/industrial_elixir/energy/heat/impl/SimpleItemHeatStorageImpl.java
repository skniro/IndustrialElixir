package com.skniro.industrial_elixir.energy.heat.impl;


import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.DelegatingHeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatItem;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
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
	public static HeatStorage createSimpleStorage(ItemAccess ctx, long capacity, long maxInsert, long maxExtract) {
		TransferPreconditions.checkNonNegative((int) capacity);
		TransferPreconditions.checkNonNegative((int) maxInsert);
		TransferPreconditions.checkNonNegative((int) maxExtract);

		Item startingItem = ctx.getResource().getItem();

		return new DelegatingHeatStorage(
				new SimpleItemHeatStorageImpl(ctx, capacity, maxInsert, maxExtract),
				() -> ctx.getResource().is(startingItem) && ctx.getAmount() > 0
		);
	}

	private final ItemAccess ctx;
	private final long capacity;
	private final long maxInsert, maxExtract;

	private SimpleItemHeatStorageImpl(ItemAccess ctx, long capacity, long maxInsert, long maxExtract) {
		this.ctx = ctx;
		this.capacity = capacity;
		this.maxInsert = maxInsert;
		this.maxExtract = maxExtract;
	}

	/**
	 * Try to set the Heat of the stack to {@code HeatAmountPerCount}, return true if success.
	 */
	private boolean trySetHeat(long HeatAmountPerCount, long count, TransactionContext transaction) {
		ItemStack newStack = ctx.getResource().toStack();
		SimpleHeatItem.setStoredHeatUnchecked(newStack, HeatAmountPerCount);
		ItemResource newVariant = ItemResource.of(newStack);

		// Try to convert exactly `count` items.
		try (Transaction nested = Transaction.open(transaction)) {
			if (ctx.extract(ctx.getResource(), (int) count, nested) == count && ctx.insert(newVariant, (int) count, nested) == count) {
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
		return ctx.getAmount() * SimpleHeatItem.getStoredHeatUnchecked(ctx.getResource().getComponents());
	}

	@Override
	public long getCapacity() {
		return ctx.getAmount() * capacity;
	}
}

