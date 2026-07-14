package com.skniro.industrial_elixir.block.entity.machine.heat;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleSidedHeatContainer;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.ModBlastFurnaceCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.heat.ModBlastFurnaceScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ModBlastFurnaceBlockEntity extends AbstractHeatMachineEntity {
    public int maxProgress = 6000;
    public SingleVariantStorage<FluidVariant> fluidContainer = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return (FluidConstants.BUCKET / 81) * 8; // 1 Bucket = 81000 Droplets = 1000mB || * 16 ==> 16,000mB = 16 Buckets
        }

        @Override
        protected void onFinalCommit() {
            setChanged(level, getBlockPos(), getBlockState());
            getLevel().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };

    protected final ContainerData propertyDelegate;
    public ModBlastFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(AlchemyBlockEntityType.BLAST_FURNACE_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, pos, blockState);;
        heatContainer = new SimpleSidedHeatContainer() {
            @Override
            public long getCapacity() {
                return 50000;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                if (side == null) return 300;
                return side == getBlockState().getValue(AbstractMachineblock.FACING) ? 300 : 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return 300;
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
            }
        };
        this.inventory = NonNullList.withSize(12, ItemStack.EMPTY);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ModBlastFurnaceBlockEntity.this.progress;
                    case 1 -> ModBlastFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: ModBlastFurnaceBlockEntity.this.progress = value;
                    case 1: ModBlastFurnaceBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public void updateHeatInput() {
        receivingHeat = false;

        Direction direction = getBlockState().getValue(AbstractMachineblock.FACING);
        HeatStorage input = HeatStorage.SIDED.find(level, worldPosition.relative(direction), direction.getOpposite());
        if (input == null) return;

        try (Transaction tx = Transaction.openOuter()) {
            long moved = input.extract(1, tx);

            if (moved > 0) {
                heatContainer.getSideStorage(null).insert(moved, tx);
                receivingHeat = true;
                tx.commit();
            }
        }
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;

        if (hasFluidStackInFluidSlot()) {
            fillUpFluidTank();
        }

        updateHeatInput();

        long cap = heatContainer.getSideStorage(null).getCapacity();
        long stored = heatContainer.getSideStorage(null).getAmount();
        boolean heated = stored >= cap;

        if (!receivingHeat && stored > 0) {
            try (Transaction tx = Transaction.openOuter()) {
                heatContainer.getSideStorage(null).extract(Math.min(20, stored), tx);
                tx.commit();
            }
        }

        if (heated && hasRecipe() && canInsertIntoOutputSlot()) {
            increaseCraftingProgress();
            setChanged(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }

        boolean isWorking = heated && hasRecipe();

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

    @Override
    public boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    @Override
    public void resetProgress() {
        this.progress = 0;
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
                ? new ItemStack(GrowableOresItems.EMPTY_CELL)
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

    @Override
    protected void craftItem() {
        ModBlastFurnaceCraftingRecipe recipe = (ModBlastFurnaceCraftingRecipe) getCurrentRecipe().get().value();
        this.removeItem(INPUT_SLOT, recipe.requiredCount());
        insertOutput(OUTPUT_SLOT, recipe.output().create());
        recipe.output2().ifPresent(output -> insertOutput(OUTPUT_SLOT_2, output.create()));
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

        if (slot != INPUT_SLOT && slot != FLUID_ITEM_SLOT) {
            return false;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(AbstractMachineblock.FACING);
        return switch (slot) {
            case INPUT_SLOT, SECOND_INPUT_SLOT -> side != localDir.getOpposite(); // If not Up/Down/Back, it must be Front/Left/Right
            case FLUID_ITEM_SLOT -> side == localDir.getClockWise(); // Left
            default -> false; // Fallback
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if ((slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2) || side == Direction.UP) {
            return false;
        }

        if (side == Direction.DOWN) {
            return true;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(AbstractMachineblock.FACING);
        return side == localDir.getOpposite() || side == localDir.getClockWise();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT, FLUID_ITEM_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT, OUTPUT_SLOT_2};
        }
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> opt = getCurrentRecipe();
        if (opt.isEmpty()) return false;

        var recipeHolder = opt.get();
        var recipe = recipeHolder.value();
        ModBlastFurnaceCraftingRecipe washingRecipe = (ModBlastFurnaceCraftingRecipe) recipe;

        if (!canInsertIntoSlot(OUTPUT_SLOT, washingRecipe.output().create())) return false;
        if (washingRecipe.output2().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_2, washingRecipe.output2().get().create())) return false;

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
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Blast_Furnace);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.MOD_BLAST_FURNACE.type;
    }


    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ModBlastFurnaceScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("blast_furnace_machine.progress", progress);
        nbt.putInt("blast_furnace_machine.max_progress", maxProgress);
        nbt.putLong("blast_furnace_machine.heat", heatContainer.amount);
        SingleVariantStorage.writeValue(fluidContainer, FluidVariant.CODEC, nbt);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getIntOr("blast_furnace_machine.progress", 0);
        maxProgress = nbt.getIntOr("blast_furnace_machine.max_progress", maxProgress);
        heatContainer.amount = nbt.getLongOr("blast_furnace_machine.heat", 0);
        SingleVariantStorage.readValue(fluidContainer, FluidVariant.CODEC, FluidVariant::blank, nbt);
        super.loadAdditional(nbt);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveCustomOnly(registryLookup);
    }

}
