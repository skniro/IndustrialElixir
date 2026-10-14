package com.skniro.industrial_elixir.client;

import com.google.common.base.Preconditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModItemBlockRenderTypes {
    private static final Map<Block, RenderType> TYPE_BY_BLOCK = new HashMap<>();
    private static final Map<Fluid, RenderType> LAYER_BY_FLUID = new HashMap<>();

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

    public static RenderType getRenderLayer(FluidState state) {
        RenderType layer = LAYER_BY_FLUID.get(state.getType());
        return layer != null ? layer : RenderType.solid();
    }

    public static void setCutoutLeaves(final boolean cutoutLeaves) {
        ModItemBlockRenderTypes.cutoutLeaves = cutoutLeaves;
    }

    public static synchronized void setRenderLayer(Block block, RenderType layer) {
        checkClientLoading();
        TYPE_BY_BLOCK.put(block, layer);
    }

    public static synchronized void setRenderLayer(Fluid fluid, RenderType layer) {
        checkClientLoading();
        LAYER_BY_FLUID.put(fluid, layer);
    }

    private static void checkClientLoading() {
        Preconditions.checkState(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT, "Render layers can only be set during client loading! This should ideally be done from `FMLClientSetupEvent`.");
    }
}
