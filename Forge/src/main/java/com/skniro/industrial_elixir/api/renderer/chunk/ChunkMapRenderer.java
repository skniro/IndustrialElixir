package com.skniro.industrial_elixir.api.renderer.chunk;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ChunkMapRenderer {

    public static final int GRID_SIZE = 5;

    private static final int CELL_SIZE = 14;
    private static final int CELL_GAP = 2;
    private static final int CELL_STRIDE = CELL_SIZE + CELL_GAP;


    /**
     * 绘制 Chunk 地图
     *
     * @param graphics GUI graphics
     * @param level 当前世界
     * @param center 中心Chunk
     * @param x 左上角
     * @param y 左上角
     * @param selectedMask 已选择chunk
     */
    public void render(
            GuiGraphicsExtractor graphics,
            Level level,
            ChunkPos center,
            int x,
            int y,
            int selectedMask,
            int mouseX,
            int mouseY
    ) {


        for (int i = 0; i < 25; i++) {

            int row = i / GRID_SIZE;
            int col = i % GRID_SIZE;


            int drawX =
                    x + col * CELL_STRIDE;

            int drawY =
                    y + row * CELL_STRIDE;


            ChunkPos chunkPos =
                    new ChunkPos(
                            center.x() + col - 2,
                            center.z() + row - 2
                    );


            /*
             * 绘制Chunk纹理
             */
            Identifier texture =
                    ChunkTextureCache.getTexture(
                            level,
                            chunkPos
                    );


            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    texture,
                    drawX,
                    drawY,
                    0,
                    0,
                    CELL_SIZE,
                    CELL_SIZE,
                    16,
                    16
            );



            boolean centerChunk =
                    i == 12;


            boolean selected =
                    centerChunk
                    ||
                    (selectedMask & (1 << i)) != 0;



            /*
             * 已加载覆盖层
             */
            if (selected) {

                int color =
                        centerChunk
                                ?
                                0x6600FF00
                                :
                                0x5500AA00;


                graphics.fill(
                        drawX,
                        drawY,
                        drawX + CELL_SIZE,
                        drawY + CELL_SIZE,
                        color
                );
            }



            /*
             * 鼠标边框
             */
            boolean hovered =
                    mouseX >= drawX
                    && mouseX < drawX + CELL_SIZE
                    && mouseY >= drawY
                    && mouseY < drawY + CELL_SIZE;


            int border =
                    hovered
                            ?
                            0xFFFFFFFF
                            :
                            0xFF555555;


            drawBorder(
                    graphics,
                    drawX,
                    drawY,
                    border
            );
        }
    }



    private void drawBorder(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            int color
    ) {


        graphics.fill(
                x,
                y,
                x + CELL_SIZE,
                y + 1,
                color
        );


        graphics.fill(
                x,
                y + CELL_SIZE - 1,
                x + CELL_SIZE,
                y + CELL_SIZE,
                color
        );


        graphics.fill(
                x,
                y,
                x + 1,
                y + CELL_SIZE,
                color
        );


        graphics.fill(
                x + CELL_SIZE - 1,
                y,
                x + CELL_SIZE,
                y + CELL_SIZE,
                color
        );
    }



    /**
     * 获取鼠标所在Chunk按钮
     *
     * @return 0-24，没有返回-1
     */
    public int getHoveredChunk(
            int mouseX,
            int mouseY,
            int x,
            int y
    ) {


        for (int i = 0; i < 25; i++) {

            int row = i / GRID_SIZE;
            int col = i % GRID_SIZE;


            int cx =
                    x + col * CELL_STRIDE;

            int cy =
                    y + row * CELL_STRIDE;


            if (mouseX >= cx
                    && mouseX < cx + CELL_SIZE
                    && mouseY >= cy
                    && mouseY < cy + CELL_SIZE) {

                return i;
            }
        }


        return -1;
    }
}