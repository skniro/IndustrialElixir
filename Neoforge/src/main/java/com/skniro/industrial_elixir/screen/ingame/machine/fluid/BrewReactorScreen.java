package com.skniro.industrial_elixir.screen.ingame.machine.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.BrewReactorScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class BrewReactorScreen extends AbstractContainerScreen<BrewReactorScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID,"textures/gui/container/machine/brewreactor.png");
    private GuiFluidTankRenderer fluidRenderer;

    public BrewReactorScreen(BrewReactorScreenHandler menu, Inventory inventory, Component title) {
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

    public List<Component> getTooltips() {
               return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" EP"));
    }

    private void assignFluidRenderer() {
        fluidRenderer = new GuiFluidTankRenderer(menu.blockEntity.fluidContainer.getCapacity(), true, 16, 50);
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
        context.blit(TEXTURE, x + 129, y + 45, 176, 0, 13, menu.getScaledEnergyHeight(),256,256);
    }

    private void renderEnergyAreaTooltips(GuiGraphics context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 131, 45, 13, 14)) {
            context.renderTooltip(this.font, getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderFluidTooltipArea(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y,
                                        int offsetX, int offsetY, GuiFluidTankRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            guiGraphics.renderTooltip(this.font, renderer.getTooltip(menu.blockEntity.fluidContainer), Optional.empty(), pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
        super.renderLabels(graphics, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(graphics, xm, ym, x, y);
        renderFluidTooltipArea(graphics, xm, ym, x, y, 8, 7, fluidRenderer);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0,
                imageWidth, imageHeight, 256, 256);

        renderEnergyArea(graphics, x, y);
        fluidRenderer.render(graphics, x + 8, y + 5, menu.blockEntity.fluidContainer);

        renderProgressArrow(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE,x + 79, y + 32,
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
