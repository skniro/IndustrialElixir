package com.skniro.industrial_elixir.api.data.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.item.init.BatteryItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record BatteryLevelProperty() implements SelectItemModelProperty<Integer> {
    public static Identifier ID = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "active");
    public static final Codec<Integer> VALUE_CODEC = Codec.INT;

    public static final SelectItemModelProperty.Type<BatteryLevelProperty, Integer> TYPE =
            SelectItemModelProperty.Type.create(MapCodec.unit(new BatteryLevelProperty()), VALUE_CODEC);


    @Override
    public @Nullable Integer get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        Item item = stack.getItem();

        if (item instanceof BatteryItem battery) {
            float percent = (float) battery.getStoredEnergy(stack)
                    / battery.getEnergyCapacity(stack);

            if (percent <= 0.0F) return 0;
            if (percent < 0.25F) return 1;
            if (percent < 0.50F) return 2;
            if (percent < 0.75F) return 3;
            return 4;
        }

        return 0;
    }

    @Override
    public SelectItemModelProperty.Type<BatteryLevelProperty, Integer> type() {
        return TYPE;
    }

    @Override
    public Codec<Integer> valueCodec() {
        return VALUE_CODEC;
    }
}