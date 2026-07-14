package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class QuantumArmorItem extends Item implements SimpleEnergyItem, TieredEnergyItem {
    private EnergyTier energyTier;
    public static final long CAPACITY = 10000000;

    public QuantumArmorItem(Properties settings, EnergyTier energyTier) {
        super(settings);
        this.energyTier = energyTier;
        EnergyStorage.ITEM.registerForItems((stack, context) -> SimpleEnergyItem.createStorage(context, this.getEnergyCapacity(stack), this.getEnergyMaxInput(stack), this.getEnergyMaxOutput(stack)), this);
    }

    @Override
    public long getEnergyCapacity(ItemStack stack) {
        return CAPACITY;
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return 200;
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return 200;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getPowerForDurabilityBar(stack);
    }

    public static int getPowerForDurabilityBar(ItemStack stack) {
        Item var2 = stack.getItem();
        if (var2 instanceof BatteryItem energyItem) {
            return Math.round((float)energyItem.getStoredEnergy(stack) * 100.0F / (float)energyItem.getEnergyCapacity(stack) * 13.0F) / 100;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00FFFF;
    }
}