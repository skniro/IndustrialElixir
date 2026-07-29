package com.skniro.industrial_elixir.block.entity.machine.fluid;


import com.skniro.industrial_elixir.api.fluid.SingleFluidStorage;
import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractFluidMachineEntity extends AbstractMachineEntity {
    public static int FLUID_CRAFT_AMOUNT = 1000;
    public SingleFluidStorage fluidContainer = new SingleFluidStorage() {

        @Override
        protected long getCapacity(FluidVariant variant) {
            return (FluidConstants.BUCKET / 81) * 16; // 1 Bucket = 81000 Droplets = 1000mB || * 16 ==> 16,000mB = 16 Buckets
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

    public FluidVariant getFluid() {
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
        return FluidStorage.ITEM.find(inventory.get(FLUID_ITEM_SLOT), ContainerItemContext.withConstant(inventory.get(FLUID_ITEM_SLOT))) != null
                && FluidStorage.ITEM.find(inventory.get(FLUID_ITEM_SLOT), ContainerItemContext.withConstant(inventory.get(FLUID_ITEM_SLOT))).supportsExtraction()
                && FluidStorage.ITEM.find(inventory.get(FLUID_ITEM_SLOT), ContainerItemContext.withConstant(inventory.get(FLUID_ITEM_SLOT))) instanceof CombinedStorage<?, ?> combinedStorage
                && combinedStorage.parts.get(0) instanceof FullItemFluidStorage;
    }

    public void extractFluidForCrafting() {
        FluidVariant variant = fluidContainer.getResource();

        if (variant.isBlank()) {
            return;
        }

        if (fluidContainer.getAmount() < FLUID_CRAFT_AMOUNT) {
            return;
        }

        try (Transaction transaction = Transaction.openOuter()) {
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
        var itemStorage = FluidStorage.ITEM.find(inputStack, ContainerItemContext.withConstant(inputStack));
        if (itemStorage == null || !itemStorage.supportsExtraction()
                || !(itemStorage instanceof CombinedStorage<?, ?> combinedStorage)
                || !(combinedStorage.parts.get(0) instanceof FullItemFluidStorage fluidStorage)) {
            return;
        }

        ItemStack craftRemainder = inputStack.getItem() instanceof FluidCellItem
                ? new ItemStack(GrowableOresItems.EMPTY_CELL.get())
                : (inputStack.getItem().getCraftingRemainder() != null
                ? inputStack.getItem().getCraftingRemainder().create()
                : new ItemStack(Items.BUCKET));

        if (!canAcceptFluid(fluidStorage) || !canStoreCraftRemainder(inputStack, craftRemainder)) {
            return;
        }

        try (Transaction transaction = Transaction.openOuter()) {
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
        return (fluidContainer.getResource() == fluidStorage.getResource() || fluidContainer.isResourceBlank())
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
