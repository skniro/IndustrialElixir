package com.skniro.growableoresir;

import com.skniro.growableoresir.block.GrowableICOresBlocks;
import net.neoforged.bus.api.IEventBus;

public class GrowableOres {
    public void onInitialize(IEventBus eventBus) {
        GrowableICOresBlocks.registerModBlocks(eventBus);
    }
}
