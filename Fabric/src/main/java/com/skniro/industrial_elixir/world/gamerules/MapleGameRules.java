package com.skniro.industrial_elixir.world.gamerules;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;


public class MapleGameRules {
    public static final GameRule<Boolean> HOT_SPRING_SOURCE_CONVERSION =
            GameRuleBuilder.forBoolean(false).buildAndRegister(Helper.id("hot_spring_source_conversion"));

    public static void maplegamerule() {
        IndustrialElixir.LOGGER.debug("Registering IndustrialElixir Game Rules for " + IndustrialElixir.MOD_ID);
    }
}
