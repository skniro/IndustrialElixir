package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.HeatCentrifugeScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class HeatCentrifugeBlockScreen extends AbstractContainerScreen<HeatCentrifugeScreenHandler> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/heatcentrifuge.png");
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

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 129, y + 45, 176, 0, 13, menu.getScaledEnergyHeight(), 256, 256);
    }

    private void renderHeatAreaTooltips(GuiGraphicsExtractor context) {
        context.text(font, Component.literal("Heat: " + menu.getHeat() / 60 + "%"), 23, 56, -12566464, false);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int xm, int ym) {
        super.extractLabels(context, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderHeatAreaTooltips(context);
        if (MouseUtil.isMouseOver(xm, ym, x + 131, y + 45, 13, 16)) {
            context.setTooltipForNextFrame(Screens.getFont(this), getTooltips(), Optional.empty(), xm - x, ym - y);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float a) {
        super.extractBackground(context, mouseX, mouseY, a);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderEnergyArea(context, x, y);
        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(GuiGraphicsExtractor context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 70, y + 34, 189, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }
}
