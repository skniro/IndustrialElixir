package com.skniro.industrial_elixir.screen.ingame.generator.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.screen.handler.generator.fluid.FluidGeneratorScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class FluidGeneratorScreen extends AbstractContainerScreen<FluidGeneratorScreenHandler> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/generator/fluid_generator.png");
    private GuiFluidTankRenderer fluidRenderer;

    public FluidGeneratorScreen(FluidGeneratorScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = 45;
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
        fluidRenderer = new GuiFluidTankRenderer(menu.blockEntity.fluidContainer.getCapacity(), false, 16, 50);
    }

    public List<Component> getTooltips() {
               return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" EP"));
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    private void renderEnergyAreaTooltips(GuiGraphicsExtractor context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 76, 34, 31, 16)) {
            context.setTooltipForNextFrame(this.getFont(), getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 76, y + 34, 176, 14, getScaledEnergyHeight(), 16,256,256);
    }

    public int getScaledEnergyHeight() {
        long energy = menu.blockEntity.energyContainer.amount;
        long capacity = menu.blockEntity.energyContainer.getCapacity();
        int energyBarSize = 31;

        return Math.toIntExact(capacity != 0 && energy != 0 ? energy * energyBarSize / capacity : 0);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);

        if (MouseUtil.isMouseOver(mouseX, mouseY, x + 8, y + 5, fluidRenderer.getWidth(), fluidRenderer.getHeight())) {
            context.setComponentTooltipForNextFrame(this.font, fluidRenderer.getTooltip(menu.blockEntity.fluidContainer), mouseX, mouseY);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float a) {
        super.extractBackground(context, mouseX, mouseY, a);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        fluidRenderer.render(context, x + 8, y + 5, menu.blockEntity.fluidContainer);
        renderEnergyArea(context, x, y);
    }
}
