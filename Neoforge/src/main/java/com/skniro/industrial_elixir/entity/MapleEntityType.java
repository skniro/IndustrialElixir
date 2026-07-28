package com.skniro.industrial_elixir.entity;

import com.mojang.datafixers.types.Type;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MapleEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, IndustrialElixir.MOD_ID);

    public static final Supplier<EntityType<Boat>> RUBBER_BOAT = ENTITY_TYPES.register("rubber_boat", ()-> EntityType.Builder.of(getBoatFactory(() -> {
        return GrowableOresItems.RUBBER_BOAT.get();
    }), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build(keyOf("rubber_boat")));


    public static final Supplier<EntityType<ChestBoat>> RUBBER_CHEST_BOAT = ENTITY_TYPES.register("rubber_chest_boat",()-> EntityType.Builder.of(getChestBoatFactory(() -> {
        return GrowableOresItems.RUBBER_CHEST_BOAT.get();
    }), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build(keyOf("rubber_chest_boat")));

    private static ResourceKey<EntityType<?>> keyOf(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Helper.id(name));
    }

    private static EntityType.EntityFactory<Boat> getBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> {
            return new Boat(type, world, itemSupplier);
        };
    }

    private static EntityType.EntityFactory<ChestBoat> getChestBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> {
            return new ChestBoat(type, world, itemSupplier);
        };
    }
    public static void registerMapleEntityType(IEventBus eventBus) {
        IndustrialElixir.LOGGER.debug("Registering MapleEntityType for " + IndustrialElixir.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }
}