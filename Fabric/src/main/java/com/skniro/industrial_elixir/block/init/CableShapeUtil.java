package com.skniro.industrial_elixir.block.init;

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

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md

public final class CableShapeUtil {
    private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new IdentityHashMap();

    private static VoxelShape getStateShape(BlockState state) {
        CableBlock cableBlock = (CableBlock)state.getBlock();
        double size = cableBlock.type.cableThickness;
        VoxelShape baseShape = Shapes.box(size, size, size, (double)1.0F - size, (double)1.0F - size, (double)1.0F - size);
        List<VoxelShape> connections = new ArrayList();

        for(Direction dir : Direction.values()) {
            if ((Boolean)state.getValue((Property)CableBlock.PROPERTY_MAP.get(dir))) {
                double[] mins = new double[]{size, size, size};
                double[] maxs = new double[]{(double)1.0F - size, (double)1.0F - size, (double)1.0F - size};
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
        return (VoxelShape)SHAPE_CACHE.computeIfAbsent(state, CableShapeUtil::getStateShape);
    }
}