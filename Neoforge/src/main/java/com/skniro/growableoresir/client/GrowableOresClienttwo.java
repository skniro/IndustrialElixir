package com.skniro.growableoresir.client;

import com.skniro.industrial_elixir.client.ModItemBlockRenderTypes;
import com.skniro.growableoresir.block.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class GrowableOresClienttwo {
    public static void onClientSetup() {
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Bronze_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_silver_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Tin_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_SACRED_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_steel_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_LEAD_Cane.get(), ChunkSectionLayer.CUTOUT);
    }
}
