package com.skniro.industrial_elixir.screen.ingame.generator.heat;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.generator.heat.SolidFuelHeaterScreenHandler;
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
public class SolidFuelHeaterScreen extends AbstractContainerScreen<SolidFuelHeaterScreenHandler> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/heat/solid_fuel_heater.png");

    public SolidFuelHeaterScreen(SolidFuelHeaterScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
        inventoryLabelX = 8;
        inventoryLabelY = 72;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.text(font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        graphics.text(font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);

        // Heat tooltip
        if (MouseUtil.isMouseOver(mouseX, mouseY, x + 146, y + 12, 12, 52)) {
            graphics.setTooltipForNextFrame(Screens.getFont(this),
                    List.of(Component.literal(menu.blockEntity.heatContainer.amount + " / 100 HU")),
                    Optional.empty(), mouseX - x, mouseY - y);
        }

        // Fuel burn tooltip
        if (menu.isBurning()) {
            graphics.text(font,
                    Component.literal(menu.blockEntity.getBurnTime() + " / " + menu.blockEntity.getBurnDuration()),
                    78, 56, -12566464, false);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Burn progress
        if (menu.isBurning()) {
            int scaled = menu.getScaledBurnProgress();
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 80, y + 53, 176, 0, scaled, 15, 256, 256);
        }

        // Heat bar
        int heatHeight = menu.getScaledHeatHeight();
        if (heatHeight > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    x + 146, y + 62 - heatHeight, 176, 52 - heatHeight, 12, heatHeight, 256, 256);
        }
    }
}
