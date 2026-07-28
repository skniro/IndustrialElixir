package com.skniro.growableoresir.registry.tag;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class GrowableBlockTags {
    public static final TagKey<Block> GrowBlock = of("growblock");



    private static TagKey<Block> of(String id) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID ,id));
    }
}
