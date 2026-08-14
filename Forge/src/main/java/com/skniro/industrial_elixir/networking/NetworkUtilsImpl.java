package com.skniro.industrial_elixir.networking;

import com.skniro.industrial_elixir.block.entity.machine.MetalFormerBlockEntity;
import com.skniro.industrial_elixir.networking.packet.BoostJumpC2SPayload;
import com.skniro.industrial_elixir.networking.packet.JetpackInputC2SPayload;
import com.skniro.industrial_elixir.networking.packet.MetalFormerStateC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleHoverC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import com.skniro.industrial_elixir.item.init.JetpackHelper;
import com.skniro.industrial_elixir.item.init.QuantumSuitItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NetworkUtilsImpl {


    public static void handleMetalFormerState(MetalFormerStateC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ServerLevel world = player.level();

            BlockEntity be = world.getBlockEntity(payload.pos());
            if (be instanceof MetalFormerBlockEntity machine) {
                machine.setState(MetalFormerBlockEntity.MetalFormerState.values()[payload.state()]);
                machine.setChanged();
            }
        });
    }

    public static void handleToggleNightVision(ToggleNightVisionC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            QuantumSuitItem.toggleNightVision(context.player().getUUID());
        });
    }

    public static void handleBoostJump(BoostJumpC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
                    QuantumSuitItem.performBoostJump(context.player());
                }
        );
    }

    public static void handleToggleHover(ToggleHoverC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            JetpackHelper.toggleHover(context.player().getUUID());
        });
    }


    public static void handleJetpackInput(JetpackInputC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            JetpackHelper.updateInput(
                    context.player().getUUID(),
                    payload.jumpHeld(),
                    payload.forwardHeld(),
                    payload.strafe()
            );
        });
    }
}
