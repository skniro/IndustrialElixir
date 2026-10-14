package com.skniro.industrial_elixir.screen.ingame.machine;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.entity.machine.MetalFormerBlockEntity;
import com.skniro.industrial_elixir.networking.packet.MetalFormerStateC2SPayload;
import com.skniro.industrial_elixir.screen.ingame.widgets.StateButtonWidget;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import com.skniro.industrial_elixir.screen.handler.machine.MetalFormerScreenHandler;
import com.skniro.industrial_elixir.util.MouseUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MetalFormerBlockScreen extends AbstractContainerScreen<MetalFormerScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/metal_former.png");
    MetalFormerBlockEntity.MetalFormerState state = menu.blockEntity.getState();

    public MetalFormerBlockScreen(MetalFormerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;

        this.addRenderableWidget(new StateButtonWidget(this, leftPos + 71, topPos + 53, font) {
            ItemStack buttonIcon;


            @Override
            protected void initialize() {
                this.setButtonMessage();
                this.setButtonIcon();
            }

            private void setButtonMessage() {
                String translationKey = switch (state) {
                    case ROLLING -> "gui.metal_former.mode.rolling";
                    case CUTTING -> "gui.metal_former.mode.cutting";
                    case EXTRUDING -> "gui.metal_former.mode.extruding";
                    default -> "gui.metal_former.mode.error";
                };

                this.setNarrationMessage(Component.translatable(translationKey));
            }
            private void setButtonIcon() {
                switch (state) {
                    case ROLLING -> this.buttonIcon = new ItemStack(MapleArmorItems.ROLLING);
                    case CUTTING -> this.buttonIcon = new ItemStack(MapleArmorItems.CUTTING);
                    case EXTRUDING -> this.buttonIcon = new ItemStack(GrowableOresBlocks.COPPER_CABLE);
                    default -> this.buttonIcon = new ItemStack(Items.FURNACE);
                }
            }

            @Override
            protected void nextState() {
                MetalFormerBlockScreen.this.state = MetalFormerBlockEntity.MetalFormerState.values()[(state.ordinal() + 1) % MetalFormerBlockEntity.MetalFormerState.values().length];
                ClientPlayNetworking.send(new MetalFormerStateC2SPayload(menu.blockEntity.getBlockPos(), state.ordinal()));
                this.initialize();
            }

            @Override
            protected ItemStack getButtonIcon() {
                return buttonIcon;
            }
        });
    }

    public List<Component> getTooltips() {
               return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" EP"));
    }

    private void renderEnergyAreaTooltips(GuiGraphics context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 21, 31, 13, 16)) {
            context.renderTooltip(Screens.getTextRenderer(this), getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);
        }
    }

    private void renderEnergyArea(GuiGraphics context, int x, int y) {
            context.blit(TEXTURE, x + 21, y + 31, 176, 0, 13, menu.getScaledEnergyHeight(),256,256);
    }



    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);

        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }

    @Override
       protected void renderBg(GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        renderProgressArrow(context, x, y);
        renderEnergyArea(context, x, y);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if(menu.isCrafting()) {
            context.blit(TEXTURE, x + 70, y + 34, 189, 0, menu.getScaledProgress(),15,256,256);
        }
    }

    @Override
    public void render(GuiGraphics context , int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
         renderTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}

