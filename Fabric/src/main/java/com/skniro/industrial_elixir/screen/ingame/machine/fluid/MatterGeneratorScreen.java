package com.skniro.industrial_elixir.screen.ingame.machine.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.MatterGeneratorScreenHandler;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.OreWashingScreenHandler;
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

public class MatterGeneratorScreen extends AbstractContainerScreen<MatterGeneratorScreenHandler> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/matter_generator.png");
    private GuiFluidTankRenderer fluidRenderer;

    public MatterGeneratorScreen(MatterGeneratorScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = 45;
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
        fluidRenderer = new GuiFluidTankRenderer(menu.blockEntity.fluidContainer.getCapacity(), false, 16, 50);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        super.extractLabels(graphics, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.text(font, menu.blockEntity.getProgressPercent() + "%", 77, 41, -12566464, false);
        graphics.text(font, menu.blockEntity.energyContainer.getSideStorage(null).getAmount() + " EP", 72, 61, -12566464, false);

        if (MouseUtil.isMouseOver(xm, ym, x + 8, y + 5, fluidRenderer.getWidth(), fluidRenderer.getHeight())) {
            graphics.setComponentTooltipForNextFrame(this.font, fluidRenderer.getTooltip(menu.blockEntity.fluidContainer), xm, ym);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        fluidRenderer.render(graphics, x + 8, y + 5, menu.blockEntity.fluidContainer);
        renderProgressArrow(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphicsExtractor guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 79, y + 32, 189, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }
}
