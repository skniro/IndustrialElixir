package com.skniro.industrial_elixir.block.renderer.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skniro.industrial_elixir.block.entity.pipe.FluidPipeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidPipeRenderer implements BlockEntityRenderer<FluidPipeBlockEntity> {

    public FluidPipeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FluidPipeBlockEntity entity, float partialTick, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = entity.getLevel();
        int light = getLightLevel(level, entity.getBlockPos());

        for (FluidPipeBlockEntity.PipeFluid fluid : entity.getFluids()) {
            if (fluid.variant.isBlank() || fluid.amount <= 0) {
                continue;
            }

            double offsetX = fluid.direction.getStepX() * fluid.progress;
            double offsetY = fluid.direction.getStepY() * fluid.progress;
            double offsetZ = fluid.direction.getStepZ() * fluid.progress;

            matrices.pushPose();
            matrices.translate(0.5 + offsetX, 0.5 + offsetY, 0.5 + offsetZ);
            matrices.scale(0.33f, 0.33f, 0.33f);
            itemRenderer.renderStatic(getFluidDisplayStack(fluid.variant.getFluid()), ItemDisplayContext.GROUND,
                    light, OverlayTexture.NO_OVERLAY, matrices, buffer, level, 0);
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
