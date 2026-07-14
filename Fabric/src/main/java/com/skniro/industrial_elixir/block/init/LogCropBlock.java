package com.skniro.industrial_elixir.block.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LogCropBlock extends Block {
    public static final IntegerProperty AGE;
    private static final VoxelShape SHAPE;
    public final Item fruitItem;
    public static final EnumProperty<Direction.Axis> AXIS;

    public LogCropBlock(Properties settings, Item fruitItem) {
        super(settings.noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(AGE, 0));
        this.fruitItem = fruitItem;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return SHAPE;
    }

    public boolean isRandomlyTicking(BlockState state) {
        return (Integer)state.getValue(AGE) < 2;
    }


    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int i = (Integer)state.getValue(AGE);
        if (i < 2 && random.nextInt(40) == 0 && world.getRawBrightness(pos.above(), 0) >= 9) {
            BlockState blockState = (BlockState)state.setValue(AGE, i + 1);
            world.setBlock(pos, blockState, 2);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
        }
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        int i = (Integer)state.getValue(AGE);
        boolean bl = i == 2;
        if (i > 1) {
            int j = 1;
            popResource(world, pos, new ItemStack(fruitItem, j ));
            world.playSound((Player)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
            BlockState blockState = (BlockState)state.setValue(AGE, 0);
            world.setBlock(pos, blockState, 2);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState));
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, world, pos, player, hit);
        }
    }



    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE, AXIS});
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)state.getValue(AXIS)) {
                    case X -> {
                        return (BlockState)state.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return (BlockState)state.setValue(AXIS, Direction.Axis.X);
                    }
                    default -> {
                        return state;
                    }
                }
            default:
                return state;
        }
    }

    public boolean canGrow(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(2, (Integer)state.getValue(AGE) + 1);
        world.setBlock(pos, (BlockState)state.setValue(AGE, i), 2);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState)this.defaultBlockState().setValue(AXIS, ctx.getClickedFace().getAxis());
    }

    static {
        AGE = BlockStateProperties.AGE_2;
        AXIS = BlockStateProperties.AXIS;
        SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }
}
