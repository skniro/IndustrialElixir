package com.skniro.industrial_elixir.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * 26.1 lets a boat's drop item be supplied through the constructor; in 1.21.1 the drop is decided by
 * {@link Boat#getDropItem()}, so the rubber chest boat carries its own item instead.
 */
public class RubberChestBoat extends ChestBoat {
    private final Supplier<Item> dropItem;

    public RubberChestBoat(EntityType<? extends Boat> type, Level level, Supplier<Item> dropItem) {
        super(type, level);
        this.dropItem = dropItem;
    }

    @Override
    public Item getDropItem() {
        return this.dropItem.get();
    }
}
