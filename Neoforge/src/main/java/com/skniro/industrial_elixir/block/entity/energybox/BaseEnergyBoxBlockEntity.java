package com.skniro.industrial_elixir.block.entity.energybox;

import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.TieredEnergyItem;
import com.skniro.industrial_elixir.block.entity.BasePowerBlockBlockEntity;
import com.skniro.industrial_elixir.block.init.energybox.ChargePadBlock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.EnergyStorageUtil;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class BaseEnergyBoxBlockEntity extends BasePowerBlockBlockEntity {
    public SimpleSidedEnergyContainer energyContainer;
    protected final ContainerData propertyDelegate;

    public BaseEnergyBoxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        energyContainer = new SimpleSidedEnergyContainer() {

            @Override
            public long getCapacity() {
                return getMachineCapacity();
            }

            @Override
            public long getMaxInsert(@Nullable Direction side) {
                if (side == null) return energyTier.getMaxInput();
                Direction front = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
                if (side == front) return energyTier.getMaxInput();
                return 0;
            }

            @Override
            public long getMaxExtract(@Nullable Direction side) {
                if (side == null) return energyTier.getMaxOutput();
                Direction front = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
                if (side == front) {
                    return 0;
                }
                return energyTier.getMaxOutput();
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
                return 0;
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
        return this.energyContainer.getSideStorage(side);
    }

    public void chargePlayersAbove() {
        if (level == null || level.isClientSide()) return;

        AABB box = new AABB(worldPosition).move(0, 1, 0);
        List<Player> players = level.getEntitiesOfClass(Player.class, box, p -> true);
        boolean hasPlayer = !players.isEmpty();
        BlockState state = level.getBlockState(worldPosition);

        if (!hasPlayer || energyContainer.amount <= 0) {
            if (state.getValue(ChargePadBlock.LIT)) {
                level.setBlock(worldPosition, state.setValue(ChargePadBlock.LIT, false), 3);
            }
            return;
        }

        boolean charged = false;
        for (Player player : players) {
            Inventory inv = player.getInventory();

            for (int i = 0; i < inv.getContainerSize(); i++) {
                if (energyContainer.amount <= 0) break;

                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty() && stack.getItem() instanceof TieredEnergyItem energyItem) {
                    int itemTier = energyItem.getEnergyTier().ordinal();
                    if (itemTier >= this.energyTier.ordinal()) {
                        EnergyStorage itemEnergyStorage = stack.getCapability(EnergyStorage.ITEM, ItemAccess.forStack(stack));
                        long moved = EnergyStorageUtil.move(this.getSideEnergyStorage(null), itemEnergyStorage, Long.MAX_VALUE, null);
                        if (moved > 0) {
                            charged = true;
                        }
                    }
                }
            }
        }

        if (state.getValue(ChargePadBlock.LIT) != charged) {
            level.setBlock(worldPosition, state.setValue(ChargePadBlock.LIT, charged), 3);
        }
    }

    private void chargeItem(ItemStack stack) {
        EnergyStorage itemEnergyStorage = stack.getCapability(EnergyStorage.ITEM, ItemAccess.forStack(stack));
        EnergyStorageUtil.move(this.getSideEnergyStorage(null), itemEnergyStorage, Long.MAX_VALUE, null);
    }

    public long getFreeSpace() {
        return this.energyContainer.getCapacity() - energyContainer.amount;
    }


    public boolean canInsert(ItemStack stack) {
        return stack.is(ModItemTags.BATTERY);
    }


}