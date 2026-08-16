package com.skniro.growableoresir.util;

import com.skniro.industrial_elixir.item.ModCreativeTab;
import com.skniro.growableoresir.block.GrowableICOresBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public class GrowableOresItemGroups {
    public static void ie_item() {
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.Materials).register(content -> {
            content.accept(GrowableICOresBlocks.IER_Bronze_Cane);
            content.accept(GrowableICOresBlocks.IER_silver_Cane);
            content.accept(GrowableICOresBlocks.IER_Tin_Cane);
            content.accept(GrowableICOresBlocks.IER_SACRED_Cane);
            content.accept(GrowableICOresBlocks.IER_steel_Cane);
            content.accept(GrowableICOresBlocks.IER_LEAD_Cane);
        });
    }
}
