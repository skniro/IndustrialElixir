package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.machine.ChunkLoaderScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.Nullable;

public class ChunkLoaderEntity extends AbstractMachineEntity {
    // 5x5 grid, 25 chunks, center chunk (index 12) always loaded
    // Bit i represents chunk i in row-major order
    private int selectedChunks = 0;
    // Optional chunks this loader currently has force-loaded.
    private int appliedChunks = 0;
    private boolean chunksApplied = false;

    public ChunkLoaderEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.CHUNK_LOADER_BE, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.ChunkLoader);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ChunkLoaderScreenHandler(syncId, playerInventory, worldPosition);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return null;
    }

    public int getSelectedChunks() {
        return selectedChunks;
    }

    public void toggleChunk(int index) {
        if (index < 0 || index >= 25 || index == 12) return;
        selectedChunks ^= (1 << index);
        setChanged();
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;

        // Charge from battery
        charge(ENERGY_ITEM_SLOT);

        // Center chunk (index 12) always counts as loaded
        int loadedCount = Integer.bitCount(selectedChunks) + 1;
        long energyNeeded = loadedCount; // 1 EU per tick per loaded chunk

        if (energyContainer.amount >= energyNeeded) {
            try (Transaction tx = Transaction.openOuter()) {
                energyContainer.getSideStorage(null).extract(energyNeeded, tx);
                tx.commit();
            }

            forceChunksFromMask(world, pos);

            if (!state.getValue(AbstractMachineblock.LIT)) {
                world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, true), 3);
            }
        } else {
            // Not enough energy — un-force optional chunks
            unforceAllChunks(world, pos);

            if (state.getValue(AbstractMachineblock.LIT)) {
                world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, false), 3);
            }
        }

        setChanged(world, pos, state);
    }

    private void forceChunksFromMask(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel serverLevel)) return;
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        if (!chunksApplied) {
            for (int i = 0; i < 25; i++) {
                if (i == 12 || (selectedChunks & (1 << i)) != 0) {
                    serverLevel.setChunkForced(chunkX + (i % 5) - 2, chunkZ + (i / 5) - 2, true);
                }
            }
            appliedChunks = selectedChunks;
            chunksApplied = true;
        } else {
            int changed = appliedChunks ^ selectedChunks;
            if (changed != 0) {
                for (int i = 0; i < 25; i++) {
                    if (i == 12) continue;
                    if ((changed & (1 << i)) != 0) {
                        boolean force = (selectedChunks & (1 << i)) != 0;
                        serverLevel.setChunkForced(chunkX + (i % 5) - 2, chunkZ + (i / 5) - 2, force);
                    }
                }
                appliedChunks = selectedChunks;
            }
        }

        // The chunk containing the loader is always force-loaded while working.
        serverLevel.setChunkForced(chunkX, chunkZ, true);
    }

    private void unforceAllChunks(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel serverLevel)) return;
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        // Not working (no energy): release every chunk this loader forced, including its own chunk.
        for (int i = 0; i < 25; i++) {
            if (i == 12 || (appliedChunks & (1 << i)) != 0) {
                serverLevel.setChunkForced(chunkX + (i % 5) - 2, chunkZ + (i / 5) - 2, false);
            }
        }
        appliedChunks = 0;
        chunksApplied = false;
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            int chunkX = worldPosition.getX() >> 4;
            int chunkZ = worldPosition.getZ() >> 4;
            for (int i = 0; i < 25; i++) {
                serverLevel.setChunkForced(chunkX + (i % 5) - 2, chunkZ + (i / 5) - 2, false);
            }
        }
        appliedChunks = 0;
        chunksApplied = false;
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("chunkLoader.selectedChunks", selectedChunks);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        selectedChunks = (nbt.contains("chunkLoader.selectedChunks") ? nbt.getInt("chunkLoader.selectedChunks") : 0);
    }
}
