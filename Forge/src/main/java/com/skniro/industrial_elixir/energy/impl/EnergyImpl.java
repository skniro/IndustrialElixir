package com.skniro.industrial_elixir.energy.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
@ApiStatus.Internal
public class EnergyImpl {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
			DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, IndustrialElixir.MOD_ID);

	public static final DataComponentType<Long> ENERGY_COMPONENT = DataComponentType.<Long>builder()
			.persistent(nonNegativeLong())
			.networkSynchronized(ByteBufCodecs.VAR_LONG)
			.build();

	static {
		DATA_COMPONENTS.register("energy", () -> ENERGY_COMPONENT);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		List<Item> items = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof SimpleEnergyItem).toList();
		event.registerItem(EnergyStorage.ITEM, (stack, ctx) -> {
					if (stack.getItem() instanceof SimpleEnergyItem energyItem) {
						return SimpleEnergyItem.createStorage(ctx, energyItem.getEnergyCapacity(stack), energyItem.getEnergyMaxInput(stack), energyItem.getEnergyMaxOutput(stack));
					} else {
						return null;
					}
				},
				items.toArray(Item[]::new)
		);
	}

	public static void register(IEventBus eventBus) {
		DATA_COMPONENTS.register(eventBus);
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
