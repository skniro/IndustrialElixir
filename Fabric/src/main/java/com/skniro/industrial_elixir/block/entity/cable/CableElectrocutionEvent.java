package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.ModContent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
public interface CableElectrocutionEvent {
    Event<CableElectrocutionEvent> EVENT = EventFactory.createArrayBacked(CableElectrocutionEvent.class, (listeners) -> (livingEntity, cableType, blockPos, world, cableBlockEntity) -> {
            for(CableElectrocutionEvent listener : listeners) {
                if (!listener.electrocute(livingEntity, cableType, blockPos, world, cableBlockEntity)) {
                    return false;
                }
            }

            return true;
        });

    boolean electrocute(LivingEntity var1, ModContent.Cables var2, BlockPos var3, Level var4, CableBlockEntity var5);
}