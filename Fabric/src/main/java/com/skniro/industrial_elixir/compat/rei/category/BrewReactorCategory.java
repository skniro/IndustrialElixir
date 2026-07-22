package com.skniro.industrial_elixir.compat.rei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.compat.rei.display.BrewReactorDisplay;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BrewReactorCategory implements DisplayCategory<BrewReactorDisplay> {
    public static final Identifier TEXTURE = Helper.id(
            "textures/gui/container/machine/brewreactor.png");

    private final GuiFluidTankRenderer fluidRenderer =
            new GuiFluidTankRenderer(
                    1000,
                    true,
                    16,
                    50
            );

    @Override
    public CategoryIdentifier<? extends BrewReactorDisplay>
    getCategoryIdentifier() {
        return BrewReactorDisplay.BREW_REACTOR;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.BrewReactor);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(new ItemStack(GrowableOresBlocks.Brew_Reactor_BLOCK));
    }

    @Override
    public List<Widget> setupDisplay(BrewReactorDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82), 0, 0));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 52, startPoint.y + 13))
                .entries(display.getInputEntries().get(0)));

        widgets.add(Widgets.createTexturedWidget(TEXTURE,
                new Rectangle(startPoint.x + 129, startPoint.y + 45, 13, 16),
                176, 0));

        if (display.getInputEntries().size() > 1) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 52, startPoint.y + 49))
                .entries(display.getInputEntries().get(1)));
        }

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 104, startPoint.y + 34))
                .entries(display.getOutputEntries().get(0)).disableBackground().markOutput()
        );


        widgets.add(Widgets.createDrawableWidget((context, mouseX, mouseY, delta) -> {
            int count = display.getCount();
            if (count > 1) {
                Font font = Minecraft.getInstance().font;
                String text = String.valueOf(count);
                context.text(font, text, startPoint.x + 52 + 16 - font.width(text) - 1, startPoint.y + 20, 0xFFFFFFFF, true);
            }
        }));
        widgets.add(Widgets.createDrawableWidget((context, mouseX, mouseY, delta) -> {
        long tick = System.currentTimeMillis() / 50;
        int progress = (int) ((tick % 72) * 11 / 72);
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, startPoint.x + 79, startPoint.y + 32, 189, 0, progress, 16, 256, 256);
        var storage = getFluid(display);
        fluidRenderer.render(context, startPoint.x + 8, startPoint.y + 5, storage);
        }));
        return widgets;
    }

    private SingleVariantStorage<FluidVariant> getFluid(BrewReactorDisplay display) {
        Optional<Identifier> fid = display.getFluid();
        Optional<Integer> famt = display.getFluidAmount();

        if (fid.isPresent()) {

            Fluid fluid = BuiltInRegistries.FLUID.getOptional(fid.get()).orElse(Fluids.EMPTY);
            int amount = famt.orElse(1000);

            return new SingleVariantStorage<>() {
                @Override
                protected FluidVariant
                getBlankVariant() {
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

        return new SingleVariantStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return 1000;
            }
        };
    }
}