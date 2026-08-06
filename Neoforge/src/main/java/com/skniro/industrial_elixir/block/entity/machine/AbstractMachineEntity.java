package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.api.block.MachineRecipeProvider;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.BasePowerBlockBlockEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMachineEntity extends BasePowerBlockBlockEntity implements MachineRecipeProvider  {
    public int progress = 0;
    public int maxProgress = 72;
    protected int DEFAULT_MAX_PROGRESS = 72;
    protected final ContainerData propertyDelegate;
    private final Map<Direction, ResourceHandler<ItemResource>> itemHandlers = new EnumMap<>(Direction.class);

    public AbstractMachineEntity(BlockEntityType entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
        for(Direction direction : Direction.values()) {
            itemHandlers.put(direction, new WorldlyContainerWrapper(this, direction));
        }
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbstractMachineEntity.this.progress;
                    case 1 -> AbstractMachineEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: AbstractMachineEntity.this.progress = value;
                    case 1: AbstractMachineEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                return getMachineCapacity();
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                return getEffectiveTier().getMaxInput();
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                if (side == null) return getEffectiveTier().getMaxOutput();
                return 0;
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
            }
        };
    }


    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        Direction localDir = this.getLevel().getBlockState(worldPosition).getValue(AbstractMachineblock.FACING);

        if(side == null) {
            return false;
        }

        if(side == Direction.DOWN) {
            return false;
        }

        if(side == Direction.UP) {
            return slot == INPUT_SLOT;
        }

        return switch (localDir) {
            default -> //NORTH
                    side == Direction.NORTH && slot == INPUT_SLOT ||
                            side == Direction.EAST && slot == INPUT_SLOT ||
                            side == Direction.WEST && slot == INPUT_SLOT;
            case EAST ->
                    side.getCounterClockWise() == Direction.NORTH && slot == INPUT_SLOT ||
                            side.getCounterClockWise() == Direction.EAST && slot == INPUT_SLOT ||
                            side.getCounterClockWise() == Direction.WEST && slot == INPUT_SLOT;
            case SOUTH ->
                    side.getOpposite() == Direction.NORTH && slot == INPUT_SLOT ||
                            side.getOpposite()  == Direction.EAST && slot == INPUT_SLOT ||
                            side.getOpposite()  == Direction.WEST && slot == INPUT_SLOT;
            case WEST ->
                    side.getClockWise() == Direction.NORTH && slot == INPUT_SLOT ||
                            side.getClockWise() == Direction.EAST && slot == INPUT_SLOT ||
                            side.getClockWise() == Direction.WEST && slot == INPUT_SLOT;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        Direction localDir = this.getLevel().getBlockState(this.worldPosition).getValue(AbstractMachineblock.FACING);

        if(side == Direction.UP) {
            return false;
        }

        // Down extract
        if(side == Direction.DOWN) {
            return slot == OUTPUT_SLOT;
        }

        // backside extract
        // right extract
        return switch (localDir) {
            default ->  side == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side == Direction.EAST && slot == OUTPUT_SLOT;

            case EAST -> side.getCounterClockWise() == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side.getCounterClockWise() == Direction.EAST && slot == OUTPUT_SLOT;

            case SOUTH ->  side.getOpposite() == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side.getOpposite() == Direction.EAST && slot == OUTPUT_SLOT;

            case WEST -> side.getClockWise() == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side.getClockWise() == Direction.EAST && slot == OUTPUT_SLOT;
        };
    }

    public ItemStack getRenderStack() {
            return this.getItem(INPUT_SLOT);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("abstract_machine.progress", progress);
        nbt.putInt("abstract_machine.max_progress", maxProgress);
        nbt.putLong("abstract_machine.energy", energyContainer.amount);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getIntOr("abstract_machine.progress", 0);
        maxProgress = nbt.getIntOr("abstract_machine.max_progress", DEFAULT_MAX_PROGRESS);
        energyContainer.amount = nbt.getLongOr("abstract_machine.energy", 300);
        super.loadAdditional(nbt);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if(world.isClientSide()) {
            return;
        }
        charge(ENERGY_ITEM_SLOT);

        if (hasRecipe() && canInsertIntoOutputSlot()) {
            increaseCraftingProgress();
            useEnergyForCrafting();
            setChanged(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }

        boolean isWorking = hasRecipe() && hasEnoughEnergyToCraft();
        if (state.getValue(AbstractMachineblock.LIT) != isWorking) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, isWorking), 3);
        }
    }

    public void useEnergyForCrafting() {
        long baseUse = getCraftEnergyCost() / 20;
        long effectiveUse = (long)(baseUse * getEnergyDemandMultiplier());
        try (Transaction tx = Transaction.openRoot()) {
            energyContainer.getSideStorage(null).extract(effectiveUse, tx);
            tx.commit();
        }
    }


    public void resetProgress() {
        this.progress = 0;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
    }

    protected void craftItem() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output().create();
        this.removeItem(INPUT_SLOT, recipe.get().value().requiredCount());
        if (this.getItem(OUTPUT_SLOT).isEmpty()) {
            this.setItem(OUTPUT_SLOT, output.copy());
        } else {
            this.getItem(OUTPUT_SLOT).grow(output.getCount());
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT};
        }
    }
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT;
    }

    public boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    // 处理速度受 Overclocker 升级影响
    public void increaseCraftingProgress() {
        double speedMult = getProcessTimeMultiplier();
        this.progress += (int) speedMult;
    }

    protected boolean canInsertIntoOutputSlot() {
        return this.getItem(OUTPUT_SLOT).isEmpty() ||
                this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output().create();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) && hasEnoughEnergyToCraft();
    }

    protected boolean hasEnoughEnergyToCraft() {
        return this.energyContainer.amount >= (long) (getCraftEnergyCost()/ 20) * maxProgress;
    }

    protected Optional<RecipeHolder<AbstractMachineCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor((RecipeType<AbstractMachineCraftingRecipe>) getCurrentRecipeType(), new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT)), this.getLevel());
    }

    protected boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getItem(OUTPUT_SLOT).isEmpty() || ItemStack.isSameItemSameComponents(this.getItem(OUTPUT_SLOT), output);
    }

    protected boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getItem(OUTPUT_SLOT).isEmpty() ? 64 : this.getItem(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = this.getItem(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Override
    public long getMachineCapacity() {
        long extra = getEnergyStorageUpgrade();
        return energyTier == EnergyTier.INFINITE ? Long.MAX_VALUE : 512 + extra;
    }

    @Override
    public long getCraftEnergyCost(){
        return ENERGY_CRAFTING_AMOUNT;
    }

    public ResourceHandler<ItemResource> getItemHandler(@Nullable Direction side) {

        return new ResourceHandler<>() {

            @Override
            public int size() {
                return inventory.size();
            }


            @Override
            public ItemResource getResource(int index) {
                ItemStack stack = inventory.get(index);

                if(stack.isEmpty())
                    return ItemResource.EMPTY;

                return ItemResource.of(stack);
            }


            @Override
            public long getAmountAsLong(int index) {
                return inventory.get(index).getCount();
            }


            @Override
            public long getCapacityAsLong(int index, ItemResource resource) {
                return inventory.get(index).getMaxStackSize();
            }


            @Override
            public boolean isValid(int index, ItemResource resource) {

                ItemStack stack = resource.toStack();

                return canPlaceItemThroughFace(
                        index,
                        stack,
                        side
                );
            }


            @Override
            public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
                if(!isValid(index, resource)) return 0;
                ItemStack insert = resource.toStack(amount);
                ItemStack old = inventory.get(index);
                int inserted = Math.min(amount, old.isEmpty() ? insert.getMaxStackSize() : old.getMaxStackSize() - old.getCount());
                if(inserted <= 0) return 0;
                inventorySnapshots.updateSnapshots(transaction);
                if(old.isEmpty()) {
                    inventory.set(index, insert.copyWithCount(inserted));
                } else {
                    old.grow(inserted);
                }
                setChanged();
                return inserted;
            }



            @Override
            public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {

                ItemStack current = inventory.get(index);

                if(current.isEmpty()) return 0;
                if(!ItemResource.of(current).equals(resource)) return 0;
                if(!canTakeItemThroughFace(index, current, side)) return 0;
                int extracted = Math.min(amount, current.getCount());
                if(extracted <= 0) return 0;
                inventorySnapshots.updateSnapshots(transaction);
                current.shrink(extracted);
                if(current.isEmpty()) inventory.set(index, ItemStack.EMPTY);
                setChanged();
                return extracted;
            }
        };
    }

    private final SnapshotJournal<ItemStack[]> inventorySnapshots = new SnapshotJournal<>() {

        @Override
        protected ItemStack[] createSnapshot() {
            ItemStack[] copy = new ItemStack[inventory.size()];
            for(int i = 0; i < inventory.size(); i++) {
                copy[i] = inventory.get(i).copy();
            }
            return copy;
        }

        @Override
        protected void revertToSnapshot(ItemStack[] snapshot) {
            for(int i = 0; i < snapshot.length; i++) {
                inventory.set(i, snapshot[i]);
            }
            setChanged();
        }
    };
}
