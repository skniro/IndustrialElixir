package com.skniro.industrial_elixir.energy.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
@ApiStatus.Internal
public class EnergyImpl {
	public static final DataComponentType<Long> ENERGY_COMPONENT = DataComponentType.<Long>builder()
			.persistent(nonNegativeLong())
			.networkSynchronized(ByteBufCodecs.VAR_LONG)
			.build();

	public static void init() {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "energy"), ENERGY_COMPONENT);
		EnergyStorage.ITEM.registerFallback((stack, ctx) -> {
			if (stack.getItem() instanceof SimpleEnergyItem energyItem) {
				return SimpleEnergyItem.createStorage(ctx, energyItem.getEnergyCapacity(stack), energyItem.getEnergyMaxInput(stack), energyItem.getEnergyMaxOutput(stack));
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

			return DataResult.error(() -> "Energy value must be non-negative: " + value);
		});
	}
}
