package com.skniro.industrial_elixir.datagen.dynamic;

import com.skniro.industrial_elixir.init.ModDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeProvider{

    public static void damageTypes(BootstrapContext<DamageType> context) {
        context.register(ModDamageTypes.ELECTRIC_SHOCK, new DamageType("electric_shock", 0.1F, DamageEffects.BURNING));
        context.register(ModDamageTypes.FUSION, new DamageType("fusion", 0.1F, DamageEffects.BURNING));
    }
}