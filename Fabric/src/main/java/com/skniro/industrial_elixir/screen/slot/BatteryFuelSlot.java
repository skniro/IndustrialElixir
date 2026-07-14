package com.skniro.industrial_elixir.screen.slot;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BatteryFuelSlot extends Slot {
    private final EnergyTier energyTier;
    public BatteryFuelSlot(Container inventory, int index, int x, int y, EnergyTier energyTier) {
        super(inventory, index, x, y);
        this.energyTier = energyTier;
    }

    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof TieredEnergyItem tiered) {return tiered.getEnergyTier().ordinal() <= this.energyTier.ordinal();
        }
        return false;
    }

    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }
}
