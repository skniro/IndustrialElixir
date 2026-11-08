package com.skniro.industrial_elixir.item.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RollingCuttingToolItem extends Item {

    public RollingCuttingToolItem(Properties settings) {
        super(settings.durability(80));
    }
    public static ItemStack getRemainingTool(ItemStack stack) {
        int damage = stack.getDamageValue() + 1;
        if (damage >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = stack.copy();
        copy.setDamageValue(damage);
        return copy;
    }
}
