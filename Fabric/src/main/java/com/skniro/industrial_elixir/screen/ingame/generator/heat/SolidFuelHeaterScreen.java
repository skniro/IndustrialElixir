package com.skniro.industrial_elixir.screen.ingame.generator.heat;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.generator.heat.SolidFuelHeaterScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class SolidFuelHeaterScreen extends AbstractContainerScreen<SolidFuelHeaterScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/heat/solid_fuel_heater.png");
    private static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.parse("container/furnace/lit_progress");

    public SolidFuelHeaterScreen(SolidFuelHeaterScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
        inventoryLabelX = 145;
        inventoryLabelY = 72;
    }

    private void renderHeatAreaTooltips(GuiGraphics context) {
        context.drawString(font, menu.getHeatTooltips(), 47, 62,  -12566464, false);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.drawString(font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.drawString(font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
        renderHeatAreaTooltips(context);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Burn progress flame icon
        renderBurnProgress(graphics, x, y);

        // Heat bar
        int heatHeight = menu.getScaledHeatHeight();
        if (heatHeight > 0) {
            graphics.blit(TEXTURE,
                    x + 146, y + 62 - heatHeight, 176, 52 - heatHeight, 12, heatHeight, 256, 256);
        }
    }

    private void renderBurnProgress(GuiGraphics context, int x, int y) {
        if(menu.isBurning()) {
            int l = Mth.ceil(this.menu.getFuelProgress() * 13.0f) + 1;
            context.blitSprite(LIT_PROGRESS_TEXTURE, 14, 14, 0, 14 - l, x + 80, y + 18 + 14 - l, 14, l);
        }
    }
}
