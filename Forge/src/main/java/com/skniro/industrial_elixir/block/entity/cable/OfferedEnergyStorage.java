package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.minecraft.core.Direction;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
record OfferedEnergyStorage(CableBlockEntity sourceCable, Direction direction, EnergyStorage storage) {
    void afterTransfer() {
        CableBlockEntity var10000 = this.sourceCable;
        var10000.blockedSides |= 1 << this.direction.ordinal();
    }
}