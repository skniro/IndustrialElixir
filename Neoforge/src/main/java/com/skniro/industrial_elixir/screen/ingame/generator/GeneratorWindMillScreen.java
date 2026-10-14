package com.skniro.industrial_elixir.screen.ingame.generator;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.generator.GeneratorWindMillBlockEntity;
import com.skniro.industrial_elixir.screen.handler.generator.GeneratorWindMillScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GeneratorWindMillScreen extends AbstractContainerScreen<GeneratorWindMillScreenHandler> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/guiwindmill.png");

    public GeneratorWindMillScreen(GeneratorWindMillScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Center title
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    public void drawState(GuiGraphics context) {
        GeneratorWindMillBlockEntity.GeneratorState state = menu.getState();
        Integer statey = switch (state) {
            case IDLE -> 0;
            case GENERATING -> 13;
            case DANGER -> 26;
        };
        context.blit(GUI_TEXTURE, leftPos + 80, topPos + 65, 176, statey, 13, 15,256,256);
    }

    @Override
    protected void renderBg(GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        drawState(context);
    }


    @Override
    public void render(GuiGraphics context , int mouseX, int mouseY, float delta) {
        renderBg(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
