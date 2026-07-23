package com.skniro.industrial_elixir.mixin;

import com.skniro.industrial_elixir.api.item.EnergyTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemTooltipMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void icr$appendEnergyTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag flag, CallbackInfo ci) {
        EnergyTooltipHelper.appendEnergyTooltip(stack, consumer);
    }
}
