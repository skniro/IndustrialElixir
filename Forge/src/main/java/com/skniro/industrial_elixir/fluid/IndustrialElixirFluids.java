package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IndustrialElixirFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, IndustrialElixir.MOD_ID);

    public static final Supplier<FlowingFluid> STILL_Fluid_UU = registerfluid("fluid_uu", () ->  new IndustrialElixirFluidUUFluid.Still());

    public static final Supplier<FlowingFluid> FLOWING_Fluid_UU = registerfluid( "flowing_fluid_uu",() -> new IndustrialElixirFluidUUFluid.Flowing());

    public static Supplier<FlowingFluid> STILL_Fluid_AIR = registerfluid( "fluid_air", () -> new IndustrialElixirFluidAIRFluid.Still());

    public static Supplier<FlowingFluid> FLOWING_Fluid_AIR = registerfluid( "flowing_fluid_air", () -> new IndustrialElixirFluidAIRFluid.Flowing());

    public static Supplier<FlowingFluid> STILL_Hot_Spring = registerfluid("hot_spring", () -> new MapleHotSpringFluid.Still());

    public static Supplier<FlowingFluid> FLOWING_Hot_Spring = registerfluid("flowing_hot_spring_water", () -> new MapleHotSpringFluid.Flowing());


    private static <T extends Fluid> Supplier<T> registerfluid(String name, Supplier<T> fluid) {
        Supplier<T> toReturn = FLUIDS.register(name, fluid);
        return toReturn;
    }

    public static void registerFluids(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}

