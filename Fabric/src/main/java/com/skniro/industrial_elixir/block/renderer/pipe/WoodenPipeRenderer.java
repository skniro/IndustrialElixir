package com.skniro.industrial_elixir.block.renderer.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity;
import com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity.PipeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class WoodenPipeRenderer implements BlockEntityRenderer<PipeBlockEntity> {

    public WoodenPipeRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(PipeBlockEntity entity, float partialTick, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = entity.getLevel();
        int light = getLightLevel(level, entity.getBlockPos());

        for (PipeItem item : entity.getItems()) {
            if (item.variant.isBlank() || item.amount <= 0) {
                continue;
            }

            matrices.pushPose();
            matrices.translate(0.5 + item.direction.getStepX() * item.progress,
                    0.5 + item.direction.getStepY() * item.progress,
                    0.5 + item.direction.getStepZ() * item.progress);
            matrices.scale(0.35f, 0.35f, 0.35f);
            itemRenderer.renderStatic(item.variant.toStack(), ItemDisplayContext.GROUND, light,
                    OverlayTexture.NO_OVERLAY, matrices, buffer, level, 0);
            matrices.popPose();
        }
    }

    private int getLightLevel(Level world, BlockPos pos) {
        if (world == null) {
            return LightTexture.FULL_BRIGHT;
        }
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, Math.max(sLight, 15));
    }
}
