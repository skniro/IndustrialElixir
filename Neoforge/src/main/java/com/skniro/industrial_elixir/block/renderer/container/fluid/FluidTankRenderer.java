package com.skniro.industrial_elixir.block.renderer.container.fluid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.skniro.industrial_elixir.block.entity.container.fluid.FluidTankBlockEntity;
import com.skniro.industrial_elixir.block.renderer.state.FluidTankBlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class FluidTankRenderer implements BlockEntityRenderer<FluidTankBlockEntity, FluidTankBlockEntityRenderState> {
    private static final float TANK_INSET = 0.14f;
    private static final float MIN_FLUID_Y = 0.02f;
    private static final float MAX_FLUID_Y = 0.98f;
    private static final float MIN_VISIBLE_HEIGHT = 0.02f;

    public FluidTankRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public FluidTankBlockEntityRenderState createRenderState() {
        return new FluidTankBlockEntityRenderState();
    }

    @Override
    public void submit(FluidTankBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        
        if (!state.hasFluid) {
            System.out.println("[FluidTankRenderer.submit] No fluid, skipping render");
            return;
        }

        matrices.pushPose();
        
        // 即使没有精灵，我们也可以渲染纯色液体
        // 这会允许至少看到液体的存在
        if (state.fluidStillSprite != null && state.fluidFlowSprite != null) {
            queue.submitCustomGeometry(matrices, RenderTypes.translucentMovingBlock(), (pose, vertexConsumer) ->
                    renderFluidBox(pose, vertexConsumer, state.fluidStillSprite, state.fluidFlowSprite, state.fluidColor, state.lightmapCoordinates, state.fluidHeight));
        } else {
            // 作为回退，使用纯色渲染
            queue.submitCustomGeometry(matrices, RenderTypes.translucentMovingBlock(), (pose, vertexConsumer) ->
                    renderFluidBoxSolidColor(pose, vertexConsumer, state.fluidColor, state.lightmapCoordinates, state.fluidHeight));
        }
        
        matrices.popPose();
    }

    private static void renderFluidBoxSolidColor(PoseStack.Pose pose, VertexConsumer vertexConsumer, int color, int light, float height) {
        float minX = TANK_INSET;
        float maxX = 1.0f - TANK_INSET;
        float minZ = TANK_INSET;
        float maxZ = 1.0f - TANK_INSET;
        float minY = MIN_FLUID_Y;
        float maxY = Math.min(MAX_FLUID_Y, MIN_FLUID_Y + height);

        if (maxY <= minY) {
            return;
        }

        // 使用固定的 UV（不需要精灵）
        float u0 = 0.0f;
        float u1 = 1.0f;
        float v0 = 0.0f;
        float v1 = 1.0f;

        // 顶面
        quad(vertexConsumer, pose, color, light,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                u0, v1, u1, v0,
                0.0f, 1.0f, 0.0f);

        // 底面
        quad(vertexConsumer, pose, color, light,
                minX, minY, maxZ,
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                u0, v1, u1, v0,
                0.0f, -1.0f, 0.0f);

        // 四个侧面
        quad(vertexConsumer, pose, color, light,
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                maxX, minY, minZ,
                u0, v1, u1, v0,
                0.0f, 0.0f, -1.0f);

        quad(vertexConsumer, pose, color, light,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                minX, minY, maxZ,
                u0, v1, u1, v0,
                0.0f, 0.0f, 1.0f);

        quad(vertexConsumer, pose, color, light,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                u0, v1, u1, v0,
                1.0f, 0.0f, 0.0f);

        quad(vertexConsumer, pose, color, light,
                minX, minY, minZ,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                minX, minY, maxZ,
                u0, v1, u1, v0,
                -1.0f, 0.0f, 0.0f);
    }

    @Override
    public void extractRenderState(FluidTankBlockEntity entity, FluidTankBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(entity, state, tickProgress, cameraPos, crumblingOverlay);

        Level level = entity.getLevel();
        state.lightmapCoordinates = level == null ? 15728880 : getLightLevel(level, entity.getBlockPos());
        state.fluidVariant = entity.getFluidVariant();
        state.hasFluid = entity.getAmount() > 0 && !state.fluidVariant.isEmpty();
        
        if (!state.hasFluid) {
            state.fluidStillSprite = null;
            state.fluidFlowSprite = null;
            state.fluidColor = 0;
            state.fluidHeight = 0.0f;
            return;
        }

        FluidResource fluid = state.fluidVariant;
        state.fluidFlowSprite = state.fluidStillSprite;

        
        state.fluidColor = resolveColor(fluid);
        state.fluidHeight = getFluidHeight(entity.getFillRatio());
    }

    private int getLightLevel(Level world, BlockPos pos) {
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(bLight, Math.max(sLight, 15));
    }

    private static float getFluidHeight(float fillRatio) {
        float clampedRatio = Math.max(0.0f, Math.min(1.0f, fillRatio));
        float usableHeight = MAX_FLUID_Y - MIN_FLUID_Y;
        if (clampedRatio <= 0.0f) {
            return 0.0f;
        }
        return Math.max(MIN_VISIBLE_HEIGHT, clampedRatio * usableHeight);
    }

    private static int resolveColor(FluidResource variant) {
        Fluid fluid = variant.getFluid();
        // 回退到硬编码的颜色
        if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
            return 0xFFFFA000;
        }
        if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER) {
            return 0xFF3F76E4;
        }
        return 0xFFFFFFFF;
    }

    private static void renderFluidBox(PoseStack.Pose pose, VertexConsumer vertexConsumer, TextureAtlasSprite stillSprite, TextureAtlasSprite flowSprite, int color, int light, float height) {
        float minX = TANK_INSET;
        float maxX = 1.0f - TANK_INSET;
        float minZ = TANK_INSET;
        float maxZ = 1.0f - TANK_INSET;
        float minY = MIN_FLUID_Y;
        float maxY = Math.min(MAX_FLUID_Y, MIN_FLUID_Y + height);

        if (maxY <= minY) {
            return;
        }

        float u0 = stillSprite.getU0();
        float u1 = stillSprite.getU1();
        float v0 = stillSprite.getV0();
        float v1 = stillSprite.getV1();
        float fu0 = flowSprite.getU0();
        float fu1 = flowSprite.getU1();
        float fv0 = flowSprite.getV0();
        float fv1 = flowSprite.getV1();
        float sideV1 = lerp(maxY - minY, fv0, fv1);

        quad(vertexConsumer, pose, color, light,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                u0, v1, u1, v0,
                0.0f, 1.0f, 0.0f);

        quad(vertexConsumer, pose, color, light,
                minX, minY, maxZ,
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                u0, v1, u1, v0,
                0.0f, -1.0f, 0.0f);

        quad(vertexConsumer, pose, color, light,
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                maxX, minY, minZ,
                fu0, sideV1, fu1, fv0,
                0.0f, 0.0f, -1.0f);

        quad(vertexConsumer, pose, color, light,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                minX, minY, maxZ,
                fu0, sideV1, fu1, fv0,
                0.0f, 0.0f, 1.0f);

        quad(vertexConsumer, pose, color, light,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                fu0, sideV1, fu1, fv0,
                1.0f, 0.0f, 0.0f);

        quad(vertexConsumer, pose, color, light,
                minX, minY, minZ,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                minX, minY, maxZ,
                fu0, sideV1, fu1, fv0,
                -1.0f, 0.0f, 0.0f);
    }

    private static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    private static Object findFirstResult(Object target, String... methodNames) {
        return invokeFirstResult(target, null, methodNames);
    }

    private static Object invokeFirstResult(Object target, @Nullable Object argument, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                Method method = argument == null
                        ? target.getClass().getMethod(methodName)
                        : target.getClass().getMethod(methodName, argument.getClass());
                return argument == null ? method.invoke(target) : method.invoke(target, argument);
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return null;
    }

    private static void quad(VertexConsumer consumer, PoseStack.Pose pose, int color, int light,
                             float x0, float y0, float z0,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3,
                             float u0, float v0, float u1, float v1,
                             float nx, float ny, float nz) {
        vertex(consumer, pose, x0, y0, z0, color, u0, v1, light, nx, ny, nz);
        vertex(consumer, pose, x1, y1, z1, color, u0, v0, light, nx, ny, nz);
        vertex(consumer, pose, x2, y2, z2, color, u1, v0, light, nx, ny, nz);
        vertex(consumer, pose, x3, y3, z3, color, u1, v1, light, nx, ny, nz);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int color, float u, float v, int light, float nx, float ny, float nz) {
        consumer.addVertex(pose, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }
}
