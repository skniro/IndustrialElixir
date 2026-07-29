package com.skniro.industrial_elixir.block.entity;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.api.block.MachineEnergyProvider;
import com.skniro.industrial_elixir.api.block.MachineRecipeProvider;
import com.skniro.industrial_elixir.api.block.TieredEnergyBlock;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.item.init.ItemUpgradeModule;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class BasePowerBlockBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos>, ImplementedInventory, ItemOwner, MachineEnergyProvider {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(13, ItemStack.EMPTY);
    private float rotation = 0;
    protected static final int FLUID_ITEM_SLOT = 0;
    protected static final int INPUT_SLOT = 1;
    protected static final int OUTPUT_SLOT = 2;
    protected static final int ENERGY_ITEM_SLOT = 3;
    protected static final int UPGRADE_START = 4;
    protected static final int UPGRADE_END = 7;
    protected static final int EMPTY_FLUID_ITEM_SLOT = 8;
    // Additional second input slot (used by machines that need two inputs, e.g. Brew Reactor)
    protected static final int SECOND_INPUT_SLOT = 9;
    protected static final int OUTPUT_SLOT_2 = 10;
    protected static final int OUTPUT_SLOT_3 = 11;
    protected static final int THIRD_INPUT_SLOT = 12;
    protected static final int ENERGY_CRAFTING_AMOUNT = 32;
    protected final EnergyTier energyTier;
    public SimpleSidedEnergyContainer energyContainer;


    public BasePowerBlockBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.energyTier = ((AbstractMachineblock)getBlockState().getBlock()).getEnergyTier();

        energyContainer = new SimpleSidedEnergyContainer() {
            @Override
            public long getCapacity() {
                return getMachineCapacity();
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
    };

    // 获取有效的 EnergyTier（支持 Transformer 升级）
    public EnergyTier getEffectiveTier() {
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

    // 计算处理速度倍率
    public double getProcessTimeMultiplier() {
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
    public double getEnergyDemandMultiplier() {
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
    public int getEnergyStorageUpgrade() {
        int total = 0;
        for (int i = UPGRADE_START; i <= UPGRADE_END; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgradeModule u) {
                total += u.getExtraEnergyStorage(stack);
            }
        }
        return total;
    }


    public void charge(int slot) {
        if (this.level != null) {
            if (!this.level.isClientSide()) {
                long chargeEnergy = Math.min(this.getFreeSpace(), energyTier.getMaxInput());
                if (chargeEnergy > 0L) {
                    if (!this.getOptionalInventory().isEmpty()) {
                        Container inventory = this.getOptionalInventory().get();
                        ItemStack stack = inventory.getItem(slot);
                        if(!stack.isEmpty()) {
                            EnergyStorage itemEnergyStorage = stack.getCapability(EnergyStorage.ITEM, ItemAccess.forStack(stack));
                            EnergyStorageUtil.move(itemEnergyStorage, this.getSideEnergyStorage(null), Long.MAX_VALUE, null);
                        }
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
                    if(!stack.isEmpty()) {
                        EnergyStorage itemEnergyStorage = stack.getCapability(EnergyStorage.ITEM, ItemAccess.forStack(stack));
                        EnergyStorageUtil.move(this.getSideEnergyStorage(null), itemEnergyStorage, Long.MAX_VALUE, null);
                    }
                }
            }
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

    public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
        return this.energyContainer.getSideStorage(side);
    }

    public void pushEnergyToNeighbours() {
        if (energyContainer.amount <= 0) return;
        for (Direction dir : Direction.values()) {
            EnergyStorage target = EnergyStorage.SIDED.find(level, worldPosition.relative(dir), dir.getOpposite());
            if (target == null) continue;
            EnergyStorageUtil.move(energyContainer.getSideStorage(dir), target, getEffectiveTier().getMaxOutput(), null);
        }
    }

    public EnergyTier getEnergyTier(){
        return energyTier;
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
    public Level level() {
        return this.level;
    }

    @Override
    public Vec3 position() {
        return this.getBlockPos().getCenter();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return this.getBlockState().getValue(AbstractMachineblock.FACING).getOpposite().getStepY();
    }

    @Override
    public long getMachineCapacity(){
        if(getBlockState().getBlock() instanceof AbstractMachineblock) {
            return ((AbstractMachineblock) getBlockState().getBlock()).getMaxCapacity();
        }
        return 0;
    };

    public long getCraftEnergyCost(){
        return 0;
    }
}
