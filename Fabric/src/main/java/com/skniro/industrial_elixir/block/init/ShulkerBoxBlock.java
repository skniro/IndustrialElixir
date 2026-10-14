package com.skniro.industrial_elixir.block.init;

import com.mojang.serialization.MapCodec;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity.AnimationStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ShulkerBoxBlock extends BaseEntityBlock {
    public static final MapCodec<ShulkerBoxBlock> CODEC = simpleCodec(ShulkerBoxBlock::new);
    // 26.1 builds this with Shapes.rotateAll(Block.boxZ(...)); 1.21.1 has neither helper, so the six
    // rotated copies of the 1/16 thick slab are listed explicitly (the same values vanilla uses).
    public static final Map<Direction, VoxelShape> SHAPES_BY_DIRECTION = createShapesByDirection();
    public static final EnumProperty<Direction> FACING;
    public static final ResourceLocation CONTENTS_DYNAMIC_DROP_ID;

    private static Map<Direction, VoxelShape> createShapesByDirection() {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        shapes.put(Direction.NORTH, Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0));
        shapes.put(Direction.SOUTH, Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0));
        shapes.put(Direction.WEST, Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0));
        shapes.put(Direction.EAST, Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0));
        shapes.put(Direction.DOWN, Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0));
        shapes.put(Direction.UP, Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0));
        return shapes;
    }

    public MapCodec<ShulkerBoxBlock> codec() {
        return CODEC;
    }

    public ShulkerBoxBlock(Properties settings) {
        super(settings);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP));
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerBoxBlockEntity(pos, state);
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityType.SHULKER_BOX, ShulkerBoxBlockEntity::tick);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world instanceof ServerLevel) {
            BlockEntity var8 = world.getBlockEntity(pos);
            if (var8 instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                    player.openMenu(shulkerBoxBlockEntity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinAi.angerNearbyPiglins(player, true);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private static boolean canOpen(BlockState state, Level world, BlockPos pos, ShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStatus() != AnimationStatus.CLOSED) {
            return true;
        } else {
            AABB box = Shulker.getProgressDeltaAabb(1.0F, (Direction)state.getValue(FACING), 0.0F, 0.5F).move(pos).deflate(1.0E-6);
            return world.noCollision(box);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getClickedFace());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            if (!world.isClientSide() && player.isCreative() && !shulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = state.getBlock().asItem().getDefaultInstance();
                itemStack.applyComponents(blockEntity.collectComponents());
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
            } else {
                shulkerBoxBlockEntity.unpackLootTable(player);
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = (BlockEntity)builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS_DYNAMIC_DROP_ID, (consumer) -> {
                for(int i = 0; i < shulkerBoxBlockEntity.getContainerSize(); ++i) {
                    consumer.accept(shulkerBoxBlockEntity.getItem(i));
                }

            });
        }

        return super.getDrops(state, builder);
    }

    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            super.onRemove(state, world, pos, newState, moved);
            if (blockEntity instanceof ShulkerBoxBlockEntity) {
                world.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }
    }

    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            if (!shulkerBoxBlockEntity.isClosed()) {
                return (VoxelShape)SHAPES_BY_DIRECTION.get(((Direction)state.getValue(FACING)).getOpposite());
            }
        }

        return Shapes.block();
    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            return Shapes.create(shulkerBoxBlockEntity.getBoundingBox(state));
        } else {
            return Shapes.block();
        }
    }

    protected boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    static {
        FACING = DirectionalBlock.FACING;
        CONTENTS_DYNAMIC_DROP_ID = ResourceLocation.withDefaultNamespace("contents");
    }
}
