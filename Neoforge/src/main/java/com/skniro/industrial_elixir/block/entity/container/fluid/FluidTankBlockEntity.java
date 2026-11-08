package com.skniro.industrial_elixir.block.entity.container.fluid;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.api.fluid.ContainerInfo;
import com.skniro.industrial_elixir.api.fluid.FluidOutputMap;
import com.skniro.industrial_elixir.api.fluid.SingleFluidStorage;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.machine.fluid.AbstractFluidMachineEntity;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.screen.handler.container.fluid.FluidTankScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.ContainerHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.nbt.NbtOps;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;


public class FluidTankBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    private static final int BUCKET_VOLUME_MB = 1000;
    private static final int TANK_CAPACITY_MB = BUCKET_VOLUME_MB * 16;
    protected static final int FLUID_ITEM_SLOT = 0;
    protected static final int EMPTY_FLUID_ITEM_SLOT = 8;
    protected static final int OUTPUT_EMPTY_FLUID_ITEM_SLOT = 9;
    protected static final int FLUID_ITEM_OUTPUT_SLOT = 10;
    public final NonNullList<ItemStack> inventory = NonNullList.withSize(11, ItemStack.EMPTY);
    protected final ContainerData propertyDelegate;
    public SingleFluidStorage fluidContainer = new SingleFluidStorage() {

        @Override
        protected int getCapacity(FluidStack variant) {
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

    /**
     * NeoForge menu opening: the client-side factories read the machine position from the
     * extra opening data. Without this override the buffer is empty and the client throws
     * a network protocol error while decoding the open-screen packet.
     */
    @Override
    public void writeClientSideData(AbstractContainerMenu menu, net.minecraft.network.RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.getBlockPos());
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
        } else {
            // Actively pull fluid from adjacent blocks that expose the standard Neoforge
            // fluid capability (e.g. fluid pipes, tanks) so the tank can be filled by pipes.
            worked = AbstractFluidMachineEntity.suckFluidFromAdjacent(level, worldPosition, fluidContainer, 100);
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

    public FluidStack getFluidVariant() {
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
            FluidStack toInsert = new FluidStack(fluid, BUCKET_VOLUME_MB);
            if (!fluidContainer.variant.isEmpty() && !fluidContainer.variant.isFluidEqual(toInsert)) {
                return InteractionResult.PASS;
            }
            int inserted = fluidContainer.insert(toInsert, BUCKET_VOLUME_MB, true);
            if (inserted == BUCKET_VOLUME_MB) {
                fluidContainer.insert(toInsert, BUCKET_VOLUME_MB, false);
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
        if (stack.is(Items.BUCKET) && fluidContainer.amount >= BUCKET_VOLUME_MB && !fluidContainer.variant.isEmpty()) {
            ItemStack filled = new ItemStack(fluidContainer.variant.getFluid().getBucket());
            if (filled.isEmpty()) {
                return InteractionResult.PASS;
            }
            int extracted = fluidContainer.extract(fluidContainer.variant, BUCKET_VOLUME_MB, true);
            if (extracted == BUCKET_VOLUME_MB) {
                fluidContainer.extract(fluidContainer.variant, BUCKET_VOLUME_MB, false);
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
        ItemStack stack = inventory.get(FLUID_ITEM_SLOT);
        if (stack.isEmpty()) return false;

        // Use the standard Neoforge fluid item capability to detect the fluid inside
        // any container (cells, buckets, tanks, etc.) instead of a hard-coded map.
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        return handler != null && !handler.getFluidInTank(0).isEmpty();
    }

    public void fillUpFluidTank() {
        ItemStack inputStack = inventory.get(FLUID_ITEM_SLOT);
        fillFromKnownContainer(inputStack);
    }

    private boolean fillFromKnownContainer(ItemStack inputStack) {
        FluidStack toInsert = null;
        Item remainderItem = null;
        for (var entry : FluidOutputMap.FLUID_CONTAINERS.entrySet()) {
            for (ContainerInfo info : entry.getValue()) {
                if (inputStack.is(info.fullItem())) {
                    toInsert = new FluidStack(entry.getKey(), BUCKET_VOLUME_MB);
                    remainderItem = info.emptyItem();
                    break;
                }
            }
            if (toInsert != null) break;
        }
        if (toInsert == null || !canAcceptFluid(toInsert)) return false;

        ItemStack remainder = new ItemStack(remainderItem);
        if (inputStack.getItem() instanceof FluidCellItem && !canStoreCraftRemainder(inputStack, remainder)) {
            return false;
        }

        if (this.fluidContainer.insert(toInsert, BUCKET_VOLUME_MB, true) != BUCKET_VOLUME_MB) {
            return false;
        }
        this.fluidContainer.insert(toInsert, BUCKET_VOLUME_MB, false);

        if (inputStack.getItem() instanceof FluidCellItem) {
            inputStack.shrink(1);
            storeCraftRemainder(remainder);
        } else {
            inventory.set(FLUID_ITEM_SLOT, remainder);
        }
        return true;
    }

    private boolean canAcceptFluid(FluidStack fluid) {
        return (fluidContainer.getResource(0).isFluidEqual(fluid) || fluidContainer.isResourceBlank())
                && fluidContainer.getAmount() + BUCKET_VOLUME_MB <= fluidContainer.getCapacity();
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

        Fluid fluid = fluidContainer.getResource(0).getFluid();
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

        int extracted = fluidContainer.extract(
                new FluidStack(fluid, BUCKET_VOLUME_MB),
                BUCKET_VOLUME_MB,
                true
        );

        if (extracted != BUCKET_VOLUME_MB) {
            return;
        }

        fluidContainer.extract(new FluidStack(fluid, BUCKET_VOLUME_MB), BUCKET_VOLUME_MB, false);

        emptyContainerStack.shrink(1);
        storeFilledContainer(fullContainer);
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
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        ContainerHelper.saveAllItems(nbt, inventory, registries);
        SingleFluidStorage.writeValue(fluidContainer, nbt);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        ContainerHelper.loadAllItems(nbt, inventory, registries);
        SingleFluidStorage.readValue(fluidContainer, nbt);
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
