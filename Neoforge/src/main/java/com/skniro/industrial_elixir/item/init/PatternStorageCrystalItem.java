package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.block.entity.machine.PatternStorageBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class PatternStorageCrystalItem extends Item {

    public PatternStorageCrystalItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag flag) {
        if (PatternStorageBlockEntity.hasPatternData(stack)) {
            var itemId = PatternStorageBlockEntity.getPatternItem(stack);
            int uuCost = PatternStorageBlockEntity.getPatternUUCost(stack);
            int energyCost = PatternStorageBlockEntity.getPatternEnergyCost(stack);

            if (itemId != null) {
                var item = BuiltInRegistries.ITEM.get(itemId);
                ItemStack stack1 = new ItemStack(item.get());
                if (item != null) {
                    textConsumer.accept(Component.translatable("tooltip.industrial_elixir.pattern_storage.stored_item")
                            .append(": ")
                            .append(Component.translatable(stack1.getHoverName().getString()))
                            .withStyle(ChatFormatting.GOLD));
                }
            }

            textConsumer.accept(Component.translatable("tooltip.industrial_elixir.pattern_storage.energy_cost")
                    .append(": ")
                    .append(NumberFormat.getNumberInstance(Locale.US).format(energyCost))
                    .append(" EU")
                    .withStyle(ChatFormatting.RED));

            textConsumer.accept(Component.translatable("tooltip.industrial_elixir.pattern_storage.uu_cost")
                    .append(": ")
                    .append(String.valueOf(uuCost))
                    .append(" mB")
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}
