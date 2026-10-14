package com.skniro.industrial_elixir.fluid.init;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

/**
 * 26.1 describes a fluid's appearance with a data-driven {@code FluidModel.Unbaked} (still/flowing/overlay
 * sprites plus a tint function); 1.21.1 has no such model, so the same three appearances are described
 * through NeoForge's {@link IClientFluidTypeExtensions} instead.
 */
public class BaseFluidType {

    public static final IClientFluidTypeExtensions Fluid_UU_MODEL = new FluidModelExtension(
            ResourceLocation.withDefaultNamespace("block/water_still"),
            ResourceLocation.withDefaultNamespace("block/water_flow"),
            ResourceLocation.withDefaultNamespace("block/water_overlay"),
            0xA1BFBFBF);

    public static final IClientFluidTypeExtensions Fluid_AIR_MODEL = new FluidModelExtension(
            ResourceLocation.withDefaultNamespace("block/water_still"),
            ResourceLocation.withDefaultNamespace("block/water_flow"),
            ResourceLocation.withDefaultNamespace("block/water_overlay"),
            0xA1C64CEB);

    public static final IClientFluidTypeExtensions Fluid_HOT_SPRING_MODEL = new FluidModelExtension(
            ResourceLocation.parse("industrial_elixir:block/spring_still"),
            ResourceLocation.parse("industrial_elixir:block/spring_flow"),
            null,
            0xEDDBDBDB);

    /** Simple record-like fluid appearance, the 1.21.1 equivalent of a 26.1 {@code FluidModel}. */
    public static class FluidModelExtension implements IClientFluidTypeExtensions {
        private final ResourceLocation still;
        private final ResourceLocation flowing;
        @Nullable
        private final ResourceLocation overlay;
        private final int tintColor;

        public FluidModelExtension(ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, int tintColor) {
            this.still = still;
            this.flowing = flowing;
            this.overlay = overlay;
            this.tintColor = tintColor;
        }

        @Override
        public ResourceLocation getStillTexture() {
            return still;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return flowing;
        }

        @Override
        public ResourceLocation getOverlayTexture() {
            return overlay;
        }

        @Override
        public int getTintColor() {
            return tintColor;
        }
    }
}
