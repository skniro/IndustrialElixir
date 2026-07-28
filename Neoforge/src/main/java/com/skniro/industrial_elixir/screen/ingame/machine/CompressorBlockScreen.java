package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.machine.CompressorScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CompressorBlockScreen extends AbstractContainerScreen<CompressorScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/compressor.png");

    public CompressorBlockScreen(CompressorScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    public List<Component> getTooltips() {
               return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" EP"));
    }

    private void renderEnergyAreaTooltips(GuiGraphicsExtractor context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 52, 32, 13, 16)) {
            context.setTooltipForNextFrame(Screens.getFont(this), getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 52, y + 31, 176, 0, 13, menu.getScaledEnergyHeight(),256,256);
    }



    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);
    }

    @Override
       public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        renderProgressArrow(context, x, y);
        renderEnergyArea(context, x, y);
    }

    private void renderProgressArrow(GuiGraphicsExtractor context, int x, int y) {
        if(menu.isCrafting()) {
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 72, y + 33, 189, 0, menu.getScaledProgress(),15,256,256);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        super.extractRenderState(context, mouseX, mouseY, deltaTicks);
        this.extractTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}

