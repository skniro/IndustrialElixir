package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

public class ElectricJetpackItem extends Item implements SimpleEnergyItem, TieredEnergyItem {

    private static final long CAPACITY = 300000;
    private static final TooltipDisplay UNBREAKABLE_HIDE = new TooltipDisplay(
            false, new LinkedHashSet<>(Set.of(DataComponents.UNBREAKABLE))
    );

    private final EnergyTier energyTier;

    public ElectricJetpackItem(Properties settings, ArmorMaterial material, ArmorType chestplate, EnergyTier energyTier) {
        super(settings.stacksTo(1)
                .humanoidArmor(material, chestplate)
                .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                .component(DataComponents.TOOLTIP_DISPLAY, UNBREAKABLE_HIDE));
        this.energyTier = energyTier;
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(EnergyStorage.ITEM, (stack, context) -> SimpleEnergyItem.createStorage(context, this.getEnergyCapacity(stack), this.getEnergyMaxInput(stack), this.getEnergyMaxOutput(stack)), this);
    }

    // ===============================================
    // Energy
    // ===============================================

    @Override
    public long getEnergyCapacity(ItemStack stack) { return CAPACITY; }

    @Override
    public long getEnergyMaxInput(ItemStack stack) { return energyTier.getMaxInput(); }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) { return 0; }

    @Override
    public EnergyTier getEnergyTier() { return energyTier; }

    // ===============================================
    // Durability bar → energy bar
    // ===============================================

    @Override
    public boolean isBarVisible(ItemStack stack) { return true; }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getStoredEnergy(stack) * 13.0F / (float) getEnergyCapacity(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) { return 0x00FFFF; }

    // ===============================================
    // Tick — jetpack flight delegated to JetpackHelper
    // ===============================================

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!(entity instanceof Player player)) return;
        if (slot != EquipmentSlot.CHEST) return;

        boolean powered = getStoredEnergy(stack) > 0;

        if (player.isOnFire()) player.clearFire();

        JetpackHelper.onChestplateTick(player, powered,
                JetpackHelper.FLIGHT_COST_PER_TICK,
                JetpackHelper.HOVER_COST_PER_TICK);
    }
}
