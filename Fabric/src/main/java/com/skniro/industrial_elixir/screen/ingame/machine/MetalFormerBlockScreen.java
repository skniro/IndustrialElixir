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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MetalFormerBlockScreen extends AbstractContainerScreen<MetalFormerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "textures/gui/container/machine/metal_former.png");
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
        return List.of(Component.literal(menu.blockEntity.energyContainer.getSideStorage(null).getAmount()+" / "+ menu.blockEntity.energyContainer.getSideStorage(null).getCapacity()+" E"));
    }

    private void renderEnergyAreaTooltips(GuiGraphicsExtractor context, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 22, 32, 13, 14)) {
            context.setTooltipForNextFrame(Screens.getFont(this), getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyArea(GuiGraphicsExtractor context, int x, int y) {
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 22, y + 32, 176, 0, 13, menu.getScaledEnergyHeight(),256,256);
    }



    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        renderEnergyAreaTooltips(context, mouseX, mouseY, leftPos, topPos);

        context.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
        context.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }

    @Override
       public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight,256,256);
        renderProgressArrow(context, x, y);
        renderEnergyArea(context, x, y);
    }

    private void renderProgressArrow(GuiGraphicsExtractor context, int x, int y) {
        if(menu.isCrafting()) {
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 54, y + 37, 190, 0, menu.getScaledProgress(),15,256,256);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context , int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
         extractTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}

