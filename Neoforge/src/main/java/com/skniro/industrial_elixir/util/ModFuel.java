package com.skniro.industrial_elixir.util;

import com.skniro.industrial_elixir.api.item.ModFuelRegistry;
import com.skniro.industrial_elixir.item.GrowableOresItems;

public class ModFuel {
    public static void registerFuel() {
        ModFuelRegistry.registerFuel(GrowableOresItems.Scrap.get(), 900);
    }
}
