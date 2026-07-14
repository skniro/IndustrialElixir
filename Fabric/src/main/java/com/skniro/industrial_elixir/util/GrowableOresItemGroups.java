package com.skniro.industrial_elixir.util;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GrowableOresItemGroups {
    public static final ResourceKey<CreativeModeTab> Growable_Ores_Group = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "test_group"));

    public static void vanilla_item() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Growable_Ores_Group, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(GrowableOresBlocks.GrowableOres_Block))
                .title(Component.translatable("itemGroup.growable_ores.test_group"))
                .build()); // build() no longer registers by itself

    }
}
