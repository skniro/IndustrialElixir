package com.skniro.industrial_elixir.client;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.HashMap;
import java.util.Map;

public class ModItemBlockRenderTypes {
    private static final Map<Block, RenderType> TYPE_BY_BLOCK = new HashMap<>();

    public static synchronized void setRenderLayer(Block block, RenderType layer) {
        checkClientLoading();
        TYPE_BY_BLOCK.put(block, layer);
    }
    private static boolean cutoutLeaves;

    public static RenderType getChunkRenderType(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof LeavesBlock) {
            return cutoutLeaves ? RenderType.cutout() : RenderType.solid();
        } else {
            RenderType layer = TYPE_BY_BLOCK.get(block);
            return layer != null ? layer : RenderType.solid();
        }
    }

    public static RenderType getMovingBlockRenderType(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof LeavesBlock) {
            return cutoutLeaves ? RenderType.cutoutMipped() : RenderType.solid();
        } else {
            RenderType layer = TYPE_BY_BLOCK.get(block);
            if (layer != null) {
                return layer == RenderType.translucent() ? RenderType.translucentMovingBlock() : layer;
            }
            return RenderType.solid();
        }
    }
    public static RenderType getRenderType(BlockState state) {
        RenderType renderType = getChunkRenderType(state);
        return renderType == RenderType.translucent() ? Sheets.translucentItemSheet() : Sheets.cutoutBlockSheet();
    }


    private static void checkClientLoading() {
        Preconditions.checkState(FMLEnvironment.dist == Dist.CLIENT, "Render layers can only be set during client loading! This should ideally be done from `FMLClientSetupEvent`.");
    }

    public static void setRenderLayer(FlowingFluid flowingFluid, RenderType renderLayer4) {

    }
}
