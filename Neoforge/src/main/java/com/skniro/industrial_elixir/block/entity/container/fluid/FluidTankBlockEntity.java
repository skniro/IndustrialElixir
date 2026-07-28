package com.skniro.industrial_elixir.block.entity.container.fluid;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.api.fluid.ContainerInfo;
import com.skniro.industrial_elixir.api.fluid.FluidOutputMap;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.screen.handler.container.fluid.FluidTankScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.skniro.industrial_elixir.api.fluid.FluidOutputMap.FLUID_CONTAINERS;

public class FluidTankBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory {
    private static final long BUCKET_VOLUME_MB = FluidConstants.BUCKET / 81;
    private static final long TANK_CAPACITY_MB = BUCKET_VOLUME_MB * 16;
    protected static final int FLUID_ITEM_SLOT = 0;
    protected static final int EMPTY_FLUID_ITEM_SLOT = 8;
    protected static final int OUTPUT_EMPTY_FLUID_ITEM_SLOT = 9;
    protected static final int FLUID_ITEM_OUTPUT_SLOT = 10;
    private record FluidInput(FluidVariant fluid, ItemStack remainder) {}
    public final NonNullList<ItemStack> inventory = NonNullList.withSize(11, ItemStack.EMPTY);
    protected final ContainerData propertyDelegate;
    public SingleVariantStorage<FluidVariant> fluidContainer = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return TANK_CAPACITY_MB;
        }

        @Override
        protected void onFinalCommit() {
            setChanged(level, getBlockPos(), getBlockState());
            getLevel().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };

    public FluidTankBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.FLUID_TANK_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData(){

            @Override
            public int get(int dataId) {
                return 0;
            }

            @Override
            public void set(int dataId, int value) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        boolean worked = false;

        if (hasFluidStackInFluidSlot()) {
            long amount = fluidContainer.getAmount();

            fillUpFluidTank();

            worked = amount != fluidContainer.getAmount();
        }

        if (!worked) {
            long amount = fluidContainer.getAmount();

            fillFluidContainer();

            worked = amount != fluidContainer.getAmount();
        }

        if (worked) {
            setChanged(level, pos, state);
        }
    }

    public float getFillRatio() {
        if (fluidContainer.amount <= 0) return 0.0f;
        return Math.min(1.0f, (float) fluidContainer.amount / (float) fluidContainer.getCapacity());
    }

    public FluidVariant getFluidVariant() {
        return fluidContainer.variant;
    }

    public long getAmount() {
        return fluidContainer.amount;
    }

    public InteractionResult interactBucket(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Fill tank from filled bucket
        if (stack.is(Items.WATER_BUCKET) || stack.is(Items.LAVA_BUCKET)) {
            Fluid fluid = stack.is(Items.WATER_BUCKET) ? Fluids.WATER : Fluids.LAVA;
            FluidVariant toInsert = FluidVariant.of(fluid);
            if (!fluidContainer.variant.isBlank() && !fluidContainer.variant.equals(toInsert)) {
                return InteractionResult.PASS;
            }
            long inserted;
            try (Transaction tx = Transaction.openOuter()) {
                inserted = fluidContainer.insert(toInsert, BUCKET_VOLUME_MB, tx);
                if (inserted == BUCKET_VOLUME_MB) {
                    tx.commit();
                }
            }
            if (inserted == BUCKET_VOLUME_MB) {
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        // Drain tank to empty bucket
        if (stack.is(Items.BUCKET) && fluidContainer.amount >= BUCKET_VOLUME_MB && !fluidContainer.variant.isBlank()) {
            ItemStack filled = new ItemStack(fluidContainer.variant.getFluid().getBucket());
            if (filled.isEmpty()) {
                return InteractionResult.PASS;
            }
            long extracted;
            try (Transaction tx = Transaction.openOuter()) {
                extracted = fluidContainer.extract(fluidContainer.variant, BUCKET_VOLUME_MB, tx);
                if (extracted == BUCKET_VOLUME_MB) {
                    tx.commit();
                }
            }
            if (extracted == BUCKET_VOLUME_MB) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, filled);
                    } else if (!player.getInventory().add(filled)) {
                        player.drop(filled, false);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean hasFluidStackInFluidSlot() {
        ItemStack inputStack = inventory.get(FLUID_ITEM_SLOT);
        if (getKnownFluidInput(inputStack) != null) {
            return true;
        }

        var itemStorage = FluidStorage.ITEM.find(inputStack, ContainerItemContext.withConstant(inputStack));
        return itemStorage != null
                && itemStorage.supportsExtraction()
                && itemStorage instanceof CombinedStorage<?, ?> combinedStorage
                && combinedStorage.parts.get(0) instanceof FullItemFluidStorage;
    }

    public void fillUpFluidTank() {
        ItemStack inputStack = inventory.get(FLUID_ITEM_SLOT);
        if (fillFromKnownContainer(inputStack)) {
            return;
        }

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
            long inserted = this.fluidContainer.insert(fluidStorage.getResource(), BUCKET_VOLUME_MB, transaction);
            if (inserted != BUCKET_VOLUME_MB) {
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

    private boolean fillFromKnownContainer(ItemStack inputStack) {
        FluidInput fluidInput = getKnownFluidInput(inputStack);
        if (fluidInput == null || !canAcceptFluid(fluidInput.fluid())) {
            return false;
        }

        if (inputStack.getItem() instanceof FluidCellItem && !canStoreCraftRemainder(inputStack, fluidInput.remainder())) {
            return false;
        }

        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = this.fluidContainer.insert(fluidInput.fluid(), BUCKET_VOLUME_MB, transaction);
            if (inserted != BUCKET_VOLUME_MB) {
                return false;
            }

            if (inputStack.getItem() instanceof FluidCellItem) {
                inputStack.shrink(1);
                storeCraftRemainder(fluidInput.remainder());
            } else {
                inventory.set(FLUID_ITEM_SLOT, fluidInput.remainder());
            }

            transaction.commit();
            return true;
        }
    }

    private FluidInput getKnownFluidInput(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        for (Map.Entry<Fluid, List<ContainerInfo>> entry : FLUID_CONTAINERS.entrySet()) {
            for (ContainerInfo info : entry.getValue()) {
                if (stack.is(info.fullItem())) {
                    return new FluidInput(FluidVariant.of(entry.getKey()), new ItemStack(info.emptyItem()));
                }
            }
        }

        return null;
    }

    private boolean canAcceptFluid(FluidVariant fluid) {
        return (fluidContainer.getResource().equals(fluid) || fluidContainer.isResourceBlank())
                && fluidContainer.getAmount() + BUCKET_VOLUME_MB <= fluidContainer.getCapacity();
    }

    private boolean canAcceptFluid(FullItemFluidStorage fluidStorage) {
        return canAcceptFluid(fluidStorage.getResource());
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

    public void fillFluidContainer() {
        if (fluidContainer.isResourceBlank() || fluidContainer.getAmount() < BUCKET_VOLUME_MB) {
            return;
        }

        Fluid fluid = fluidContainer.getResource().getFluid();
        ItemStack emptyContainerStack = inventory.get(OUTPUT_EMPTY_FLUID_ITEM_SLOT);
        if (emptyContainerStack.isEmpty()) {
            return;
        }

        ContainerInfo containerInfo = FluidOutputMap.getContainerInfo(fluid, emptyContainerStack.getItem());

        if (containerInfo == null) {
            return;
        }

        ItemStack fullContainer = new ItemStack(containerInfo.fullItem());

        if (!canStoreFilledContainer(fullContainer)) {
            return;
        }

        try (Transaction transaction = Transaction.openOuter()) {

            long extracted = fluidContainer.extract(
                    FluidVariant.of(fluid),
                    BUCKET_VOLUME_MB,
                    transaction
            );

            if (extracted != BUCKET_VOLUME_MB) {
                return;
            }

            emptyContainerStack.shrink(1);
            storeFilledContainer(fullContainer);

            transaction.commit();
        }
    }

    private boolean canStoreFilledContainer(ItemStack stack) {
        ItemStack output = inventory.get(FLUID_ITEM_OUTPUT_SLOT);

        return output.isEmpty()
                || (ItemStack.isSameItemSameComponents(output, stack)
                && output.getCount() + stack.getCount() <= output.getMaxStackSize());
    }

    private void storeFilledContainer(ItemStack stack) {
        ItemStack output = inventory.get(FLUID_ITEM_OUTPUT_SLOT);

        if (output.isEmpty()) {
            inventory.set(FLUID_ITEM_OUTPUT_SLOT, stack.copy());
        } else {
            output.grow(stack.getCount());
        }
    }


    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putLong("tank.amount", fluidContainer.amount);
        if (!fluidContainer.variant.isBlank()) {
            nbt.putInt("tank.fluid_id", BuiltInRegistries.FLUID.getId(fluidContainer.variant.getFluid()));
        } else {
            nbt.putInt("tank.fluid_id", -1);
        }
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        long amount = nbt.getLongOr("tank.amount", 0);
        int fluidId = nbt.getIntOr("tank.fluid_id", -1);
        if (amount <= 0 || fluidId < 0) {
            fluidContainer.variant = FluidVariant.blank();
            fluidContainer.amount = 0;
            return;
        }
        fluidContainer.variant = FluidVariant.of(BuiltInRegistries.FLUID.byId(fluidId));
        fluidContainer.amount = Math.min(amount, fluidContainer.getCapacity());
        StoragePreconditions.notBlankNotNegative(fluidContainer.variant, fluidContainer.amount);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Fluid_Tank);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FluidTankScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }
}
