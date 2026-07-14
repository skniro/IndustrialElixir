package com.skniro.industrial_elixir.block.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class AlchemyBlockEntityRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
}