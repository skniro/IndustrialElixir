package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import net.minecraft.core.Direction;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
record OfferedEnergyStorage(CableBlockEntity sourceCable, Direction direction, EnergyStorage storage) {
    void afterTransfer() {
        sourceCable.blockedSides |= 1 << direction.ordinal();
    }
}