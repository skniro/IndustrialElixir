package com.skniro.industrial_elixir.compat.rei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.rei.display.BlastFurnaceDisplay;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class BlastFurnaceCategory implements DisplayCategory<BlastFurnaceDisplay> {
    public static final Identifier TEXTURE = Helper.id("textures/gui/container/machine/blastfurnace.png");

    private final GuiFluidTankRenderer fluidRenderer = new GuiFluidTankRenderer(1000, true, 16, 50);

    @Override
    public CategoryIdentifier<? extends BlastFurnaceDisplay> getCategoryIdentifier() {
        return BlastFurnaceDisplay.BLAST_FURNACE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.Blast_Furnace);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(new ItemStack(GrowableOresBlocks.BLAST_FURNACE_BLOCK.get()));
    }

    @Override
    public List<Widget> setupDisplay(BlastFurnaceDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82), 0, 0));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 52, startPoint.y + 34))
                .entries(display.getInputEntries().get(0)).markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 101, startPoint.y + 26))
                .entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        if (display.getOutputEntries().size() > 1) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 101, startPoint.y + 44))
                    .entries(display.getOutputEntries().get(1)).disableBackground().markOutput());
        }

        widgets.add(Widgets.createDrawableWidget((context, mouseX, mouseY, delta) -> {
            long tick = System.currentTimeMillis() / 50;
            int progress = (int) ((tick % 72) * 21 / 72);
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, startPoint.x + 73, startPoint.y + 34, 189, 0, progress, 16, 256, 256);

            var storage = getFluid(display);
            fluidRenderer.render(context, startPoint.x + 8, startPoint.y + 5, storage);

            int count = display.getCount();
            if (count > 1) {
                Font font = Minecraft.getInstance().font;
                String text = String.valueOf(count);
                context.text(font, text, startPoint.x + 52 + 16 - font.width(text) - 1, startPoint.y + 34 + 16 - 8 - 1, 0xFFFFFFFF, true);
            }
        }));

        return widgets;
    }

    private SingleVariantStorage<FluidVariant> getFluid(BlastFurnaceDisplay display) {
        Identifier fluidId = display.getFluid();
        int amount = display.getFluidAmount();
        Fluid fluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(Fluids.EMPTY);

        return new SingleVariantStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return 1000;
            }

            {
                this.variant = FluidVariant.of(fluid);
                this.amount = amount;
            }
        };
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
