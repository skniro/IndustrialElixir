package com.skniro.industrial_elixir.compat.rei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.rei.display.MetalFormerCuttingDisplay;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.LinkedList;
import java.util.List;

public class MetalFormerCuttingCategory implements DisplayCategory<MetalFormerCuttingDisplay> {
    public static final Identifier TEXTURE = Helper.id(
            "textures/gui/container/machine/metal_former.png");

    private static final int ENERGY_X = 22;
    private static final int ENERGY_Y = 32;
    private static final int ENERGY_U = 176;
    private static final int ENERGY_V = 0;
    private static final int ENERGY_WIDTH = 14;
    private static final int ENERGY_HEIGHT = 14;

    @Override
    public CategoryIdentifier<? extends MetalFormerCuttingDisplay> getCategoryIdentifier() {
        return MetalFormerCuttingDisplay.MetalFormerCutting;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.CUTTING);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(GrowableOresBlocks.MetalFormerBlock.asItem().getDefaultInstance());
    }

    @Override
    public List<Widget> setupDisplay(MetalFormerCuttingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82)));

        widgets.add(Widgets.createTexturedWidget(
                TEXTURE,
                new Rectangle(startPoint.x + ENERGY_X, startPoint.y + ENERGY_Y, ENERGY_WIDTH, ENERGY_HEIGHT),
                ENERGY_U, ENERGY_V
        ));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 21, startPoint.y + 13))
                .entries(display.getInputEntries().get(0)).markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 118, startPoint.y + 34)).entries(
                display.getOutputEntries().get(0)).disableBackground().markOutput());

        widgets.add(Widgets.createDrawableWidget((context, mouseX, mouseY, delta) -> {
            long ticks = System.currentTimeMillis() / 50;
            int totalTicks = 72;
            int arrowWidth = 48;
            int currentTick = (int)(ticks % totalTicks);
            int progress = currentTick * arrowWidth / totalTicks;
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, startPoint.x + 54, startPoint.y + 37, 190, 0, progress, 12, 256, 256);
        }));

        widgets.add(Widgets.createDrawableWidget((context, mouseX, mouseY, delta) -> {
            int count = display.getCount();

            if (count > 1) {
                var textRenderer = Minecraft.getInstance().font;
                String text = String.valueOf(count);
                int x = startPoint.x + 52 + 16 - textRenderer.width(text) - 1;
                int y = startPoint.y + 13 + 16 - 8 - 1;
                context.drawString(textRenderer, text, x, y, -1, true);
            }
        }));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}