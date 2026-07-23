package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public abstract class NewBaseGeneratorBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory {

    public SimpleSidedEnergyContainer energyContainer;

    public NewBaseGeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        energyContainer = new SimpleSidedEnergyContainer() {

            @Override
            public long getCapacity() {
                return ((AbstractMachineblock)state.getBlock()).getMaxCapacity();
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                if (side == null) return ((AbstractMachineblock)state.getBlock()).getEnergyTier().getMaxInput();
                return 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return ((AbstractMachineblock)state.getBlock()).getEnergyTier().getMaxOutput();
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

    private void pushEnergyToNeighbours() {
        for (Direction dir : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.find(level, worldPosition.relative(dir), dir.getOpposite());
            if (target == null) continue;
            EnergyStorageUtil.move(energyContainer.getSideStorage(dir), target, ((AbstractMachineblock)getBlockState().getBlock()).getEnergyTier().getMaxOutput(), null);
        }
    }

    public void discharge(int slot) {
        if (this.level != null) {
            if (!this.level.isClientSide()) {
                if (!this.getOptionalInventory().isEmpty()) {
                    Container inventory = this.getOptionalInventory().get();
                    EnergyStorageUtil.move(this.getSideEnergyStorage(null), ContainerItemContext.ofSingleSlot(ContainerStorage.of(inventory, null).getSlots().get(slot)).find(EnergyStorage.ITEM), Long.MAX_VALUE, null);
                }
            }
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