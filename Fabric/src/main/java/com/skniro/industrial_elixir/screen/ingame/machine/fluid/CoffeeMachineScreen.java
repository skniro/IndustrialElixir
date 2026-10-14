package com.skniro.industrial_elixir.screen.ingame.machine.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.CoffeeMachineScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class CoffeeMachineScreen extends AbstractContainerScreen<CoffeeMachineScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/coffee_machine.png");
    private GuiFluidTankRenderer fluidRenderer;

    public CoffeeMachineScreen(CoffeeMachineScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = 45;
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
        fluidRenderer = new GuiFluidTankRenderer(menu.blockEntity.fluidContainer.getCapacity(), true, 16, 50);
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount() + " / " + menu.blockEntity.energyContainer.getSideStorage(null).getCapacity() + " E"));
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
        context.blit(TEXTURE, x + 129, y + 45, 176, 0, 13, menu.getScaledEnergyHeight(), 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
        super.renderLabels(graphics, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (MouseUtil.isMouseOver(xm, ym, x + 131, y + 45, 13, 14)) {
            graphics.renderTooltip(Screens.getTextRenderer(this), getTooltips(), Optional.empty(), xm, ym);
        }
        if (MouseUtil.isMouseOver(xm, ym, x + 8, y + 5, fluidRenderer.getWidth(), fluidRenderer.getHeight())) {
            graphics.renderTooltip(this.font, fluidRenderer.getTooltip(menu.blockEntity.fluidContainer), Optional.empty(), xm, ym);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderEnergyArea(graphics, x, y);
        fluidRenderer.render(graphics, x + 8, y + 5, menu.blockEntity.fluidContainer);
        renderProgressArrow(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 70, y + 34, 189, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }
}
