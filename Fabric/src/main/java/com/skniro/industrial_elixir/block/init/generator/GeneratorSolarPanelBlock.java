package com.skniro.industrial_elixir.block.init.generator;


import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.generator.GeneratorSolarPanelBlockEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GeneratorSolarPanelBlock extends AbstractMachineblock {
    private final int DayPower, NightPower;

    public GeneratorSolarPanelBlock(Properties settings, EnergyTier energyTier, int DayPower, int NightPower, long capacity) {
        super(settings, energyTier, capacity);
        this.DayPower = DayPower;
        this.NightPower = NightPower;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("Block does not support getCodec!");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratorSolarPanelBlockEntity(pos, state);
    }

/*    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof GeneratorSolarPanelBlockEntity generator) {
            generator.setPower(DayPower, NightPower);
        }
    }*/

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, AlchemyBlockEntityType.GENERATOR_SOLAR_PANEL_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    public int getDayPower(){
        return DayPower;
    }

    public int getNightPower(){
        return NightPower;
    }

}
