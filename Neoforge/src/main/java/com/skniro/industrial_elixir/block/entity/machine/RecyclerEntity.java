package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.RecyclerCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.RecyclerScreenHandler;
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
import net.minecraft.world.level.block.state.BlockState;

public class RecyclerEntity extends AbstractMachineEntity {

    private static final int INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public RecyclerEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.Recycler_BLOCK_ENTITY.get(),pos, state);
        this.DEFAULT_MAX_PROGRESS = 45;
        this.maxProgress = 45;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Recycler);
    }

    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.RECYCLER.type.get();
    }

    @Override
    protected void craftItem() {
        Optional<RecipeHolder<RecyclerCraftingRecipe>> recipe = getRecyclerRecipe();
        this.removeItem(INPUT_SLOT, 1);
        this.setItem(OUTPUT_SLOT, new ItemStack(recipe.get().value().output().item(),
                this.getItem(OUTPUT_SLOT).getCount() + recipe.get().value().output().count()));
    }

    protected Optional<RecipeHolder<RecyclerCraftingRecipe>> getRecyclerRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<RecyclerCraftingRecipe>) getCurrentRecipeType(), new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT)), this.getLevel());
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<RecyclerCraftingRecipe>> recipe = getRecyclerRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output().create();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) && hasEnoughEnergyToCraft();
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new RecyclerScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
}
