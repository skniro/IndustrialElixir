package com.skniro.industrial_elixir.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.skniro.industrial_elixir.networking.packet.BoostJumpC2SPayload;
import com.skniro.industrial_elixir.networking.packet.JetpackInputC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleHoverC2SPayload;
import com.skniro.industrial_elixir.networking.packet.ToggleNightVisionC2SPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ModClientEvents {
    private static boolean lastJumpHeld = false;
    private static boolean lastForwardHeld = false;
    private static float lastStrafe = 0f;

    public static void onEndTick(Minecraft client) {
        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (ModKeyMappings.Mode_Switch_Keybind.consumeClick()) {
                while (ModKeyMappings.ALT_Keybind.consumeClick()) {
                    ClientPlayNetworking.send(new ToggleNightVisionC2SPayload());
                }
            }

            // Boost Jump: hold Boost key + press Space
            if (ModKeyMappings.Boost_Keybind.isDown()
                    && client.options.keyJump.consumeClick()) {
                ClientPlayNetworking.send(new BoostJumpC2SPayload());
            }

            // Toggle Hover: Mode Switch key + Space (while wearing chestplate)
            if (ModKeyMappings.Mode_Switch_Keybind.isDown()
                    && client.options.keyJump.consumeClick()
                    && client.player != null) {
                ClientPlayNetworking.send(new ToggleHoverC2SPayload());
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
                    ClientPlayNetworking.send(new JetpackInputC2SPayload(jumpHeld, forwardHeld, strafe));
                }
            }
        });
    }
}