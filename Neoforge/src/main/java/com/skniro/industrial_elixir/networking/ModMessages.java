package com.skniro.industrial_elixir.networking;

import com.skniro.industrial_elixir.networking.packet.BoostJumpC2SPayload;
import com.skniro.industrial_elixir.networking.packet.JetpackInputC2SPayload;
import com.skniro.industrial_elixir.networking.packet.MetalFormerStateC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleHoverC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import com.skniro.industrial_elixir.networking.packet.VendorMachineSynPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ModMessages {
    public static void register() {
        registerS2CPackets(PayloadTypeRegistry.clientboundPlay());
        registerC2SPackets(PayloadTypeRegistry.serverboundPlay());
        NetworkUtilsImpl.getInstance().initialize();
    }

    public static void registerC2SPackets(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        registry.register(MetalFormerStateC2SPayload.TYPE, MetalFormerStateC2SPayload.CODEC);
        registry.register(ToggleNightVisionC2SPayload.TYPE, ToggleNightVisionC2SPayload.CODEC);
        registry.register(BoostJumpC2SPayload.TYPE, BoostJumpC2SPayload.CODEC);
        registry.register(ToggleHoverC2SPayload.TYPE, ToggleHoverC2SPayload.CODEC);
        registry.register(JetpackInputC2SPayload.TYPE, JetpackInputC2SPayload.CODEC);
        //registry.register(VendorMachineSynPayload.TYPE, VendorMachineSynPayload.CODEC);
    }

    public static void registerS2CPackets(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        registry.register(VendorMachineSynPayload.TYPE, VendorMachineSynPayload.CODEC);
    }
}