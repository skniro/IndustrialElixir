package com.skniro.industrial_elixir.screen.renderer;

import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense" (FORGE VERSION)
 *  Modified for Fabric by: Kaupenjoe
 */
public class EnergyInfoArea {
    private final Rect2i area;
    private final EnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null,8,64);
    }

    public EnergyInfoArea(int xMin, int yMin, EnergyStorage energy)  {
        this(xMin, yMin, energy,8,64);
    }

    public EnergyInfoArea(int xMin, int yMin, EnergyStorage energy, int width, int height)  {
        area = new Rect2i(xMin, yMin, width, height);
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(energy.getAmount()+" / "+energy.getCapacity()+" E"));
    }

    public void draw(GuiGraphicsExtractor context) {
        final int height = area.getHeight();
        int stored = (int)(height*(energy.getAmount()/(float)energy.getCapacity()));
        context.fillGradient(
                area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                0xffb51500, 0xff600b00
        );
    }
}