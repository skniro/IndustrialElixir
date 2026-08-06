package com.skniro.industrial_elixir.block.entity.machine.fluid;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.fluid.BrewReactorBlock;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.BrewReactorCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.BrewReactorScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BrewReactorBlockEntity extends AbstractFluidMachineEntity {

    public BrewReactorBlockEntity(BlockPos pos, BlockState blockState) {
        super(AlchemyBlockEntityType.Brew_Reactor_BLOCK_ENTITY, pos, blockState);
        this.DEFAULT_MAX_PROGRESS = 100;
        this.maxProgress = 100;
    }

    // 40/20 = 2 EU/t
    @Override
    public long getCraftEnergyCost() {
        return 40;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        SingleVariantStorage.writeValue(fluidContainer, FluidVariant.CODEC, output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        SingleVariantStorage.readValue(fluidContainer, FluidVariant.CODEC, FluidVariant::blank, input);
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.BrewReactor);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new BrewReactorScreenHandler(containerId, inventory, this, this.propertyDelegate);
    }

    @Override
    protected void craftItem() {
        java.util.Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        net.minecraft.world.item.ItemStack output = recipe.get().value().output().create();
        // consume primary input according to recipe
        this.removeItem(INPUT_SLOT, recipe.get().value().requiredCount());
        // consume one from the secondary input slot
        this.removeItem(SECOND_INPUT_SLOT, 1);
        if (this.getItem(OUTPUT_SLOT).isEmpty()) {
            this.setItem(OUTPUT_SLOT, output.copy());
        } else {
            this.getItem(OUTPUT_SLOT).grow(output.getCount());
        }
        try (Transaction tx = Transaction.openOuter()) {
            fluidContainer.extract(fluidContainer.getResource(), FLUID_CRAFT_AMOUNT, tx);
            tx.commit();
        }
    }

    /* SIDED INVENTORY */
    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        if (side == null || side == Direction.DOWN) return false;
        if (side == Direction.UP) return slot == INPUT_SLOT;

        if (slot != INPUT_SLOT && slot != FLUID_ITEM_SLOT && slot != ENERGY_ITEM_SLOT) {
            return false;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(BrewReactorBlock.FACING);
        return switch (slot) {
            case INPUT_SLOT, SECOND_INPUT_SLOT -> side != localDir.getOpposite(); // If not Up/Down/Back, it must be Front/Left/Right
            case FLUID_ITEM_SLOT -> side == localDir.getClockWise(); // Left
            case ENERGY_ITEM_SLOT -> side == localDir.getCounterClockWise(); // Right
            default -> false; // Fallback
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

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(BrewReactorBlock.FACING);
        return side == localDir.getOpposite() || side == localDir.getClockWise();
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.BREW_REACTOR.type;
    }

    @Override
    public int[] getSlotsForFace(net.minecraft.core.Direction direction) {
        if (direction != net.minecraft.core.Direction.DOWN) {
            return new int[]{INPUT_SLOT, SECOND_INPUT_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT};
        }
    }

    @Override
    public Optional<RecipeHolder<AbstractMachineCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<AbstractMachineCraftingRecipe>) getCurrentRecipeType(),
                        new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT), inventory.get(SECOND_INPUT_SLOT)), this.getLevel());
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> opt = getCurrentRecipe();
        if (opt.isEmpty()) return false;

        var recipeHolder = opt.get();
        var recipe = recipeHolder.value();
        net.minecraft.world.item.ItemStack output = recipe.output().create();

        if (!canInsertAmountIntoOutputSlot(output.getCount()) || !canInsertItemIntoOutputSlot(output)) return false;
        if (!hasEnoughEnergyToCraft()) return false;

        // If recipe is a BrewReactorCraftingRecipe and specifies a fluid, check fluid type and amount
        if (recipe instanceof BrewReactorCraftingRecipe brew) {
            java.util.Optional<net.minecraft.resources.Identifier> fluidOpt = brew.requiredFluid();
            java.util.Optional<Integer> amtOpt = brew.requiredFluidAmount();
            if (fluidOpt != null && fluidOpt.isPresent()) {
                try {
                    // compare registry ids to avoid Holder vs Fluid type issues
                    var currentFluid = this.fluidContainer.getResource().getFluid();
                    var currentId = BuiltInRegistries.FLUID.getKey(currentFluid);
                    if (currentId == null || !fluidOpt.get().equals(currentId)) return false;
                    int required = (amtOpt != null && amtOpt.isPresent()) ? amtOpt.get() : AbstractFluidMachineEntity.FLUID_CRAFT_AMOUNT;
                    return this.fluidContainer.getAmount() >= required;
                } catch (Throwable t) {
                    return false;
                }
            }
        }

        return hasEnoughFluidToCraft();
    }
}