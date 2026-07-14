package com.skniro.industrial_elixir.item.init;

import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class ItemUpgradeModule extends Item {

    public final UpgradeType type;

    public ItemUpgradeModule(Properties settings, UpgradeType type) {
        super(settings);
        this.type = type;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {

        switch (this.type) {

            case OVERCLOCKER -> {
                textConsumer.accept(Component.literal("Speed: -30%")
                        .withStyle(ChatFormatting.GRAY));
                textConsumer.accept(Component.literal("Power: +60%")
                        .withStyle(ChatFormatting.GRAY));
            }

            case TRANSFORMER -> {
                textConsumer.accept(Component.literal("Energy Tier +1 per item")
                        .withStyle(ChatFormatting.GRAY));
            }

            case ENERGY_STORAGE -> {
                textConsumer.accept(Component.literal("Extra Storage: +10000 EU")
                        .withStyle(ChatFormatting.GRAY));
            }

            case REDSTONE_INVERTER -> {
                textConsumer.accept(Component.literal("Inverts Redstone Signal")
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }


    /**
     *  Transformer 升级数量
     */
    public int getTierIncrease(ItemStack stack) {
        if (this.type == UpgradeType.TRANSFORMER) {
            return stack.getCount(); // 支持叠加
        }
        return 0;
    }

    /**
     *  处理速度倍率
     */
    public double getProcessTimeMultiplier(ItemStack stack) {
        if (this.type == UpgradeType.OVERCLOCKER) {
            return 1.3 * stack.getCount();
        }
        return 1.0;
    }

    /**
     *  能耗倍率
     */
    public double getEnergyDemandMultiplier(ItemStack stack) {
        if (this.type == UpgradeType.OVERCLOCKER) {
            return 1.6 * stack.getCount();
        }
        return 1.0;
    }

    /**
     *  额外储能
     */
    public int getExtraEnergyStorage(ItemStack stack) {
        if (this.type == UpgradeType.ENERGY_STORAGE) {
            return 10000 * stack.getCount();
        }
        return 0;
    }

    public boolean modifiesRedstoneInput(ItemStack stack) {
        return this.type == UpgradeType.REDSTONE_INVERTER;
    }

    public int getRedstoneInput(ItemStack stack, int externalInput) {
        return 15 - externalInput;
    }



    public boolean isSuitableFor(ItemStack stack, Set<UpgradableProperty> properties) {

        return switch (this.type) {

            case OVERCLOCKER ->
                    properties.contains(UpgradableProperty.Processing);

            case TRANSFORMER ->
                    properties.contains(UpgradableProperty.Transformer);

            case ENERGY_STORAGE ->
                    properties.contains(UpgradableProperty.EnergyStorage);

            case REDSTONE_INVERTER ->
                    properties.contains(UpgradableProperty.RedstoneSensitive);

        };
    }


    public enum UpgradeType {
        OVERCLOCKER,
        TRANSFORMER,
        ENERGY_STORAGE,
        REDSTONE_INVERTER
    }
}