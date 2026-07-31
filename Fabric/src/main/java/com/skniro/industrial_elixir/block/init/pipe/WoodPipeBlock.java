package com.skniro.industrial_elixir.block.init.pipe;

import com.mojang.serialization.MapCodec;
import com.skniro.industrial_elixir.block.entity.pipe.PipeExtractDirectionController;
import com.skniro.industrial_elixir.block.entity.pipe.WoodPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodPipeBlock extends PipeBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty PULL_EAST = BooleanProperty.create("pull_east");
    public static final BooleanProperty PULL_WEST = BooleanProperty.create("pull_west");
    public static final BooleanProperty PULL_NORTH = BooleanProperty.create("pull_north");
    public static final BooleanProperty PULL_SOUTH = BooleanProperty.create("pull_south");
    public static final BooleanProperty PULL_UP = BooleanProperty.create("pull_up");
    public static final BooleanProperty PULL_DOWN = BooleanProperty.create("pull_down");

    public static final Map<Direction, BooleanProperty> PULL_PROPERTY_MAP = Util.make(new HashMap<>(), map -> {
        map.put(Direction.EAST, PULL_EAST);
        map.put(Direction.WEST, PULL_WEST);
        map.put(Direction.NORTH, PULL_NORTH);
        map.put(Direction.SOUTH, PULL_SOUTH);
        map.put(Direction.UP, PULL_UP);
        map.put(Direction.DOWN, PULL_DOWN);
    });

    public WoodPipeBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(PULL_EAST, false)
                .setValue(PULL_WEST, false)
                .setValue(PULL_NORTH, false)
                .setValue(PULL_SOUTH, false)
                .setValue(PULL_UP, false)
                .setValue(PULL_DOWN, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodPipeBlockEntity(pos, state);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof PipeExtractDirectionController controller)) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            controller.clearPreferredExtractDirection();
        } else {
            Direction current = controller.getPreferredExtractDirection();
            // 收集所有已连接的方向
            List<Direction> connected = new ArrayList<>();
            for (Direction dir : Direction.values()) {
                if (state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) {
                    connected.add(dir);
                }
            }
            if (connected.isEmpty()) return InteractionResult.SUCCESS;

            Direction next;
            if (current == null || !connected.contains(current)) {
                next = connected.get(0);
            } else {
                int index = connected.indexOf(current);
                next = connected.get((index + 1) % connected.size());
            }
            controller.setPreferredExtractDirection(next);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) ->
                ((WoodPipeBlockEntity) blockEntity).tick(world1, pos, state1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PULL_EAST, PULL_WEST, PULL_NORTH, PULL_SOUTH, PULL_UP, PULL_DOWN);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("PipeBlock does not support getCodec!");
    }
}
