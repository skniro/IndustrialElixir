package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.machine.ElectricFurnaceScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricFurnaceEntity extends AbstractMachineEntity {
    private static final int INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public ElectricFurnaceEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.Electric_Furnace_BLOCK_ENTITY ,pos, state);
    }

    // 60/20 = 3 EU/t
    @Override
    public long getCraftEnergyCost() {
        return 60;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.ElectricFurnace);
    }

    public RecipeType<?> getCurrentRecipeType() {
        return RecipeType.SMELTING;
    }

    @Override
    protected void craftItem() {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = getRecipe();
        ItemStack output = recipe.get().value().assemble(new SingleRecipeInput(this.getItem(INPUT_SLOT))).copy();
        this.removeItem(INPUT_SLOT, 1);
        this.setItem(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getItem(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    protected Optional<RecipeHolder<SmeltingRecipe>> getRecipe() {
        if (this.getLevel() == null) return Optional.empty();
        ItemStack input = this.getItem(INPUT_SLOT);
        return this.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), this.getLevel());
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = getRecipe();
        if(recipe.isEmpty()) {
            return false;
        }
        ItemStack output = recipe.get().value().assemble(new SingleRecipeInput(this.getItem(INPUT_SLOT))).copy();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) && hasEnoughEnergyToCraft();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ElectricFurnaceScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
}
