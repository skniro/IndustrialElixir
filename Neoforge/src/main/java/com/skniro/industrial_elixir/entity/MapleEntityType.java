package com.skniro.industrial_elixir.entity;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MapleEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, IndustrialElixir.MOD_ID);

    public static final Supplier<EntityType<Boat>> RUBBER_BOAT = ENTITY_TYPES.register("rubber_boat", ()-> EntityType.Builder.of(getBoatFactory(() -> {
        return GrowableOresItems.RUBBER_BOAT.get();
    }), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build("rubber_boat"));


    public static final Supplier<EntityType<ChestBoat>> RUBBER_CHEST_BOAT = ENTITY_TYPES.register("rubber_chest_boat",()-> EntityType.Builder.of(getChestBoatFactory(() -> {
        return GrowableOresItems.RUBBER_CHEST_BOAT.get();
    }), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build("rubber_chest_boat"));

    private static ResourceKey<EntityType<?>> keyOf(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Helper.id(name));
    }

    private static EntityType.EntityFactory<Boat> getBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> {
            return new RubberBoat(type, world, itemSupplier);
        };
    }

    private static EntityType.EntityFactory<ChestBoat> getChestBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> {
            return new RubberChestBoat(type, world, itemSupplier);
        };
    }
    public static void registerMapleEntityType(IEventBus eventBus) {
        IndustrialElixir.LOGGER.debug("Registering MapleEntityType for " + IndustrialElixir.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }
}
