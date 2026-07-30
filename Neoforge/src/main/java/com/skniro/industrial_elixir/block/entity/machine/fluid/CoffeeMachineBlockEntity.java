package com.skniro.industrial_elixir.block.entity.machine.fluid;

import com.skniro.industrial_elixir.api.fluid.SingleFluidStorage;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.fluid.CoffeeMachineBlock;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.CoffeeMachineCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.CoffeeMachineScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class CoffeeMachineBlockEntity extends AbstractFluidMachineEntity {


    public CoffeeMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(AlchemyBlockEntityType.COFFEE_MACHINE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        SingleFluidStorage.writeValue(fluidContainer, output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        SingleFluidStorage.readValue(fluidContainer, input);
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.CoffeeMachine);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CoffeeMachineScreenHandler(containerId, inventory, this, this.propertyDelegate);
    }

    @Override
    protected void craftItem() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        CoffeeMachineCraftingRecipe coffeeRecipe = (CoffeeMachineCraftingRecipe) recipe.get().value();
        ItemStack output = coffeeRecipe.output().create();

        this.removeItem(INPUT_SLOT, coffeeRecipe.requiredCount());

        if (coffeeRecipe.ingredient2().isPresent()) {
            this.removeItem(SECOND_INPUT_SLOT, 1);
        }

        if (coffeeRecipe.ingredient3().isPresent()) {
            this.removeItem(THIRD_INPUT_SLOT, 1);
        }

        if (this.getItem(OUTPUT_SLOT).isEmpty()) {
            this.setItem(OUTPUT_SLOT, output.copy());
        } else {
            this.getItem(OUTPUT_SLOT).grow(output.getCount());
        }

        extractFluidForCrafting();
    }

    @Override
    public void extractFluidForCrafting() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipeOpt = getCurrentRecipe();
        if (recipeOpt.isEmpty() || !(recipeOpt.get().value() instanceof CoffeeMachineCraftingRecipe coffeeRecipe)) {
            return;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            fluidContainer.extract(fluidContainer.getResource(0), coffeeRecipe.requiredFluidAmount(), transaction);
            transaction.commit();
        }
    }

    /* SIDED INVENTORY */
    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        if (side == null || side == Direction.DOWN) return false;
        if (side == Direction.UP) {
            return slot == INPUT_SLOT || slot == SECOND_INPUT_SLOT || slot == THIRD_INPUT_SLOT;
        }

        if (slot != INPUT_SLOT && slot != SECOND_INPUT_SLOT && slot != THIRD_INPUT_SLOT
                && slot != FLUID_ITEM_SLOT && slot != ENERGY_ITEM_SLOT) {
            return false;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(CoffeeMachineBlock.FACING);
        return switch (slot) {
            case INPUT_SLOT, SECOND_INPUT_SLOT, THIRD_INPUT_SLOT -> side != localDir.getOpposite();
            case FLUID_ITEM_SLOT -> side == localDir.getClockWise();
            case ENERGY_ITEM_SLOT -> side == localDir.getCounterClockWise();
            default -> false;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if (slot != OUTPUT_SLOT || side == Direction.UP) {
            return false;
        }

        if (side == Direction.DOWN) {
            return true;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(CoffeeMachineBlock.FACING);
        return side == localDir.getOpposite() || side == localDir.getClockWise();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT;
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.COFFEE_MACHINE.type.get();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT, SECOND_INPUT_SLOT, THIRD_INPUT_SLOT, FLUID_ITEM_SLOT, ENERGY_ITEM_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT};
        }
    }

    @Override
    public Optional<RecipeHolder<AbstractMachineCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<AbstractMachineCraftingRecipe>) getCurrentRecipeType(),
                        new AlchemyCraftingRecipeInput(
                                inventory.get(INPUT_SLOT),
                                inventory.get(SECOND_INPUT_SLOT),
                                inventory.get(THIRD_INPUT_SLOT)), this.getLevel());
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> opt = getCurrentRecipe();
        if (opt.isEmpty()) return false;

        var recipeHolder = opt.get();
        var recipe = recipeHolder.value();
        CoffeeMachineCraftingRecipe coffeeRecipe = (CoffeeMachineCraftingRecipe) recipe;
        ItemStack output = coffeeRecipe.output().create();

        if (!canInsertAmountIntoOutputSlot(output.getCount()) || !canInsertItemIntoOutputSlot(output)) return false;
        if (!hasEnoughEnergyToCraft()) return false;

        // Check fluid requirement
        var currentFluid = this.fluidContainer.getResource(0).getFluid();
        var currentId = BuiltInRegistries.FLUID.getKey(currentFluid);
        if (currentId == null || !coffeeRecipe.requiredFluid().equals(currentId)) return false;
        if (this.fluidContainer.getAmount() < coffeeRecipe.requiredFluidAmount()) return false;

        return true;
    }
}
