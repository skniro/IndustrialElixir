package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.handler.machine.CuttingScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CuttingEntity extends AbstractMachineEntity {

    public CuttingEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.Cutting_BLOCK_ENTITY ,pos, state);
        this.DEFAULT_MAX_PROGRESS = 450;
        this.maxProgress = 450;
    }

    // 80/20 = 4 EU/t
    @Override
    public long getCraftEnergyCost() {
        return 80;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Block_Cutter);
    }

    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.CUTTING.type;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CuttingScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
}
