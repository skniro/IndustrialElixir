package com.skniro.industrial_elixir.block.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.skniro.industrial_elixir.block.entity.Alchemyblockentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AlchemyblockentityRenderer implements BlockEntityRenderer<Alchemyblockentity> {
    public AlchemyblockentityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(Alchemyblockentity entity, float partialTick, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction direction = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        matrices.pushPose();
        switch (direction) {
            case NORTH -> matrices.translate(0.5f, 0.5f, -0.01f);
            case SOUTH -> matrices.translate(0.5f, 0.5f, 1.01f);
            case WEST -> matrices.translate(-0.01f, 0.5f, 0.5f);
            case EAST -> matrices.translate(1.01f, 0.5f, 0.5f);
        }
        matrices.scale(0.35f, 0.35f, 0.35f);
        switch (direction) {
            case NORTH -> matrices.mulPose(Axis.YP.rotationDegrees(0));
            case SOUTH -> matrices.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> matrices.mulPose(Axis.YP.rotationDegrees(90));
            case EAST -> matrices.mulPose(Axis.YP.rotationDegrees(270));
        }
        ItemStack stack = entity.getRenderStack();
        if (!stack.isEmpty()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(stack, ItemDisplayContext.GUI,
                    getLightLevel(entity.getLevel(), entity.getBlockPos()), OverlayTexture.NO_OVERLAY,
                    matrices, buffer, entity.getLevel(), 0);
        }
        matrices.popPose();
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
