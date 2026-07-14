package com.skniro.industrial_elixir.entity;

import com.mojang.datafixers.types.Type;
import com.skniro.industrial_elixir.IndustrialElixir;
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

public class MapleEntityType {
    public static final EntityType<Boat> RUBBER_BOAT = register("rubber_boat", EntityType.Builder.of(getBoatFactory(() -> {
        return GrowableOresItems.RUBBER_BOAT;
    }), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

    public static final EntityType<ChestBoat> RUBBER_CHEST_BOAT = register("rubber_chest_boat", EntityType.Builder.of(getChestBoatFactory(() -> {
        return GrowableOresItems.RUBBER_CHEST_BOAT;
    }), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        Type<?> type = Util.fetchChoiceType(References.ENTITY, name);
        return (EntityType) Registry.register(BuiltInRegistries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name), builder.build(keyOf(name)));
    }
    private static ResourceKey<EntityType<?>> keyOf(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
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

    public static void registerMapleEntityType() {
        IndustrialElixir.LOGGER.debug("Registering MapleEntityType for " + IndustrialElixir.MOD_ID);
    }
}