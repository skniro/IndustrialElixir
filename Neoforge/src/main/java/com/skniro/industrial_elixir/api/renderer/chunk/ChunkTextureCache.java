package com.skniro.industrial_elixir.api.renderer.chunk;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public final class ChunkTextureCache {

    private static final Map<ChunkPos, ResourceLocation> CACHE = new HashMap<>();

    private static final String TEXTURE_PREFIX =
            "chunk_map/";

    private ChunkTextureCache() {
    }


    /**
     * 获取 Chunk 的地图纹理
     */
    public static ResourceLocation getTexture(
            Level level,
            ChunkPos chunkPos
    ) {

        ResourceLocation cached = CACHE.get(chunkPos);

        if (cached != null) {
            return cached;
        }


        ResourceLocation textureId = createTexture(
                level,
                chunkPos
        );


        CACHE.put(
                chunkPos,
                textureId
        );


        return textureId;
    }



    /**
     * 创建并上传 GPU 纹理
     */
    private static ResourceLocation createTexture(
            Level level,
            ChunkPos chunkPos
    ) {

        Minecraft minecraft = Minecraft.getInstance();

        TextureManager textureManager =
                minecraft.getTextureManager();


        NativeImage image =
                ChunkTextureGenerator.generate(
                        level,
                        chunkPos
                );


        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                "industrialelixir",
                TEXTURE_PREFIX
                        + chunkPos.x
                        + "_"
                        + chunkPos.z
        );


        DynamicTexture texture = new DynamicTexture(image);


        textureManager.register(
                id,
                texture
        );


        return id;
    }



    /**
     * 删除一个 Chunk 的缓存
     */
    public static void invalidate(
            ChunkPos pos
    ) {

        CACHE.remove(pos);
    }



    /**
     * 清空全部纹理
     *
     * GUI关闭或世界切换时调用
     */
    public static void clear() {

        Minecraft minecraft =
                Minecraft.getInstance();


        TextureManager manager =
                minecraft.getTextureManager();


        for (ResourceLocation id : CACHE.values()) {

            manager.release(id);
        }


        CACHE.clear();
    }
}