package com.skniro.industrial_elixir.block.init;


import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;

import com.skniro.industrial_elixir.ModContent;
import com.skniro.industrial_elixir.block.entity.cable.CableBlockEntity;
import com.skniro.industrial_elixir.block.entity.cable.CableElectrocutionEvent;
import com.skniro.industrial_elixir.init.ModDamageTypes;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md

public class CableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty EAST;
    public static final BooleanProperty WEST;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty COVERED;
    public static final Map<Direction, BooleanProperty> PROPERTY_MAP;
    public final ModContent.Cables type;

    public CableBlock(Properties settings,ModContent.Cables type, String name) {
        super(settings);
        this.type = type;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue(EAST, false)).setValue(WEST, false)).setValue(NORTH, false)).setValue(SOUTH, false)).setValue(UP, false)).setValue(DOWN, false)).setValue(WATERLOGGED, false)).setValue(COVERED, false));
        //BlockWrenchEventHandler.wrenchableBlocks.add(this);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("CableBlock does not support getCodec!");
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBlockEntity(pos, state, this.type);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((CableBlockEntity)blockEntity).tick(world1, pos, state1, (CableBlockEntity)blockEntity);
    }

    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
        ItemStack stack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity == null) {
            return InteractionResult.FAIL;
        } else if (stack.isEmpty()) {
            return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
        }
        return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{EAST, WEST, NORTH, SOUTH, UP, DOWN, WATERLOGGED, COVERED});
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if ((Boolean)state.getValue(WATERLOGGED) && world instanceof LevelAccessor worldIn) {
            worldIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return state;
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, @Nullable Orientation wireOrientation, boolean notify) {
        BlockEntity var8 = world.getBlockEntity(pos);
        if (var8 instanceof CableBlockEntity cable) {
            cable.neighborUpdate();
        }

        super.neighborChanged(state, world, pos, block, wireOrientation, notify);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext shapeContext) {
        return (Boolean)state.getValue(COVERED) ? Shapes.block() : CableShapeUtil.getShape(state);
    }

    public VoxelShape getOcclusionShape(BlockState state) {
        return CableShapeUtil.getShape(state);
    }

    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        super.entityInside(state, world, pos, entity, handler, bl);
        if (this.type.canKill) {
            if (entity instanceof LivingEntity) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null) {
                    if (blockEntity instanceof CableBlockEntity) {
                        CableBlockEntity blockEntityCable = (CableBlockEntity)blockEntity;
                        if (blockEntityCable.getEnergy() > 0L) {
                            if (((CableElectrocutionEvent)CableElectrocutionEvent.EVENT.invoker()).electrocute((LivingEntity)entity, this.type, pos, world, blockEntityCable)) {

                                if (this.type == ModContent.Cables.HV) {
                                    entity.igniteForSeconds(1.0F);
                                }

                                if (world instanceof ServerLevel) {
                                    ServerLevel serverWorld = (ServerLevel)world;
                                    entity.hurtServer(serverWorld, ModDamageTypes.create(world, ModDamageTypes.ELECTRIC_SHOCK), 1.0F);
                                }

                                blockEntityCable.setEnergy(0L);



                                //world.playSound((Entity)null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.CABLE_SHOCK, SoundCategory.BLOCKS, 0.6F, 1.0F);



                                world.addParticle(ParticleTypes.CRIT, entity.getX(), entity.getY(), entity.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);


                            }
                        }
                    }
                }
            }
        }
    }

    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return !(Boolean)state.getValue(COVERED) && SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluidState);
    }

    public boolean canPlaceLiquid(LivingEntity player, BlockGetter view, BlockPos pos, BlockState state, Fluid fluid) {
        return !(Boolean)state.getValue(COVERED) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, view, pos, state, fluid);
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public BlockState getAppearance(BlockState state, BlockAndTintGetter renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        if ((Boolean)state.getValue(COVERED)) {
            Object var9 = renderView.getBlockEntity(pos);
            BlockState cover;
            if (var9 instanceof BlockState) {
                BlockState blockState = (BlockState)var9;
                cover = blockState;
            } else {
                cover = Blocks.OAK_PLANKS.defaultBlockState();
            }

            return cover;
        } else {
            return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
        }
    }

    static {
        EAST = BlockStateProperties.EAST;
        WEST = BlockStateProperties.WEST;
        NORTH = BlockStateProperties.NORTH;
        SOUTH = BlockStateProperties.SOUTH;
        UP = BlockStateProperties.UP;
        DOWN = BlockStateProperties.DOWN;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        COVERED = BooleanProperty.create("covered");
        PROPERTY_MAP = (Map) Util.make(new HashMap(), (map) -> {
            map.put(Direction.EAST, EAST);
            map.put(Direction.WEST, WEST);
            map.put(Direction.NORTH, NORTH);
            map.put(Direction.SOUTH, SOUTH);
            map.put(Direction.UP, UP);
            map.put(Direction.DOWN, DOWN);
        });
    }
}