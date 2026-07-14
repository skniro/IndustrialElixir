package com.skniro.industrial_elixir.api.item;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;

public interface TieredEnergyItem {
    EnergyTier getEnergyTier();
}