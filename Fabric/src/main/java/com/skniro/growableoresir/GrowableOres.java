package com.skniro.growableoresir;

import com.skniro.growableoresir.block.GrowableICOresBlocks;
import com.skniro.growableoresir.item.GrowableOresItems;
import com.skniro.growableoresir.util.GrowableOresItemGroups;
import net.fabricmc.api.ModInitializer;


public class GrowableOres implements ModInitializer {
    @Override
    public void onInitialize() {
        GrowableOresItems.shield_item();
        GrowableOresItemGroups.ie_item();;
        GrowableICOresBlocks.registerModBlocks();
    }
}
