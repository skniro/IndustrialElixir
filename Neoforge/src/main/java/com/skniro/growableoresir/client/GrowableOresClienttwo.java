package com.skniro.growableoresir.client;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.growableoresir.block.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = IndustrialElixir.MOD_ID, value = Dist.CLIENT)
public class GrowableOresClienttwo {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Bronze_Cane.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_silver_Cane.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_Tin_Cane.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_SACRED_Cane.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_steel_Cane.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GrowableICOresBlocks.IER_LEAD_Cane.get(), RenderType.cutout());
    }
}
