package com.skniro.industrial_elixir.api.item;

import com.skniro.industrial_elixir.ModContent;
import com.skniro.industrial_elixir.api.block.TieredEnergyBlock;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.init.CableBlock;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class EnergyTooltipHelper {

    private static final String ENERGY_UNIT = "EP";

    public static void appendEnergyTooltip(ItemStack stack, Consumer<Component> tooltip) {
        // For items that store energy (batteries, quantum armor, jetpack)
        if (stack.getItem() instanceof SimpleEnergyItem energyItem) {
            long stored = energyItem.getStoredEnergy(stack);
            long capacity = energyItem.getEnergyCapacity(stack);

            tooltip.accept(Component.translatable("tooltip.industrial_elixir.energy.stored",
                            formatNumber(stored), formatNumber(capacity))
                    .withStyle(ChatFormatting.GOLD));

            if (energyItem instanceof TieredEnergyItem tieredItem) {
                EnergyTier tier = tieredItem.getEnergyTier();
                tooltip.accept(Component.translatable("tooltip.industrial_elixir.energy.tier",
                                tier.name())
                        .withStyle(ChatFormatting.GRAY));

                long maxIn = energyItem.getEnergyMaxInput(stack);
                long maxOut = energyItem.getEnergyMaxOutput(stack);
                if (maxIn > 0 || maxOut > 0) {
                    tooltip.accept(Component.translatable("tooltip.industrial_elixir.energy.max_io",
                                    formatNumber(maxIn), formatNumber(maxOut))
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }

        // For machine block items
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CableBlock cableBlock) {
                ModContent.Cables cable = cableBlock.type;
                tooltip.accept(Component.translatable("tooltip.industrial_elixir.cable.transfer_rate",
                                formatNumber(cable.transferRate))
                        .withStyle(ChatFormatting.GOLD));
                tooltip.accept(Component.translatable("tooltip.industrial_elixir.cable.tier",
                                cable.tier.name())
                        .withStyle(ChatFormatting.GRAY));
                if (cable.canKill) {
                    tooltip.accept(Component.translatable("tooltip.industrial_elixir.cable.uninsulated")
                            .withStyle(ChatFormatting.RED));
                } else {
                    tooltip.accept(Component.translatable("tooltip.industrial_elixir.cable.insulated")
                            .withStyle(ChatFormatting.DARK_GREEN));
                }
            }

            if (blockItem.getBlock() instanceof TieredEnergyBlock energyBlock) {
                EnergyTier tier = energyBlock.getEnergyTier();
                long capacity = energyBlock.getMaxCapacity();

                tooltip.accept(Component.translatable("tooltip.industrial_elixir.energy.tier",
                                tier.name())
                        .withStyle(ChatFormatting.GRAY));
                tooltip.accept(Component.translatable("tooltip.industrial_elixir.energy.capacity",
                                formatNumber(capacity) + " " + ENERGY_UNIT)
                        .withStyle(ChatFormatting.DARK_GRAY));
                tooltip.accept(Component.translatable("tooltip.industrial_elixir.energy.max_io",
                                formatNumber(tier.getMaxInput()), formatNumber(tier.getMaxOutput()))
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    private static String formatNumber(long number) {
        if (number >= 1_000_000_000) {
            double val = number / 1_000_000_000.0;
            if (val == Math.floor(val)) {
                return String.format("%.0fG", val);
            }
            return String.format("%.1fG", val);
        }
        if (number >= 1_000_000) {
            double val = number / 1_000_000.0;
            if (val == Math.floor(val)) {
                return String.format("%.0fM", val);
            }
            return String.format("%.1fM", val);
        }
        if (number >= 1_000) {
            double val = number / 1_000.0;
            if (val == Math.floor(val)) {
                return String.format("%.0fK", val);
            }
            return String.format("%.1fK", val);
        }
        java.text.NumberFormat nf = java.text.NumberFormat.getIntegerInstance();
        return nf.format(number);
    }
}
