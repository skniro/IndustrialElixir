package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.machine.InductionFurnaceScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;


public class InductionFurnaceBlockScreen extends AbstractContainerScreen<InductionFurnaceScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/inductionfurnace.png");

    public InductionFurnaceBlockScreen(InductionFurnaceScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    private void renderHeatAreaTooltips(GuiGraphicsExtractor context) {
        context.text(font, Component.literal("Heat: " + menu.getHeat() + "%"), 88, 68, -12566464, false);
    }

    private void renderEnergyAreaTooltips(GuiGraphicsExtractor context, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 15, 32, 13, 13)) {
            context.setTooltipForNextFrame(this.getFont(),
                    List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount() + " / " + menu.blockEntity.energyContainer.getSideStorage(null).getCapacity() + " E")),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 15, y + 32, 176, 0, 13, menu.getScaledEnergyHeight(),256,256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(context, mouseX, mouseY, x, y);
        renderHeatAreaTooltips(context);
        context.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 73, y + 34, 190, 0, progress, 15, 256, 256);
        }

        renderEnergyArea(context, x, y);

/*        int heatWidth = menu.getScaledHeatWidth();
        if (heatWidth > 0) {
            context.fill(x + 62, y + 72, x + 62 + heatWidth, y + 77, 0xFFFF6A00);
        }*/
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
