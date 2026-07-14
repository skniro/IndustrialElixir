package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public class BatteryItem extends Item implements SimpleEnergyItem, TieredEnergyItem {
    private EnergyTier energyTier;
    private long capacity;

    public BatteryItem(Properties settings, EnergyTier energyTier, long capacity) {
        super(settings.stacksTo(1));
        this.energyTier = energyTier;
        this.capacity = capacity;
        EnergyStorage.ITEM.registerForItems((stack, context) -> SimpleEnergyItem.createStorage(context, this.getEnergyCapacity(stack), this.getEnergyMaxInput(stack), this.getEnergyMaxOutput(stack)), this);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getPowerForDurabilityBar(stack);
    }

    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return getColorForDurabilityBar(stack);
    }

    public long getEnergyCapacity(ItemStack stack) {
        return capacity;
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return energyTier.getMaxInput();
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return energyTier.getMaxOutput();
    }

    public static int getPowerForDurabilityBar(ItemStack stack) {
        Item var2 = stack.getItem();
        if (var2 instanceof BatteryItem energyItem) {
            return Math.round((float)energyItem.getStoredEnergy(stack) * 100.0F / (float)energyItem.getEnergyCapacity(stack) * 13.0F) / 100;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static int getColorForDurabilityBar(ItemStack stack) {
        return 16744454;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
    }
}
