package com.skniro.industrial_elixir.block.renderer.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skniro.industrial_elixir.block.renderer.state.PipeBlockEntityRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StonePipeRenderer implements BlockEntityRenderer<WoodPipeBlockEntity, PipeBlockEntityRenderState> {
    @Override
    public PipeBlockEntityRenderState createRenderState() {
        return new PipeBlockEntityRenderState();
    }

    @Override
    public void submit(PipeBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        for (PipeBlockEntityRenderState.PipeItemRender item : state.items) {
            matrices.pushPose();
            matrices.translate(0.5 + item.offset.x, 0.5 + item.offset.y, 0.5 + item.offset.z);
            matrices.scale(0.35f, 0.35f, 0.35f);
            item.item.submit(matrices, queue, item.lightmapCoordinates, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    @Override
    public void extractRenderState(WoodPipeBlockEntity entity, PipeBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickProgress, cameraPos, crumblingOverlay);
        ItemModelResolver itemModelResolver = Minecraft.getInstance().getItemModelResolver();

        state.items.clear();
        for (PipeItem item : entity.getItems()) {
            if (item.variant.isBlank() || item.amount <= 0) {
                continue;
            }

            PipeBlockEntityRenderState.PipeItemRender renderItem = new PipeBlockEntityRenderState.PipeItemRender();
            renderItem.offset = new Vec3(
                    item.direction.getStepX() * item.progress,
                    item.direction.getStepY() * item.progress,
                    item.direction.getStepZ() * item.progress
            );
            itemModelResolver.updateForTopItem(renderItem.item, item.variant.toStack(), ItemDisplayContext.GROUND, entity.getLevel(), (ItemOwner) entity, 1);
            renderItem.lightmapCoordinates = getLightLevel(entity.level(), entity.getBlockPos());
            state.items.add(renderItem);
        }
    }

    private int getLightLevel(Level world, BlockPos pos) {
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(bLight, Math.max(sLight, 15));
    }
}
