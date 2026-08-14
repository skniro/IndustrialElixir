package com.skniro.industrial_elixir.screen.ingame.widgets;

import com.skniro.industrial_elixir.block.entity.machine.fluid.ReplicatorBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

public class ReplicatorModeButton extends Button {
    private static final WidgetSprites SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("widget/button"),
            Identifier.withDefaultNamespace("widget/button_disabled"),
            Identifier.withDefaultNamespace("widget/button_highlighted"));

    private final ReplicatorBlockEntity.Mode mode;
    private final boolean active;
    private final ItemStack icon;

    public ReplicatorModeButton(int x, int y, int width, int height, ReplicatorBlockEntity.Mode mode, boolean active, ItemStack icon, Component tooltip, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.mode = mode;
        this.active = active;
        this.icon = icon;
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            context.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.isActive(), this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));

            // Render icon in the center of the button
            if (!icon.isEmpty()) {
                context.item(icon, getX() + 1, getY() + 1);
            }
        }
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        super.onPress(inputWithModifiers);
    }
}
