package com.skniro.industrial_elixir.block.entity.cable;

import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
public class StringUtils {
    public static String toFirstCapital(String input) {
        if (input != null && input.length() != 0) {
            String var10000 = input.substring(0, 1).toUpperCase();
            return var10000 + input.substring(1);
        } else {
            return input;
        }
    }

    public static String toFirstCapitalAllLowercase(String input) {
        if (input != null && input.length() != 0) {
            String output = input.toLowerCase(Locale.ROOT);
            String var10000 = output.substring(0, 1).toUpperCase();
            return var10000 + output.substring(1);
        } else {
            return input;
        }
    }

    public static ChatFormatting getPercentageColour(int percentage) {
        if (percentage <= 10) {
            return ChatFormatting.RED;
        } else {
            return percentage >= 75 ? ChatFormatting.GREEN : ChatFormatting.YELLOW;
        }
    }

    public static MutableComponent getPercentageText(int percentage) {
        return Component.literal(String.valueOf(percentage)).withStyle(getPercentageColour(percentage)).append("%");
    }
}