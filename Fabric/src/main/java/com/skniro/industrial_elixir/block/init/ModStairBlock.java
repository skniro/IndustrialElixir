package com.skniro.industrial_elixir.block.init;

import com.skniro.industrial_elixir.api.block.SimpleHotsSpringLoggedBlock;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.util.ModBooleanPropertys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ModStairBlock extends StairBlock implements SimpleHotsSpringLoggedBlock {

    public static final BooleanProperty HOT_SPRING_LOGGED = ModBooleanPropertys.HOT_SPRING_LOGGED;

    public ModStairBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ModBooleanPropertys.HOT_SPRING_LOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModBooleanPropertys.HOT_SPRING_LOGGED);
    }

    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos pos = context.getClickedPos();
        FluidState replacedFluidState = context.getLevel().getFluidState(pos);
        BlockState state = (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())).setValue(HALF, clickedFace != Direction.DOWN && (clickedFace == Direction.UP || !(context.getClickLocation().y - (double)pos.getY() > (double)0.5F)) ? Half.BOTTOM : Half.TOP)).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER)).setValue(HOT_SPRING_LOGGED, replacedFluidState.is(IndustrialElixirFluids.STILL_Hot_Spring));
        return (BlockState)state.setValue(SHAPE, getStairsShape(state, context.getLevel(), pos));
    }

    private static StairsShape getStairsShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
        Direction facing = (Direction)state.getValue(FACING);
        BlockState behindState = level.getBlockState(pos.relative(facing));
        if (isStairs(behindState) && state.getValue(HALF) == behindState.getValue(HALF)) {
            Direction behindFacing = (Direction)behindState.getValue(FACING);
            if (behindFacing.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && canTakeShape(state, level, pos, behindFacing.getOpposite())) {
                if (behindFacing == facing.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState frontState = level.getBlockState(pos.relative(facing.getOpposite()));
        if (isStairs(frontState) && state.getValue(HALF) == frontState.getValue(HALF)) {
            Direction frontFacing = (Direction)frontState.getValue(FACING);
            if (frontFacing.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && canTakeShape(state, level, pos, frontFacing)) {
                if (frontFacing == facing.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction neighbour) {
        BlockState neighborState = level.getBlockState(pos.relative(neighbour));
        return !isStairs(neighborState) || neighborState.getValue(FACING) != state.getValue(FACING) || neighborState.getValue(HALF) != state.getValue(HALF);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED)) {
            ticks.scheduleTick(pos, IndustrialElixirFluids.STILL_Hot_Spring, IndustrialElixirFluids.STILL_Hot_Spring.getTickDelay(level));
        }

        return directionToNeighbour.getAxis().isHorizontal() ? state.setValue(SHAPE, getStairsShape(state, level, pos)) : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED)) {
            return IndustrialElixirFluids.STILL_Hot_Spring.getSource(false);
        }
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndLightGetter blockAndLightGetter, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return super.getAppearance(state, blockAndLightGetter, pos, side, sourceState, sourcePos);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
        if (type == Fluids.WATER || type == IndustrialElixirFluids.STILL_Hot_Spring) {
            return !state.getValue(BlockStateProperties.WATERLOGGED) && !state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED);
        }
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (state.getValue(BlockStateProperties.WATERLOGGED) || state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED)) {
            return false;
        }

        Fluid fluid = fluidState.getType();

        if (fluid == Fluids.WATER) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, true), 3);
                level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
            }
            return true;
        }

        if (fluid == IndustrialElixirFluids.STILL_Hot_Spring) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(ModBooleanPropertys.HOT_SPRING_LOGGED, true), 3);
                level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
            }
            return true;
        }

        return false;
    }

    @Override
    public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(ModBooleanPropertys.HOT_SPRING_LOGGED)) {
            level.setBlock(pos, state.setValue(ModBooleanPropertys.HOT_SPRING_LOGGED, false), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }
            return new ItemStack(IndustrialElixirFluidItems.Hot_Spring_BUCKET);
        }

        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, false), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return IndustrialElixirFluids.STILL_Hot_Spring.getPickupSound();
    }


}
