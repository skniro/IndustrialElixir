package com.skniro.industrial_elixir.item.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RollingCuttingToolItem extends Item {

    public RollingCuttingToolItem(Properties settings) {
        super(settings.durability(80));
    }

    /**
     * Returns the stack left behind after this tool is used as a crafting ingredient.
     *
     * <p>26.1 reached this through {@code ItemStack#getCraftingRemainder()}, which has no 1.21.1
     * counterpart, so the mod keeps its own helper with the same signature.</p>
     */
    public ItemStack getCraftingRemainder(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);

        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }

        return copy;
    }
}
