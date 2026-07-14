package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared IC2-style jetpack flight logic used by Quantum Chestplate and Electric Jetpack.
 * All state is static so it works across different item classes.
 */
public final class JetpackHelper {

    // ===============================================
    // Thrust constants
    // ===============================================

    public static final double THRUST_UP_FLIGHT = 0.30;
    public static final double THRUST_UP_HOVER = 0.08;
    public static final double THRUST_UP_HOVER_BOOST = 0.04;
    public static final double THRUST_FORWARD = 0.08;
    public static final double MAX_H_SPEED = 0.35;
    public static final double MAX_FALL_SPEED = -4;
    public static final long FLIGHT_COST_PER_TICK = 25;
    public static final long HOVER_COST_PER_TICK = 37;

    // ===============================================
    // Effect throttling
    // ===============================================

    private static final int PARTICLE_EVERY_TICKS = 2;
    private static final int SOUND_EVERY_TICKS = 4;

    // ===============================================
    // Per-player state
    // ===============================================

    private static final Map<UUID, Boolean> hoverToggled = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> jumpHeld = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> forwardHeld = new ConcurrentHashMap<>();
    private static final Map<UUID, Float> strafe = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> tickCounter = new ConcurrentHashMap<>();

    // ===============================================
    // Public API (called from network / lifecycle)
    // ===============================================

    public static void toggleHover(UUID playerId) {
        boolean wasOn = hoverToggled.getOrDefault(playerId, false);
        if (wasOn) {
            hoverToggled.remove(playerId);
        } else {
            hoverToggled.put(playerId, true);
        }
    }

    public static boolean isHoverToggled(UUID playerId) {
        return hoverToggled.getOrDefault(playerId, false);
    }

    public static void updateInput(UUID playerId, boolean jump, boolean forward, float s) {
        jumpHeld.put(playerId, jump);
        forwardHeld.put(playerId, forward);
        strafe.put(playerId, s);
    }

    public static void onUnequip(UUID playerId) {
        hoverToggled.remove(playerId);
        jumpHeld.remove(playerId);
        forwardHeld.remove(playerId);
        strafe.remove(playerId);
        tickCounter.remove(playerId);
    }

    // ===============================================
    // Per-tick handler — call from inventoryTick for CHEST slot
    // ===============================================

    public static void onChestplateTick(Player player, boolean powered,
                                        long flightCostPerTick, long hoverCostPerTick) {
        UUID id = player.getUUID();
        boolean hover = isHoverToggled(id);

        if (!powered) {
            onUnequip(id);
            return;
        }

        boolean jh = jumpHeld.getOrDefault(id, false);
        float s = strafe.getOrDefault(id, 0f);
        boolean anyHoriz = forwardHeld.getOrDefault(id, false) || s != 0f;

        if (hover) {
            handleHover(player, id, jh, s, anyHoriz, hoverCostPerTick);
        } else {
            handleFlight(player, id, jh, s, anyHoriz, flightCostPerTick);
        }
    }

    // ===============================================
    // Flight / Hover modes
    // ===============================================

    private static void handleFlight(Player player, UUID id, boolean jh, float s,
                                     boolean anyHoriz, long costPerTick) {
        if (!jh) {
            applySlowFall(player, MAX_FALL_SPEED);
            return;
        }

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        SimpleEnergyItem storage = (SimpleEnergyItem) chest.getItem();
        if (!storage.tryUseEnergy(chest, costPerTick)) return;

        applyThrust(player, THRUST_UP_FLIGHT, THRUST_FORWARD, MAX_H_SPEED, s, anyHoriz);
        spawnEffects(player, id);
        player.fallDistance = 0;
    }

    private static void handleHover(Player player, UUID id, boolean jh, float s,
                                    boolean anyHoriz, long costPerTick) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        SimpleEnergyItem storage = (SimpleEnergyItem) chest.getItem();
        if (!storage.tryUseEnergy(chest, costPerTick)) {
            applySlowFall(player, MAX_FALL_SPEED);
            return;
        }

        double up = jh ? THRUST_UP_HOVER + THRUST_UP_HOVER_BOOST : THRUST_UP_HOVER;
        double fwd = anyHoriz ? THRUST_FORWARD * 0.4 : 0;
        applyThrust(player, up, fwd, MAX_H_SPEED * 0.3, s, anyHoriz);
        spawnEffects(player, id);
        player.fallDistance = 0;
    }

    // ===============================================
    // Physics helpers
    // ===============================================

    private static void applyThrust(Player player, double upThrust, double forwardThrust,
                                    double maxHSpeed, float strafeVal, boolean anyHoriz) {
        var m = player.getDeltaMovement();
        double dy = m.y() + upThrust;
        if (dy > upThrust * 1.5) dy = upThrust * 1.5;

        double dx = m.x();
        double dz = m.z();
        if (anyHoriz) {
            var look = player.getLookAngle();
            dx += look.x * forwardThrust;
            dz += look.z * forwardThrust;
            dx += (-look.z) * strafeVal * forwardThrust;
            dz += look.x * strafeVal * forwardThrust;
        }

        double hs = Math.sqrt(dx * dx + dz * dz);
        if (hs > maxHSpeed) {
            double scale = maxHSpeed / hs;
            dx *= scale;
            dz *= scale;
        }

        player.setDeltaMovement(dx, dy, dz);
        player.hurtMarked = true;
    }

    private static void applySlowFall(Player player, double maxFallSpeed) {
        var m = player.getDeltaMovement();
        if (m.y() < maxFallSpeed) {
            player.setDeltaMovement(m.x(), maxFallSpeed, m.z());
            player.hurtMarked = true;
        }
    }

    // ===============================================
    // Particles and sound
    // ===============================================

    private static void spawnEffects(Player player, UUID id) {
        if (!(player.level() instanceof ServerLevel level)) return;
        int t = tickCounter.getOrDefault(id, 0) + 1;
        tickCounter.put(id, t);

        if (t % PARTICLE_EVERY_TICKS == 0) {
            level.sendParticles(
                    ParticleTypes.FLAME,
                    player.getX(), player.getY() + 0.2, player.getZ(),
                    2, 0.3, 0.0, 0.3, 0.02
            );
        }
        if (t % SOUND_EVERY_TICKS == 0) {
            player.playSound(SoundEvents.FIRECHARGE_USE, 0.15f, 1.5f);
        }
    }

    private JetpackHelper() {}
}
