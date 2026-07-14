package com.skniro.industrial_elixir.block.init.pipe;

import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;

import com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity;
import com.skniro.industrial_elixir.item.init.PipePlugItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;

import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class PipeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty EAST;
    public static final BooleanProperty WEST;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty COVERED;

    public static final Map<Direction, BooleanProperty> PROPERTY_MAP;

    public PipeBlock(Properties settings) {
        super(settings);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false)
                .setValue(COVERED, false)
        );
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity be = world.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(be instanceof PipeBlockEntity pipe)) return InteractionResult.PASS;

        Direction side = hit.getDirection();
        if (!(stack.getItem() instanceof PipePlugItem)) {
            if (pipe.isBlocked(side)) {
                pipe.setBlocked(side, false);
            } else {
                pipe.setBlocked(side, true);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.PASS;
        }
        world.setBlock(pos, state, Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) ->
                ((PipeBlockEntity) blockEntity).tick(world1, pos, state1);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if ((Boolean)state.getValue(WATERLOGGED) && world instanceof LevelAccessor worldIn) {
            worldIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block,
                               @Nullable Orientation wireOrientation, boolean notify) {

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof PipeBlockEntity pipe) {
            pipe.neighborUpdate();
        }

        super.neighborChanged(state, world, pos, block, wireOrientation, notify);
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(COVERED)
                ? Shapes.block()
                : PipeShapeUtil.getShape(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return PipeShapeUtil.getShape(state);
    }


    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return !state.getValue(COVERED) && SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(LivingEntity player, BlockGetter view, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(COVERED) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, view, pos, state, fluid);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndLightGetter view, BlockPos pos,
                                    Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {

        if (state.getValue(COVERED)) {
            Object data = view.getBlockEntityRenderData(pos);
            if (data instanceof BlockState cover) {
                return cover;
            }
            return Blocks.OAK_PLANKS.defaultBlockState();
        }

        return super.getAppearance(state, view, pos, side, sourceState, sourcePos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN, WATERLOGGED, COVERED);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("PipeBlock does not support getCodec!");
    }

    // =========================
    // 静态
    // =========================

    static {
        EAST = BlockStateProperties.EAST;
        WEST = BlockStateProperties.WEST;
        NORTH = BlockStateProperties.NORTH;
        SOUTH = BlockStateProperties.SOUTH;
        UP = BlockStateProperties.UP;
        DOWN = BlockStateProperties.DOWN;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        COVERED = BooleanProperty.create("covered");

        PROPERTY_MAP = Util.make(new HashMap<>(), map -> {
            map.put(Direction.EAST, EAST);
            map.put(Direction.WEST, WEST);
            map.put(Direction.NORTH, NORTH);
            map.put(Direction.SOUTH, SOUTH);
            map.put(Direction.UP, UP);
            map.put(Direction.DOWN, DOWN);
        });
    }
}
