package com.skniro.industrial_elixir.init;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public final class ModDamageTypes {
    public static final ResourceKey<DamageType> ELECTRIC_SHOCK;
    public static final ResourceKey<DamageType> FUSION;

    public static DamageSource create(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }

    static {
        ELECTRIC_SHOCK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "electric_shock"));
        FUSION = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "fusion"));
    }
}