package com.skniro.industrial_elixir.block.init.generator.init;

import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.generator.heat.SolidFuelHeaterEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SolidFuelHeaterBlock extends AbstractMachineblock {
    public static final MapCodec<SolidFuelHeaterBlock> CODEC = simpleCodec(SolidFuelHeaterBlock::new);

    public SolidFuelHeaterBlock(Properties settings) {
        super(settings, EnergyTier.TIER1, 1000);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SolidFuelHeaterEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, AlchemyBlockEntityType.SOLID_FUEL_HEATER_BE,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
