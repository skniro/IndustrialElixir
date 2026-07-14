package com.skniro.industrial_elixir.block.init.machine;

import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.machine.fluid.MatterGeneratorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Minimal Matter Generator block. Energy tier set to TIER3.
 * BlockEntity and ticking are wired similarly to other machine blocks.
 * The BlockEntity implements the machine logic; user will complete remaining behavior.
 */
public class MatterGeneratorBlock extends AbstractMachineblock {
    public MatterGeneratorBlock(Properties settings) {
        super(settings, EnergyTier.TIER3);
    }

    public static final MapCodec<MatterGeneratorBlock> CODEC = simpleCodec(MatterGeneratorBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MatterGeneratorEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, AlchemyBlockEntityType.MATTER_GENERATOR_BE, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}

