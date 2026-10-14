package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.screen.handler.machine.VendorMachineScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class VendorMachineScreen extends AbstractContainerScreen<VendorMachineScreenHandler> {
    private static final ResourceLocation OUT_OF_STOCK_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/out_of_stock");
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_CURRENT_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/experience_bar_current");
    private static final ResourceLocation EXPERIENCE_BAR_RESULT_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/experience_bar_result");
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/scroller_disabled");
    private static final ResourceLocation TRADE_ARROW_OUT_OF_STOCK_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/trade_arrow_out_of_stock");
    private static final ResourceLocation TRADE_ARROW_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/trade_arrow");
    private static final ResourceLocation DISCOUNT_STRIKETHRUOGH_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/discount_strikethrough");
    private static final ResourceLocation VILLAGER_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/villager.png");
    private static final Component TRADES_LABEL = Component.translatable("merchant.trades");
    private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");
    private int shopItem;
    private final TradeOfferButton[] tradeOfferButtons = new TradeOfferButton[7];
    private int scrollOff;
    private boolean isDragging;

    public VendorMachineScreen(final VendorMachineScreenHandler menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title);
        this.imageWidth = 276;
        this.imageHeight = 166;
        this.inventoryLabelX = 107;
    }

    private void postButtonClick() {
        (this.menu).setSelectionHint(this.shopItem);
        (this.menu).tryMoveItems(this.shopItem);
        this.minecraft.getConnection().send(
                new ServerboundSelectTradePacket(this.shopItem)
        );
    }

    protected void init() {
        super.init();
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        int buttonY = yo + 16 + 2;

        for(int i = 0; i < 7; ++i) {
            this.tradeOfferButtons[i] = (TradeOfferButton)this.addRenderableWidget(new TradeOfferButton(xo + 5, buttonY, i, (button) -> {
                if (button instanceof TradeOfferButton) {
                    this.shopItem = ((TradeOfferButton)button).getIndex() + this.scrollOff;
                    this.postButtonClick();
                }

            }));
            buttonY += 20;
        }

    }

    protected void renderLabels(final GuiGraphics graphics, final int xm, final int ym) {
        int traderLevel = (this.menu).getTraderLevel();
        if (traderLevel > 0 && traderLevel <= 5 && (this.menu).showProgressBar()) {
            Component titleAndLevel = Component.translatable("merchant.title", new Object[]{this.title, Component.translatable("merchant.level." + traderLevel)});
            int totalWidth = this.font.width(titleAndLevel);
            int startX = 49 + this.imageWidth / 2 - totalWidth / 2;
            graphics.drawString(this.font, titleAndLevel, startX, 6, -12566464, false);
        } else {
            graphics.drawString(this.font, this.title, 49 + this.imageWidth / 2 - this.font.width(this.title) / 2, 6, -12566464, false);
        }

        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
        int textWidth = this.font.width(TRADES_LABEL);
        graphics.drawString(this.font, TRADES_LABEL, 5 - textWidth / 2 + 48, 6, -12566464, false);
    }

    protected void renderBg(GuiGraphics graphics, float a, int mouseX, int mouseY) {

        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(VILLAGER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
        MerchantOffers offers = (this.menu).getOffers();
        if (!offers.isEmpty()) {
            int itemIndex = this.shopItem;
            if (itemIndex < 0 || itemIndex >= offers.size()) {
                return;
            }

            MerchantOffer offer = (MerchantOffer)offers.get(itemIndex);
            if (offer.isOutOfStock()) {
                graphics.blitSprite(OUT_OF_STOCK_SPRITE, this.leftPos + 83 + 99, this.topPos + 35, 28, 21);
            }
        }

    }

    private void extractProgressBar(final GuiGraphics graphics, final int xo, final int yo, final MerchantOffer offer) {
        int traderLevel = (this.menu).getTraderLevel();
        int traderXp = (this.menu).getTraderXp();
        if (traderLevel < 5) {
            graphics.blitSprite(EXPERIENCE_BAR_BACKGROUND_SPRITE, xo + 136, yo + 16, 102, 5);
            int minXp = VillagerData.getMinXpPerLevel(traderLevel);
            if (traderXp >= minXp && VillagerData.canLevelUp(traderLevel)) {
                int progressLength = 102;
                float multiplier = 102.0F / (float)(VillagerData.getMaxXpPerLevel(traderLevel) - minXp);
                int w = Math.min(Mth.floor(multiplier * (float)(traderXp - minXp)), 102);
                graphics.blitSprite(EXPERIENCE_BAR_CURRENT_SPRITE, 102, 5, 0, 0, xo + 136, yo + 16, w, 5);
                int futureXp = (this.menu).getFutureTraderXp();
                if (futureXp > 0) {
                    int futureXpWidth = Math.min(Mth.floor((float)futureXp * multiplier), 102 - w);
                    graphics.blitSprite(EXPERIENCE_BAR_RESULT_SPRITE, 102, 5, w, 0, xo + 136 + w, yo + 16, futureXpWidth, 5);
                }

            }
        }
    }

    private void extractScroller(final GuiGraphics graphics, final int xo, final int yo, final int mouseX, final int mouseY, final MerchantOffers offers) {
        int steps = offers.size() + 1 - 7;
        if (steps > 1) {
            int leftOver = 139 - (27 + (steps - 1) * 139 / steps);
            int stepHeight = 1 + leftOver / steps + 139 / steps;
            int maxScrollerOff = 113;
            int scrollerYOff = Math.min(113, this.scrollOff * stepHeight);
            if (this.scrollOff == steps - 1) {
                scrollerYOff = 113;
            }

            int scrollerX = xo + 94;
            int scrollerY = yo + 18 + scrollerYOff;
            graphics.blitSprite(SCROLLER_SPRITE, scrollerX, scrollerY, 6, 27);
        } else {
            graphics.blitSprite(SCROLLER_DISABLED_SPRITE, xo + 94, yo + 18, 6, 27);
        }

    }

    public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, final float a) {
        super.render(graphics, mouseX, mouseY, a);
        MerchantOffers offers = this.menu.getOffers();
/*        IndustrialElixir.LOGGER.info(
                "CLIENT MERCHANT OFFERS = "
                        + offers.size()
        );*/
        if (!offers.isEmpty()) {
            int xo = (this.width - this.imageWidth) / 2;
            int yo = (this.height - this.imageHeight) / 2;
            int offerY = yo + 16 + 1;
            int sellItem1X = xo + 5 + 5;
            this.extractScroller(graphics, xo, yo, mouseX, mouseY, offers);
            int currentOfferIndex = 0;
            for(MerchantOffer offer : offers) {
                if (!this.canScroll(offers.size()) || currentOfferIndex >= this.scrollOff && currentOfferIndex < 7 + this.scrollOff) {
                    ItemStack baseCostA = offer.getBaseCostA();
                    ItemStack costA = offer.getCostA();
                    ItemStack costB = offer.getCostB();
                    ItemStack result = offer.getResult();
                    int decorHeight = offerY + 2;
                    this.extractAndDecorateCostA(graphics, costA, baseCostA, sellItem1X, decorHeight);
                    if (!costB.isEmpty()) {
                        graphics.renderFakeItem(costB, xo + 5 + 35, decorHeight);
                        graphics.renderItemDecorations(this.font, costB, xo + 5 + 35, decorHeight);
                    }

                    this.extractButtonArrows(graphics, offer, xo, decorHeight);
                    graphics.renderFakeItem(result, xo + 5 + 68, decorHeight);
                    graphics.renderItemDecorations(this.font, result, xo + 5 + 68, decorHeight);
                    offerY += 20;
                    ++currentOfferIndex;
                } else {
                    ++currentOfferIndex;
                }
            }

            int itemIndex = this.shopItem;
            MerchantOffer selectedOffer = (MerchantOffer)offers.get(itemIndex);
            if ((this.menu).showProgressBar()) {
                this.extractProgressBar(graphics, xo, yo, selectedOffer);
            }

            if (selectedOffer.isOutOfStock() && this.isHovering(186, 35, 22, 21, (double)mouseX, (double)mouseY) && (this.menu).canRestock()) {
                graphics.renderTooltip(this.font, DEPRECATED_TOOLTIP, mouseX, mouseY);
            }

            for(TradeOfferButton button : this.tradeOfferButtons) {
                if (button.isHoveredOrFocused()) {
                    button.extractToolTip(graphics, mouseX, mouseY);
                }

                button.visible = button.index < (this.menu).getOffers().size();
            }
        }

    }

    private void extractButtonArrows(final GuiGraphics graphics, final MerchantOffer offer, final int xo, final int decorHeight) {
        if (offer.isOutOfStock()) {
            graphics.blitSprite(TRADE_ARROW_OUT_OF_STOCK_SPRITE, xo + 5 + 35 + 20, decorHeight + 3, 10, 9);
        } else {
            graphics.blitSprite(TRADE_ARROW_SPRITE, xo + 5 + 35 + 20, decorHeight + 3, 10, 9);
        }

    }

    private void extractAndDecorateCostA(final GuiGraphics graphics, final ItemStack costA, final ItemStack baseCostA, final int sellItem1X, final int decorHeight) {
        graphics.renderFakeItem(costA, sellItem1X, decorHeight);
        if (baseCostA.getCount() == costA.getCount()) {
            graphics.renderItemDecorations(this.font, costA, sellItem1X, decorHeight);
        } else {
            graphics.renderItemDecorations(this.font, baseCostA, sellItem1X, decorHeight, baseCostA.getCount() == 1 ? "1" : null);
            graphics.renderItemDecorations(this.font, costA, sellItem1X + 14, decorHeight, costA.getCount() == 1 ? "1" : null);
            graphics.blitSprite(DISCOUNT_STRIKETHRUOGH_SPRITE, sellItem1X + 7, decorHeight + 12, 9, 2);
        }

    }

    private boolean canScroll(final int numberOfOffers) {
        return numberOfOffers > 7;
    }

    public boolean mouseScrolled(final double x, final double y, final double scrollX, final double scrollY) {
        if (super.mouseScrolled(x, y, scrollX, scrollY)) {
            return true;
        } else {
            int numberOfOffers = (this.menu).getOffers().size();
            if (this.canScroll(numberOfOffers)) {
                int maxScrollOff = numberOfOffers - 7;
                this.scrollOff = Mth.clamp((int)((double)this.scrollOff - scrollY), 0, maxScrollOff);
            }

            return true;
        }
    }

    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double dx, final double dy) {
        int numberOfOffers = (this.menu).getOffers().size();
        if (this.isDragging) {
            int fullScrollTopPos = this.topPos + 18;
            int fullScrollBottomPos = fullScrollTopPos + 139;
            int maxScrollOff = numberOfOffers - 7;
            float scrolling = ((float)mouseY - (float)fullScrollTopPos - 13.5F) / ((float)(fullScrollBottomPos - fullScrollTopPos) - 27.0F);
            scrolling = scrolling * (float)maxScrollOff + 0.5F;
            this.scrollOff = Mth.clamp((int)scrolling, 0, maxScrollOff);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dx, dy);
        }
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        if (this.canScroll((this.menu).getOffers().size()) && mouseX > (double)(xo + 94) && mouseX < (double)(xo + 94 + 6) && mouseY > (double)(yo + 18) && mouseY <= (double)(yo + 18 + 139 + 1)) {
            this.isDragging = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private class TradeOfferButton extends Button {
        final int index;

        public TradeOfferButton(final int x, final int y, final int index, final Button.OnPress onPress) {
            super(x, y, 88, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
            this.index = index;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void extractToolTip(final GuiGraphics graphics, final int xm, final int ym) {
            if (this.isHovered && (VendorMachineScreen.this.menu).getOffers().size() > this.index + VendorMachineScreen.this.scrollOff) {
                if (xm < this.getX() + 20) {
                    ItemStack item = ((MerchantOffer)(VendorMachineScreen.this.menu).getOffers().get(this.index + VendorMachineScreen.this.scrollOff)).getCostA();
                    graphics.renderTooltip(VendorMachineScreen.this.font, item, xm, ym);
                } else if (xm < this.getX() + 50 && xm > this.getX() + 30) {
                    ItemStack item = ((MerchantOffer)(VendorMachineScreen.this.menu).getOffers().get(this.index + VendorMachineScreen.this.scrollOff)).getCostB();
                    if (!item.isEmpty()) {
                        graphics.renderTooltip(VendorMachineScreen.this.font, item, xm, ym);
                    }
                } else if (xm > this.getX() + 65) {
                    ItemStack item = ((MerchantOffer)(VendorMachineScreen.this.menu).getOffers().get(this.index + VendorMachineScreen.this.scrollOff)).getResult();
                    graphics.renderTooltip(VendorMachineScreen.this.font, item, xm, ym);
                }
            }

        }
    }
}
