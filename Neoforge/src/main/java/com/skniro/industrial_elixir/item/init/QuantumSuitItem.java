package com.skniro.industrial_elixir.item.init;

import com.google.common.collect.ImmutableMultimap;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QuantumSuitItem extends Item implements SimpleEnergyItem, TieredEnergyItem {

    public static final long CAPACITY = 10_000_000;

    // Energy costs
    private static final long AIR_REFILL_COST = 1_000;
    private static final long HUNGER_REFILL_COST = 10_000;
    private static final long POISON_REMOVE_COST = 10_000;
    private static final long WITHER_REMOVE_COST = 25_000;
    private static final long NIGHT_VISION_COST = 25_000;
    private static final long BOOST_JUMP_COST = 1_000;
    private static final double BOOST_JUMP_VELOCITY = 1.6;

    private static final int AIR_REFILL_THRESHOLD = 180; // 6 bubbles
    private static final int HUNGER_REFILL_THRESHOLD = 14; // 7 shanks
    private static final int HUNGER_REFILL_AMOUNT = 3;

    // Sprint speed modifier ID for Quantum Leggings
    private static final Identifier LEGGINGS_SPRINT_SPEED_ID = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "leggings_sprint_speed");

    // Damage absorption
    private static final long DAMAGE_BASE_COST = 900;
    private static final long DAMAGE_PER_POINT_COST = 30;
    private static final int MAX_ABSORBABLE_DAMAGE = 1333;
    private static final float[] DAMAGE_REDUCTION = {0.15f, 0.44f, 0.30f, 0.15f}; // Helmet, Chestplate, Leggings, Boots

    // Re-entrancy guard for damage absorption
    private static final Set<UUID> absorbingDamage = ConcurrentHashMap.newKeySet();

    // Night vision toggle state per player UUID (server-only)
    private static final Map<UUID, Boolean> nightVisionToggled = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> nightVisionPaid = new ConcurrentHashMap<>();

    private ArmorType slotType;
    private EnergyTier energyTier;

    private final ImmutableMultimap<Holder<net.minecraft.world.entity.ai.attributes.Attribute>, AttributeModifier> noPowerAttributes;
    private final ImmutableMultimap<Holder<net.minecraft.world.entity.ai.attributes.Attribute>, AttributeModifier> hasPowerAttributes;
    private final ImmutableMultimap<Holder<net.minecraft.world.entity.ai.attributes.Attribute>, AttributeModifier> fullSuitAttributes;

    public QuantumSuitItem(Properties settings, ArmorMaterial material, ArmorType slotType, EnergyTier energyTier) {
        super(settings.stacksTo(1).humanoidArmor(material, slotType).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).component(DataComponents.TOOLTIP_DISPLAY, UNBREAKABLE_HIDE));
        this.energyTier = energyTier;
        this.slotType = slotType;
        switch (slotType) {
            case HELMET, BOOTS -> {
                noPowerAttributes = createAttrs(0, 0, 0, 0);
                hasPowerAttributes = createAttrs(3, 3, 0.05, 0);
                fullSuitAttributes = createAttrs(5, 5, 0.1, 0);
            }
            case CHESTPLATE -> {
                noPowerAttributes = createAttrs(0, 0, 0, 0);
                hasPowerAttributes = createAttrs(6, 3, 0.1, 0);
                fullSuitAttributes = createAttrs(10, 5, 0.15, 0);
            }
            case LEGGINGS -> {
                noPowerAttributes = createAttrs(0, 0, 0, 0);
                hasPowerAttributes = createAttrs(8, 3, 0.05, 0.15);
                fullSuitAttributes = createAttrs(10, 5, 0.1, 0.15);
            }
            default -> throw new IllegalArgumentException("Invalid slot type");
        }
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(EnergyStorage.ITEM, (stack, context) -> SimpleEnergyItem.createStorage(context, this.getEnergyCapacity(stack), this.getEnergyMaxInput(stack), this.getEnergyMaxOutput(stack)), this);
    }

    private static ImmutableMultimap<Holder<net.minecraft.world.entity.ai.attributes.Attribute>, AttributeModifier> createAttrs(
            double armor, double toughness, double knockback, double speed
    ) {
        ImmutableMultimap.Builder<Holder<net.minecraft.world.entity.ai.attributes.Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
        if (armor > 0) {
            builder.put(Attributes.ARMOR,
                    new AttributeModifier(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "armor"), armor, AttributeModifier.Operation.ADD_VALUE));
        }
        if (toughness > 0) {
            builder.put(Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "toughness"), toughness, AttributeModifier.Operation.ADD_VALUE));
        }
        if (knockback > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "knockback"), knockback, AttributeModifier.Operation.ADD_VALUE));
        }
        if (speed > 0) {
            builder.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "speed"), speed, AttributeModifier.Operation.ADD_VALUE));
        }
        return builder.build();
    }

    @Override
    public long getEnergyCapacity(ItemStack stack) { return CAPACITY; }

    @Override
    public long getEnergyMaxInput(ItemStack stack) { return energyTier.getMaxInput(); }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) { return 0; }

    @Override
    public int getBarWidth(ItemStack stack) { return getPowerForDurabilityBar(stack); }

    public boolean isBarVisible(ItemStack stack) { return true; }

    public static int getPowerForDurabilityBar(ItemStack stack) {
        Item var2 = stack.getItem();
        if (var2 instanceof QuantumSuitItem energyItem) {
            return Math.round((float) energyItem.getStoredEnergy(stack) * 100.0F / (float) energyItem.getEnergyCapacity(stack) * 13.0F) / 100;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static int getColorForDurabilityBar(ItemStack stack) { return 16744454; }

    @Override
    public int getBarColor(ItemStack stack) { return 0x00FFFF; }

    // ===============================================
    // Toggle API (called from network packet handler)
    // ===============================================

    public static void toggleNightVision(UUID playerId) {
        boolean wasOn = nightVisionToggled.getOrDefault(playerId, false);
        if (wasOn) {
            nightVisionToggled.remove(playerId);
            nightVisionPaid.remove(playerId);
        } else {
            nightVisionToggled.put(playerId, true);
            // Energy will be charged on next tick in handleNightVision
        }
    }

    public static boolean isNightVisionToggled(UUID playerId) {
        return nightVisionToggled.getOrDefault(playerId, false);
    }

    // ===============================================
    // Main tick logic
    // ===============================================

    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!(entity instanceof Player player)) return;
        SimpleEnergyItem storage = (SimpleEnergyItem) stack.getItem();
        boolean powered = storage.getStoredEnergy(stack) > 0;

        if (slot == EquipmentSlot.HEAD) {
            handleWaterBreathing(player, stack, storage, powered);
            handleHungerRefill(player, stack, storage, powered);
            handleDebuffRemoval(player, stack, storage, powered);
            handleNightVision(player, stack, storage, powered);
        }

        if (slot == EquipmentSlot.CHEST) {
            handleChestplateTick(player, powered);
        }

        if (slot == EquipmentSlot.LEGS && powered) {
            handleSprintSpeed(player, powered);
            if (player.isSwimming()) {
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 5, 1, true, false));
            }
        }
    }

    // ===============================================
    // Helmet abilities
    // ===============================================

    private void handleWaterBreathing(Player player, ItemStack stack, SimpleEnergyItem storage, boolean powered) {
        if (!player.isUnderWater() || !powered) return;

        int air = player.getAirSupply();
        if (air <= AIR_REFILL_THRESHOLD) {
            if (storage.tryUseEnergy(stack, AIR_REFILL_COST)) {
                player.setAirSupply(player.getMaxAirSupply());
            }
        }
    }

    private void handleHungerRefill(Player player, ItemStack stack, SimpleEnergyItem storage, boolean powered) {
        if (!powered) return;

        if (player.getFoodData().getFoodLevel() <= HUNGER_REFILL_THRESHOLD) {
            if (storage.tryUseEnergy(stack, HUNGER_REFILL_COST)) {
                int currentFood = player.getFoodData().getFoodLevel();
                int newFood = Math.min(20, currentFood + HUNGER_REFILL_AMOUNT);
                player.getFoodData().setFoodLevel(newFood);
                player.getFoodData().setSaturation(Math.min(newFood, player.getFoodData().getSaturationLevel() + 0.6f));
            }
        }
    }

    private void handleDebuffRemoval(Player player, ItemStack stack, SimpleEnergyItem storage, boolean powered) {
        if (!powered) return;

        // Remove Poison
        MobEffectInstance poison = player.getEffect(MobEffects.POISON);
        if (poison != null) {
            int levels = poison.getAmplifier() + 1;
            long cost = POISON_REMOVE_COST * levels;
            if (storage.tryUseEnergy(stack, cost)) {
                player.removeEffect(MobEffects.POISON);
            }
        }

        // Remove Wither
        MobEffectInstance wither = player.getEffect(MobEffects.WITHER);
        if (wither != null) {
            int levels = wither.getAmplifier() + 1;
            long cost = WITHER_REMOVE_COST * levels;
            if (storage.tryUseEnergy(stack, cost)) {
                player.removeEffect(MobEffects.WITHER);
            }
        }
    }

    private void handleNightVision(Player player, ItemStack stack, SimpleEnergyItem storage, boolean powered) {
        boolean toggled = isNightVisionToggled(player.getUUID());
        boolean paid = nightVisionPaid.getOrDefault(player.getUUID(), false);

        if (toggled) {
            if (!powered) {
                // Out of power — disable everything
                player.removeEffect(MobEffects.NIGHT_VISION);
                nightVisionToggled.remove(player.getUUID());
                nightVisionPaid.remove(player.getUUID());
                return;
            }

            if (!paid) {
                // Just toggled on — charge activation cost
                if (storage.tryUseEnergy(stack, NIGHT_VISION_COST)) {
                    nightVisionPaid.put(player.getUUID(), true);
                } else {
                    // Not enough energy — keep toggle on but don't activate yet
                    return;
                }
            }

            // Night vision active — refresh effect
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 1, false, false));
        } else {
            // Toggled off — remove everything
            player.removeEffect(MobEffects.NIGHT_VISION);
            nightVisionPaid.remove(player.getUUID());
        }
    }

    // ===============================================
    // Leggings abilities
    // ===============================================

    private static final Set<Block> ICE_BLOCKS = Set.of(
            Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.FROSTED_ICE
    );

    private static boolean isOnIce(Player player) {
        BlockState below = player.getBlockStateOn();
        return ICE_BLOCKS.contains(below.getBlock());
    }

    private void handleSprintSpeed(Player player, boolean powered) {
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;

        // Remove existing modifier first
        attr.removeModifier(LEGGINGS_SPRINT_SPEED_ID);

        if (powered && player.isSprinting()) {
            double multiplier = isOnIce(player) ? 9.0 : 3.0;
            // vanilla base speed is 0.1, sprint applies 1.3x on top
            // set base attribute = 0.1 * multiplier, so sprint = 0.1 * multiplier * 1.3
            double newBase = 0.1 * multiplier;
            attr.addTransientModifier(new AttributeModifier(
                    LEGGINGS_SPRINT_SPEED_ID,
                    newBase - 0.1,
                    AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }

    // ===============================================
    // Jetpack (Chestplate) — delegated to JetpackHelper
    // ===============================================

    public static long getFlightCostPerTick() { return JetpackHelper.FLIGHT_COST_PER_TICK; }
    public static long getHoverCostPerTick() { return JetpackHelper.HOVER_COST_PER_TICK; }

    public static void toggleHover(UUID playerId) { JetpackHelper.toggleHover(playerId); }
    public static boolean isHoverToggled(UUID playerId) { return JetpackHelper.isHoverToggled(playerId); }
    public static void updateJetpackInput(UUID pid, boolean j, boolean f, float s) { JetpackHelper.updateInput(pid, j, f, s); }

    private void handleChestplateTick(Player player, boolean powered) {
        if (player.isOnFire()) player.clearFire();
        JetpackHelper.onChestplateTick(player, powered, getFlightCostPerTick(), getHoverCostPerTick());
    }

    // ===============================================
    // Damage absorption (all pieces)
    // ===============================================

    private static final EquipmentSlot[] QUANTUM_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public static boolean hasFullQuantumSet(Player player) {
        for (int i = 0; i < QUANTUM_SLOTS.length; i++) {
            ItemStack stack = player.getItemBySlot(QUANTUM_SLOTS[i]);
            if (!(stack.getItem() instanceof QuantumSuitItem suitItem)
                    || suitItem.getSlotType() != getArmorTypeForSlot(QUANTUM_SLOTS[i]))
                return false;
        }
        return true;
    }

    private static ArmorType getArmorTypeForSlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> ArmorType.HELMET;
            case CHEST -> ArmorType.CHESTPLATE;
            case LEGS -> ArmorType.LEGGINGS;
            case FEET -> ArmorType.BOOTS;
            default -> null;
        };
    }

    public static boolean tryNegateFallDamage(Player player) {
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (!(boots.getItem() instanceof QuantumSuitItem suitItem)
                || suitItem.getSlotType() != ArmorType.BOOTS) return false;

        SimpleEnergyItem storage = (SimpleEnergyItem) boots.getItem();
        long stored = storage.getStoredEnergy(boots);
        int fallDist = (int) Math.ceil(player.fallDistance);
        long cost = Math.max(0, (long) (fallDist - 12) * 900);
        if (stored >= cost) {
            storage.tryUseEnergy(boots, cost);
            return true;
        }
        return false;
    }

    public static boolean absorbDamage(Player player, float amount, DamageSource source) {
        // Guard against re-entrancy
        UUID id = player.getUUID();
        if (absorbingDamage.contains(id)) return true;
        if (amount <= 0) return true;

        int totalDamage = Math.min((int) amount, MAX_ABSORBABLE_DAMAGE);

        // Check each quantum armor piece and deduct energy
        float totalReduction = 0f;
        boolean anyPieceAbsorbed = false;

        for (int i = 0; i < QUANTUM_SLOTS.length; i++) {
            ItemStack stack = player.getItemBySlot(QUANTUM_SLOTS[i]);
            if (!(stack.getItem() instanceof QuantumSuitItem suitItem)
                    || suitItem.getSlotType() != getArmorTypeForSlot(QUANTUM_SLOTS[i]))
                continue;

            // Cost: 900 base + 30 per damage point
            long cost = DAMAGE_BASE_COST + DAMAGE_PER_POINT_COST * totalDamage;
            SimpleEnergyItem storage = (SimpleEnergyItem) stack.getItem();
            if (storage.tryUseEnergy(stack, cost)) {
                totalReduction += DAMAGE_REDUCTION[i];
                anyPieceAbsorbed = true;
            }
        }

        if (!anyPieceAbsorbed) return true; // no absorption — let damage through

        // Apply reduced damage
        float remaining = amount * (1.0f - Math.min(totalReduction, 1.0f));
        if (remaining <= 0.01f) return false; // fully absorbed

        // Apply remaining damage directly (bypass re-entrancy)
        absorbingDamage.add(id);
        try {
            player.setHealth(Math.max(0f, player.getHealth() - remaining));
        } finally {
            absorbingDamage.remove(id);
        }
        return false; // cancelled original damage
    }

    // ===============================================
    // Cleanup on unequip
    // ===============================================

    public void onUnequip(Player player) {
        ArmorType slot = slotType;
        if (slot == ArmorType.CHESTPLATE) {
            JetpackHelper.onUnequip(player.getUUID());
        }
        if (slot == ArmorType.HELMET) {
            player.removeEffect(MobEffects.NIGHT_VISION);
            nightVisionToggled.remove(player.getUUID());
            nightVisionPaid.remove(player.getUUID());
        }
        if (slot == ArmorType.LEGGINGS) {
            var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attr != null) attr.removeModifier(LEGGINGS_SPRINT_SPEED_ID);
        }
    }

    @Override
    public EnergyTier getEnergyTier() { return energyTier; }

    public ArmorType getSlotType() { return slotType; }

    // ===============================================
    // Boost Jump (Quantum Boots)
    // ===============================================

    public static void performBoostJump(Player player) {
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (!(boots.getItem() instanceof QuantumSuitItem suitItem)
                || suitItem.getSlotType() != ArmorType.BOOTS) return;

        SimpleEnergyItem storage = (SimpleEnergyItem) boots.getItem();
        if (storage.getStoredEnergy(boots) < BOOST_JUMP_COST) return;
        if (!player.onGround()) return;

        if (storage.tryUseEnergy(boots, BOOST_JUMP_COST)) {
            player.setDeltaMovement(player.getDeltaMovement().x(), BOOST_JUMP_VELOCITY, player.getDeltaMovement().z());
            player.hurtMarked = true;
        }
    }

    public static TooltipDisplay UNBREAKABLE_HIDE = new TooltipDisplay(
            false, new LinkedHashSet<>(Set.of(DataComponents.UNBREAKABLE))
    );
}
