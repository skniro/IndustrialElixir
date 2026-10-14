package com.skniro.industrial_elixir.world.gamerules;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;


public class MapleGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> HOT_SPRING_SOURCE_CONVERSION =
            GameRuleRegistry.register("hotSpringSourceConversion", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));

    public static void maplegamerule() {
        IndustrialElixir.LOGGER.debug("Registering IndustrialElixir Game Rules for " + IndustrialElixir.MOD_ID);
    }
}
