package com.skniro.industrial_elixir.screen.ingame.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class StateButtonWidget extends Button {
    private static final BlockEntityRenderDispatcher itemRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    private static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));
    private final Screen screen;
    private net.minecraft.network.chat.Component narrationMessage;
    private Font font;

    public StateButtonWidget(Screen screen, int x, int y, Font font) {
        super(x, y, 20, 20, net.minecraft.network.chat.Component.empty(), (b) -> {}, Button.DEFAULT_NARRATION);
        this.screen = screen;
        this.font = font;
        this.initialize();
    }

    public Font getFont() {
        return this.font;
    }

    protected abstract void initialize();

    protected abstract void nextState();

    protected abstract ItemStack getButtonIcon();

    @Override
    protected final void renderWidget(GuiGraphics context, int mouseX, int mouseY, float partialTicks){
        if (visible) {
            context.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            context.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
            context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            context.renderItem(this.getButtonIcon(),getX() + 2, getY() + 2);
            if (this.isHovered) {
                context.renderTooltip(this.getFont(), narrationMessage,getX() + 14, getY() + 18);
            }
        }
    }

    @Override
    public void onPress() {
        this.nextState();
    }

    @Override
    public MutableComponent createNarrationMessage() {
        return net.minecraft.network.chat.Component.translatable("gui.narrate.button", narrationMessage);
    }

    protected void setNarrationMessage(net.minecraft.network.chat.Component narrationMessage) {
        this.narrationMessage = narrationMessage;
    }
}
