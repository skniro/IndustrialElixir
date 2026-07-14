package com.skniro.industrial_elixir.energy.heat.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
@ApiStatus.Internal
public class HeatImpl {
	public static final DataComponentType<Long> Heat_COMPONENT = DataComponentType.<Long>builder()
			.persistent(nonNegativeLong())
			.networkSynchronized(ByteBufCodecs.VAR_LONG)
			.build();

	public static void init() {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "heat"), Heat_COMPONENT);
		HeatStorage.ITEM.registerFallback((stack, ctx) -> {
			if (stack.getItem() instanceof SimpleHeatItem HeatItem) {
				return SimpleHeatItem.createStorage(ctx, HeatItem.getHeatCapacity(stack), HeatItem.getHeatMaxInput(stack), HeatItem.getHeatMaxOutput(stack));
			} else {
				return null;
			}
		});
	}

	private static Codec<Long> nonNegativeLong() {
		return Codec.LONG.validate((Long value) -> {
			if (value >= 0) {
				return DataResult.success(value);
			}

			return DataResult.error(() -> "heat value must be non-negative: " + value);
		});
	}
}
