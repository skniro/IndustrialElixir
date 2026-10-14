package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.HeatCentrifugeScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class HeatCentrifugeBlockScreen extends AbstractContainerScreen<HeatCentrifugeScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/heatcentrifuge.png");
    private GuiFluidTankRenderer fluidRenderer;

    public HeatCentrifugeBlockScreen(HeatCentrifugeScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = 45;
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount() + " / " + menu.blockEntity.energyContainer.getSideStorage(null).getCapacity() + " EP"));
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
        context.blit(TEXTURE, x + 129, y + 45, 176, 0, 13, menu.getScaledEnergyHeight(), 256, 256);
    }

    private void renderHeatAreaTooltips(GuiGraphics context) {
        context.drawString(font, Component.literal("Heat: " + menu.getHeat() / 60 + "%"), 23, 56, -12566464, false);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int xm, int ym) {
        super.renderLabels(context, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderHeatAreaTooltips(context);
        if (MouseUtil.isMouseOver(xm, ym, x + 131, y + 45, 13, 16)) {
            context.renderTooltip(Screens.getTextRenderer(this), getTooltips(), Optional.empty(), xm, ym);
        }
    }

    @Override
    protected void renderBg(GuiGraphics context, float a, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderEnergyArea(context, x, y);
        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 70, y + 34, 189, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }
}
