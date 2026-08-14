package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HeatItem extends Item implements SimpleHeatItem {
    private long capacity;
    private long maxinput;
    private long maxoutput;
    public HeatItem(Properties properties, long capacity, long maxinput, long maxoutput) {
        super(properties);
        this.capacity = capacity;
        this.maxinput = maxinput;
        this.maxoutput = maxoutput;
    }

    @Override
    public long getHeatCapacity(ItemStack stack) {
        return capacity;
    }

    @Override
    public long getHeatMaxInput(ItemStack stack) {
        return maxinput;
    }

    @Override
    public long getHeatMaxOutput(ItemStack stack) {
        return maxoutput;
    }
}
