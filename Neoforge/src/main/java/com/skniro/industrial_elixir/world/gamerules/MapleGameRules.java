package com.skniro.industrial_elixir.world.gamerules;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.IEventBus;


public class MapleGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> HOT_SPRING_SOURCE_CONVERSION =
            GameRules.register("hotSpringSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    public static void maplegamerule(IEventBus eventBus) {
        IndustrialElixir.LOGGER.debug("Registering IndustrialElixir Game Rules for " + IndustrialElixir.MOD_ID);
    }
}
