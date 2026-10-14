package com.skniro.industrial_elixir.item.init;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 26.1's {@code BoatItem} is built from an {@link EntityType}; 1.21.1's vanilla {@code BoatItem} is built
 * from a {@code Boat.Type} and always spawns the vanilla boat entities. This item keeps the 26.1
 * behaviour of placing the entity type it was given.
 */
public class ModBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    private final EntityType<? extends Boat> boatType;

    public ModBoatItem(EntityType<? extends Boat> boatType, Item.Properties properties) {
        super(properties);
        this.boatType = boatType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 viewVector = player.getViewVector(1.0F);
        double reach = 5.0;
        List<Entity> nearby = level.getEntities(player, player.getBoundingBox().expandTowards(viewVector.scale(reach)).inflate(1.0), ENTITY_PREDICATE);
        if (!nearby.isEmpty()) {
            Vec3 eyePosition = player.getEyePosition();
            for (Entity entity : nearby) {
                AABB boundingBox = entity.getBoundingBox().inflate((double)entity.getPickRadius());
                if (boundingBox.contains(eyePosition)) {
                    return InteractionResultHolder.pass(stack);
                }
            }
        }

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        Boat boat = this.createBoat(level, hitResult, stack, player);
        if (boat == null) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide()) {
            level.addFreshEntity(boat);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private Boat createBoat(Level level, HitResult hitResult, ItemStack stack, Player player) {
        Vec3 location = hitResult.getLocation();
        Boat boat = this.boatType.create(level);
        if (boat == null) {
            return null;
        }

        boat.setPos(location.x, location.y, location.z);
        boat.setYRot(player.getYRot());
        if (!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1))) {
            return null;
        }

        if (level instanceof ServerLevel serverLevel) {
            EntityType.createDefaultStackConfig(serverLevel, stack, player).accept(boat);
        }

        return boat;
    }
}
