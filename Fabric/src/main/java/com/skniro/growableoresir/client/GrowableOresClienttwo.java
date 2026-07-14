package com.skniro.growableoresir.client;

import com.skniro.industrial_elixir.client.ModItemBlockRenderTypes;
import com.skniro.growableoresir.block.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Environment(EnvType.CLIENT)
public class GrowableOresClienttwo {
    public static void register() {
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Bronze_Cane, ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_silver_Cane, ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Tin_Cane, ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_SACRED_Cane, ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_steel_Cane, ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_LEAD_Cane, ChunkSectionLayer.CUTOUT);
    }
}
