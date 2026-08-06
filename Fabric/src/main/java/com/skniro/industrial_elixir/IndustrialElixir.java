package com.skniro.industrial_elixir;

import com.skniro.industrial_elixir.energy.heat.impl.HeatImpl;
import com.skniro.industrial_elixir.networking.ModMessages;

import com.skniro.industrial_elixir.energy.impl.EnergyImpl;
import dev.architectury.platform.Mod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDate;


public class IndustrialElixir implements ModInitializer {
    public static final String MOD_ID = "industrial_elixir";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        //checkExpiration();
        
        ModContent.registerCommand();
        ModContent.registerFluids();
        ModContent.registerItem();
        ModContent.registerBlock();
        ModContent.registerEntity();
        ModContent.CreativeTab();
        ModContent.WorldGen();
        ModContent.registerOthers();

        EnergyImpl.init();
        HeatImpl.init();
        ModMessages.register();
        ModContent.Compat();
    }

    public static void checkExpiration() {
        String version = FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
                .orElse("");

        boolean isDev = version.contains("alpha") || version.contains("beta");

        LocalDate expireDate = LocalDate.of(2099, 8, 1);

        if (isDev && LocalDate.now().isAfter(expireDate)) {

            LOGGER.error("====================================");
            LOGGER.error(" This mod version has expired!");
            LOGGER.error(" Please update to a newer version.");
            LOGGER.error("====================================");
        }
    }
}
