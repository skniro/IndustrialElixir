package com.skniro.industrial_elixir.block.entity.cable;

import com.skniro.industrial_elixir.ModContent;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.CableBlock;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleSidedEnergyContainer;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// CREDIT: https://github.com/techreborn/techreborn
// Under MIT-License: https://github.com/TechReborn/TechReborn/blob/26.1/LICENSE.md
public class CableBlockEntity extends BlockEntity implements BlockEntityTicker<CableBlockEntity> {
    final SimpleSidedEnergyContainer energyContainer;
    private ModContent.Cables cableType;
    private @Nullable BlockState cover;
    long lastTick;
    List<CableTarget> targets;
    private final BlockApiCache<EnergyStorage, Direction>[] adjacentCaches;
    int blockedSides;
    boolean ioBlocked;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.CABLE.get(), pos, state);
        class NamelessClass_1 extends SimpleSidedEnergyContainer {
            public long getCapacity() {
                return (long)CableBlockEntity.this.getCableType().transferRate * 4L;
            }

            public long getMaxInsert(Direction side) {
                return CableBlockEntity.this.allowTransfer(side) ? (long)CableBlockEntity.this.getCableType().transferRate : 0L;
            }

            public long getMaxExtract(Direction side) {
                return CableBlockEntity.this.allowTransfer(side) ? (long)CableBlockEntity.this.getCableType().transferRate : 0L;
            }
        }

        this.energyContainer = new NamelessClass_1();
        this.cableType = null;
        this.cover = null;
        this.lastTick = 0L;
        this.targets = null;
        this.adjacentCaches = new BlockApiCache[6];
        this.blockedSides = 0;
        this.ioBlocked = false;
    }

    public CableBlockEntity(BlockPos pos, BlockState state, ModContent.Cables type) {
        super(AlchemyBlockEntityType.CABLE.get(), pos, state);

        this.energyContainer = new SimpleSidedEnergyContainer(){
            public long getCapacity() {
                return (long)CableBlockEntity.this.getCableType().transferRate * 4L;
            }

            public long getMaxInsert(Direction side) {
                return CableBlockEntity.this.allowTransfer(side) ? (long)CableBlockEntity.this.getCableType().transferRate : 0L;
            }

            public long getMaxExtract(Direction side) {
                return CableBlockEntity.this.allowTransfer(side) ? (long)CableBlockEntity.this.getCableType().transferRate : 0L;
            }
        };
        this.cableType = null;
        this.cover = null;
        this.lastTick = 0L;
        this.targets = null;
        this.adjacentCaches = new BlockApiCache[6];
        this.blockedSides = 0;
        this.ioBlocked = false;
        this.cableType = type;
    }

    ModContent.Cables getCableType() {
        if (this.cableType != null) {
            return this.cableType;
        } else if (this.level == null) {
            return ModContent.Cables.COPPER;
        } else {
            Block block = this.level.getBlockState(this.worldPosition).getBlock();
            return block instanceof CableBlock ? ((CableBlock)block).type : ModContent.Cables.COPPER;
        }
    }

    private boolean allowTransfer(Direction side) {
        if (side == null) {
            return true;
        } else {
            return !this.ioBlocked && (this.blockedSides & 1 << side.ordinal()) == 0;
        }
    }

    public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
        return this.energyContainer.getSideStorage(side);
    }

    public long getEnergy() {
        return this.energyContainer.amount;
    }

    public void setEnergy(long energy) {
        this.energyContainer.amount = energy;
    }

    private BlockApiCache<EnergyStorage, Direction> getAdjacentCache(Direction direction) {
        if (this.adjacentCaches[direction.get3DDataValue()] == null) {
            this.adjacentCaches[direction.get3DDataValue()] = BlockApiCache.create(EnergyStorage.SIDED, (ServerLevel)this.level, this.worldPosition.relative(direction));
        }

        return this.adjacentCaches[direction.get3DDataValue()];
    }

    @Nullable BlockEntity getAdjacentBlockEntity(Direction direction) {
        return this.getAdjacentCache(direction).getBlockEntity();
    }

    void appendTargets(List<OfferedEnergyStorage> targetStorages) {
        ServerLevel serverWorld = (ServerLevel)this.level;
        if (serverWorld != null) {
            if (this.targets == null) {
                BlockState newBlockState = this.getBlockState();
                this.targets = new ArrayList();

                for(Direction direction : Direction.values()) {
                    boolean foundSomething = false;
                    BlockApiCache<EnergyStorage, Direction> adjCache = this.getAdjacentCache(direction);
                    BlockEntity var11 = adjCache.getBlockEntity();
                    if (var11 instanceof CableBlockEntity) {
                        CableBlockEntity adjCable = (CableBlockEntity)var11;
                        if (adjCable.getCableType().transferRate == this.getCableType().transferRate) {
                            foundSomething = true;
                        }
                    } else if (adjCache.find(direction.getOpposite()) != null) {
                        foundSomething = true;
                        this.targets.add(new CableTarget(direction, adjCache));
                    }

                    newBlockState = (BlockState)newBlockState.setValue((Property) CableBlock.PROPERTY_MAP.get(direction), foundSomething);
                }

                serverWorld.setBlockAndUpdate(this.getBlockPos(), newBlockState);
            }

            for(CableTarget target : this.targets) {
                EnergyStorage storage = target.find();
                if (storage == null) {
                    this.targets = null;
                } else {
                    targetStorages.add(new OfferedEnergyStorage(this, target.directionTo, storage));
                }
            }

            this.blockedSides = 0;
        }
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        new CompoundTag();
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.energyContainer.amount = view.getLongOr("energy", 0L);
        this.cover = (BlockState)view.read("cover", BlockState.CODEC).orElse((BlockState) null);
    }

    public void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        view.putLong("energy", this.energyContainer.amount);
        if (this.cover != null) {
            view.store("cover", BlockState.CODEC, this.cover);
        }

    }

    public void neighborUpdate() {
        this.targets = null;
    }

    public void tick(Level world, BlockPos pos, BlockState state, CableBlockEntity blockEntity2) {
        if (world != null && !world.isClientSide()) {
            CableTickManager.handleCableTick(this);
        }
    }

    public void addInfo(List<Component> info, boolean isReal, boolean hasData) {
        info.add(Component.translatable("techreborn.tooltip.transferRate").withStyle(ChatFormatting.GRAY).append(": ").append(PowerSystem.getLocalizedPower((double)this.getCableType().transferRate)).withStyle(ChatFormatting.GOLD).append("/t"));
        info.add(Component.translatable("techreborn.tooltip.tier").withStyle(ChatFormatting.GRAY).append(": ").append(Component.literal(StringUtils.toFirstCapitalAllLowercase(this.getCableType().tier.toString())).withStyle(ChatFormatting.GOLD)));
        if (!this.getCableType().canKill) {
            info.add(Component.translatable("techreborn.tooltip.cable.can_cover").withStyle(ChatFormatting.GRAY));
        }

    }

    public @Nullable BlockState getRenderData() {
        return this.cover;
    }

    private static record CableTarget(Direction directionTo, BlockApiCache<EnergyStorage, Direction> cache) {
        @Nullable EnergyStorage find() {
            return this.cache.find(this.directionTo.getOpposite());
        }
    }
}
