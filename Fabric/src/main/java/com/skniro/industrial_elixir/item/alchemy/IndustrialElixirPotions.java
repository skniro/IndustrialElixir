package com.skniro.industrial_elixir.item.alchemy;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class IndustrialElixirPotions {
    public static final Holder.Reference<Potion> SUPER_LEAPING = register("super_leaping", new Potion("leaping", new MobEffectInstance[]{new MobEffectInstance(MobEffects.JUMP_BOOST, 1800, 3)}));
    public static final Holder.Reference<Potion> SUPER_SWIFTNESS = register("super_swiftness", new Potion("swiftness", new MobEffectInstance[]{new MobEffectInstance(MobEffects.SPEED, 1800, 3)}));
    public static final Holder.Reference<Potion> SUPER_SLOWNESS = register("super_slowness", new Potion("slowness", new MobEffectInstance[]{new MobEffectInstance(MobEffects.SLOWNESS, 400, 5)}));
    public static final Holder.Reference<Potion> SUPER_TURTLE_MASTER = register("super_turtle_master", new Potion("turtle_master", new MobEffectInstance[]{new MobEffectInstance(MobEffects.SLOWNESS, 400, 7), new MobEffectInstance(MobEffects.RESISTANCE, 400, 5)}));
    public static final Holder.Reference<Potion> SUPER_HEALING = register("super_healing", new Potion("healing", new MobEffectInstance[]{new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 3)}));
    public static final Holder.Reference<Potion> SUPER_HARMING = register("super_harming", new Potion("harming", new MobEffectInstance[]{new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 3)}));
    public static final Holder.Reference<Potion> SUPER_POISON = register("super_poison", new Potion("poison", new MobEffectInstance[]{new MobEffectInstance(MobEffects.POISON, 432, 3)}));
    public static final Holder.Reference<Potion> SUPER_REGENERATION = register("super_regeneration", new Potion("regeneration", new MobEffectInstance[]{new MobEffectInstance(MobEffects.REGENERATION, 450, 3)}));
    public static final Holder.Reference<Potion> SUPER_STRENGTH = register("super_strength", new Potion("strength", new MobEffectInstance[]{new MobEffectInstance(MobEffects.STRENGTH, 1800, 3)}));

    private static Holder.Reference<Potion> register(final String name, final Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Helper.id(name), potion);
    }

    public static void registerPotions(){
        IndustrialElixir.LOGGER.debug("register Advanced Item.");
    }
}