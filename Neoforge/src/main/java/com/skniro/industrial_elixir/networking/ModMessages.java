package com.skniro.industrial_elixir.networking;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.networking.packet.BoostJumpC2SPayload;
import com.skniro.industrial_elixir.networking.packet.JetpackInputC2SPayload;
import com.skniro.industrial_elixir.networking.packet.MetalFormerStateC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleHoverC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = IndustrialElixir.MOD_ID)
public class ModMessages {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(IndustrialElixir.MOD_ID);
        registrar.playToServer(MetalFormerStateC2SPayload.TYPE, MetalFormerStateC2SPayload.CODEC, NetworkUtilsImpl::handleMetalFormerState);
        registrar.playToServer(ToggleNightVisionC2SPayload.TYPE, ToggleNightVisionC2SPayload.CODEC, NetworkUtilsImpl::handleToggleNightVision);
        registrar.playToServer(BoostJumpC2SPayload.TYPE, BoostJumpC2SPayload.CODEC, NetworkUtilsImpl::handleBoostJump);
        registrar.playToServer(ToggleHoverC2SPayload.TYPE, ToggleHoverC2SPayload.CODEC, NetworkUtilsImpl::handleToggleHover);
        registrar.playToServer(JetpackInputC2SPayload.TYPE, JetpackInputC2SPayload.CODEC, NetworkUtilsImpl::handleJetpackInput);
    }
}