package com.skniro.industrial_elixir.block.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CupSpecialBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CupSpecialBlock> CODEC = simpleCodec(CupSpecialBlock::new);
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape WEST_SHAPE;

    public CupSpecialBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends CupSpecialBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case NORTH:
            default:
                return NORTH_SHAPE;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
    }

    private static VoxelShape buildCupShape(Direction facing) {
        // Element data from coffee_sp.json model (north-facing base orientation)
        double[][] from = {
                {6, 0, 3}, {6, 1, 3}, {6, 1, 6.6}, {6.5, 1, 3}, {9.5, 1, 3},
                {4.2, 1, 4.5}, {4.2, 3, 4.5}, {4.2, 2, 4.5},
                {6.4, 4, 3.3}
        };
        double[][] to = {
                {10, 1, 7}, {6.5, 5, 6.6}, {10, 5, 7}, {9.5, 5, 3.4}, {10, 5, 6.6},
                {6, 2, 5.3}, {6, 4, 5.3}, {5, 3, 5.3},
                {9.5, 4, 6.7}
        };

        VoxelShape shape = Shapes.empty();
        for (int i = 0; i < from.length; i++) {
            double[] f = rotatePoint(from[i][0], from[i][1], from[i][2], facing);
            double[] t = rotatePoint(to[i][0], to[i][1], to[i][2], facing);
            shape = Shapes.or(shape, box(
                    Math.min(f[0], t[0]), Math.min(f[1], t[1]), Math.min(f[2], t[2]),
                    Math.max(f[0], t[0]), Math.max(f[1], t[1]), Math.max(f[2], t[2])
            ));
        }
        return shape;
    }

    private static double[] rotatePoint(double x, double y, double z, Direction facing) {
        return switch (facing) {
            case NORTH -> new double[]{x, y, z};
            case SOUTH -> new double[]{16 - x, y, 16 - z};
            case EAST -> new double[]{16 - z, y, x};
            case WEST -> new double[]{z, y, 16 - x};
            default -> new double[]{x, y, z};
        };
    }

    static {
        NORTH_SHAPE = buildCupShape(Direction.NORTH);
        SOUTH_SHAPE = buildCupShape(Direction.SOUTH);
        EAST_SHAPE = buildCupShape(Direction.EAST);
        WEST_SHAPE = buildCupShape(Direction.WEST);
    }
}
