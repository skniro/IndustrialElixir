package com.skniro.industrial_elixir.block.entity.generator.heat;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorageUtil;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleSidedHeatContainer;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.handler.generator.heat.ElectricHeaterScreenHandler;
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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class ElectricHeaterBlockEntity extends AbstractMachineEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(21, ItemStack.EMPTY);
    public SimpleSidedHeatContainer heatContainer;
    private static final int COIL_SLOT_START = 11;
    private static final int COIL_SLOT_END = 20;
    private static final long EU_PER_COIL = 10;
    private static final long HEAT_PER_COIL = 10;

    public ElectricHeaterBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.ELECTRIC_HEATER_BLOCK_ENTITY.get(), pos, state);;
        heatContainer = new SimpleSidedHeatContainer() {

            @Override
            public long getCapacity() {
                return 100;
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                if (side == null) return 100;
                return 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                if (side == null) return 100;
                return side == getBlockState().getValue(AbstractMachineblock.FACING) ? 100 : 0;
            }

            @Override
            protected void onFinalCommit() {
                setChanged();
                getLevel().sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
            }
        };
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        boolean heating = canProduceHeat();
        produceHeat();
        if (state.getValue(AbstractMachineblock.LIT) != heating) {
            level.setBlock(pos, state.setValue(AbstractMachineblock.LIT, heating), 3);
        }
        pushHeatToNeighbours();
        charge(ENERGY_ITEM_SLOT);
        setChanged();
    }

    private boolean canProduceHeat() {
        int coilCount = 0;
        for (int i = COIL_SLOT_START; i <= COIL_SLOT_END; i++) if (inventory.get(i).is(GrowableOresItems.Coil.get())) coilCount += inventory.get(i).getCount();
        return coilCount > 0 && energyContainer.getSideStorage(null).getAmount() >= coilCount * EU_PER_COIL;
    }

    private void produceHeat() {
        int coilCount = 0;
        for (int i = COIL_SLOT_START; i <= COIL_SLOT_END; i++) if (inventory.get(i).is(GrowableOresItems.Coil.get())) coilCount += inventory.get(i).getCount();
        if (coilCount > 0) {
            long required = coilCount * EU_PER_COIL;
            if (energyContainer.getSideStorage(null).getAmount() >= required) {
                try (Transaction tx = Transaction.openRoot()) {
                    if (energyContainer.getSideStorage(null).extract(required, tx) == required) {
                        long heat = coilCount * HEAT_PER_COIL;
                        if (heatContainer.getSideStorage(null).insert(heat, tx) == heat) {
                            tx.commit();
                        }
                    }
                }
            }
        }
    }

    private void pushHeatToNeighbours() {
        if (heatContainer.amount <= 0) return;
        Direction direction = getBlockState().getValue(AbstractMachineblock.FACING);
        HeatStorage target = HeatStorage.SIDED.getCapability(level, worldPosition.relative(direction),null,null,direction.getOpposite());
        if (target == null) return;
        HeatStorageUtil.move(this.heatContainer.getSideStorage(direction), target, heatContainer.getSideStorage(null).getAmount(), null);
    }

    public long getHeatProduction() {
        int coilCount = 0;
        for (int i = COIL_SLOT_START; i <= COIL_SLOT_END; i++) {
            if (inventory.get(i).is(GrowableOresItems.Coil.get())) {
                coilCount += inventory.get(i).getCount();
            }
        }

        long required = coilCount * EU_PER_COIL;

        if (energyContainer.getSideStorage(null).getAmount() < required) {
            return 0;
        }

        return coilCount * HEAT_PER_COIL;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, inventory);
    }

    public HeatStorage getHeatStorage() {
        return heatContainer.getSideStorage(null);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Electric_Heater);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ElectricHeaterScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}
