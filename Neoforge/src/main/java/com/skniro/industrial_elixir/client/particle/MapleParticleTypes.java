package com.skniro.industrial_elixir.client.particle;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class MapleParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, IndustrialElixir.MOD_ID);

    public static final Supplier<SimpleParticleType> HOT_SPRING = register("hot_spring", ()-> new SimpleParticleType(true));


    public static <T extends ParticleType<?>> Supplier<T> register(String name, Supplier<T> particleType){
        return PARTICLE_TYPES.register(name, particleType);
    }


    public static void registerParticleTypes(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("register Industrial Elixir Particle Types for"+ IndustrialElixir.MOD_ID );
        PARTICLE_TYPES.register(eventBus);
    }
}
