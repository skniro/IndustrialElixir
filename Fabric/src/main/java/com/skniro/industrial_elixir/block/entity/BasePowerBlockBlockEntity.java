package com.skniro.industrial_elixir.block.entity;

import com.skniro.industrial_elixir.api.ImplementedInventory;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;

public abstract class BasePowerBlockBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory {
    protected final EnergyTier energyTier;

    public BasePowerBlockBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, EnergyTier energyTier) {
        super(blockEntityType, pos, state);
        this.energyTier = energyTier;
    };

    public EnergyTier getEnergyTier(){
        return energyTier;
    }

    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
    }

}
