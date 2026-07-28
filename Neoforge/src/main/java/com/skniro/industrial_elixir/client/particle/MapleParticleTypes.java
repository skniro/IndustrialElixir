package com.skniro.industrial_elixir.client.particle;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;


public class MapleParticleTypes {
    public static final SimpleParticleType HOT_SPRING = FabricParticleTypes.simple();

    static {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Helper.id("hot_spring"), HOT_SPRING);
    }

    public static void registerParticleTypes() {
        IndustrialElixir.LOGGER.info("register Industrial Elixir Particle Types for"+ IndustrialElixir.MOD_ID );
    }
}
