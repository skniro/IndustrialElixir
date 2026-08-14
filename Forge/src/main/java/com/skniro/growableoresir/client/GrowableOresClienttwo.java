package com.skniro.growableoresir.client;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.client.ModItemBlockRenderTypes;
import com.skniro.growableoresir.block.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = IndustrialElixir.MOD_ID, value = Dist.CLIENT)
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
