package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.block.entity.BasePowerBlockBlockEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public abstract class NewBaseGeneratorBlockEntity extends BasePowerBlockBlockEntity {

    private long capacity;

    public NewBaseGeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);

        // Generators are sources: they must never accept energy from a neighbouring cable or
        // storage, and every side has to be extractable so the cable network can drain them.
        // This matches BaseGeneratorBlockEntity / NuclearReactorBlockEntity in 26.1.2.
        energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                return getMachineCapacity();
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                return side == null ? getEffectiveTier().getMaxInput() : 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return getEffectiveTier().getMaxOutput();
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
            }
        };
    }

    public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
        return this.energyContainer.getSideStorage(side);
    }

    public void pushEnergyToNeighbours() {
        for (Direction dir : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.getCapability(level, worldPosition.relative(dir), null,null, dir.getOpposite());
            if (target == null) continue;
            EnergyStorageUtil.move(energyContainer.getSideStorage(dir), target, ((AbstractMachineblock)getBlockState().getBlock()).getEnergyTier().getMaxOutput());
        }
    }


    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
    }

    public boolean canInsert(ItemStack stack) {
        return stack.is(ModItemTags.BATTERY);
    }
}