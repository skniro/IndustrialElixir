package com.skniro.industrial_elixir.networking.packet;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BoostJumpC2SPayload() implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "boost_jump");
    public static final Type<BoostJumpC2SPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BoostJumpC2SPayload> CODEC =
            StreamCodec.unit(new BoostJumpC2SPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
