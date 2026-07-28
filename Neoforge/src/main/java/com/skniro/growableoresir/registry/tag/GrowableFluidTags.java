package com.skniro.growableoresir.registry.tag;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class GrowableFluidTags {
    public static final TagKey<Fluid> GrowFluid = of("growfluid");



    private static TagKey<Fluid> of(String id) {
        return TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID ,id));
    }
}
