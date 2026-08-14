package com.skniro.industrial_elixir.networking.packet;


import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;


public record EnergySyncS2CPayload(long amount, BlockPos blockPos) implements CustomPacketPayload {
    public static final Identifier EnergySync = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID,"open_torcherino_screen");
    public static final Type<EnergySyncS2CPayload> TYPE = new Type<>(EnergySync);
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergySyncS2CPayload> CODEC = CustomPacketPayload.codec(EnergySyncS2CPayload::write, EnergySyncS2CPayload::new);

    public EnergySyncS2CPayload(final FriendlyByteBuf buf){
        this(
                buf.readLong(),
                buf.readBlockPos()
                );
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(amount);
        buffer.writeBlockPos(blockPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}