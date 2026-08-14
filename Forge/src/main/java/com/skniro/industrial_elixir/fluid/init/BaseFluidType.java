package com.skniro.industrial_elixir.fluid.init;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

public class BaseFluidType {

    public static final FluidModel.Unbaked Fluid_UU_MODEL = new FluidModel.Unbaked(
            new Material(Identifier.withDefaultNamespace("block/water_still")),
            new Material(Identifier.withDefaultNamespace("block/water_flow")),
            new Material(Identifier.withDefaultNamespace("block/water_overlay")), _ -> 0xA1BFBFBF);

    public static final FluidModel.Unbaked Fluid_AIR_MODEL = new FluidModel.Unbaked(
            new Material(Identifier.withDefaultNamespace("block/water_still")),
            new Material(Identifier.withDefaultNamespace("block/water_flow")),
            new Material(Identifier.withDefaultNamespace("block/water_overlay")), _ -> 0xA1C64CEB);

    public static final FluidModel.Unbaked Fluid_HOT_SPRING_MODEL = new FluidModel.Unbaked(
            new Material(Identifier.parse("industrial_elixir:block/spring_still")),
            new Material(Identifier.parse("industrial_elixir:block/spring_flow")),
            null,
            _ -> 0xEDDBDBDB);
}