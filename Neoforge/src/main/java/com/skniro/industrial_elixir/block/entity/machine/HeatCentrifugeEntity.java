package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.block.init.machine.HeatCentrifugeBlock;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.item.init.ItemUpgradeModule;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.AbstractMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.HeatCentrifugeCraftingRecipe;
import com.skniro.industrial_elixir.screen.handler.machine.HeatCentrifugeScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HeatCentrifugeEntity extends AbstractMachineEntity {
    private static final int MIN_VOLTAGE = 48; // minimum working voltage (EU/t)
    private static final int PROCESS_ENERGY_PER_TICK = 49; // EU/t consumed while processing
    private static final int HEAT_KEEP_COST = 1; // EU/t to keep heat when redstone powered
    private static final int MAX_HEAT = 6000;
    private static final int HEAT_UP_INTERVAL = 1; // heat increases every tick while active
    private static final int COOL_DOWN_INTERVAL = 40;
    private static final int PROCESS_SECONDS = 25;
    private static final int PROCESS_TICKS = PROCESS_SECONDS * 20;

    private int heat = 0;
    private int heatTick = 0;

    private final ContainerData centrifugeData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> HeatCentrifugeEntity.this.progress;
                case 1 -> HeatCentrifugeEntity.this.maxProgress;
                case 2 -> HeatCentrifugeEntity.this.heat;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> HeatCentrifugeEntity.this.progress = value;
                case 1 -> HeatCentrifugeEntity.this.maxProgress = value;
                case 2 -> HeatCentrifugeEntity.this.heat = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public HeatCentrifugeEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.HEAT_CENTRIFUGE_BE.get(), pos, state);
        // set machine defaults
        this.maxProgress = PROCESS_TICKS;
        this.energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                return 10000 + getEnergyStorageUpgrade();
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
                getLevel().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.Heat_Centrifuge);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return AlchemyRecipeType.HEAT_CENTRIFUGE.type;
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) {
            return;
        }

        // charge from battery slot
        charge(ENERGY_ITEM_SLOT);

        boolean hasRecipe = hasRecipe();
        boolean shouldKeepHeat = shouldKeepHeat(world, pos);

        // heat management
        if ((hasRecipe || shouldKeepHeat) && heat < MAX_HEAT) {
            heatTick++;
            if (heatTick >= HEAT_UP_INTERVAL) {
                heat++;
                heatTick = 0;
            }
        } else if (!hasRecipe && !shouldKeepHeat && heat > 0) {
            heatTick++;
            if (heatTick >= COOL_DOWN_INTERVAL) {
                heat--;
                heatTick = 0;
            }
        } else {
            heatTick = 0;
        }

        if (shouldKeepHeat && energyContainer.amount >= HEAT_KEEP_COST) {
            extractEnergy(HEAT_KEEP_COST);
        }

        // ensure working voltage: require effective tier input capacity >= MIN_VOLTAGE
        boolean hasMinVoltage = getEffectiveTier().getMaxInput() >= MIN_VOLTAGE;

        if (hasRecipe && hasMinVoltage && energyContainer.amount >= PROCESS_ENERGY_PER_TICK && heat >= MAX_HEAT) {
            progress++;
            extractEnergy(PROCESS_ENERGY_PER_TICK);
            if (progress >= maxProgress) {
                craftItem();
                progress = 0;
            }
        } else {
            progress = 0;
        }

        boolean isWorking = hasRecipe && hasMinVoltage && energyContainer.amount >= PROCESS_ENERGY_PER_TICK;
        if (state.getValue(AbstractMachineblock.LIT) != isWorking) {
            world.setBlock(pos, state.setValue(AbstractMachineblock.LIT, isWorking), 3);
        }
        setChanged(world, pos, state);
    }

    private boolean shouldKeepHeat(Level world, BlockPos pos) {
        boolean powered = world.hasNeighborSignal(pos);
        if (hasRedstoneInverterUpgrade()) {
            powered = !powered;
        }
        return powered && energyContainer.amount >= HEAT_KEEP_COST;
    }

    private boolean hasRedstoneInverterUpgrade() {
        for (int slot = UPGRADE_START; slot <= UPGRADE_END; slot++) {
            net.minecraft.world.item.ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof ItemUpgradeModule upgrade && upgrade.modifiesRedstoneInput(stack)) {
                return true;
            }
        }
        return false;
    }

    public EnergyTier getEffectiveTier() {
        int tierBoost = 0;
        for (int slot = UPGRADE_START; slot <= UPGRADE_END; slot++) {
            net.minecraft.world.item.ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof com.skniro.industrial_elixir.item.init.ItemUpgradeModule upgrade) {
                tierBoost += upgrade.getTierIncrease(stack);
            }
        }
        int newTier = Math.min(energyTier.ordinal() + tierBoost, EnergyTier.values().length - 1);
        return EnergyTier.values()[newTier];
    }

    private void extractEnergy(long amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            energyContainer.getSideStorage(null).extract(amount, transaction);
            transaction.commit();
        }
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("heat_centrifuge.heat", heat);
        output.putInt("heat_centrifuge.heat_tick", heatTick);
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        heat = input.getIntOr("heat_centrifuge.heat", 0);
        heatTick = input.getIntOr("heat_centrifuge.heat_tick", 0);
        maxProgress = PROCESS_TICKS;
    }

    public int getHeat() {
        return heat;
    }

    public int getScaledHeatWidth() {
        return heat * 54 / MAX_HEAT;
    }

    public int getScaledEnergyHeight() {
        long energy = energyContainer.amount;
        long capacity = energyContainer.getCapacity();
        return capacity != 0 && energy != 0 ? (int) (energy * 16 / capacity) : 0;
    }

    @Override
    protected void craftItem() {
        HeatCentrifugeCraftingRecipe recipe = (HeatCentrifugeCraftingRecipe) getCurrentRecipe().get().value();
        this.removeItem(INPUT_SLOT, recipe.requiredCount());
        insertOutput(OUTPUT_SLOT, recipe.output().create());
        recipe.output2().ifPresent(output -> insertOutput(OUTPUT_SLOT_2, output.create()));
        recipe.output3().ifPresent(output -> insertOutput(OUTPUT_SLOT_3, output.create()));
    }

    private void insertOutput(int slot, ItemStack output) {
        if (output.isEmpty()) {
            return;
        }
        if (this.getItem(slot).isEmpty()) {
            this.setItem(slot, output.copy());
        } else {
            this.getItem(slot).grow(output.getCount());
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if ((slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2 && slot != OUTPUT_SLOT_3) || side == Direction.UP) {
            return false;
        }

        if (side == Direction.DOWN) {
            return true;
        }

        Direction localDir = this.level.getBlockState(this.getBlockPos()).getValue(HeatCentrifugeBlock.FACING);
        return side == localDir.getOpposite() || side == localDir.getClockWise();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT_SLOT && slot != OUTPUT_SLOT_2 && slot != OUTPUT_SLOT_3;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction != Direction.DOWN) {
            return new int[]{INPUT_SLOT, FLUID_ITEM_SLOT, ENERGY_ITEM_SLOT};
        } else {
            return new int[]{OUTPUT_SLOT, OUTPUT_SLOT_2, OUTPUT_SLOT_3};
        }
    }

    @Override
    public boolean hasRecipe() {
        Optional<RecipeHolder<AbstractMachineCraftingRecipe>> opt = getCurrentRecipe();
        if (opt.isEmpty()) return false;

        var recipeHolder = opt.get();
        var recipe = recipeHolder.value();
        HeatCentrifugeCraftingRecipe washingRecipe = (HeatCentrifugeCraftingRecipe) recipe;

        if (!canInsertIntoSlot(OUTPUT_SLOT, washingRecipe.output().create())) return false;
        if (washingRecipe.output2().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_2, washingRecipe.output2().get().create())) return false;
        if (washingRecipe.output3().isPresent() && !canInsertIntoSlot(OUTPUT_SLOT_3, washingRecipe.output3().get().create())) return false;
        if (!hasEnoughEnergyToCraft()) return false;
        return true;
    }

    private boolean canInsertIntoSlot(int slot, ItemStack output) {
        if (output.isEmpty()) {
            return true;
        }
        ItemStack stack = this.getItem(slot);
        int maxCount = stack.isEmpty() ? output.getMaxStackSize() : stack.getMaxStackSize();
        return (stack.isEmpty() || ItemStack.isSameItemSameComponents(stack, output))
                && maxCount >= stack.getCount() + output.getCount();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new  HeatCentrifugeScreenHandler(syncId, playerInventory, this, centrifugeData);
    }
}




