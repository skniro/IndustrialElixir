package com.skniro.industrial_elixir.client;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

public class WindowsWatermarkRenderer {

    private static final Minecraft mc = Minecraft.getInstance();

    public static void render(GuiGraphicsExtractor context) {

        if (mc == null || mc.font == null) return;

        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();

        matrices.translate(0, 0);

        String line1 = "Unauthorized use or disclosure in any manner";
        String line2 = "may result in disciplinary action";
        String line3 = "and potential civil and criminal liability.";
        String line4 = "Industrial Elixir";
        String line5 = "For testing purposes only.";
        String line6 = "Build " + FabricLoader.getInstance().getModContainer(IndustrialElixir.MOD_ID).get().getMetadata().getVersion().getFriendlyString();

        String playerName = mc.player != null ? mc.player.getName().getString() : "Player";
        int height = mc.getWindow().getGuiScaledHeight();
        int color = 0x80FFFFFF;
        int x = 5;
        int y = height - 75;
        context.text(mc.font, line1, x, y, color, false);
        context.text(mc.font, line2, x, y + 10, color, false);
        context.text(mc.font, line3, x, y + 20, color, false);
        context.text(mc.font, line4, x, y + 30, color, false);
        context.text(mc.font, line5, x, y + 40, color, false);
        context.text(mc.font, line6, x, y + 50, color, false);
        context.text(mc.font, playerName, x, y + 60, color, false);

        matrices.popMatrix();
    }
}