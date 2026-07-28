package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.machine.MolecularTransformerBlockEntity;
import com.skniro.industrial_elixir.screen.handler.machine.MolecularTransformerScreenHandler;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MolecularTransformerBlockScreen extends AbstractContainerScreen<MolecularTransformerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/molecular_transformer.png");

    public MolecularTransformerBlockScreen(MolecularTransformerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    public Component getTooltips() {
        return Component.literal(((MolecularTransformerBlockEntity) menu.blockEntity).energyInputPerTick + " EP/t");
    }

    private void renderArrowTooltips(GuiGraphicsExtractor context) {
        context.text(font, Component.literal( menu.getProgressPercent() + "%"), 74, 22, -12566464, false);
    }
    
    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
        context.text(font, getTooltips(), 64, 61, -12566464, false);
        renderArrowTooltips(context);
    }

    @Override
       public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(GuiGraphicsExtractor context, int x, int y) {
        int progress = menu.getScaledProgress();
        if (progress > 0) {
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    x + 74, y + 37, 194, 2, progress, 9,
                    256, 256);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context , int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
         extractTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}

