package com.skniro.industrial_elixir.block.entity;


import com.skniro.industrial_elixir.block.init.MapleBlockSetType;
import com.skniro.industrial_elixir.mixin.SignTypeAccessor;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MapleSignTypes {
    public static final WoodType Rubber =
            SignTypeAccessor.registerNew(SignTypeAccessor.newSignType("maple_rubber", MapleBlockSetType.Rubber));
}