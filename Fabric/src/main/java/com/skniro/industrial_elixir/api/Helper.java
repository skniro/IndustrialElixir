package com.skniro.industrial_elixir.api;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.resources.ResourceLocation;

public class Helper {

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, path);
    }
}
