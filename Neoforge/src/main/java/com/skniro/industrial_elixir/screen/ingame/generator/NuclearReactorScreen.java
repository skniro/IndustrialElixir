package com.skniro.industrial_elixir.screen.ingame.generator;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.generator.NuclearReactorScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class NuclearReactorScreen extends AbstractContainerScreen<NuclearReactorScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/guinuclearreactor.png");
    private static final int HEAT_BAR_X = 8;
    private static final int STATUS_BAR_Y = 96;
    private static final int OUTPUT_BAR_X = 68;
    public NuclearReactorScreen(NuclearReactorScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 211;
        this.imageHeight = 242;
        this.inventoryLabelY = 98;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 5;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);

        int localX = mouseX - leftPos;
        int localY = mouseY - topPos;
        if (MouseUtil.isMouseOver(mouseX, mouseY, leftPos + HEAT_BAR_X, topPos + STATUS_BAR_Y, 54, 8)) {
            graphics.renderTooltip(this.font,
                    List.of(Component.literal("Heat: " + menu.getHeat() + " / " + menu.getMaxHeat())),
                    Optional.empty(), localX, localY);
        }
        if (MouseUtil.isMouseOver(mouseX, mouseY, leftPos + OUTPUT_BAR_X, topPos + STATUS_BAR_Y, 106, 8)) {
            graphics.renderTooltip(this.font,
                    List.of(Component.literal("Output: " + menu.getGeneration() + " E/t")),
                    Optional.empty(), localX, localY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        int heatWidth = menu.getScaledHeatWidth();
        if (heatWidth > 0) {
            graphics.blit(TEXTURE, leftPos + HEAT_BAR_X, topPos + STATUS_BAR_Y, 176, 0, heatWidth, 8, 256, 256);
        }

        int outputWidth = Math.min(106, menu.getGeneration() * 106 / 420);
        if (outputWidth > 0) {
            graphics.blit(TEXTURE, leftPos + OUTPUT_BAR_X, topPos + STATUS_BAR_Y, 176, 8, outputWidth, 8, 256, 256);
        }
    }
}
