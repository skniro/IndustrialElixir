package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.block.entity.pipe.PipeBlockEntity;
import com.skniro.industrial_elixir.item.AdvancedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PipePlugItem extends Item {

    public PipePlugItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        Player player = context.getPlayer();

        BlockEntity be = world.getBlockEntity(pos);

        if (!(be instanceof PipeBlockEntity pipe)) {
            return InteractionResult.PASS;
        }

        if (world.isClientSide()) return InteractionResult.SUCCESS;

        if (pipe.isBlocked(side)) {

            pipe.setBlocked(side, false);

            if (!player.isCreative()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(AdvancedItems.PIPE_PLUG));
            }

        } else {
            pipe.setBlocked(side, true);

            if (!player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }

        world.setBlock(pos, world.getBlockState(pos), Block.UPDATE_ALL);

        return InteractionResult.SUCCESS;
    }
}