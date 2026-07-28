package com.skniro.industrial_elixir.block.renderer.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skniro.industrial_elixir.block.entity.pipe.FluidPipeBlockEntity;
import com.skniro.industrial_elixir.block.renderer.state.PipeBlockEntityRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FluidPipeRenderer implements BlockEntityRenderer<FluidPipeBlockEntity, PipeBlockEntityRenderState> {

    public FluidPipeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public PipeBlockEntityRenderState createRenderState() {
        return new PipeBlockEntityRenderState();
    }

    @Override
    public void submit(PipeBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        for (PipeBlockEntityRenderState.PipeFluidRender fluid : state.fluids) {
            matrices.pushPose();
            matrices.translate(0.5 + fluid.offset.x, 0.5 + fluid.offset.y, 0.5 + fluid.offset.z);
            matrices.scale(0.33f, 0.33f, 0.33f);
            fluid.item.item.submit(matrices, queue, fluid.lightmapCoordinates, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    @Override
    public void extractRenderState(FluidPipeBlockEntity entity, PipeBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickProgress, cameraPos, crumblingOverlay);
        ItemModelResolver itemModelResolver = Minecraft.getInstance().getItemModelResolver();
        state.fluids.clear();
        for (FluidPipeBlockEntity.PipeFluid fluid : entity.getFluids()) {
            if (fluid.variant.isBlank() || fluid.amount <= 0) continue;

            PipeBlockEntityRenderState.PipeFluidRender renderFluid = new PipeBlockEntityRenderState.PipeFluidRender();
            renderFluid.offset = new Vec3(
                    fluid.direction.getStepX() * fluid.progress,
                    fluid.direction.getStepY() * fluid.progress,
                    fluid.direction.getStepZ() * fluid.progress
            );
            renderFluid.lightmapCoordinates = getLightLevel(entity.level(), entity.getBlockPos());
            itemModelResolver.updateForTopItem(
                    renderFluid.item.item,
                    getFluidDisplayStack(fluid.variant.getFluid()),
                    ItemDisplayContext.GROUND,
                    entity.getLevel(),
                    (ItemOwner) entity,
                    1
            );
            state.fluids.add(renderFluid);
        }
    }

    private int getLightLevel(Level world, BlockPos pos) {
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(bLight, Math.max(sLight, 15));
    }

    private static ItemStack getFluidDisplayStack(Fluid fluid) {
        if (fluid.isSame(Fluids.WATER) || fluid.isSame(Fluids.FLOWING_WATER)) {
            return new ItemStack(Items.LIGHT_BLUE_STAINED_GLASS);
        }
        if (fluid.isSame(Fluids.LAVA) || fluid.isSame(Fluids.FLOWING_LAVA)) {
            return new ItemStack(Items.ORANGE_STAINED_GLASS);
        }
        return new ItemStack(Items.CYAN_STAINED_GLASS);
    }
}
