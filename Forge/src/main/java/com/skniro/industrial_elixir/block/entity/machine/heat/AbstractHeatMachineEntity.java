package com.skniro.industrial_elixir.block.entity.machine.heat;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.api.block.MachineRecipeProvider;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleHeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleSidedHeatContainer;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import net.minecraft.world.MenuProvider;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractHeatMachineEntity extends BlockEntity implements MenuProvider, ImplementedInventory, ItemOwner, MachineRecipeProvider {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);
    private float rotation = 0;
    protected static final int FLUID_ITEM_SLOT = 0;
    protected static final int INPUT_SLOT = 1;
    protected static final int OUTPUT_SLOT = 2;
    protected static final int OUTPUT_SLOT_2 = 10;
    protected static final int heat_ITEM_SLOT = 3;
    protected static final int UPGRADE_START = 4;
    protected static final int UPGRADE_END = 7;
    protected static final int EMPTY_FLUID_ITEM_SLOT = 8;
    protected static final int SECOND_INPUT_SLOT = 9;
    protected static final int heat_CRAFTING_AMOUNT = 32;
    protected static final int heat_TRANSFER_AMOUNT = 32;

    public SimpleSidedHeatContainer heatContainer;

    protected final ContainerData propertyDelegate;
    public int progress = 0;
    public int maxProgress = 72;
    private final int DEFAULT_MAX_PROGRESS = 72;
    public boolean receivingHeat;

    public AbstractHeatMachineEntity(BlockEntityType entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);

        heatContainer = new SimpleSidedHeatContainer() {
            @Override
            public long getCapacity() {
                return 50000;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                return 300;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return 300;
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);

/*            if(!world.isClient()) {
                for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, new heatSyncS2CPayload(amount, getPos()));
                }
            }*/
            }
        };
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbstractHeatMachineEntity.this.progress;
                    case 1 -> AbstractHeatMachineEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: AbstractHeatMachineEntity.this.progress = value;
                    case 1: AbstractHeatMachineEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
    public void setChanged() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        super.setChanged();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("abstract_machine.progress", progress);
        nbt.putInt("abstract_machine.max_progress", maxProgress);
        nbt.putLong("abstract_machine.heat", heatContainer.amount);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getIntOr("abstract_machine.progress", 0);
        maxProgress = nbt.getIntOr("abstract_machine.max_progress", maxProgress);
        heatContainer.amount = nbt.getLongOr("abstract_machine.heat", 300);
        super.loadAdditional(nbt);
    }

    public void updateHeatInput() {
        receivingHeat = false;

        for (Direction dir : Direction.values()) {
            HeatStorage input = SimpleHeatStorage.SIDED.getCapability(level, worldPosition.relative(dir), null,null,dir.getOpposite());
            if (input == null) continue;

            try (Transaction tx = Transaction.openRoot()) {
                long moved = input.extract(1, tx);

                if (moved > 0) {
                    heatContainer.getSideStorage(null).insert(moved, tx);
                    receivingHeat = true;
                    tx.commit();
                }
            }
        }
    }

    public long getFreeSpace() {
        return this.heatContainer.getCapacity() - heatContainer.amount;
    }

    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
    }

    public HeatStorage getSideheatStorage(@Nullable Direction side) {
        return this.heatContainer.getSideStorage(side);
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

    public void increaseCraftingProgress() {
        this.progress++;
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
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) && hasEnoughheatToCraft();
    }

    protected boolean hasEnoughheatToCraft() {
        return this.heatContainer.amount >= (long) (heat_CRAFTING_AMOUNT/ 20) * maxProgress;
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


   @Nullable
   @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
}

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveCustomOnly(registryLookup);
    }

    @Override
    public Level level() {
        return this.level;
    }

    @Override
    public Vec3 position() {
        return Vec3.atCenterOf(this.getBlockPos());
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return this.getBlockState().getValue(AbstractMachineblock.FACING).getOpposite().getStepY();
    }

}
