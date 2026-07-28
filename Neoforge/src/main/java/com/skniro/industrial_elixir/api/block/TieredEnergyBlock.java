package com.skniro.industrial_elixir.api.block;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;

public interface TieredEnergyBlock {
    EnergyTier getEnergyTier();

    long getMaxCapacity();
}