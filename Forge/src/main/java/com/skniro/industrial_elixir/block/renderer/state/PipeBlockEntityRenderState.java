package com.skniro.industrial_elixir.block.renderer.state;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PipeBlockEntityRenderState extends BlockEntityRenderState {
    public final List<PipeItemRender> items = new ArrayList<>();
    public final List<PipeFluidRender> fluids = new ArrayList<>();

    public static class PipeItemRender {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public Vec3 offset = Vec3.ZERO;
        public int lightmapCoordinates;
    }

    public static class PipeFluidRender {
        public final PipeItemRender item = new PipeItemRender();
        public BlockState state;
        public Vec3 offset = Vec3.ZERO;
        public int lightmapCoordinates;
        public int[] colorRgba = new int[]{255, 255, 255, 180};
    }
}
