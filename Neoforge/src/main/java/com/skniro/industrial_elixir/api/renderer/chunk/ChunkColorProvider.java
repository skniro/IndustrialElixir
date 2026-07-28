package com.skniro.industrial_elixir.api.renderer.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public final class ChunkColorProvider {

    private ChunkColorProvider() {
    }

    /**
     * 获取区块地图颜色
     *
     * @param level 世界
     * @param pos 方块位置
     * @param state 顶层方块状态
     * @return ARGB颜色
     */
    public static int getColor(BlockGetter level, BlockPos pos, BlockState state) {

/*        // 空气
        if (state.isAir()) {
            return 0x00000000;
        }

        // 水
        if (state.is(Blocks.WATER)) {
            return 0xFF3F76E4;
        }

        // 熔岩
        if (state.is(Blocks.LAVA)) {
            return 0xFFFF5500;
        }

        // 冰类
        if (state.is(Blocks.ICE)
                || state.is(Blocks.PACKED_ICE)
                || state.is(Blocks.BLUE_ICE)) {
            return 0xFF8AD8FF;
        }

        // 雪
        if (state.is(Blocks.SNOW)
                || state.is(Blocks.SNOW_BLOCK)) {
            return 0xFFF5F5F5;
        }*/

        /*
         * 使用 Minecraft 原版地图颜色
         */
        MapColor mapColor = state.getMapColor(level, pos);

        if (mapColor == MapColor.NONE) {
            return 0xFF808080;
        }


        /*
         * 模拟原版地图亮度
         *
         * NORMAL:
         * - 不额外变暗
         */
        return 0xFF000000 | mapColor.calculateARGBColor(MapColor.Brightness.NORMAL);
    }


    /**
     * 根据高度增加阴影
     * 让小地图更像 FTB Chunks
     */
    public static int applyHeightShade(int color, int heightDiff) {

        if (color == 0) {
            return color;
        }

        float factor;

        if (heightDiff > 2) {
            factor = 1.15f;
        } else if (heightDiff < -2) {
            factor = 0.85f;
        } else {
            factor = 1.0f;
        }

        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;


        r = Math.min(255, (int) (r * factor));
        g = Math.min(255, (int) (g * factor));
        b = Math.min(255, (int) (b * factor));

        return a << 24
                | r << 16
                | g << 8
                | b;
    }
}