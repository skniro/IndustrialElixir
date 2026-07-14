package com.skniro.industrial_elixir.block.init.energybox;


import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.energybox.BaseEnergyBoxBlockEntity;
import com.skniro.industrial_elixir.block.entity.energybox.EnergyBoxBlockEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnergyBoxBlock extends AbstractMachineblock {
    private int capacity;
    public EnergyBoxBlock(Properties settings, int capacity, EnergyTier energyTier) {
        super(settings, energyTier);
        this.capacity = capacity;
    }

/*    public static final MapCodec<EnergyBoxBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    createSettingsCodec(),
                    Codec.INT.fieldOf("capacity").forGetter(block -> block.capacity),
                    EnergyTier.CODEC.fieldOf("energy_tier").forGetter(block -> block.energyTier)
            ).apply(instance, EnergyBoxBlock::new)
    );*/

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("Block does not support getCodec!");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyBoxBlockEntity(pos, state, capacity, energyTier);
    }


    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BaseEnergyBoxBlockEntity) {
            Containers.dropContents(world, pos, (Container) blockEntity);
            world.updateNeighbourForOutputSignal(pos,this);
        }
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, AlchemyBlockEntityType.EnergyBox_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

}
