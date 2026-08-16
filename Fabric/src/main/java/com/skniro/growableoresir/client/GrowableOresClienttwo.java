package com.skniro.growableoresir.client;


import com.skniro.growableoresir.block.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

@Environment(EnvType.CLIENT)
public class GrowableOresClienttwo {
    public static void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(GrowableICOresBlocks.IER_Bronze_Cane, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GrowableICOresBlocks.IER_silver_Cane, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GrowableICOresBlocks.IER_Tin_Cane, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GrowableICOresBlocks.IER_SACRED_Cane, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GrowableICOresBlocks.IER_steel_Cane, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GrowableICOresBlocks.IER_LEAD_Cane, RenderType.cutout());
    }
}
