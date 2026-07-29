package com.skniro.industrial_elixir.block.entity.generator;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.machine.fluid.AbstractFluidMachineEntity;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.generator.fluid.FluidGeneratorScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidGeneratorEntity extends AbstractFluidMachineEntity {
    // Lava: 1000mB per 25s = 40mB/s = 2mB/t, generates 20EU/t
    // Water: 20mB/s = 1mB/t, generates 1EU/t
    // Internal unit: FluidConstants.BUCKET / 81 = 1000 mB per bucket
    private static final long LAVA_CONSUME_PER_TICK = (FluidConstants.BUCKET / 81) / 500;  // 2mB
    private static final long WATER_CONSUME_PER_TICK = (FluidConstants.BUCKET / 81) / 1000; // 1mB
    private static final long LAVA_EU_PER_TICK = 20;
    private static final long WATER_EU_PER_TICK = 1;

    public FluidGeneratorEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.FLUID_GENERATOR_BE.get(), pos, state);
        energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                return 10000;
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
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Fluid_Generator);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FluidGeneratorScreenHandler(syncId, playerInventory,  this, propertyDelegate);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return null;
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world == null || world.isClientSide()) return;

        // Output energy to batteries
        discharge(ENERGY_ITEM_SLOT);

        // Push energy to adjacent machines
        pushEnergyToNeighbours();

        // Fill from fluid containers
        if (hasFluidStackInFluidSlot()) {
            fillUpFluidTank();
        }

        boolean working = false;

        // Only work if internal storage is not full
        if (energyContainer.amount < energyContainer.getCapacity()) {
            FluidVariant fluid = fluidContainer.getResource();

            // Try lava first (priority)
            if (!fluid.isBlank() && fluid.getFluid() == Fluids.LAVA) {
                if (fluidContainer.getAmount() >= LAVA_CONSUME_PER_TICK) {
                    try (Transaction tx = Transaction.openRoot()) {
                        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx2 = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                            long extracted = fluidContainer.extract(fluid, LAVA_CONSUME_PER_TICK, tx2);
                            if (extracted == LAVA_CONSUME_PER_TICK) {
                                energyContainer.getSideStorage(null).insert(LAVA_EU_PER_TICK, tx);
                                tx.commit();
                                working = true;
                            }
                        }
                    }
                }
            }

            // If lava didn't work, try water
            if (!working && !fluid.isBlank() && fluid.getFluid() == Fluids.WATER) {
                if (fluidContainer.getAmount() >= WATER_CONSUME_PER_TICK) {
                    try (Transaction tx = Transaction.openRoot()) {
                        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx2 = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                            long extracted = fluidContainer.extract(fluid, WATER_CONSUME_PER_TICK, tx2);
                            if (extracted == WATER_CONSUME_PER_TICK) {
                                energyContainer.getSideStorage(null).insert(WATER_EU_PER_TICK, tx);
                                tx.commit();
                                working = true;
                            }
                        }
                    }
                }
            }

            if (!working && !fluid.isBlank() && fluid.getFluid() == IndustrialElixirFluids.STILL_Hot_Spring) {
                if (fluidContainer.getAmount() >= WATER_CONSUME_PER_TICK) {
                    try (Transaction tx = Transaction.openRoot()) {
                        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction tx2 = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                            long extracted = fluidContainer.extract(fluid, WATER_CONSUME_PER_TICK, tx2);
                            if (extracted == WATER_CONSUME_PER_TICK) {
                                energyContainer.getSideStorage(null).insert(WATER_EU_PER_TICK, tx);
                                tx.commit();
                                working = true;
                            }
                        }
                    }
                }
            }
        }

        if (state.getValue(AbstractMachineblock.LIT) != working) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, working), 3);
        }

        setChanged(world, pos, state);
    }

    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        SingleVariantStorage.writeValue(fluidContainer, FluidVariant.CODEC, output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        SingleVariantStorage.readValue(fluidContainer, FluidVariant.CODEC, FluidVariant::blank, input);
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
