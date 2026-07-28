package com.skniro.industrial_elixir.item.alchemy;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class IndustrialElixirPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, IndustrialElixir.MOD_ID);

    public static final DeferredHolder<Potion, Potion>  SUPER_LEAPING = POTIONS.register("super_leaping", () -> new Potion("leaping", new MobEffectInstance[]{new MobEffectInstance(MobEffects.JUMP_BOOST, 1800, 3)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_SWIFTNESS = POTIONS.register("super_swiftness", () -> new Potion("swiftness", new MobEffectInstance[]{new MobEffectInstance(MobEffects.SPEED, 1800, 3)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_SLOWNESS = POTIONS.register("super_slowness", () -> new Potion("slowness", new MobEffectInstance[]{new MobEffectInstance(MobEffects.SLOWNESS, 400, 5)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_TURTLE_MASTER = POTIONS.register("super_turtle_master", () -> new Potion("turtle_master", new MobEffectInstance[]{new MobEffectInstance(MobEffects.SLOWNESS, 400, 7), new MobEffectInstance(MobEffects.RESISTANCE, 400, 5)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_HEALING = POTIONS.register("super_healing", () -> new Potion("healing", new MobEffectInstance[]{new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 3)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_HARMING = POTIONS.register("super_harming", () -> new Potion("harming", new MobEffectInstance[]{new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 3)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_POISON = POTIONS.register("super_poison", () -> new Potion("poison", new MobEffectInstance[]{new MobEffectInstance(MobEffects.POISON, 432, 3)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_REGENERATION = POTIONS.register("super_regeneration", () -> new Potion("regeneration", new MobEffectInstance[]{new MobEffectInstance(MobEffects.REGENERATION, 450, 3)}));
    public static final DeferredHolder<Potion, Potion>  SUPER_STRENGTH = POTIONS.register("super_strength", () -> new Potion("strength", new MobEffectInstance[]{new MobEffectInstance(MobEffects.STRENGTH, 1800, 3)}));

    public static void registerPotions(IEventBus eventBus){
        IndustrialElixir.LOGGER.debug("register Advanced Item.");
        POTIONS.register(eventBus);
    }
}