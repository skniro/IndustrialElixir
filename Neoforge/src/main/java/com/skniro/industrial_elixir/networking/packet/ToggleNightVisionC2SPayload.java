package com.skniro.industrial_elixir.networking.packet;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleNightVisionC2SPayload() implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "toggle_night_vision");
    public static final Type<ToggleNightVisionC2SPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleNightVisionC2SPayload> CODEC =
            StreamCodec.unit(new ToggleNightVisionC2SPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
