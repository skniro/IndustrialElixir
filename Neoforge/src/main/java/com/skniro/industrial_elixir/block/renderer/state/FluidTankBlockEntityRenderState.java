package com.skniro.industrial_elixir.block.renderer.state;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

public class FluidTankBlockEntityRenderState extends BlockEntityRenderState {
    public FluidVariant fluidVariant = FluidVariant.blank();
    public TextureAtlasSprite fluidStillSprite;
    public TextureAtlasSprite fluidFlowSprite;
    public int fluidColor;
    public float fluidHeight;
    public int lightmapCoordinates;
    public boolean hasFluid;
}
