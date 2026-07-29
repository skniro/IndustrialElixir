package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.generator.NuclearReactorBlock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.init.ReactorComponentItem;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import com.skniro.industrial_elixir.screen.handler.generator.NuclearReactorScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
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
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class NuclearReactorBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory {
    public static final int GRID_WIDTH = 9;
    public static final int GRID_HEIGHT = 6;
    public static final int REACTOR_SLOT_COUNT = GRID_WIDTH * GRID_HEIGHT;
    public static final int BATTERY_SLOT = REACTOR_SLOT_COUNT;
    private static final int BASE_MAX_HEAT = 10000;
    private static final int ENERGY_PER_PULSE = 20;
    private static final int ENERGY_TRANSFER_AMOUNT = 2048;
    private static final int HEAT_VENT_ADJACENCY_BONUS = 4;
    private static final int HEAT_VENT_COOLANT_ADJACENCY_BONUS = 2;
    private static final int HEAT_VENT_VENT_ADJACENCY_BONUS = 1;

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(REACTOR_SLOT_COUNT + 1, ItemStack.EMPTY);
    public final com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer energyContainer;
    private int heat;
    private int maxHeat = BASE_MAX_HEAT;
    private int generation;
    private boolean active;
    private static final int DURABILITY_TICK_INTERVAL = 40; // 1 durability point per 2 seconds
    private int durabilityTickCounter = 0;

    private final ContainerData propertyDelegate;

    public NuclearReactorBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.NUCLEAR_REACTOR_BE.get(), pos, state);
        this.energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                return 10000000;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                return side == null ? EnergyTier.TIER4.getMaxInput() : 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                return EnergyTier.TIER4.getMaxOutput();
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        };
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> NuclearReactorBlockEntity.this.heat;
                    case 1 -> NuclearReactorBlockEntity.this.maxHeat;
                    case 2 -> NuclearReactorBlockEntity.this.generation;
                    case 3 -> (int) Math.min(Integer.MAX_VALUE, NuclearReactorBlockEntity.this.energyContainer.amount);
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NuclearReactorBlockEntity.this.heat = value;
                    case 1 -> NuclearReactorBlockEntity.this.maxHeat = value;
                    case 2 -> NuclearReactorBlockEntity.this.generation = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, inventory);
        output.putInt("nuclear_reactor.heat", heat);
        output.putInt("nuclear_reactor.max_heat", maxHeat);
        output.putInt("nuclear_reactor.generation", generation);
        output.putLong("nuclear_reactor.energy", energyContainer.amount);
        output.putBoolean("nuclear_reactor.active", active);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        ContainerHelper.loadAllItems(input, inventory);
        heat = input.getIntOr("nuclear_reactor.heat", 0);
        maxHeat = input.getIntOr("nuclear_reactor.max_heat", BASE_MAX_HEAT);
        generation = input.getIntOr("nuclear_reactor.generation", 0);
        energyContainer.amount = input.getLongOr("nuclear_reactor.energy", 0);
        active = input.getBooleanOr("nuclear_reactor.active", false);
        super.loadAdditional(input);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        pushEnergyToNeighbours();
        maxHeat = calculateMaxHeat();
        generation = 0;

        int heatGenerated = 0;
        for (int slot = 0; slot < REACTOR_SLOT_COUNT; slot++) {
            ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof ReactorComponentItem component && component.isFuelRod()) {
                int pulses = component.getRodCount() * (1 + getAdjacentComponentEffect(slot, true));
                generation += pulses * ENERGY_PER_PULSE;
                heatGenerated += pulses * pulses * 4;
                damageOrConsume(stack, slot, 1);
            }
        }

        if (generation > 0) {
            insertGeneratedEnergy(generation);
        }

        heat += heatGenerated;
        coolReactor();
        active = generation > 0;

        if (heat >= maxHeat) {
            meltdown(level, pos);
            return;
        }

        if (state.getValue(NuclearReactorBlock.LIT) != active) {
            level.setBlock(pos, state.setValue(NuclearReactorBlock.LIT, active), Block.UPDATE_ALL);
        }
        setChanged(level, pos, state);
    }

    private void insertGeneratedEnergy(int amount) {
        try (Transaction transaction = Transaction.openRoot()) {
            energyContainer.getSideStorage(null).insert(amount, transaction);
            transaction.commit();
        }
    }

    private void pushEnergyToNeighbours() {
        if (energyContainer.amount <= 0) {
            return;
        }
        for (Direction direction : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.find(level, worldPosition.relative(direction), direction.getOpposite());
            if (target == null) {
                continue;
            }
            EnergyStorageUtil.move(energyContainer.getSideStorage(direction), target, ENERGY_TRANSFER_AMOUNT, null);
        }
    }

    private int calculateMaxHeat() {
        int value = BASE_MAX_HEAT;
        for (int slot = 0; slot < REACTOR_SLOT_COUNT; slot++) {
            ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof ReactorComponentItem component) {
                value += component.getExtraMaxHeat();
            }
        }
        return value;
    }

    private void coolReactor() {
        durabilityTickCounter++;
        
        // Only apply durability damage every DURABILITY_TICK_INTERVAL (40) ticks = 2 seconds
        boolean shouldApplyDamage = durabilityTickCounter >= DURABILITY_TICK_INTERVAL;
        if (shouldApplyDamage) {
            durabilityTickCounter = 0;
        }
        
        for (int slot = 0; slot < REACTOR_SLOT_COUNT && heat > 0; slot++) {
            ItemStack stack = inventory.get(slot);
            if (!(stack.getItem() instanceof ReactorComponentItem component)) {
                continue;
            }

            int cooling = 0;

            if (component.getComponentType() == ReactorComponentItem.ComponentType.COOLANT_CELL) {
                cooling = component.getCoolingPerTick(stack);
            } else if (component.getComponentType() == ReactorComponentItem.ComponentType.HEAT_VENT) {
                cooling = getHeatVentEffectiveCooling(slot);
            } else if (component.getComponentType() == ReactorComponentItem.ComponentType.HEAT_EXCHANGER) {
                cooling = getHeatExchangerEffectiveCooling(slot);
            }

            if (cooling > 0) {
                int actualCooling = Math.min(heat, cooling);
                heat -= actualCooling;
                // Apply 1 durability damage only once every 40 ticks
                if (shouldApplyDamage) {
                    damageOrConsume(stack, slot, 1);
                }
            }
        }
    }

    private boolean isNeutronReflectorAt(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        ItemStack stack = inventory.get(y * GRID_WIDTH + x);
        return stack.getItem() instanceof ReactorComponentItem component && component.getComponentType() == ReactorComponentItem.ComponentType.NEUTRON_REFLECTOR;
    }

    private boolean isHeatVentAt(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        ItemStack stack = inventory.get(y * GRID_WIDTH + x);
        return stack.getItem() instanceof ReactorComponentItem component && component.getComponentType() == ReactorComponentItem.ComponentType.HEAT_VENT;
    }

    private boolean isCoolantCellAt(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        ItemStack stack = inventory.get(y * GRID_WIDTH + x);
        return stack.getItem() instanceof ReactorComponentItem component && component.getComponentType() == ReactorComponentItem.ComponentType.COOLANT_CELL;
    }

    private boolean isHeatExchangerAt(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        ItemStack stack = inventory.get(y * GRID_WIDTH + x);
        return stack.getItem() instanceof ReactorComponentItem component && component.getComponentType() == ReactorComponentItem.ComponentType.HEAT_EXCHANGER;
    }

    private int getHeatVentEffectiveCooling(int slot) {
        ItemStack stack = inventory.get(slot);
        if (!(stack.getItem() instanceof ReactorComponentItem component) || component.getComponentType() != ReactorComponentItem.ComponentType.HEAT_VENT) {
            return 0;
        }

        int effectiveCooling = component.getHeatVentCoolingAmount();
        int x = slot % GRID_WIDTH;
        int y = slot / GRID_WIDTH;

        int[][] adjacentOffsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] offset : adjacentOffsets) {
            int adjX = x + offset[0];
            int adjY = y + offset[1];

            if (isFuelAt(adjX, adjY)) {
                effectiveCooling += HEAT_VENT_ADJACENCY_BONUS;
            } else if (isCoolantCellAt(adjX, adjY)) {
                effectiveCooling += HEAT_VENT_COOLANT_ADJACENCY_BONUS;
            } else if (isHeatVentAt(adjX, adjY)) {
                effectiveCooling += HEAT_VENT_VENT_ADJACENCY_BONUS;
            } else if (isHeatExchangerAt(adjX, adjY)) {
                effectiveCooling += HEAT_VENT_VENT_ADJACENCY_BONUS;
            }
        }
        return effectiveCooling;
    }

    private int getHeatExchangerEffectiveCooling(int slot) {
        ItemStack stack = inventory.get(slot);
        if (!(stack.getItem() instanceof ReactorComponentItem component) || component.getComponentType() != ReactorComponentItem.ComponentType.HEAT_EXCHANGER) {
            return 0;
        }

        int effectiveCooling = component.getHeatExchangerCoolingAmount();
        int x = slot % GRID_WIDTH;
        int y = slot / GRID_WIDTH;

        int[][] adjacentOffsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] offset : adjacentOffsets) {
            int adjX = x + offset[0];
            int adjY = y + offset[1];

            if (isFuelAt(adjX, adjY)) {
                effectiveCooling += component.getHeatExchangerTransferAmount();
            } else if (isCoolantCellAt(adjX, adjY) || isHeatVentAt(adjX, adjY)) {
                effectiveCooling += component.getHeatExchangerTransferAmount() / 2;
            }
        }
        return effectiveCooling;
    }

    private int getAdjacentComponentEffect(int slot, boolean damageReflectors) {
        int effect = 0;
        int x = slot % GRID_WIDTH;
        int y = slot / GRID_WIDTH;

        int[][] adjacentOffsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] offset : adjacentOffsets) {
            int adjX = x + offset[0];
            int adjY = y + offset[1];

            if (isFuelAt(adjX, adjY)) {
                effect += 1; // Each adjacent fuel rod adds 1 to the pulse multiplier
            } else if (isNeutronReflectorAt(adjX, adjY)) {
                ItemStack adjacentStack = inventory.get(adjY * GRID_WIDTH + adjX);
                if (adjacentStack.getItem() instanceof ReactorComponentItem component) {
                    effect += component.getNeutronReflectionBonus();
                    if (damageReflectors) {
                        damageOrConsume(adjacentStack, adjY * GRID_WIDTH + adjX, 1);
                    }
                }
            }
        }
        return effect;
    }

    private boolean isFuelAt(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        ItemStack stack = inventory.get(y * GRID_WIDTH + x);
        return stack.getItem() instanceof ReactorComponentItem component && component.isFuelRod();
    }

    private void damageOrConsume(ItemStack stack, int slot, int amount) {
        if (!stack.isDamageableItem()) {
            return;
        }
        stack.setDamageValue(stack.getDamageValue() + amount);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            inventory.set(slot, ItemStack.EMPTY);
        }
    }

    private void meltdown(Level level, BlockPos pos) {
        Containers.dropContents(level, pos, this);
        level.removeBlock(pos, false);
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6.0f, true, Level.ExplosionInteraction.BLOCK);
    }

    public boolean canPlaceComponent(ItemStack stack) {
        return stack.getItem() instanceof ReactorComponentItem || stack.is(ModItemTags.BATTERY);
    }

    public long getEnergy() {
        return energyContainer.amount;
    }

    public long getEnergyCapacity() {
        return energyContainer.getCapacity();
    }

    public int getHeat() {
        return heat;
    }

    public int getMaxHeat() {
        return maxHeat;
    }

    public int getGeneration() {
        return generation;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return worldPosition;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.NuclearReactor);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new NuclearReactorScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] slots = new int[inventory.size()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == BATTERY_SLOT) {
            return stack.is(ModItemTags.BATTERY);
        }
        return stack.getItem() instanceof ReactorComponentItem;
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
