package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.*;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
@EventBusSubscriber(modid = IndustrialElixir.MOD_ID)
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
                    long minTransferRate = Long.MAX_VALUE;

                    for(CableBlockEntity cable : cableList) {
                        networkAmount += cable.energyContainer.amount;
                        networkCapacity += cable.energyContainer.getCapacity();
                        minTransferRate = Math.min(minTransferRate, cable.getCableType().transferRate);
                        cable.appendTargets(targetStorages);
                        cable.ioBlocked = true;
                    }

                    if (networkAmount > networkCapacity) {
                        networkAmount = networkCapacity;
                    }

                    networkAmount += dispatchTransfer(minTransferRate, TransferOperation.EXTRACT, networkCapacity - networkAmount);
                    networkAmount -= dispatchTransfer(minTransferRate, TransferOperation.INSERT, networkAmount);
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
                        if (shouldTickCable(adjCable)) {
                            bfsQueue.add(adjCable);
                            adjCable.lastTick = tickCounter;
                            cableList.add(adjCable);
                        }
                    }
                }
            }

        }
    }

    private static long dispatchTransfer(long transferRate, TransferOperation operation, long maxAmount) {
        List<SortableStorage> sortedTargets = new ArrayList();

        for(OfferedEnergyStorage storage : targetStorages) {
            sortedTargets.add(new SortableStorage(operation, storage));
        }

        Collections.shuffle(sortedTargets);
        sortedTargets.sort(Comparator.comparingLong((sortableStorage) -> sortableStorage.simulationResult));

        long transferredAmount = 0L;

        for(int i = 0; i < sortedTargets.size(); ++i) {
            SortableStorage target = (SortableStorage)sortedTargets.get(i);
            int remainingTargets = sortedTargets.size() - i;
            long remainingAmount = maxAmount - transferredAmount;
            long targetMaxAmount = Math.min(remainingAmount / (long)remainingTargets, transferRate);
            long localTransferred = operation.transfer(target.storage.storage(), targetMaxAmount);
            if (localTransferred > 0L) {
                transferredAmount += localTransferred;
                target.storage.afterTransfer();
            }
        }

        return transferredAmount;
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        ++tickCounter;
    }

    private static class SortableStorage {
        private final OfferedEnergyStorage storage;
        private final long simulationResult;

        SortableStorage(TransferOperation operation, OfferedEnergyStorage storage) {
            this.storage = storage;
            this.simulationResult = operation.simulate(storage.storage(), Long.MAX_VALUE);
        }
    }

    /**
     * A single energy direction: either pulling energy out of the adjacent storage into
     * the cable network, or pushing network energy into the adjacent storage.
     *
     * <p>Without a transaction system the "dry run" used for sorting has to be computed
     * from the storage's own amount/capacity instead of by rolling a mutation back.
     */
    private enum TransferOperation {
        EXTRACT {
            @Override
            long transfer(EnergyStorage storage, long maxAmount) {
                return storage.extract(maxAmount, false);
            }

            @Override
            long simulate(EnergyStorage storage, long maxAmount) {
                if (!storage.supportsExtraction()) return 0L;
                return Math.min(maxAmount, storage.getAmount());
            }
        },
        INSERT {
            @Override
            long transfer(EnergyStorage storage, long maxAmount) {
                return storage.insert(maxAmount, false);
            }

            @Override
            long simulate(EnergyStorage storage, long maxAmount) {
                if (!storage.supportsInsertion()) return 0L;
                long space = storage.getCapacity() - storage.getAmount();
                return Math.max(0L, Math.min(maxAmount, space));
            }
        };

        abstract long transfer(EnergyStorage storage, long maxAmount);

        abstract long simulate(EnergyStorage storage, long maxAmount);
    }
}
