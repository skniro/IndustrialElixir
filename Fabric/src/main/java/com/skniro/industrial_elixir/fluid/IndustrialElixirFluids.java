package com.skniro.industrial_elixir.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
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

        public static void registerFluids() {
            STILL_Fluid_UU = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fluid_uu"), new IndustrialElixirFluidUUFluid.Still());

            FLOWING_Fluid_UU = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "flowing_fluid_uu"), new IndustrialElixirFluidUUFluid.Flowing());

            STILL_Fluid_AIR = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fluid_air"), new IndustrialElixirFluidAIRFluid.Still());

            FLOWING_Fluid_AIR = Registry.register(BuiltInRegistries.FLUID,
                    Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "flowing_fluid_air"), new IndustrialElixirFluidAIRFluid.Flowing());
        }

        public static final FluidModel.Unbaked Fluid_UU_MODEL = new FluidModel.Unbaked(
                new Material(Identifier.withDefaultNamespace("block/water_still")),
                new Material(Identifier.withDefaultNamespace("block/water_flow")),
                new Material(Identifier.withDefaultNamespace("block/water_overlay")), _ -> 0xA1C64CEB);
    }

