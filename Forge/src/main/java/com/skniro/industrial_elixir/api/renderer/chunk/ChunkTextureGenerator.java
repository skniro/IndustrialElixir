package com.skniro.industrial_elixir.api.renderer.chunk;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ChunkTextureGenerator {

    public static final int SIZE = 16;

    private ChunkTextureGenerator() {
    }

    /**
     * Generate a 16x16 top-down map image for a chunk.
     * Falls back to a dark placeholder if the chunk is not loaded.
     */
    public static NativeImage generate(Level level, ChunkPos chunkPos) {

        NativeImage image = new NativeImage(NativeImage.Format.RGBA, SIZE, SIZE, false);

        LevelChunk chunk = level.getChunk(chunkPos.x(), chunkPos.z());

        // If chunk is not fully loaded, return a dark placeholder image
        if (chunk == null || chunk.isEmpty()) {
            for (int x = 0; x < SIZE; x++) {
                for (int z = 0; z < SIZE; z++) {
                    image.setPixelABGR(x, z, 0xFF222222);
                }
            }
            return image;
        }

        int lastHeight = 0;

        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {

                int height = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                int worldX = chunkPos.getMinBlockX() + x;
                int worldZ = chunkPos.getMinBlockZ() + z;

                BlockPos pos = new BlockPos(worldX, height - 1, worldZ);
                var state = chunk.getBlockState(pos);

                int color = ChunkColorProvider.getColor(level, pos, state);
                color = ChunkColorProvider.applyHeightShade(color, height - lastHeight);
                lastHeight = height;

                image.setPixel(x, z, color);
            }
        }

        return image;
    }
}
