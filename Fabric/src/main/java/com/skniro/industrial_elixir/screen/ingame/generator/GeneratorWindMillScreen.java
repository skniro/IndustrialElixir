package com.skniro.industrial_elixir.screen.ingame.generator;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.generator.GeneratorWindMillBlockEntity;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorWindMillScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class GeneratorWindMillScreen extends AbstractContainerScreen<GeneratorWindMillScreenHandler> {
    private static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/guiwindmill.png");

    public GeneratorWindMillScreen(GeneratorWindMillScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Center title
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    public void drawState(GuiGraphicsExtractor context) {
        GeneratorWindMillBlockEntity.GeneratorState state = menu.getState();
        Integer statey = switch (state) {
            case IDLE -> 0;
            case GENERATING -> 13;
            case DANGER -> 26;
        };
        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, leftPos + 80, topPos + 65, 176, statey, 13, 13,256,256);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        drawState(context);
    }


    @Override
    public void extractRenderState(GuiGraphicsExtractor context , int mouseX, int mouseY, float delta) {
        extractBackground(context, mouseX, mouseY, delta);
        super.extractRenderState(context, mouseX, mouseY, delta);
        extractTooltip(context, mouseX, mouseY);
    }
}