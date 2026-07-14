package com.skniro.industrial_elixir.screen.slot;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

public class BootsSlot extends BatteryChargeSlot {
    public BootsSlot(Container inventory, int index, int x, int y, EnergyTier energyTier) {
        super(inventory, index, x, y, energyTier);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) return false;
        if (equippable.slot() != EquipmentSlot.FEET) return false;
        if (stack.getItem() instanceof TieredEnergyItem tiered) {return tiered.getEnergyTier().ordinal() >= this.energyTier.ordinal();}
        return false;
    }

}
