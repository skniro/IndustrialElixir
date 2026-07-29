package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
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


public abstract class BaseGeneratorBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory {

    private long capacity;
    protected final EnergyTier energyTier;
    public SimpleSidedEnergyContainer energyContainer;

    public BaseGeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, long capacity, EnergyTier energyTier) {
        super(blockEntityType, pos, state);
        this.capacity = capacity;
        this.energyTier = energyTier;
        energyContainer = new SimpleSidedEnergyContainer() {

            @Override
            public long getCapacity() {
                return capacity;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                if (side == null) return energyTier.getMaxInput();
                return 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return energyTier.getMaxOutput();
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
            EnergyStorage target = EnergyStorage.SIDED.getCapability(level, worldPosition.relative(dir), null,null, dir.getOpposite());
            if (target == null) continue;
            EnergyStorageUtil.move(energyContainer.getSideStorage(dir), target, energyTier.getMaxOutput(), null);
        }
    }

    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
    }

    public EnergyTier getEnergyTier(){
        return energyTier;
    }

    public boolean canInsert(ItemStack stack) {
        return stack.is(ModItemTags.BATTERY);
    }

    public long getCapacity() {
        return capacity;
    }
}