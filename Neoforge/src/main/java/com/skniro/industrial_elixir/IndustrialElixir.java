package com.skniro.industrial_elixir;

import com.skniro.industrial_elixir.api.item.replicator.ReplicatorValueMap;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.energy.heat.impl.HeatImpl;
import com.skniro.industrial_elixir.item.ModCreativeTab;
import com.skniro.industrial_elixir.networking.ModMessages;
import com.skniro.industrial_elixir.util.ModFuel;

import com.skniro.industrial_elixir.energy.impl.EnergyImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(IndustrialElixir.MOD_ID)
public class IndustrialElixir {
    public static final String MOD_ID = "industrial_elixir";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public IndustrialElixir(IEventBus modEventBus) {
        ModContent.registerCommand(modEventBus);
        ModContent.registerItem(modEventBus);
        ModContent.registerFluids(modEventBus);
        ModContent.registerBlock(modEventBus);
        ModContent.registerEntity(modEventBus);
        ModContent.CreativeTab(modEventBus);
        ModContent.WorldGen(modEventBus);
        ModContent.registerOthers(modEventBus);

        EnergyImpl.register(modEventBus);
        HeatImpl.register(modEventBus);
        EnergyImpl.init();
        HeatImpl.init();
        ModMessages.register();
        ModContent.Compat();

        modEventBus.addListener(FMLCommonSetupEvent.class, event -> {
            AlchemyBlockEntityType.registerMachineEnergyEntity();
            ReplicatorValueMap.registerDefaults();
            ModFuel.registerFuel();
        });

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
