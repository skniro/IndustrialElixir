package com.skniro.industrial_elixir.api.data.family;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import net.minecraft.data.BlockFamily;

public class GrowableOresBlockFamilies {

    public static final BlockFamily RUBBER_PLANKS = new BlockFamily.Builder(GeneralBlocks.Rubber_PLANKS)
            .button(GeneralBlocks.Rubber_BUTTON)
            .fence(GeneralBlocks.Rubber_FENCE)
            .fenceGate(GeneralBlocks.Rubber_FENCE_GATE)
            .pressurePlate(GeneralBlocks.Rubber_PRESSURE_PLATE)
            .sign(MapleSignBlocks.Rubber_SIGN, MapleSignBlocks.Rubber_WALL_SIGN)
            .slab(GeneralBlocks.Rubber_SLAB)
            .stairs(GeneralBlocks.Rubber_STAIRS)
            .door(GeneralBlocks.Rubber_DOOR)
            .trapdoor(GeneralBlocks.Rubber_TRAPDOOR)
            .getFamily();
}