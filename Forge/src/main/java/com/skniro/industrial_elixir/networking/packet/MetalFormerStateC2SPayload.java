package com.skniro.industrial_elixir.networking.packet;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MetalFormerStateC2SPayload(BlockPos pos, int state) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "metal_former_state");

    public static final Type<MetalFormerStateC2SPayload> TYPE =
            new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MetalFormerStateC2SPayload> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, MetalFormerStateC2SPayload::pos,
                    ByteBufCodecs.INT, MetalFormerStateC2SPayload::state,
                    MetalFormerStateC2SPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}