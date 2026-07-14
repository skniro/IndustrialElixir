package com.skniro.industrial_elixir.api.energytier;

public enum EnergyTier {
    TIER1(32,32),
    TIER2(128,128),
    TIER3(512,512),
    TIER4(2048,2048),
    TIER5(8192, 8192),
    INFINITE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int maxInput;
    private final int maxOutput;

    EnergyTier(int maxInput, int maxOutput) {
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    public int getMaxInput() {
        return this.maxInput;
    }

    public int getMaxOutput() {
        return this.maxOutput;
    }
}