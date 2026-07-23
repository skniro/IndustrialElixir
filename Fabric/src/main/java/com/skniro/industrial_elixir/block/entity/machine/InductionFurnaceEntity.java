package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.init.ItemUpgradeModule;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import com.skniro.industrial_elixir.screen.handler.machine.InductionFurnaceScreenHandler;
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
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InductionFurnaceEntity extends AbstractMachineEntity {
    public static final int INPUT_SLOT_A = 1;
    public static final int INPUT_SLOT_B = 9;
    public static final int OUTPUT_SLOT = 2;
    public static final int ENERGY_SLOT = 3;
    private static final int HEAT_KEEP_COST = 1;
    private static final int MAX_HEAT = 100;
    private static final int MIN_RECIPE_EU = 195;
    private static final int MAX_RECIPE_EU = 6000;
    private static final int MAX_EU_PER_TICK_PER_SLOT = 16;
    private static final int HEAT_UP_INTERVAL = 20;
    private static final int COOL_DOWN_INTERVAL = 40;

    private int heat;
    private int heatTick;

    private final ContainerData inductionData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> InductionFurnaceEntity.this.progress;
                case 1 -> InductionFurnaceEntity.this.maxProgress;
                case 2 -> InductionFurnaceEntity.this.heat;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> InductionFurnaceEntity.this.progress = value;
                case 1 -> InductionFurnaceEntity.this.maxProgress = value;
                case 2 -> InductionFurnaceEntity.this.heat = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public InductionFurnaceEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.INDUCTION_FURNACE_BLOCK_ENTITY, pos, state);
        this.maxProgress = getProgressTicksForHeat();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.InductionFurnace);
    }

    @Override
    public RecipeType<?> getCurrentRecipeType() {
        return RecipeType.SMELTING;
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide()) {
            return;
        }

        charge(ENERGY_SLOT);
        boolean hasRecipe = getActiveRecipeCount() > 0;
        boolean shouldKeepHeat = shouldKeepHeat(world, pos);

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

        maxProgress = getProgressTicksForHeat();
        if (hasRecipe && hasEnoughEnergyForActiveSlots()) {
            progress++;
            extractEnergy(getProcessingEnergyPerTick());
            if (progress >= maxProgress) {
                smelt(INPUT_SLOT_A);
                smelt(INPUT_SLOT_B);
                progress = 0;
            }
        } else {
            progress = 0;
        }

        boolean isWorking = hasRecipe && hasEnoughEnergyForActiveSlots();
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
            ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof ItemUpgradeModule upgrade && upgrade.modifiesRedstoneInput(stack)) {
                return true;
            }
        }
        return false;
    }

    private int getEnergyStorageUpgrade() {
        int total = 0;
        for (int slot = UPGRADE_START; slot <= UPGRADE_END; slot++) {
            ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof ItemUpgradeModule upgrade) {
                total += upgrade.getExtraEnergyStorage(stack);
            }
        }
        return total;
    }

    public EnergyTier getEffectiveTier() {
        int tierBoost = 0;
        for (int slot = UPGRADE_START; slot <= UPGRADE_END; slot++) {
            ItemStack stack = inventory.get(slot);
            if (stack.getItem() instanceof ItemUpgradeModule upgrade) {
                tierBoost += upgrade.getTierIncrease(stack);
            }
        }
        int newTier = Math.min(energyTier.ordinal() + tierBoost, EnergyTier.values().length - 1);
        return EnergyTier.values()[newTier];
    }

    private int getProgressTicksForHeat() {
        return Math.max(1, (int) Math.ceil((double) getEnergyPerItem() / MAX_EU_PER_TICK_PER_SLOT));
    }

    private int getEnergyPerItem() {
        int effectiveHeat = Math.max(1, heat);
        return MIN_RECIPE_EU + (MAX_HEAT - effectiveHeat) * (MAX_RECIPE_EU - MIN_RECIPE_EU) / (MAX_HEAT - 1);
    }

    private int getActiveRecipeCount() {
        int count = 0;
        ItemStack projectedOutput = this.getItem(OUTPUT_SLOT).copy();
        Optional<RecipeHolder<SmeltingRecipe>> firstRecipe = getSmeltingRecipe(INPUT_SLOT_A);
        if (firstRecipe.isPresent()) {
            ItemStack output = firstRecipe.get().value().assemble(new SingleRecipeInput(this.getItem(INPUT_SLOT_A))).copy();
            if (canMergeOutput(projectedOutput, output)) {
                projectedOutput = mergedOutput(projectedOutput, output);
                count++;
            }
        }
        Optional<RecipeHolder<SmeltingRecipe>> secondRecipe = getSmeltingRecipe(INPUT_SLOT_B);
        if (secondRecipe.isPresent()) {
            ItemStack output = secondRecipe.get().value().assemble(new SingleRecipeInput(this.getItem(INPUT_SLOT_B))).copy();
            if (canMergeOutput(projectedOutput, output)) {
                count++;
            }
        }
        return count;
    }

    private int getProcessingEnergyPerTick() {
        return MAX_EU_PER_TICK_PER_SLOT * getActiveRecipeCount();
    }

    private boolean hasEnoughEnergyForActiveSlots() {
        return getActiveRecipeCount() > 0 && energyContainer.amount >= getProcessingEnergyPerTick();
    }

    private void extractEnergy(long amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            energyContainer.getSideStorage(null).extract(amount, transaction);
            transaction.commit();
        }
    }

    private boolean hasSmeltingRecipe(int inputSlot) {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = getSmeltingRecipe(inputSlot);
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack output = recipe.get().value().assemble(new SingleRecipeInput(this.getItem(inputSlot))).copy();
        return canMergeOutput(this.getItem(OUTPUT_SLOT), output);
    }

    private boolean canMergeOutput(ItemStack currentOutput, ItemStack output) {
        return currentOutput.isEmpty() || (ItemStack.isSameItemSameComponents(currentOutput, output) && currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize());
    }

    private ItemStack mergedOutput(ItemStack currentOutput, ItemStack output) {
        if (currentOutput.isEmpty()) {
            return output.copy();
        }
        ItemStack merged = currentOutput.copy();
        merged.grow(output.getCount());
        return merged;
    }

    private Optional<RecipeHolder<SmeltingRecipe>> getSmeltingRecipe(int inputSlot) {
        if (this.getLevel() == null || this.getLevel().getServer() == null || this.getItem(inputSlot).isEmpty()) {
            return Optional.empty();
        }
        return this.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(this.getItem(inputSlot)), this.getLevel());
    }

    private void smelt(int inputSlot) {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = getSmeltingRecipe(inputSlot);
        if (recipe.isEmpty()) {
            return;
        }
        ItemStack output = recipe.get().value().assemble(new SingleRecipeInput(this.getItem(inputSlot))).copy();
        if (!hasSmeltingRecipe(inputSlot)) {
            return;
        }
        this.removeItem(inputSlot, 1);
        if (this.getItem(OUTPUT_SLOT).isEmpty()) {
            this.setItem(OUTPUT_SLOT, output);
        } else {
            this.getItem(OUTPUT_SLOT).grow(output.getCount());
        }
    }

    @Override
    public void charge(int slot) {
        if (this.level != null && !this.level.isClientSide() && getFreeSpace() > 0) {
            Container inventory = this;
            EnergyStorageUtil.move(
                    ContainerItemContext.ofSingleSlot(ContainerStorage.of(inventory, null).getSlots().get(slot)).find(EnergyStorage.ITEM),
                    this.getSideEnergyStorage(null),
                    Long.MAX_VALUE,
                    null
            );
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return new int[]{OUTPUT_SLOT};
        }
        return new int[]{INPUT_SLOT_A, INPUT_SLOT_B, ENERGY_SLOT};
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT_A || slot == INPUT_SLOT_B) {
            return true;
        }
        if (slot == ENERGY_SLOT) {
            return stack.is(ModItemTags.BATTERY);
        }
        return slot >= UPGRADE_START && slot <= UPGRADE_END;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && slot == OUTPUT_SLOT;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("induction_furnace.heat", heat);
        output.putInt("induction_furnace.heat_tick", heatTick);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        heat = input.getIntOr("induction_furnace.heat", 0);
        heatTick = input.getIntOr("induction_furnace.heat_tick", 0);
        maxProgress = getProgressTicksForHeat();
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

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new InductionFurnaceScreenHandler(syncId, playerInventory, this, inductionData);
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
