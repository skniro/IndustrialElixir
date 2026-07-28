package com.skniro.industrial_elixir.item.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class RollingCuttingToolItem extends Item {

    public RollingCuttingToolItem(Properties settings) {
        super(settings.durability(80));
    }

    @Override
    public ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);

        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY.getCraftingRemainder();
        }

        return copy.getCraftingRemainder();
    }
}