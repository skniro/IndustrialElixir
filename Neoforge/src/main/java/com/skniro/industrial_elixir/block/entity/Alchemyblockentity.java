package com.skniro.industrial_elixir.block.entity;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.AlchemyBlockScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Alchemyblockentity extends AbstractMachineEntity {

    public Alchemyblockentity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.ALCHEMY_BLOCK_ENTITY.get(), pos, state);
    }

    // 40/20 = 2 EU/t
    @Override
    public long getCraftEnergyCost() {
        return 40;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.CaneConverter);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.CANE_CONVERTER.type.get();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new AlchemyBlockScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public long getMachineCapacity() {
        long extra = getEnergyStorageUpgrade();
        if(getBlockState().getBlock() instanceof AbstractMachineblock machineblock) {
            return energyTier == EnergyTier.INFINITE ? Long.MAX_VALUE : machineblock.getMaxCapacity() + extra + energyTier.getMaxOutput();
        }
        return energyTier == EnergyTier.INFINITE ? Long.MAX_VALUE : 512 + extra + energyTier.getMaxOutput();
    }
}
