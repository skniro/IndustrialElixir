package com.skniro.industrial_elixir.screen.ingame.widgets;

import com.skniro.industrial_elixir.block.entity.machine.fluid.ReplicatorBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ReplicatorModeButton extends Button {
    private static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

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
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            context.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            context.blitSprite(SPRITES.get(this.isActive(), this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
            context.setColor(1.0F, 1.0F, 1.0F, 1.0F);

            // Render icon in the center of the button
            if (!icon.isEmpty()) {
                context.renderItem(icon, getX() + 1, getY() + 1);
            }
        }
    }

    @Override
    public void onPress() {
        super.onPress();
    }
}
