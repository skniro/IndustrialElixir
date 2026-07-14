package com.skniro.industrial_elixir.api.data.model;

import static net.minecraft.client.data.models.model.TextureMapping.getBlockTexture;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.Block;

public class ModTextureMap {

    public static TextureMapping allSides(Block block) {
        Material id = getBlockTexture(block);
        TextureMapping map = new TextureMapping();
        map.put(TextureSlot.NORTH, id);
        map.put(TextureSlot.SOUTH, id);
        map.put(TextureSlot.EAST, id);
        map.put(TextureSlot.WEST, id);
        map.put(TextureSlot.UP, id);
        map.put(TextureSlot.DOWN, id);
        return map;
    }

}