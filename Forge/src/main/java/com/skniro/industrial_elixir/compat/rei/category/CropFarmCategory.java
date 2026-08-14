package com.skniro.industrial_elixir.compat.rei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.rei.display.CropFarmDisplay;
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
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CropFarmCategory implements DisplayCategory<CropFarmDisplay> {
    public static final Identifier TEXTURE = Helper.id("textures/gui/container/machine/crop_farm.png");

    @Override
    public CategoryIdentifier<? extends CropFarmDisplay> getCategoryIdentifier() {
        return CropFarmDisplay.CROP_FARM;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.CropFarm);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(new ItemStack(GrowableOresBlocks.CROP_FARM_Block.get()));
    }

    @Override
    public List<Widget> setupDisplay(CropFarmDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82), 0, 0));

        widgets.add(Widgets.createTexturedWidget(TEXTURE,
                new Rectangle(startPoint.x + 129, startPoint.y + 45, 13, 16),
                176, 0));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 52, startPoint.y + 33))
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
            long tick = System.currentTimeMillis() / 50;
            int progress = (int) ((tick % 72) * 24 / 72);
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, startPoint.x + 70, startPoint.y + 34, 189, 0, progress, 16, 256, 256);

            int count = display.getCount();
            if (count > 1) {
                Font font = Minecraft.getInstance().font;
                String text = String.valueOf(count);
                context.text(font, text, startPoint.x + 52 + 16 - font.width(text) - 1, startPoint.y + 33 + 16 - 8 - 1, 0xFFFFFFFF, true);
            }
        }));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
