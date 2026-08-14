package com.skniro.industrial_elixir.api;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.resources.Identifier;

public class Helper {

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, path);
    }
}
