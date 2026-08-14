package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
public class CableElectrocutionEvent extends Event {
    private final LivingEntity livingEntity;
    private final ModContent.Cables cableType;
    private final BlockPos blockPos;
    private final Level level;
    private final CableBlockEntity cableBlockEntity;
    private boolean allowed = true;

    public CableElectrocutionEvent(LivingEntity livingEntity, ModContent.Cables cableType, BlockPos blockPos, Level level, CableBlockEntity cableBlockEntity) {
        this.livingEntity = livingEntity;
        this.cableType = cableType;
        this.blockPos = blockPos;
        this.level = level;
        this.cableBlockEntity = cableBlockEntity;
    }

    public static boolean electrocute(LivingEntity livingEntity, ModContent.Cables cableType, BlockPos blockPos, Level level, CableBlockEntity cableBlockEntity) {
        CableElectrocutionEvent event = new CableElectrocutionEvent(livingEntity, cableType, blockPos, level, cableBlockEntity);
        NeoForge.EVENT_BUS.post(event);
        return event.allowed;
    }

    public void register(Object listener) {
        NeoForge.EVENT_BUS.register(listener);
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public ModContent.Cables getCableType() {
        return cableType;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Level getLevel() {
        return level;
    }

    public CableBlockEntity getCableBlockEntity() {
        return cableBlockEntity;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed &= allowed;
    }
}