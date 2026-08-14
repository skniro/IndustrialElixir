package com.skniro.industrial_elixir.item.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ReactorComponentItem extends Item {
    public enum ComponentType {
        SACRED_ESSENCE,
        SACRED_SHARD,
        SACRED_CORE,
        COOLANT_CELL,
        HEAT_VENT,
        ADVANCED_HEAT_VENT,
        OVERCLOCKED_HEAT_VENT,
        REACTOR_PLATING,
        NEUTRON_REFLECTOR,
        HEAT_EXCHANGER
    }

    private final ComponentType componentType;

    public ReactorComponentItem(Properties properties, ComponentType componentType) {
        super(properties);
        this.componentType = componentType;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public boolean isFuelRod() {
        return componentType == ComponentType.SACRED_ESSENCE
                || componentType == ComponentType.SACRED_SHARD
                || componentType == ComponentType.SACRED_CORE;
    }

    public int getRodCount() {
        return switch (componentType) {
            case SACRED_SHARD -> 8;
            case SACRED_CORE -> 16;
            case SACRED_ESSENCE -> 4;
            default -> 0;
        };
    }

    public int getCoolingPerTick(ItemStack stack) {
        return switch (componentType) {
            case COOLANT_CELL, ADVANCED_HEAT_VENT -> 12;
            case HEAT_VENT -> 6;
            default -> 0;
        };
    }

    public int getHeatVentCoolingAmount() {
        return componentType == ComponentType.HEAT_VENT ? 6 : 0;
    }

    public int getHeatVentDamagePerUnitCooled() {
        return componentType == ComponentType.HEAT_VENT ? 1 : 0;
    }

    public int getHeatExchangerTransferAmount() {
        return componentType == ComponentType.HEAT_EXCHANGER ? 10 : 0;
    }

    public int getHeatExchangerCoolingAmount() {
        return componentType == ComponentType.HEAT_EXCHANGER ? 5 : 0;
    }

    public int getHeatExchangerDamagePerUnitTransferred() {
        return componentType == ComponentType.HEAT_EXCHANGER ? 1 : 0;
    }

    public int getExtraMaxHeat() {
        return componentType == ComponentType.REACTOR_PLATING ? 1000 : 0;
    }

    public int getNeutronReflectionBonus() {
        return componentType == ComponentType.NEUTRON_REFLECTOR ? 1 : 0;
    }
}
