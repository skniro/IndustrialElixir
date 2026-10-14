package com.skniro.industrial_elixir.screen.slot;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;

public class LeggingsSlot extends BatteryChargeSlot {
    public LeggingsSlot(Container inventory, int index, int x, int y, EnergyTier energyTier) {
        super(inventory, index, x, y, energyTier);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        Equipable equippable = Equipable.get(stack);
        if (equippable == null) return false;
        if (equippable.getEquipmentSlot() != EquipmentSlot.LEGS) return false;
        if (stack.getItem() instanceof TieredEnergyItem tiered) {return tiered.getEnergyTier().ordinal() >= this.energyTier.ordinal();}
        return false;
    }

}
