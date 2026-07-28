package com.skniro.industrial_elixir.util;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GrowableOresItemGroups {
    public static final DeferredRegister<CreativeModeTab> GROWABLE_ORES_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialElixir.MOD_ID);

    public static final Supplier<CreativeModeTab> Growable_Ores_Group = GROWABLE_ORES_TABS.register("test_group",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(GrowableOresBlocks.GrowableOres_Block.asItem().get()))
                    .title(Component.translatable("itemGroup.growable_ores.test_group"))
                    .build());

    public static void register(IEventBus eventBus) {
        GROWABLE_ORES_TABS.register(eventBus);
    }
}
