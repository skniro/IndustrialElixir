package com.skniro.industrial_elixir.block.entity.machine.fluid;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.fluid.OreWashingBlock;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.OreWashingCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.OreWashingScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class OreWashingBlockEntity extends AbstractFluidMachineEntity {
    public int progress = 0;
    public int maxProgress = 72;

    public OreWashingBlockEntity(BlockPos pos, BlockState blockState) {
        super(AlchemyBlockEntityType.Ore_Washing_BLOCK_ENTITY, pos, blockState);
        this.inventory = NonNullList.withSize(12, ItemStack.EMPTY);
    }

    // 320/20 = 16 EU/t
    @Override
    public long getCraftEnergyCost() {
        return 320;
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
        return Component.translatable(FurnitureStrings.OreWashing);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new OreWashingScreenHandler(containerId, inventory, this, this.propertyDelegate);
    }

    @Override
    protected void craftItem() {
        OreWashingCraftingRecipe recipe = (OreWashingCraftingRecipe) getCurrentRecipe().get().value();
        this.removeItem(INPUT_SLOT, recipe.requiredCount());
        insertOutput(OUTPUT_SLOT, recipe.output().create());
        recipe.output2().ifPresent(output -> insertOutput(OUTPUT_SLOT_2, output.create()));
        recipe.output3().ifPresent(output -> insertOutput(OUTPUT_SLOT_3, output.create()));
        try (Transaction tx = Transaction.openOuter()) {
            fluidContainer.extract(fluidContainer.getResource(), recipe.requiredFluidAmount(), tx);
            tx.commit();
        }
    }

    private void insertOutput(int slot, ItemStack output) {
        if (output.isEmpty()) {
            return;
        }
        if (this.getItem(slot).isEmpty()) {
            this.setItem(slot, output.copy());
        } else {
            this.getItem(slot).grow(output.getCount());
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

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(OreWashingBlock.FACING);
        return switch (slot) {
            case INPUT_SLOT, SECOND_INPUT_SLOT -> side != localDir.getOpposite(); // If not Up/Down/Back, it must be Front/Left/Right
            case FLUID_ITEM_SLOT -> side == localDir.getClockWise(); // Left
            case ENERGY_ITEM_SLOT -> side == localDir.getCounterClockWise(); // Right
            default -> false; // Fallback
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if ((slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2 && slot != OUTPUT_SLOT_3) || side == Direction.UP) {
            return false;
        }

        if (side == Direction.DOWN) {
            return true;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(OreWashingBlock.FACING);
        return side == localDir.getOpposite() || side == localDir.getClockWise();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2 && slot != OUTPUT_SLOT_3;
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.ORE_WASHING.type;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT, FLUID_ITEM_SLOT, ENERGY_ITEM_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT, OUTPUT_SLOT_2, OUTPUT_SLOT_3};
        }
    }

    @Override
    public Optional<RecipeHolder<AbstractMachineCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<AbstractMachineCraftingRecipe>) getCurrentRecipeType(),
                        new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT)), this.getLevel());
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> opt = getCurrentRecipe();
        if (opt.isEmpty()) return false;

        var recipeHolder = opt.get();
        var recipe = recipeHolder.value();
        OreWashingCraftingRecipe washingRecipe = (OreWashingCraftingRecipe) recipe;

        if (!canInsertIntoSlot(OUTPUT_SLOT, washingRecipe.output().create())) return false;
        if (washingRecipe.output2().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_2, washingRecipe.output2().get().create())) return false;
        if (washingRecipe.output3().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_3, washingRecipe.output3().get().create())) return false;
        if (!hasEnoughEnergyToCraft()) return false;

        var currentFluid = this.fluidContainer.getResource().getFluid();
        var currentId = BuiltInRegistries.FLUID.getKey(currentFluid);
        return currentId != null
                && washingRecipe.requiredFluid().equals(currentId)
                && this.fluidContainer.getAmount() >= washingRecipe.requiredFluidAmount();
    }

    private boolean canInsertIntoSlot(int slot, ItemStack output) {
        if (output.isEmpty()) {
            return true;
        }
        ItemStack stack = this.getItem(slot);
        int maxCount = stack.isEmpty() ? output.getMaxStackSize() : stack.getMaxStackSize();
        return (stack.isEmpty() || ItemStack.isSameItemSameComponents(stack, output))
                && maxCount >= stack.getCount() + output.getCount();
    }

    @Override
    public void extractFluidForCrafting() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty() || !(recipe.get().value() instanceof OreWashingCraftingRecipe washingRecipe)) {
            return;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            fluidContainer.extract(fluidContainer.getResource(), washingRecipe.requiredFluidAmount(), transaction);
            transaction.commit();
        }
    }
}
