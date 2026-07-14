package com.skniro.industrial_elixir.block.entity.pipe;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.Iterator;

/**
 * Wraps a Storage and prevents insertion while allowing extraction.
 * Used to block external insertion to containers that pipes are extracting from.
 */
public class ExtractOnlyStorageWrapper<T> implements Storage<T> {
    private final Storage<T> delegate;

    public ExtractOnlyStorageWrapper(Storage<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public long insert(T resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(T resource, long maxAmount, TransactionContext transaction) {
        return delegate.extract(resource, maxAmount, transaction);
    }

    @Override
    public Iterator<StorageView<T>> iterator() {
        return delegate.iterator();
    }
}
