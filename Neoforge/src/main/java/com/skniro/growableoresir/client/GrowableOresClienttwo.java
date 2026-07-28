package com.skniro.growableoresir.client;

import com.skniro.industrial_elixir.client.ModItemBlockRenderTypes;
import com.skniro.growableoresir.block.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Environment(EnvType.CLIENT)
public class GrowableOresClienttwo {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Bronze_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_silver_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Tin_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_SACRED_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_steel_Cane.get(), ChunkSectionLayer.CUTOUT);
        ModItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_LEAD_Cane.get(), ChunkSectionLayer.CUTOUT);
    }
}
