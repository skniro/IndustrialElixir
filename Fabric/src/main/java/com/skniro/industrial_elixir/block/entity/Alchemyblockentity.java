package com.skniro.industrial_elixir.block.entity;
import java.util.Optional;

import com.skniro.industrial_elixir.api.ImplementedInventory;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.init.machine.Alchemyblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.init.ItemUpgradeModule;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipe;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipeInput;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.AlchemyBlockScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Alchemyblockentity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory, ItemOwner {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    private float rotation = 0;
    private static final int FLUID_ITEM_SLOT = 0;
    private static final int INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int ENERGY_ITEM_SLOT = 3;
    private static final int UPGRADE_START = 4;
    private static final int UPGRADE_END = 7;
    private static final int ENERGY_CRAFTING_AMOUNT = 32;
    private static final int ENERGY_TRANSFER_AMOUNT = 32;
    protected final EnergyTier energyTier;

    public SimpleSidedEnergyContainer energyContainer;

    protected final ContainerData propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private final int DEFAULT_MAX_PROGRESS = 72;

    public Alchemyblockentity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.ALCHEMY_BLOCK_ENTITY, pos, state);
        this.energyTier = EnergyTier.TIER1;

        energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                long extra = getEnergyStorageUpgrade();
                return energyTier == EnergyTier.INFINITE ? Long.MAX_VALUE : 300 + extra;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                return getEffectiveTier().getMaxInput();
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return getEffectiveTier().getMaxOutput();
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);

/*            if(!world.isClient()) {
                for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, new EnergySyncS2CPayload(amount, getPos()));
                }
            }*/
            }
        };
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> Alchemyblockentity.this.progress;
                    case 1 -> Alchemyblockentity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: Alchemyblockentity.this.progress = value;
                    case 1: Alchemyblockentity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    // 计算处理速度倍率
    private double getProcessTimeMultiplier() {
        double multiplier = 1.0;
        for (int i = UPGRADE_START; i <= UPGRADE_END; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgradeModule u) {
                multiplier = u.getProcessTimeMultiplier(stack);
            }
        }
        return multiplier;
    }

    // 计算能耗倍率
    private double getEnergyDemandMultiplier() {
        double multiplier = 1.0;
        for (int i = UPGRADE_START; i <= UPGRADE_END; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgradeModule u) {
                multiplier = u.getEnergyDemandMultiplier(stack);
            }
        }
        return multiplier;
    }

    // 计算额外储能
    private int getEnergyStorageUpgrade() {
        int total = 0;
        for (int i = UPGRADE_START; i <= UPGRADE_END; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgradeModule u) {
                total += u.getExtraEnergyStorage(stack);
            }
        }
        return total;
    }

    // 获取有效的 EnergyTier（支持 Transformer 升级）
    private EnergyTier getEffectiveTier() {
        int tierBoost = 0;
        for (int i = UPGRADE_START; i <= UPGRADE_END; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgradeModule u) {
                tierBoost += u.getTierIncrease(stack);
            }
        }
        int newTier = Math.min(energyTier.ordinal() + tierBoost, EnergyTier.values().length - 1);
        return EnergyTier.values()[newTier];
    }


    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        Direction localDir = this.getLevel().getBlockState(worldPosition).getValue(Alchemyblock.FACING);

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
        Direction localDir = this.getLevel().getBlockState(this.worldPosition).getValue(Alchemyblock.FACING);

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
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.CaneConverter);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new AlchemyBlockScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("cane_converter.progress", progress);
        nbt.putInt("cane_converter.max_progress", maxProgress);
        nbt.putLong("cane_converter.energy", energyContainer.amount);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getIntOr("cane_converter.progress", 0);
        maxProgress = nbt.getIntOr("cane_converter.max_progress", 72);
        energyContainer.amount = nbt.getLongOr("cane_converter.energy", 300);
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
    }

    public long getFreeSpace() {
        return this.energyContainer.getCapacity() - energyContainer.amount;
    }

    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
    }

    public void charge(int slot) {
        if (this.level != null) {
            if (!this.level.isClientSide()) {
                long chargeEnergy = Math.min(this.getFreeSpace(), energyTier.getMaxInput());
                if (chargeEnergy > 0L) {
                    if (!this.getOptionalInventory().isEmpty()) {
                        Container inventory = this.getOptionalInventory().get();
                        EnergyStorageUtil.move(ContainerItemContext.ofSingleSlot(ContainerStorage.of(inventory, null).getSlots().get(slot)).find(EnergyStorage.ITEM), this.getSideEnergyStorage(null), Long.MAX_VALUE, null);
                    }
                }
            }
        }
    }

    public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
        return this.energyContainer.getSideStorage(side);
    }

    private void useEnergyForCrafting() {
        long baseUse = ENERGY_CRAFTING_AMOUNT / 20;
        long effectiveUse = (long)(baseUse * getEnergyDemandMultiplier());
        try (Transaction tx = Transaction.openOuter()) {
            energyContainer.getSideStorage(null).extract(effectiveUse, tx);
            tx.commit();
        }
    }


    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
    }

    private void craftItem() {
        Optional<RecipeHolder<AlchemyCraftingRecipe>> recipe = getCurrentRecipe();
        this.removeItem(INPUT_SLOT, 1);
        this.setItem(OUTPUT_SLOT, new ItemStack(recipe.get().value().output().item(),
                this.getItem(OUTPUT_SLOT).getCount() + recipe.get().value().output().count()));
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

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    // 处理速度受 Overclocker 升级影响
    private void increaseCraftingProgress() {
        double speedMult = getProcessTimeMultiplier();
        this.progress += (int) speedMult;
    }

    private boolean canInsertIntoOutputSlot() {
        return this.getItem(OUTPUT_SLOT).isEmpty() ||
                this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<AlchemyCraftingRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output().create();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) && hasEnoughEnergyToCraft();
    }

    private boolean hasEnoughEnergyToCraft() {
        return this.energyContainer.amount >= (long) (ENERGY_CRAFTING_AMOUNT/ 20) * maxProgress;
    }

    private Optional<RecipeHolder<AlchemyCraftingRecipe>> getCurrentRecipe() {
        return this.getLevel().getServer().getRecipeManager()
                .getRecipeFor(AlchemyRecipeType.CANE_CONVERTER.type, new AlchemyCraftingRecipeInput(inventory.get(INPUT_SLOT)), this.getLevel());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
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
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Level level() {
        return this.level;
    }

    @Override
    public Vec3 position() {
        return this.getBlockPos().getCenter();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return this.getBlockState().getValue(Alchemyblock.FACING).getOpposite().getStepY();
    }

    public EnergyTier getEnergyTier(){
        return energyTier;
    }
}
