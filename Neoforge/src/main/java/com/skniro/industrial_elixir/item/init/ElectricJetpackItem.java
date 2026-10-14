package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;

public class ElectricJetpackItem extends ArmorItem implements SimpleEnergyItem, TieredEnergyItem {

    private static final long CAPACITY = 300000;

    private final EnergyTier energyTier;

    public ElectricJetpackItem(Properties settings, Holder<ArmorMaterial> material, ArmorItem.Type chestplate, EnergyTier energyTier) {
        super(material, chestplate, settings.stacksTo(1)
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false)));
        this.energyTier = energyTier;
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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean selected) {
        if (!(entity instanceof Player player)) return;
        if (player.getItemBySlot(EquipmentSlot.CHEST) != stack) return;

        boolean powered = getStoredEnergy(stack) > 0;

        if (player.isOnFire()) player.clearFire();

        JetpackHelper.onChestplateTick(player, powered,
                JetpackHelper.FLIGHT_COST_PER_TICK,
                JetpackHelper.HOVER_COST_PER_TICK);
    }
}
