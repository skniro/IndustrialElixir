package com.skniro.industrial_elixir.screen.slot;

import com.skniro.industrial_elixir.item.init.ItemUpgradeModule;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UpgradeSlot extends Slot {

    public UpgradeSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ItemUpgradeModule;
        //return stack.isIn(ModItemTags.Upgrade);
    }

    public int getMaxStackSize(ItemStack stack) {
        return super.getMaxStackSize(stack);
    }

}
