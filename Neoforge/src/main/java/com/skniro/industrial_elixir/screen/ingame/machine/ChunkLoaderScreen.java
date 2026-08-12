package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.renderer.chunk.ChunkMapRenderer;
import com.skniro.industrial_elixir.screen.handler.machine.ChunkLoaderScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.ChunkPos;

import java.util.List;
import java.util.Optional;


public class ChunkLoaderScreen extends AbstractContainerScreen<ChunkLoaderScreenHandler> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/chunk_loader.png");
    private final ChunkMapRenderer chunkMapRenderer = new ChunkMapRenderer();
    private static final int GRID_X = 48;
    private static final int GRID_Y = 16;
    private static final int CELL_SIZE = 14;
    private static final int CELL_GAP = 2;
    private static final int CELL_STRIDE = CELL_SIZE + CELL_GAP;

    private static final int ENERGY_BAR_X = 8;
    private static final int ENERGY_BAR_Y = 12;
    private static final int ENERGY_BAR_W = 10;
    private static final int ENERGY_BAR_H = 50;

    public ChunkLoaderScreen(ChunkLoaderScreenHandler menu, Inventory inventory, Component title) {
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

        // Status: Work / Not Work
        int loadedCount = Integer.bitCount(menu.blockEntity.getSelectedChunks()) + 1;
        long energy = menu.blockEntity.energyContainer.getSideStorage(null).getAmount();
        boolean canWork = energy >= loadedCount;
        int statusColor = canWork ? 0xFF00CC00 : 0xFFCC0000;
        Component statusText = Component.literal(canWork ? "Work" : "Not Work");
        graphics.text(font, statusText, 128, 64, statusColor, true);

        // Energy tooltip
        if (MouseUtil.isMouseOver(mouseX, mouseY, x + ENERGY_BAR_X, y + ENERGY_BAR_Y, ENERGY_BAR_W, ENERGY_BAR_H)) {
            graphics.setTooltipForNextFrame(this.getFont(),
                    List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()
                            + " / " + menu.blockEntity.energyContainer.getSideStorage(null).getCapacity() + " E")),
                    Optional.empty(), mouseX - x, mouseY - y);
        }

        // Chunk grid tooltip
        int mask = menu.blockEntity.getSelectedChunks();
        for (int i = 0; i < 25; i++) {
            int row = i / 5;
            int col = i % 5;
            int cx = x + GRID_X + col * CELL_STRIDE;
            int cy = y + GRID_Y + row * CELL_STRIDE;

            if (MouseUtil.isMouseOver(mouseX, mouseY, cx, cy, CELL_SIZE, CELL_SIZE)) {
                int chunkOffX = col - 2;
                int chunkOffZ = row - 2;
                boolean loaded = (mask & (1 << i)) != 0 || i == 12;
                String status = loaded ? "\u00a7aForce Loaded" : "\u00a77Not Loaded";
                if (i == 12) status = "\u00a7aAlways Loaded";
                graphics.setTooltipForNextFrame(this.getFont(),
                        List.of(Component.literal("Chunk (" + chunkOffX + ", " + chunkOffZ + ") " + status)),
                        Optional.empty(), mouseX - x, mouseY - y);
                break;
            }
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Background texture
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Energy bar
        int energyHeight = menu.getScaledEnergyHeight();
        if (energyHeight > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    x + ENERGY_BAR_X, y + ENERGY_BAR_Y + (ENERGY_BAR_H - energyHeight),
                    176, ENERGY_BAR_H - energyHeight, ENERGY_BAR_W, energyHeight, 256, 256);
        }

        // 5x5 chunk map grid
        //renderChunkGrid(graphics, x, y, mouseX, mouseY);
        BlockPos machinePos = menu.blockEntity.getBlockPos();
        ChunkPos machineChunk = new ChunkPos(machinePos.getX(), machinePos.getZ());

        chunkMapRenderer.render(
                graphics,
                minecraft.level,
                minecraft.player.chunkPosition(),
                x + GRID_X,
                y + GRID_Y,
                menu.blockEntity.getSelectedChunks(),
                mouseX,
                mouseY
        );
    }

    private void renderChunkGrid(GuiGraphicsExtractor graphics, int guiX, int guiY, int mouseX, int mouseY) {
        int mask = menu.blockEntity.getSelectedChunks();

        for (int i = 0; i < 25; i++) {
            int row = i / 5;
            int col = i % 5;
            int cx = guiX + GRID_X + col * CELL_STRIDE;
            int cy = guiY + GRID_Y + row * CELL_STRIDE;

            boolean selected = (mask & (1 << i)) != 0 || i == 12;
            boolean isCenter = i == 12;
            boolean hovered = MouseUtil.isMouseOver(mouseX, mouseY, cx, cy, CELL_SIZE, CELL_SIZE);

            if (isCenter) {
                graphics.fill(cx, cy, cx + CELL_SIZE, cy + CELL_SIZE, 0xFF226622);
            } else if (selected) {
                graphics.fill(cx, cy, cx + CELL_SIZE, cy + CELL_SIZE, 0x8800CC00);
            } else {
                graphics.fill(cx, cy, cx + CELL_SIZE, cy + CELL_SIZE, 0x88333333);
            }

            // Border
            int borderColor = hovered ? 0xFFFFFFFF : 0xFF555555;
            graphics.fill(cx, cy, cx + CELL_SIZE, cy + 1, borderColor);           // top
            graphics.fill(cx, cy + CELL_SIZE - 1, cx + CELL_SIZE, cy + CELL_SIZE, borderColor); // bottom
            graphics.fill(cx, cy, cx + 1, cy + CELL_SIZE, borderColor);           // left
            graphics.fill(cx + CELL_SIZE - 1, cy, cx + CELL_SIZE, cy + CELL_SIZE, borderColor); // right
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }

        double mouseX = event.x();
        double mouseY = event.y();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        for (int i = 0; i < 25; i++) {
            if (i == 12) continue;

            int row = i / 5;
            int col = i % 5;

            int cx = x + GRID_X + col * CELL_STRIDE;
            int cy = y + GRID_Y + row * CELL_STRIDE;

            if (mouseX >= cx && mouseX < cx + CELL_SIZE
                    && mouseY >= cy && mouseY < cy + CELL_SIZE) {

                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, i);
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }
}
