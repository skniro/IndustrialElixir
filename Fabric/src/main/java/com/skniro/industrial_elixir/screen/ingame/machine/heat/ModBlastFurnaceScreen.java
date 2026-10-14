package com.skniro.industrial_elixir.screen.ingame.machine.heat;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.BrewReactorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.heat.ModBlastFurnaceScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class ModBlastFurnaceScreen extends AbstractContainerScreen<ModBlastFurnaceScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID,"textures/gui/container/machine/blastfurnace.png");
    private GuiFluidTankRenderer fluidRenderer;

    public ModBlastFurnaceScreen(ModBlastFurnaceScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        inventoryLabelX = 45;
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;

        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        fluidRenderer = new GuiFluidTankRenderer(menu.blockEntity.fluidContainer.getCapacity(), true, 16, 50);
    }

    private void renderFluidTooltipArea(GuiGraphics context, int pMouseX, int pMouseY, int x, int y,
                                        int offsetX, int offsetY, GuiFluidTankRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            context.renderTooltip(this.font, renderer.getTooltip(menu.blockEntity.fluidContainer), Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderHeatAreaTooltips(GuiGraphics context) {
        context.drawString(font, menu.blockEntity.heatContainer.getSideStorage(null).getAmount() + "/" + menu.blockEntity.heatContainer.getSideStorage(null).getCapacity() + "H", 78, 68, -12566464, false);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int xm, int ym) {
        super.renderLabels(context, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderHeatAreaTooltips(context);
        renderFluidTooltipArea(context, xm, ym, x, y, 8, 7, fluidRenderer);
    }

    @Override
    protected void renderBg(GuiGraphics context, float a, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0,
                imageWidth, imageHeight, 256, 256);

        fluidRenderer.render(context, x + 8, y + 5, menu.blockEntity.fluidContainer);

        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if(menu.isCrafting()) {
            context.blit(TEXTURE,x + 73, y + 34,
                    189, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }

    public static boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, GuiFluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    public static boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}