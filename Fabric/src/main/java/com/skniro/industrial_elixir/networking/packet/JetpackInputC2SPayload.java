package com.skniro.industrial_elixir.networking.packet;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record JetpackInputC2SPayload(boolean jumpHeld, boolean forwardHeld, float strafe) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "jetpack_input");
    public static final Type<JetpackInputC2SPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, JetpackInputC2SPayload> CODEC =
            StreamCodec.composite(
                    net.minecraft.network.codec.ByteBufCodecs.BOOL, JetpackInputC2SPayload::jumpHeld,
                    net.minecraft.network.codec.ByteBufCodecs.BOOL, JetpackInputC2SPayload::forwardHeld,
                    net.minecraft.network.codec.ByteBufCodecs.FLOAT, JetpackInputC2SPayload::strafe,
                    JetpackInputC2SPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
