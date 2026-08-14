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
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class ChunkLoaderEntity extends AbstractMachineEntity {
    // 5x5 grid, 25 chunks, center chunk (index 12) always loaded
    // Bit i represents chunk i in row-major order
    private int selectedChunks = 0;

    public ChunkLoaderEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.CHUNK_LOADER_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.ChunkLoader);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ChunkLoaderScreenHandler(syncId, playerInventory,  this, propertyDelegate);
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
            try (Transaction tx = Transaction.openRoot()) {
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

        for (int i = 0; i < 25; i++) {
            if ((selectedChunks & (1 << i)) != 0 || i == 12) {
                int row = i / 5;
                int col = i % 5;
                ChunkPos chunkPos = new ChunkPos(chunkX + (col - 2), chunkZ + (row - 2));
                serverLevel.setChunkForced(chunkPos.x(), chunkPos.z(), false);
            }
        }
    }

    private void unforceAllChunks(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel serverLevel)) return;
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        for (int i = 0; i < 25; i++) {
            if (i == 12) continue; // never un-force center
            int row = i / 5;
            int col = i % 5;
            ChunkPos chunkPos = new ChunkPos(chunkX + (col - 2), chunkZ + (row - 2));
            serverLevel.setChunkForced(chunkPos.x(), chunkPos.z(), false);
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            int chunkX = worldPosition.getX() >> 4;
            int chunkZ = worldPosition.getZ() >> 4;
            for (int i = 0; i < 25; i++) {
                int row = i / 5;
                int col = i % 5;
                ChunkPos chunkPos = new ChunkPos(chunkX + (col - 2), chunkZ + (row - 2));
                serverLevel.setChunkForced(chunkPos.x(), chunkPos.z(), false);
            }
        }
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("chunkLoader.selectedChunks", selectedChunks);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        selectedChunks = nbt.getIntOr("chunkLoader.selectedChunks", 0);
    }
}
