package com.skniro.industrial_elixir.keybind;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.networking.packet.BoostJumpC2SPayload;
import com.skniro.industrial_elixir.networking.packet.JetpackInputC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleHoverC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = IndustrialElixir.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {
    private static boolean lastJumpHeld = false;
    private static boolean lastForwardHeld = false;
    private static float lastStrafe = 0f;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();


        while (ModKeyMappings.Mode_Switch_Keybind.consumeClick()) {
            while (ModKeyMappings.ALT_Keybind.consumeClick()) {
                ClientPacketDistributor.sendToServer(new ToggleNightVisionC2SPayload());
            }
        }

        // Boost Jump: hold Boost key + press Space
        if (ModKeyMappings.Boost_Keybind.isDown()
                && client.options.keyJump.consumeClick()) {
            ClientPacketDistributor.sendToServer(new BoostJumpC2SPayload());
        }

        // Toggle Hover: Mode Switch key + Space (while wearing chestplate)
        if (ModKeyMappings.Mode_Switch_Keybind.isDown()
                && client.options.keyJump.consumeClick()
                && client.player != null) {
            ClientPacketDistributor.sendToServer(new ToggleHoverC2SPayload());
        }

        // Jetpack input: send key state changes to server
        if (client.player != null) {
            boolean jumpHeld = client.options.keyJump.isDown();
            boolean forwardHeld = client.options.keyUp.isDown();
            float strafe = (client.options.keyLeft.isDown() ? -1f : 0f)
                    + (client.options.keyRight.isDown() ? 1f : 0f);
            if (jumpHeld != lastJumpHeld || forwardHeld != lastForwardHeld || strafe != lastStrafe) {
                lastJumpHeld = jumpHeld;
                lastForwardHeld = forwardHeld;
                lastStrafe = strafe;
                ClientPacketDistributor.sendToServer(new JetpackInputC2SPayload(jumpHeld, forwardHeld, strafe));
            }
        }
    }
}