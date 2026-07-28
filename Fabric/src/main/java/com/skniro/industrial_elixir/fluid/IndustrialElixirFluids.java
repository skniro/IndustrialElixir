package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FlowingFluid;

    public class IndustrialElixirFluids {
        public static FlowingFluid STILL_Fluid_UU;
        public static FlowingFluid FLOWING_Fluid_UU;
        public static FlowingFluid STILL_Fluid_AIR;
        public static FlowingFluid FLOWING_Fluid_AIR;
        public static FlowingFluid STILL_Hot_Spring;
        public static FlowingFluid FLOWING_Hot_Spring;


        public static void registerFluids() {
            STILL_Fluid_UU = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fluid_uu"), new IndustrialElixirFluidUUFluid.Still());

            FLOWING_Fluid_UU = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "flowing_fluid_uu"), new IndustrialElixirFluidUUFluid.Flowing());

            STILL_Fluid_AIR = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fluid_air"), new IndustrialElixirFluidAIRFluid.Still());

            FLOWING_Fluid_AIR = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "flowing_fluid_air"), new IndustrialElixirFluidAIRFluid.Flowing());

            STILL_Hot_Spring = Registry.register(BuiltInRegistries.FLUID,
                    Helper.id("hot_spring"), new MapleHotSpringFluid.Still());

            FLOWING_Hot_Spring = Registry.register(BuiltInRegistries.FLUID,
                    Helper.id( "flowing_hot_spring_water"), new MapleHotSpringFluid.Flowing());
        }
    }

