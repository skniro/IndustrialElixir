package com.skniro.industrial_elixir.energy.heat.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

// CREDIT: https://github.com/TechReborn/energy
// Under MIT-License: https://github.com/TechReborn/Energy/blob/master/LICENSE
@ApiStatus.Internal
public class HeatImpl {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
			DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, IndustrialElixir.MOD_ID);

	public static final DataComponentType<Long> Heat_COMPONENT = DataComponentType.<Long>builder()
			.persistent(nonNegativeLong())
			.networkSynchronized(ByteBufCodecs.VAR_LONG)
			.build();

	static {
		DATA_COMPONENTS.register("heat", () -> Heat_COMPONENT);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.registerItem(HeatStorage.ITEM, (stack, ctx) -> {
			if (stack.getItem() instanceof SimpleHeatItem HeatItem) {
				return SimpleHeatItem.createStorage(ctx, HeatItem.getHeatCapacity(stack), HeatItem.getHeatMaxInput(stack), HeatItem.getHeatMaxOutput(stack));
			} else {
				return null;
			}
		});
	}

	public static void register(IEventBus eventBus) {
		DATA_COMPONENTS.register(eventBus);
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
