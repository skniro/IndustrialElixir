package com.skniro.industrial_elixir.mixin;

import com.skniro.industrial_elixir.block.init.machine.IronFurnaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public class FurnaceSpeedMixin {

    @Shadow
    int cookingTimer;

    @Inject(method = "serverTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;cookingTimer:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private static void icr$boostIronFurnace(
            ServerLevel world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {

        if (state.getBlock() instanceof IronFurnaceBlock) {
            if (world.getGameTime() % 5 == 0) {
                ((FurnaceSpeedMixin)(Object)blockEntity).cookingTimer++;
            }
        }
    }
}