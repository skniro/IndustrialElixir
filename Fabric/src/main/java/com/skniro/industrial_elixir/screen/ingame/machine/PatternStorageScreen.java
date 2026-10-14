package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.entity.machine.PatternStorageBlockEntity;
import com.skniro.industrial_elixir.screen.handler.machine.PatternStorageScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class PatternStorageScreen extends AbstractContainerScreen<PatternStorageScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/pattern_storage.png");

    private Button copyButton;

    public PatternStorageScreen(PatternStorageScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        inventoryLabelX = 8;
        inventoryLabelY = 72;

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.copyButton = Button.builder(Component.translatable("gui.industrial_elixir.pattern_storage.copy"),
                this::onCopyButtonPressed)
                .bounds(x + 87, y + 18, 40, 14)
                .build();
        this.addRenderableWidget(this.copyButton);
    }

    private void onCopyButtonPressed(Button button) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId, PatternStorageScreenHandler.COPY_BUTTON_ID);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        ItemStack sourceCrystal = this.menu.blockEntity.inventory.get(1);
        ItemStack targetCrystal = this.menu.blockEntity.inventory.get(10);
        boolean canCopy = PatternStorageBlockEntity.hasPatternData(sourceCrystal)
                && !targetCrystal.isEmpty()
                && !PatternStorageBlockEntity.hasPatternData(targetCrystal);
        this.copyButton.active = canCopy;
    }

    public List<Component> getEnergyTooltip() {
        return List.of(Component.literal(
                NumberFormat.getNumberInstance(Locale.US).format(menu.blockEntity.energyContainer.amount)
                + " / "
                + NumberFormat.getNumberInstance(Locale.US).format(menu.blockEntity.energyContainer.getCapacity())
                + " E"));
    }

    private void renderEnergyTooltip(GuiGraphics context, int pMouseX, int pMouseY, int x, int y) {
        if (MouseUtil.isMouseOver(pMouseX, pMouseY, x + 130, y + 44, 13, 16)) {
            context.renderTooltip(Screens.getTextRenderer(this), getEnergyTooltip(),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
        context.blit(TEXTURE, x + 130, y + 44, 176, 0, 13, menu.getScaledEnergyHeight(), 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyTooltip(context, mouseX, mouseY, x, y);
        renderScanResultTooltip(context, mouseX, mouseY, x, y);

        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);

        renderScanResult(context, x, y);
    }

    private void renderScanResult(GuiGraphics context, int x, int y) {
        PatternStorageBlockEntity be = this.menu.blockEntity;
        ResourceLocation scannedId = be.getScannedItemId();

        if (scannedId != null) {
            Item item = BuiltInRegistries.ITEM.getOptional(scannedId).orElse(null);
            if (item != null) {
                ItemStack stack = new ItemStack(item);
                context.renderItem(stack, x + 103, y + 34);
            }

            String uuText = "UU: " + be.getUUCost() + " mB";
            String energyText = "EU: " + NumberFormat.getNumberInstance(Locale.US).format(be.getEnergyCost());

            context.drawString(this.font, uuText, x + 80, y + 54, 0x8888FF, false);
            context.drawString(this.font, energyText, x + 80, y + 64, 0xFF4444, false);
        }
    }

    private void renderScanResultTooltip(GuiGraphics context, int mouseX, int mouseY, int x, int y) {
        PatternStorageBlockEntity be = this.menu.blockEntity;
        ResourceLocation scannedId = be.getScannedItemId();

/*        if (scannedId != null && MouseUtil.isMouseOver(mouseX, mouseY, x + 103, y + 34, 16, 16)) {
            Item item = BuiltInRegistries.ITEM.getOptional(scannedId).orElse(null);
            if (item != null) {
                context.renderTooltip(Screens.getTextRenderer(this),
                        List.of(Component.literal(item.getDescriptionId().toString())),
                        Optional.empty(), mouseX - x, mouseY - y);
            }
        }*/

   /*     if (scannedId != null && MouseUtil.isMouseOver(mouseX, mouseY, x + 80, y + 54, 80, 8)) {
            context.renderTooltip(this.font,
                    List.of(Component.translatable("gui.industrial_elixir.pattern_storage.uu_tooltip")), Optional.empty(), mouseX - x, mouseY - y);
        }

        if (scannedId != null && MouseUtil.isMouseOver(mouseX, mouseY, x + 80, y + 64, 80, 8)) {
            context.renderTooltip(this.font,
                    List.of(Component.translatable("gui.industrial_elixir.pattern_storage.energy_tooltip")), Optional.empty(), mouseX - x, mouseY - y);
        }*/
    }

    @Override
    protected void renderBg(GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressArrow(context, x, y);
        renderEnergyArea(context, x, y);
        renderScanResultArea(context, x, y);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 45, y + 35, 190, 0, menu.getScaledProgress(), 15, 256, 256);
        }
    }

    private void renderScanResultArea(GuiGraphics context, int x, int y) {
        PatternStorageBlockEntity be = this.menu.blockEntity;
        if (be.getScannedItemId() != null) {
            context.blit(TEXTURE, x + 98, y + 29, 176, 28, 26, 26, 256, 256);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
