package com.skniro.industrial_elixir.compat.rei;

public final class IndustrialModREIUtils {
    private IndustrialModREIUtils() {}

    public static boolean isREIAvailable() {
        try {
            Class.forName("me.shedaniel.rei.api.common.plugins.REICommonPlugin");

            return true;
        }catch(ClassNotFoundException e) {
            return false;
        }
    }
}