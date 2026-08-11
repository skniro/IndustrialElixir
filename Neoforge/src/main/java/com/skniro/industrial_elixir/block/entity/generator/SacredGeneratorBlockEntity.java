package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleSidedHeatContainer;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.init.ReactorComponentItem;
import com.skniro.industrial_elixir.screen.handler.generator.SacredGeneratorScreenHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.Containers;
// ...existing code...
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SacredGeneratorBlockEntity extends NewBaseGeneratorBlockEntity {
    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int REACTOR_SLOT_COUNT = GRID_WIDTH * GRID_HEIGHT; // 9

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(REACTOR_SLOT_COUNT + 5, ItemStack.EMPTY);
    protected final ContainerData propertyDelegate;

    // Heat storage (client-visible) and generation
    public final SimpleSidedHeatContainer heatContainer;
    private int generation;
    private boolean active;
    private static final int ENERGY_PER_PULSE = 128;
    private static final int ENERGY_TRANSFER_AMOUNT = 320;
    private static final int DURABILITY_TICK_INTERVAL = 40; // 1 durability point per 2 seconds
    private int durabilityTickCounter = 0;

    public SacredGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.SACRED_GENERATOR_BE.get(), pos, state);

        this.heatContainer = new SimpleSidedHeatContainer() {
            @Override
            public long getCapacity() {
                return 5000;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                if (side == null) return 200;
                return 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return 200;
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
            }
        };

        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> (int) Math.min(Integer.MAX_VALUE, heatContainer.getSideStorage(null).getAmount());
                    case 1 -> (int) Math.min(Integer.MAX_VALUE, heatContainer.getSideStorage(null).getCapacity());
                    case 2 -> SacredGeneratorBlockEntity.this.generation;
                    case 3 -> (int) Math.min(Integer.MAX_VALUE, SacredGeneratorBlockEntity.this.energyContainer.amount);
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                if (index == 2) {
                    SacredGeneratorBlockEntity.this.generation = value;
                } else if (index == 3) {
                    SacredGeneratorBlockEntity.this.energyContainer.amount = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putLong("sacred_generator.energy", energyContainer.amount);
        nbt.putLong("sacred_generator.heat", heatContainer.amount);
        nbt.putLong("sacred_generator.max_heat", heatContainer.getSideStorage(null).getCapacity());
        nbt.putInt("sacred_generator.generation", generation);
        nbt.putBoolean("sacred_generator.active", active);
        nbt.putInt("sacred_generator.durability_tick_counter", durabilityTickCounter);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        energyContainer.amount = nbt.getLongOr("sacred_generator.energy", 0);
        heatContainer.amount = nbt.getLongOr("sacred_generator.heat", 0);
        generation = nbt.getIntOr("sacred_generator.generation", 0);
        active = nbt.getBooleanOr("sacred_generator.active", false);
        durabilityTickCounter = nbt.getIntOr("sacred_generator.durability_tick_counter", 0);
        super.loadAdditional(nbt);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.SacredReactor);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new SacredGeneratorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world == null || world.isClientSide()) return;

        // charge batteries and push energy out
        discharge(10);
        discharge(11);
        discharge(12);
        discharge(13);
        pushEnergyToNeighbours();

        generation = 0;

        // require redstone signal to run
        boolean powered = world.hasNeighborSignal(pos);

        // center is the fuel rod (index 4)
        int centerIndex = (GRID_WIDTH * GRID_HEIGHT) / 2;
        ItemStack center = inventory.get(centerIndex);
        if (powered && center.getItem() instanceof ReactorComponentItem component && component.isFuelRod()) {
            int pulses = component.getRodCount();
            generation += pulses * ENERGY_PER_PULSE;
            long heatGenerated = pulses * 4L;
            try (Transaction tx = Transaction.openRoot()) {
                heatContainer.getSideStorage(null).insert(heatGenerated, tx);
                tx.commit();
            }
            // fuel rod is NOT consumed or damaged
        }

        if (generation > 0) insertGeneratedEnergy(generation);

        // Cooling: outer ring slots provide cooling and take durability
        coolOuterRing();

        active = generation > 0 && powered;

        // update block lit state if present
        Block block = getBlockState().getBlock();
        if (block instanceof AbstractMachineblock) {
            boolean lit = active;
            if (state.hasProperty(AbstractMachineblock.LIT) && state.getValue(AbstractMachineblock.LIT) != lit) {
                world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, lit), 3);
            }
        }
        // if heat reached capacity -> explode (100%)
        long heat = heatContainer.getSideStorage(null).getAmount();
        long cap = heatContainer.getSideStorage(null).getCapacity();
        if (heat >= cap && cap > 0) {
            meltdown(world, pos);
            return;
        }

        setChanged(world, pos, state);
    }

    private void meltdown(Level world, BlockPos pos) {
        // drop inventory and explode, similar to NuclearReactor
        Containers.dropContents(world, pos, this);
        world.removeBlock(pos, false);
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 100.0f, true, Level.ExplosionInteraction.BLOCK);
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 100.0f, true, Level.ExplosionInteraction.BLOCK);
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 100.0f, true, Level.ExplosionInteraction.BLOCK);
    }

    public void pushEnergyToNeighbours() {
        if (energyContainer.amount <= 0) return;
        for (Direction direction : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.getCapability(level, worldPosition.relative(direction), null,null, direction.getOpposite());
            if (target == null) continue;
            EnergyStorageUtil.move(this.energyContainer.getSideStorage(direction), target, ENERGY_TRANSFER_AMOUNT, null);
        }
    }


    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ReactorComponentItem;
    }

    private void fillUpOnEnergy() {
        try (Transaction transaction = Transaction.openRoot()) {
            this.energyContainer.getSideStorage(null).insert(((AbstractMachineblock)getBlockState().getBlock()).getEnergyTier().getMaxInput(), transaction);
            transaction.commit();
        }
    }
    private void insertGeneratedEnergy(int amount) {
        try (Transaction transaction = Transaction.openRoot()) {
            energyContainer.getSideStorage(null).insert(amount, transaction);
            transaction.commit();
        }
    }

    private void coolOuterRing() {
        durabilityTickCounter++;

        // Only apply durability damage every DURABILITY_TICK_INTERVAL (40) ticks = 2 seconds
        boolean shouldApplyDamage = durabilityTickCounter >= DURABILITY_TICK_INTERVAL;
        if (shouldApplyDamage) {
            durabilityTickCounter = 0;
        }

        int[] outer = new int[]{0,1,2,3,5,6,7,8};
        for (int slot : outer) {
            ItemStack stack = inventory.get(slot);
            if (!(stack.getItem() instanceof ReactorComponentItem component)) continue;

            int cooling = 0;

            switch (component.getComponentType()) {
                case COOLANT_CELL, HEAT_VENT, ADVANCED_HEAT_VENT, OVERCLOCKED_HEAT_VENT -> {
                    cooling = component.getCoolingPerTick(stack);
                }
                case HEAT_EXCHANGER -> {
                    cooling = component.getHeatExchangerCoolingAmount();
                }
                default -> {}
            }

            if (cooling > 0 && heatContainer.getSideStorage(null).getAmount() > 0) {
                int actualCooling = (int) Math.min(heatContainer.getSideStorage(null).getAmount(), cooling);
                try (Transaction tx = Transaction.openRoot()) {
                    heatContainer.getSideStorage(null).extract(actualCooling, tx);
                    tx.commit();
                }
                // Apply 1 durability damage only once every 40 ticks
                if (shouldApplyDamage) {
                    switch (component.getComponentType()) {
                        case COOLANT_CELL -> {
                            damageOrConsume(stack, slot,component.getCoolingPerTick(stack));
                        }
                        case  HEAT_VENT, ADVANCED_HEAT_VENT, OVERCLOCKED_HEAT_VENT -> {
                            damageOrConsume(stack, slot,1);
                        }
                        default -> {}
                    }

                }
            }
        }
    }

    public void discharge(int slot) {
        if (this.level != null) {
            if (!this.level.isClientSide()) {
                if (!this.getOptionalInventory().isEmpty()) {
                    Container inventory = this.getOptionalInventory().get();
                    ItemStack stack = inventory.getItem(slot);
                    EnergyStorage itemEnergyStorage = stack.getCapability(EnergyStorage.ITEM, ItemAccess.forStack(stack));
                    EnergyStorageUtil.move(this.getSideEnergyStorage(null), itemEnergyStorage, Long.MAX_VALUE, null);
                }
            }
        }
    }

    private void damageOrConsume(ItemStack stack, int slot, int amount) {
        if (!stack.isDamageableItem()) return;
        stack.setDamageValue(stack.getDamageValue() + amount);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            inventory.set(slot, ItemStack.EMPTY);
        }
    }

    public int getHeat() {
        return (int) Math.min(Integer.MAX_VALUE, heatContainer.getSideStorage(null).getAmount());
    }

    public long getHeatCapacity() {
        return heatContainer.getSideStorage(null).getCapacity();
    }

    public int getGeneration() {
        return generation;
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


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}