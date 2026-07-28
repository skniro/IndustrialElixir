package com.skniro.industrial_elixir.compat.jei;

public final class IndustrialElixirJEIUtils {
    private IndustrialElixirJEIUtils() {}

    public static boolean isJEIAvailable() {
        try {
            Class.forName("mezz.jei.api.IModPlugin");

            return true;
        }catch(ClassNotFoundException e) {
            return false;
        }
    }
}