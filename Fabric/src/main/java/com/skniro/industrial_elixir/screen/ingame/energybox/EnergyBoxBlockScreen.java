package com.skniro.industrial_elixir.screen.ingame.energybox;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.energybox.EnergyBoxScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import java.util.List;
import java.util.Optional;

public class EnergyBoxBlockScreen extends AbstractContainerScreen<EnergyBoxScreenHandler> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/energybox/energy_box.png");

    public EnergyBoxBlockScreen(EnergyBoxScreenHandler handler, Inventory inventory, Component title) {
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

    private void renderEnergyAreaTooltips(GuiGraphics context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 72, 33, 31, 16)) {
            context.renderTooltip(Screens.getTextRenderer(this), getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
        context.blit(GUI_TEXTURE, x + 72, y + 30, 176, 14, getScaledEnergyHeight(), 16,256,256);
    }

    public int getScaledEnergyHeight() {
        long energy = menu.blockEntity.energyContainer.amount;
        long capacity = menu.blockEntity.energyContainer.getCapacity();
        int energyBarSize = 31;

        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);
    }

    @Override
    protected void renderBg(GuiGraphics context, float a, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);

        renderEnergyArea(context, x, y);
    }

    @Override
   public void render(GuiGraphics context , int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}