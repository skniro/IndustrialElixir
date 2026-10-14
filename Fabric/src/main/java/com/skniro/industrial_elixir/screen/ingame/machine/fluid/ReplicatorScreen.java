package com.skniro.industrial_elixir.screen.ingame.machine.fluid;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.block.entity.machine.fluid.ReplicatorBlockEntity;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.ReplicatorScreenHandler;
import com.skniro.industrial_elixir.screen.ingame.widgets.ReplicatorModeButton;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ReplicatorScreen extends AbstractContainerScreen<ReplicatorScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/replicator.png");
    private GuiFluidTankRenderer fluidRenderer;

    public ReplicatorScreen(ReplicatorScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = 45;
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 4;
        assignFluidRenderer();
        addModeButtons();
    }

    private void assignFluidRenderer() {
        fluidRenderer = new GuiFluidTankRenderer(menu.blockEntity.fluidContainer.getCapacity(), true, 16, 50);
    }

    private void addModeButtons() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        ReplicatorBlockEntity be = this.menu.blockEntity;

        this.addRenderableWidget(new ReplicatorModeButton(
                x + 56, y + 52, 18, 18,
                ReplicatorBlockEntity.Mode.STOP,
                be.getMode() == ReplicatorBlockEntity.Mode.STOP,
                Items.BARRIER.getDefaultInstance(),
                Component.translatable("gui.industrial_elixir.replicator.stop"),
                (b) -> onModeButton(ReplicatorScreenHandler.BUTTON_STOP)));

        this.addRenderableWidget(new ReplicatorModeButton(
                x + 76, y + 52, 18, 18,
                ReplicatorBlockEntity.Mode.SINGLE,
                be.getMode() == ReplicatorBlockEntity.Mode.SINGLE,
                Items.PAPER.getDefaultInstance(),
                Component.translatable("gui.industrial_elixir.replicator.single"),
                (b) -> onModeButton(ReplicatorScreenHandler.BUTTON_SINGLE)));

        this.addRenderableWidget(new ReplicatorModeButton(
                x + 96, y + 52, 18, 18,
                ReplicatorBlockEntity.Mode.LOOP,
                be.getMode() == ReplicatorBlockEntity.Mode.LOOP,
                Items.REPEATER.getDefaultInstance(),
                Component.translatable("gui.industrial_elixir.replicator.loop"),
                (b) -> onModeButton(ReplicatorScreenHandler.BUTTON_LOOP)));
    }

    private void onModeButton(int buttonId) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
        }
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(
                NumberFormat.getNumberInstance(Locale.US).format(menu.blockEntity.energyContainer.amount)
                + " / "
                + NumberFormat.getNumberInstance(Locale.US).format(menu.blockEntity.energyContainer.getCapacity())
                + " EP"));
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
        context.blit(TEXTURE, x + 129, y + 45, 176, 0, 13, menu.getScaledEnergyHeight(), 256, 256);
    }

    private void renderEnergyAreaTooltips(GuiGraphics context, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 131, 45, 13, 14)) {
            context.renderTooltip(Screens.getTextRenderer(this), getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderFluidTooltipArea(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 8, 7, fluidRenderer)) {
            guiGraphics.renderTooltip(this.font,
                    fluidRenderer.getTooltip(menu.blockEntity.fluidContainer), Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
        super.renderLabels(graphics, xm, ym);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(graphics, xm, ym, x, y);
        renderFluidTooltipArea(graphics, xm, ym, x, y);

        // Progress text
        ReplicatorBlockEntity be = this.menu.blockEntity;
        if (be.getMode() != ReplicatorBlockEntity.Mode.STOP && (be.getReplicatingEnergyCost() > 0 || be.getReplicatingUUCost() > 0)) {
            int pct = be.getOverallProgressPercent();
            graphics.drawString(font, pct + "%", 77, 41, -12566464, false);
        }

        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        renderEnergyArea(graphics, x, y);
        fluidRenderer.render(graphics, x + 8, y + 5, menu.blockEntity.fluidContainer);
        renderProgressArrow(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 72, y + 22, 189, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    public static boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, GuiFluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    public static boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
