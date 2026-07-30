package com.skniro.industrial_elixir.block.entity.machine.fluid;


import com.skniro.industrial_elixir.api.fluid.FullItemFluidStorage;
import com.skniro.industrial_elixir.api.fluid.SingleFluidStorage;
import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractFluidMachineEntity extends AbstractMachineEntity {
    public static int FLUID_CRAFT_AMOUNT = 1000;
    public SingleFluidStorage fluidContainer = new SingleFluidStorage() {

        @Override
        protected int getCapacity(FluidResource variant) {
            return 1000 * 16; // 16 buckets = 16000 mB
        }

        @Override
        protected void onFinalCommit() {
            setChanged(level, getBlockPos(), getBlockState());
            getLevel().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };

    public AbstractFluidMachineEntity(BlockEntityType entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public FluidResource getFluid() {
        return this.fluidContainer.variant;
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if(world.isClientSide()) {
            return;
        }

        charge(ENERGY_ITEM_SLOT);

        if (hasFluidStackInFluidSlot()) {
            fillUpFluidTank();
        }


        if(hasRecipe() && canInsertIntoOutputSlot()) {
            increaseCraftingProgress();
            useEnergyForCrafting();
            setChanged(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                extractFluidForCrafting();
                resetProgress();
            }

        } else {
            resetProgress();
        }

        boolean isWorking = hasRecipe() && hasEnoughEnergyToCraft() && hasEnoughFluidToCraft();
        if (state.getValue(AbstractMachineblock.LIT) != isWorking) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, isWorking), 3);
        }
    }

    public boolean hasFluidStackInFluidSlot() {
        ItemStack stack = inventory.get(FLUID_ITEM_SLOT);
        if (stack.isEmpty()) {
            return false;
        }
        var fluidHandler = Capabilities.Fluid.ITEM.getCapability(stack, ItemAccess.forStack(stack));
        if (fluidHandler == null) {
            return false;
        }

        return !fluidHandler.getResource(0).isEmpty();
    }

    public void extractFluidForCrafting() {
        FluidResource variant = fluidContainer.getResource(0);

        if (variant.isEmpty()) {
            return;
        }

        if (fluidContainer.getAmount() < FLUID_CRAFT_AMOUNT) {
            return;
        }

        try (Transaction transaction = Transaction.openRoot()) {
            long extracted = fluidContainer.extract(variant, FLUID_CRAFT_AMOUNT, transaction
            );

            if (extracted == FLUID_CRAFT_AMOUNT) {
                transaction.commit();
            }
        }
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output().create();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) && hasEnoughEnergyToCraft() && hasEnoughFluidToCraft();
    }

    public Optional<RecipeHolder<AbstractMachineCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<AbstractMachineCraftingRecipe>) getCurrentRecipeType(), new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT)), this.getLevel());
    }

    public boolean hasEnoughFluidToCraft() {
        return fluidContainer.getAmount() >= FLUID_CRAFT_AMOUNT;
    }

    public void fillUpFluidTank() {
        ItemStack inputStack = inventory.get(FLUID_ITEM_SLOT);
        var itemStorage = Capabilities.Fluid.ITEM.getCapability(inputStack, ItemAccess.forStack(inputStack));
        if (itemStorage == null || itemStorage.getResource(0).isEmpty()) {
            return;
        }

        ItemStack craftRemainder = inputStack.getItem() instanceof FluidCellItem
                ? new ItemStack(GrowableOresItems.EMPTY_CELL.get())
                : (inputStack.getItem().getCraftingRemainder() != null
                ? inputStack.getItem().getCraftingRemainder().create()
                : new ItemStack(Items.BUCKET));

        if (!(itemStorage instanceof FullItemFluidStorage fluidStorage) || !canAcceptFluid(fluidStorage) || !canStoreCraftRemainder(inputStack, craftRemainder)) {
            return;
        }

        try (Transaction transaction = Transaction.openRoot()) {
            long inserted = this.fluidContainer.insert(fluidStorage.getResource(), 1000, transaction);
            if (inserted != 1000) {
                return;
            }

            if (inputStack.getItem() instanceof FluidCellItem) {
                inputStack.shrink(1);
                storeCraftRemainder(craftRemainder);
            } else {
                inventory.set(FLUID_ITEM_SLOT, craftRemainder);
            }
            transaction.commit();
        }
    }

    private boolean canAcceptFluid(FullItemFluidStorage fluidStorage) {
        return (fluidContainer.getResource(0) == fluidStorage.getResource() || fluidContainer.isResourceBlank())
                && fluidContainer.getAmount() + 1000 <= fluidContainer.getCapacity();
    }

    private boolean canStoreCraftRemainder(ItemStack inputStack, ItemStack craftRemainder) {
        if (!(inputStack.getItem() instanceof FluidCellItem)) {
            return true;
        }

        ItemStack remainderSlot = inventory.get(EMPTY_FLUID_ITEM_SLOT);
        return remainderSlot.isEmpty()
                || (ItemStack.isSameItemSameComponents(remainderSlot, craftRemainder)
                && remainderSlot.getCount() + craftRemainder.getCount() <= remainderSlot.getMaxStackSize());
    }

    private void storeCraftRemainder(ItemStack craftRemainder) {
        ItemStack remainderSlot = inventory.get(EMPTY_FLUID_ITEM_SLOT);
        if (remainderSlot.isEmpty()) {
            inventory.set(EMPTY_FLUID_ITEM_SLOT, craftRemainder.copy());
        } else {
            remainderSlot.grow(craftRemainder.getCount());
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveCustomOnly(registryLookup);
    }
}
