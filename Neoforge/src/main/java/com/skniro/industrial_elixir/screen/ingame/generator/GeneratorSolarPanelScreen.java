package com.skniro.industrial_elixir.screen.ingame.generator;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.generator.GeneratorSolarPanelBlockEntity;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorSolarPanelScreenHandler;
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

public class GeneratorSolarPanelScreen extends AbstractContainerScreen<GeneratorSolarPanelScreenHandler> {
    private static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/guisolarpanel.png");

    public GeneratorSolarPanelScreen(GeneratorSolarPanelScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Center title
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    public List<Component> getTooltips() {
               return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" EP"));
    }

    public void drawState(GuiGraphicsExtractor context) {
        GeneratorSolarPanelBlockEntity.GeneratorState state = menu.getState();
        Integer statey = switch (state) {
            case IDLE -> 0;
            case GENERATING -> 16;
        };
        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, leftPos + 85, topPos + 60, 176, statey, 14, 13,256,256);
    }

    private void renderEnergyAreaTooltips(GuiGraphicsExtractor context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 72, 33, 31, 16)) {
            context.setTooltipForNextFrame(Screens.getFont(this), getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x + 76, y + 33, 190, 0, getScaledEnergyHeight(), 16,256,256);
    }

    public int getScaledEnergyHeight() {
        long energy = menu.blockEntity.energyContainer.amount;
        long capacity = menu.blockEntity.energyContainer.getCapacity();
        int energyBarSize = 31;

        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        context.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        drawState(context);
        renderEnergyArea(context, x, y);
    }


    @Override
   public void extractRenderState(GuiGraphicsExtractor context , int mouseX, int mouseY, float delta) {
        extractBackground(context, mouseX, mouseY, delta);
        super.extractRenderState(context, mouseX, mouseY, delta);
        extractTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}