package com.skniro.industrial_elixir.networking;

import com.google.common.base.Suppliers;
import com.skniro.industrial_elixir.block.entity.machine.MetalFormerBlockEntity;
import com.skniro.industrial_elixir.networking.packet.BoostJumpC2SPayload;
import com.skniro.industrial_elixir.networking.packet.JetpackInputC2SPayload;
import com.skniro.industrial_elixir.networking.packet.MetalFormerStateC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleHoverC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import com.skniro.industrial_elixir.item.init.JetpackHelper;
import com.skniro.industrial_elixir.item.init.QuantumSuitItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.function.Supplier;

public class NetworkUtilsImpl {
    private static final Supplier<NetworkUtilsImpl> instance = Suppliers.memoize(NetworkUtilsImpl::new);

    public static NetworkUtilsImpl getInstance() {
        return instance.get();
    }

    public void initialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientNetworking.initialize();
        }

        ServerPlayNetworking.registerGlobalReceiver(
                MetalFormerStateC2SPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    ServerLevel world = player.level();
                    context.server().execute(() -> {
                        BlockEntity be = world.getBlockEntity(payload.pos());
                        if (be instanceof MetalFormerBlockEntity machine) {
                            machine.setState(MetalFormerBlockEntity.MetalFormerState.values()[payload.state()]);
                            machine.setChanged();
                        }
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                ToggleNightVisionC2SPayload.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        QuantumSuitItem.toggleNightVision(context.player().getUUID());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                BoostJumpC2SPayload.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        QuantumSuitItem.performBoostJump(context.player());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                ToggleHoverC2SPayload.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        JetpackHelper.toggleHover(context.player().getUUID());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                JetpackInputC2SPayload.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        JetpackHelper.updateInput(
                                context.player().getUUID(),
                                payload.jumpHeld(),
                                payload.forwardHeld(),
                                payload.strafe()
                        );
                    });
                }
        );

        // Unified damage handling for all Quantum Suit pieces
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof Player player)) return true;

            // Fall damage negation (Quantum Boots)
            if (source.typeHolder().is(DamageTypeTags.IS_FALL)) {
                return !QuantumSuitItem.tryNegateFallDamage(player);
            }

            // Lava immunity (full Quantum set)
            if (source.typeHolder().is(DamageTypeTags.IS_FIRE)
                    && player.isInLava()
                    && QuantumSuitItem.hasFullQuantumSet(player)) {
                return false;
            }

            // General damage absorption for all quantum pieces
            if (amount > 0) {
                return QuantumSuitItem.absorbDamage(player, amount, source);
            }

            return true;
        });
    }

    private static class ClientNetworking {
        private static void initialize() {

        }
    }
}
