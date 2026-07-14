package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.handler.machine.ExtractorScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExtractorEntity extends AbstractMachineEntity {

    public ExtractorEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.Extractor_BLOCK_ENTITY ,pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Extractor);
    }

    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.EXTRACTOR.type;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ExtractorScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
}
