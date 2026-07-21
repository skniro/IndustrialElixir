package com.skniro.industrial_elixir.screen.ingame.generator.heat;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.generator.heat.ElectricHeaterScreenHandler;
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
public class ElectricHeaterBlockScreen extends AbstractContainerScreen<ElectricHeaterScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/heat/guielectricheatgenerator.png");

    public ElectricHeaterBlockScreen(ElectricHeaterScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = 145;
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    public List<Component> getTooltips() {
               return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" EP"));
    }

    private void renderEnergyAreaTooltips(GuiGraphicsExtractor context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 8, 42, 13, 16)) {
            context.setTooltipForNextFrame(Screens.getFont(this), getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 8, y + 42, 176, 0, 13, menu.getScaledEnergyHeight(),256,256);
    }


    private void renderHeatAreaTooltips(GuiGraphicsExtractor context) {
        context.text(font, menu.getHeatTooltips(), 43, 68,  -12566464, false);
    }


    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);
        renderHeatAreaTooltips(context);
    }

    @Override
       public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        renderEnergyArea(context, x, y);
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

