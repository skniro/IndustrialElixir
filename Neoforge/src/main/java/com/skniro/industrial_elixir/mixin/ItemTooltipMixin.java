package com.skniro.industrial_elixir.mixin;

import com.skniro.industrial_elixir.api.item.EnergyTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemTooltipMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void icr$appendEnergyTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        EnergyTooltipHelper.appendEnergyTooltip(stack, tooltip::add);
    }
}
