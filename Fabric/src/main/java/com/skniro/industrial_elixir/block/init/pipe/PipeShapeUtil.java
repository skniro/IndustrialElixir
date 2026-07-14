package com.skniro.industrial_elixir.block.init.pipe;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class PipeShapeUtil {
    private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new IdentityHashMap();

    private static VoxelShape getStateShape(BlockState state) {
        PipeBlock cableBlock = (PipeBlock)state.getBlock();
        //double size = cableBlock.type.cableThickness;
        VoxelShape baseShape = Shapes.box(10.0F/ 2.0 / 16.0, 10.0F/ 2.0 / 16.0, 10.0F/ 2.0 / 16.0, (double)1.0F - 10.0F/ 2.0 / 16.0, (double)1.0F - 10.0F/ 2.0 / 16.0, (double)1.0F - 10.0F/ 2.0 / 16.0);
        List<VoxelShape> connections = new ArrayList();

        for(Direction dir : Direction.values()) {
            if ((Boolean)state.getValue((Property) PipeBlock.PROPERTY_MAP.get(dir))) {
                double[] mins = new double[]{10.0F/ 2.0 / 16.0, 10.0F/ 2.0 / 16.0, 10.0F/ 2.0 / 16.0};
                double[] maxs = new double[]{(double)1.0F - 10.0F/ 2.0 / 16.0, (double)1.0F - 10.0F/ 2.0 / 16.0, (double)1.0F - 10.0F/ 2.0 / 16.0};
                int axis = dir.getAxis().ordinal();
                if (dir.getAxisDirection() == AxisDirection.POSITIVE) {
                    maxs[axis] = (double)1.0F;
                } else {
                    mins[axis] = (double)0.0F;
                }

                connections.add(Shapes.box(mins[0], mins[1], mins[2], maxs[0], maxs[1], maxs[2]));
            }
        }

        return Shapes.or(baseShape, (VoxelShape[])connections.toArray(new VoxelShape[0]));
    }

    public static VoxelShape getShape(BlockState state) {
        return (VoxelShape)SHAPE_CACHE.computeIfAbsent(state, PipeShapeUtil::getStateShape);
    }
}
