package com.skniro.industrial_elixir.screen.ingame.generator;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.generator.SacredGeneratorScreenHandler;
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

public class SacredGeneratorScreen extends AbstractContainerScreen<SacredGeneratorScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/guinsacredgenerator.png");
    private static final int STATUS_BAR_Y = 96;
    private static final int OUTPUT_BAR_X = 68;

    public SacredGeneratorScreen(SacredGeneratorScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelX = 122;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 5;
    }

    private void renderHeatAreaTooltips(GuiGraphicsExtractor context) {
        var storage = menu.blockEntity.heatContainer.getSideStorage(null);
        long amount = storage.getAmount();
        long capacity = storage.getCapacity();
        int percent = capacity == 0 ? 0 : (int)((amount * 100.0f) / capacity);
        context.text(font, percent + "%", 8, 61, -12566464, false);
    }
/*    private void renderHeatAreaTooltips(GuiGraphicsExtractor context) {
        context.text(font, menu.blockEntity.heatContainer.getSideStorage(null).getAmount() + "/" + menu.blockEntity.heatContainer.getSideStorage(null).getCapacity() + "E", 8, 59, -12566464, false);
    }*/

    private void renderEnergyOutputTooltips(GuiGraphicsExtractor context) {
        context.text(font, menu.getGeneration() + "EP/t", 8, 44, -12566464, false);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
        renderHeatAreaTooltips(context);
        renderEnergyOutputTooltips(context);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}

