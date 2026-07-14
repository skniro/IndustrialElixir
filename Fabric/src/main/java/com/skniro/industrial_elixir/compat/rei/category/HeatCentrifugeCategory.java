package com.skniro.industrial_elixir.compat.rei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.rei.display.HeatCentrifugeDisplay;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.LinkedList;
import java.util.List;

public class HeatCentrifugeCategory implements DisplayCategory<HeatCentrifugeDisplay> {
    public static final Identifier TEXTURE = Helper.id("textures/gui/container/machine/heatcentrifuge.png");

    @Override
    public CategoryIdentifier<? extends HeatCentrifugeDisplay> getCategoryIdentifier() {
        return HeatCentrifugeDisplay.HEAT_CENTRIFUGE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.Heat_Centrifuge);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(GrowableOresBlocks.HEAT_CENTRIFUGE.asItem().getDefaultInstance());
    }

    @Override
    public List<Widget> setupDisplay(HeatCentrifugeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82)));

        widgets.add(Widgets.createTexturedWidget(TEXTURE,
                new Rectangle(startPoint.x + 129, startPoint.y + 45, 13, 14),
                176, 0));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 52, startPoint.y + 34))
                .entries(display.getInputEntries().get(0)).markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 104, startPoint.y + 16))
                .entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        if (display.getOutputEntries().size() > 1) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 104, startPoint.y + 34))
                    .entries(display.getOutputEntries().get(1)).disableBackground().markOutput());
        }

        if (display.getOutputEntries().size() > 2) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 104, startPoint.y + 52))
                    .entries(display.getOutputEntries().get(2)).disableBackground().markOutput());
        }

        widgets.add(Widgets.createDrawableWidget((context, mouseX, mouseY, delta) -> {
            long ticks = System.currentTimeMillis() / 50;
            int totalTicks = 72;
            int arrowWidth = 21;
            int currentTick = (int) (ticks % totalTicks);
            int progress = currentTick * arrowWidth / totalTicks;
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, startPoint.x + 79, startPoint.y + 32, 189, 0, progress, 16, 256, 256);

            int count = display.getCount();
            if (count > 1) {
                Font font = Minecraft.getInstance().font;
                String text = String.valueOf(count);
                context.text(font, text, startPoint.x + 52 + 16 - font.width(text) - 1, startPoint.y + 34 + 16 - 8 - 1, 0xFFFFFFFF, true);
            }
        }));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
