package com.skniro.industrial_elixir.block.entity.generator.heat;

import com.skniro.industrial_elixir.api.item.ModFuelRegistry;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.machine.AbstractMachineEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorage;
import com.skniro.industrial_elixir.energy.heat.api.HeatStorageUtil;
import com.skniro.industrial_elixir.energy.heat.api.base.SimpleSidedHeatContainer;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.handler.generator.heat.SolidFuelHeaterScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class SolidFuelHeaterEntity extends AbstractMachineEntity {

    public SimpleSidedHeatContainer heatContainer;

    private int burnTime = 0;
    private int burnDuration = 0;

    private static final long HEAT_PER_TICK = 10;
    private static final long HEAT_CAPACITY = 100;

    public SolidFuelHeaterEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.SOLID_FUEL_HEATER_BE, pos, state);
        heatContainer = new SimpleSidedHeatContainer() {

            @Override
            public long getCapacity() {
                return 8000;
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
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) return;

        // Push heat to neighbors
        pushHeatToNeighbours();

        boolean burning = false;

        if (burnTime > 0) {
            // Currently burning fuel
            if (heatContainer.amount < HEAT_CAPACITY) {
                // Heat has room: produce heat and consume burn time
                try (Transaction tx = Transaction.openOuter()) {
                    heatContainer.getSideStorage(null).insert(HEAT_PER_TICK, tx);
                    tx.commit();
                }
                burnTime--;
                burning = true;
            }
            // If heat is full, burnTime is NOT decremented (paused)
        }

        if (burnTime <= 0 && heatContainer.amount < HEAT_CAPACITY) {
            // Try to start burning new fuel
            ItemStack fuel = inventory.get(INPUT_SLOT);
            int fuelTime = getFuelTime(fuel);
            if (fuelTime > 0) {
                fuel.shrink(1);
                outputAsh();
                burnTime = fuelTime;
                burnDuration = fuelTime;
                burning = true;
            }
        }

        // Sync progress to property delegate
        this.progress = burnTime;
        this.maxProgress = burnDuration;

        if (state.getValue(AbstractMachineblock.LIT) != (burning || burnTime > 0)) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, burning || burnTime > 0), 3);
        }

        setChanged();
    }

    private void outputAsh() {
        ItemStack ash = new ItemStack(GrowableOresItems.ASHES);
        ItemStack output = inventory.get(OUTPUT_SLOT);
        if (output.isEmpty()) {
            inventory.set(OUTPUT_SLOT, ash.copy());
        } else if (ItemStack.isSameItemSameComponents(output, ash) && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
        }
    }

    private void pushHeatToNeighbours() {
        if (heatContainer.amount <= 0) return;
        Direction direction = getBlockState().getValue(AbstractMachineblock.FACING);
        HeatStorage target = HeatStorage.SIDED.find(level, worldPosition.relative(direction), direction.getOpposite());
        if (target == null) return;
        HeatStorageUtil.move(heatContainer.getSideStorage(direction), target,
                heatContainer.getSideStorage(null).getAmount(), null);
    }

    private int getFuelTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        Integer custom = ModFuelRegistry.getModFuel().get(stack.getItem());
        if (custom != null) return custom;
        for (var entry : ModFuelRegistry.getModFuelTags().entrySet()) {
            if (stack.is(entry.getKey())) return entry.getValue();
        }
        if (this.level != null) {
            return this.level.fuelValues().burnDuration(stack) / 2;
        }
        return 0;
    }

    public int getBurnTime() { return burnTime; }
    public int getBurnDuration() { return burnDuration; }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Solid_Fuel_Heater);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new SolidFuelHeaterScreenHandler(syncId, playerInventory, worldPosition);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() { return null; }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("solid_fuel_heater.burnTime", burnTime);
        output.putInt("solid_fuel_heater.burnDuration", burnDuration);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        burnTime = input.getIntOr("solid_fuel_heater.burnTime", 0);
        burnDuration = input.getIntOr("solid_fuel_heater.burnDuration", 0);
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
