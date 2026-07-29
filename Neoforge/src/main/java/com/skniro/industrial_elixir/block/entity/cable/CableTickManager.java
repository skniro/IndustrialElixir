package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.ModContent;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.*;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
class CableTickManager {
    private static final List<CableBlockEntity> cableList = new ArrayList();
    private static final List<OfferedEnergyStorage> targetStorages = new ArrayList();
    private static final Deque<CableBlockEntity> bfsQueue = new ArrayDeque();
    private static long tickCounter = 0L;

    static void handleCableTick(CableBlockEntity startingCable) {
        if (!(startingCable.getLevel() instanceof ServerLevel)) {
            throw new IllegalStateException();
        } else {
            try {
                gatherCables(startingCable);
                if (!cableList.isEmpty()) {
                    long networkCapacity = 0L;
                    long networkAmount = 0L;

                    for(CableBlockEntity cable : cableList) {
                        networkAmount += cable.energyContainer.amount;
                        networkCapacity += cable.energyContainer.getCapacity();
                        cable.appendTargets(targetStorages);
                        cable.ioBlocked = true;
                    }

                    if (networkAmount > networkCapacity) {
                        networkAmount = networkCapacity;
                    }

                    networkAmount += dispatchTransfer(startingCable.getCableType(), EnergyStorage::extract, networkCapacity - networkAmount);
                    networkAmount -= dispatchTransfer(startingCable.getCableType(), EnergyStorage::insert, networkAmount);
                    int cableCount = cableList.size();

                    for(CableBlockEntity cable : cableList) {
                        cable.energyContainer.amount = networkAmount / (long)cableCount;
                        networkAmount -= cable.energyContainer.amount;
                        --cableCount;
                        cable.setChanged();
                        cable.ioBlocked = false;
                    }

                    return;
                }
            } finally {
                cableList.clear();
                targetStorages.clear();
                bfsQueue.clear();
            }

        }
    }

    private static boolean shouldTickCable(CableBlockEntity current) {
        if (current.lastTick == tickCounter) {
            return false;
        } else {
            Level var2 = current.getLevel();
            boolean var10000;
            if (var2 instanceof ServerLevel) {
                ServerLevel sw = (ServerLevel)var2;
                if (sw.hasChunkAt(current.getBlockPos())) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    private static void gatherCables(CableBlockEntity start) {
        if (shouldTickCable(start)) {
            bfsQueue.add(start);
            start.lastTick = tickCounter;
            cableList.add(start);

            while(!bfsQueue.isEmpty()) {
                CableBlockEntity current = (CableBlockEntity)bfsQueue.removeFirst();

                for(Direction direction : Direction.values()) {
                    BlockEntity var7 = current.getAdjacentBlockEntity(direction);
                    if (var7 instanceof CableBlockEntity) {
                        CableBlockEntity adjCable = (CableBlockEntity)var7;
                        if (current.getCableType().transferRate == adjCable.getCableType().transferRate && shouldTickCable(adjCable)) {
                            bfsQueue.add(adjCable);
                            adjCable.lastTick = tickCounter;
                            cableList.add(adjCable);
                        }
                    }
                }
            }

        }
    }

    private static long dispatchTransfer(ModContent.Cables cableType, TransferOperation operation, long maxAmount) {
        List<SortableStorage> sortedTargets = new ArrayList();

        for(OfferedEnergyStorage storage : targetStorages) {
            sortedTargets.add(new SortableStorage(operation, storage));
        }

        Collections.shuffle(sortedTargets);
        sortedTargets.sort(Comparator.comparingLong((sortableStorage) -> sortableStorage.simulationResult));

        try (Transaction transaction = Transaction.openRoot()) {
            long transferredAmount = 0L;

            for(int i = 0; i < sortedTargets.size(); ++i) {
                SortableStorage target = (SortableStorage)sortedTargets.get(i);
                int remainingTargets = sortedTargets.size() - i;
                long remainingAmount = maxAmount - transferredAmount;
                long targetMaxAmount = Math.min(remainingAmount / (long)remainingTargets, (long)cableType.transferRate);
                long localTransferred = operation.transfer(target.storage.storage(), targetMaxAmount, transaction);
                if (localTransferred > 0L) {
                    transferredAmount += localTransferred;
                    target.storage.afterTransfer();
                }
            }

            transaction.commit();
            return transferredAmount;
        }
    }

    static {
        ServerTickEvents.START_SERVER_TICK.register((ServerTickEvents.StartTick)(server) -> ++tickCounter);
    }

    private static class SortableStorage {
        private final OfferedEnergyStorage storage;
        private final long simulationResult;

        SortableStorage(TransferOperation operation, OfferedEnergyStorage storage) {
            this.storage = storage;

            try (Transaction tx = Transaction.openRoot()) {
                this.simulationResult = operation.transfer(storage.storage(), Long.MAX_VALUE, tx);
            }

        }
    }

    private interface TransferOperation {
        long transfer(EnergyStorage var1, long var2, Transaction var4);
    }
}
