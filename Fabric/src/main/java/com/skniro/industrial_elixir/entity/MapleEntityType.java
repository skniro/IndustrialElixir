package com.skniro.industrial_elixir.entity;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

public class MapleEntityType {
    public static final EntityType<Boat> RUBBER_BOAT = register("rubber_boat", EntityType.Builder.<Boat>of((type, world) ->
            new RubberBoat(type, world, () -> GrowableOresItems.RUBBER_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

    public static final EntityType<ChestBoat> RUBBER_CHEST_BOAT = register("rubber_chest_boat", EntityType.Builder.<ChestBoat>of((type, world) ->
            new RubberChestBoat(type, world, () -> GrowableOresItems.RUBBER_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, keyOf(name), builder.build(name));
    }

    private static ResourceKey<EntityType<?>> keyOf(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }

    public static void registerMapleEntityType() {
        IndustrialElixir.LOGGER.debug("Registering MapleEntityType for " + IndustrialElixir.MOD_ID);
    }
}
