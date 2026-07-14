package com.skniro.industrial_elixir.block.entity.pipe;

import com.skniro.industrial_elixir.block.init.pipe.PipeBlock;
import com.skniro.industrial_elixir.block.init.pipe.WoodPipeBlock;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FluidPipeBlockEntity extends PipeBlockEntity {
    private static final long STEP = 81;
    private static final int MAX_PACKETS = 64;
    private static final int MAX_LIFE = 40;
    private final List<FluidPacket> fluids = new ArrayList<>();

    protected FluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected boolean isConnectable(Direction dir) {
        if (isBlocked(dir)) return false;
        BlockPos target = worldPosition.relative(dir);
        BlockEntity be = level.getBlockEntity(target);
        if (be instanceof FluidPipeBlockEntity) return true;
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, target, dir.getOpposite());
        return storage != null;
    }

    protected void moveFluids(Level world, BlockPos pos) {
        boolean changed = false;
        Iterator<FluidPacket> it = fluids.iterator();

        while (it.hasNext()) {
            FluidPacket packet = it.next();

            if (packet.life-- <= 0) {
                it.remove();
                changed = true;
                continue;
            }

            if (isBlocked(packet.direction) || packet.amount <= 0 || packet.variant.isBlank()) {
                it.remove();
                changed = true;
                continue;
            }

            packet.progress += 0.1;
            changed = true;
            if (packet.progress < 1.0) continue;

            BlockPos target = pos.relative(packet.direction);

            boolean moved = tryMoveToNext(world, target, packet);
            boolean inserted = false;
            if (!moved) {
                inserted = tryInsert(world, target, packet);
            }
            if (moved || (inserted && packet.amount <= 0)) {
                it.remove();
            } else {
                packet.progress = 1.0;
            }
            changed = true;
        }
        if (changed) {
            syncToClient();
        }
    }

    protected void extractFluids(Level world, BlockPos pos) {
        if (!fluids.isEmpty()) return;
        if (fluids.size() >= MAX_PACKETS) return;
        for (Direction dir : Direction.values()) {
            if (tryExtractFromSide(world, pos, dir)) {
                onExtractDirectionChanged(dir);
                return;
            }
        }
        onExtractDirectionChanged(null);
    }

    public List<PipeFluid> getFluids() {
        List<PipeFluid> result = new ArrayList<>();
        for (FluidPacket packet : fluids) {
            result.add(new PipeFluid(packet.variant, packet.amount, packet.direction, packet.progress));
        }
        return result;
    }

    private boolean tryMoveToNext(Level world, BlockPos pos, FluidPacket packet) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof FluidPipeBlockEntity pipe)) return false;
        if (be instanceof WoodFluidPipeBlockEntity) return false;
        if (pipe.fluids.size() >= MAX_PACKETS) return false;
        Direction newDir = getNextDirection(world, pos, packet);
        FluidPacket moved = new FluidPacket(packet.variant, packet.amount, newDir);
        moved.lastDirection = packet.direction;
        moved.life = packet.life;
        pipe.fluids.add(moved);
        pipe.syncToClient();
        return true;
    }

    private Direction getNextDirection(Level world, BlockPos pos, FluidPacket packet) {
        List<Direction> possible = new ArrayList<>();
        BlockState state = world.getBlockState(pos);
        for (Direction dir : Direction.values()) {
            if (isBlocked(dir)) continue;
            if (dir == packet.lastDirection.getOpposite()) continue;
            if (state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) possible.add(dir);
        }
        if (possible.isEmpty()) return packet.lastDirection.getOpposite();
        return possible.get(world.getRandom().nextInt(possible.size()));
    }

    private boolean tryInsert(Level world, BlockPos pos, FluidPacket packet) {
        if (isBlocked(packet.direction)) return false;
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, pos, packet.direction.getOpposite());
        if (storage == null) return false;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof WoodFluidPipeBlockEntity) {
            BlockState state = world.getBlockState(pos);
            Direction insertDir = packet.direction.getOpposite();
            BooleanProperty pullProp = WoodPipeBlock.PULL_PROPERTY_MAP.get(insertDir);
            if (pullProp != null && state.getValue(pullProp)) return false;
        }

        try (Transaction tx = Transaction.openOuter()) {
            long inserted = storage.insert(packet.variant, packet.amount, tx);
            if (inserted <= 0) return false;
            packet.amount -= inserted;
            if (packet.amount <= 0) packet.variant = FluidVariant.blank();
            tx.commit();
            syncToClient();
            return true;
        }
    }

    private boolean tryExtractFromSide(Level world, BlockPos pos, Direction dir) {
        if (isBlocked(dir)) return false;
        BlockPos targetPos = pos.relative(dir);
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, targetPos, dir.getOpposite());
        if (storage != null) {
            try (Transaction tx = Transaction.openOuter()) {
                StorageView<FluidVariant> view = null;
                for (StorageView<FluidVariant> v : storage) {
                    if (!v.isResourceBlank()) {
                        view = v;
                        break;
                    }
                }
                if (view != null) {
                    long extracted = storage.extract(view.getResource(), STEP, tx);
                    if (extracted > 0) {
                        fluids.add(new FluidPacket(view.getResource(), extracted, dir.getOpposite()));
                        tx.commit();
                        syncToClient();
                        return true;
                    }
                }
            }
        }

        // Fallback: pull directly from source fluid blocks (e.g. water/lava source).
        FluidState fluidState = world.getFluidState(targetPos);
        if (!fluidState.isEmpty() && fluidState.isSource()) {
            FluidVariant source = FluidVariant.of(fluidState.getType());
            fluids.add(new FluidPacket(source, FluidConstants.BLOCK, dir.getOpposite()));
            world.removeBlock(targetPos, false);
            syncToClient();
            return true;
        }

        return false;
    }

    private static final class FluidPacket {
        FluidVariant variant;
        long amount;
        Direction direction;
        Direction lastDirection;
        double progress;
        int life;

        FluidPacket(FluidVariant variant, long amount, Direction direction) {
            this.variant = variant;
            this.amount = amount;
            this.direction = direction;
            this.lastDirection = direction;
            this.progress = 0.0;
            this.life = MAX_LIFE;
        }
    }
    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput nbt) {
        super.loadAdditional(nbt);
        fluids.clear();
        int count = Math.min(nbt.getIntOr("pipe.fluid_count", 0), MAX_PACKETS);
        for (int i = 0; i < count; i++) {
            int fluidId = nbt.getIntOr("pipe.fid_" + i, -1);
            long amount = nbt.getLongOr("pipe.amt_" + i, 0L);
            int dirId = nbt.getIntOr("pipe.dir_" + i, -1);
            int progress = nbt.getIntOr("pipe.prog_" + i, 0);
            if (fluidId < 0 || dirId < 0 || amount <= 0) continue;
            Direction dir = Direction.from3DDataValue(dirId);
            if (dir == null) continue;
            FluidPacket packet = new FluidPacket(FluidVariant.of(BuiltInRegistries.FLUID.byId(fluidId)), amount, dir);
            packet.progress = progress / 1000.0;
            fluids.add(packet);
        }
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput nbt) {
        super.saveAdditional(nbt);
        int count = Math.min(fluids.size(), MAX_PACKETS);
        nbt.putInt("pipe.fluid_count", count);

        for (int i = 0; i < count; i++) {
            FluidPacket packet = fluids.get(i);
            nbt.putInt("pipe.fid_" + i, BuiltInRegistries.FLUID.getId(packet.variant.getFluid()));
            nbt.putLong("pipe.amt_" + i, packet.amount);
            nbt.putInt("pipe.dir_" + i, packet.direction.get3DDataValue());
            nbt.putInt("pipe.prog_" + i, (int) Math.round(packet.progress * 1000.0));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    private void syncToClient() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public static final class PipeFluid {
        public final FluidVariant variant;
        public final long amount;
        public final Direction direction;
        public final double progress;

        private PipeFluid(FluidVariant variant, long amount, Direction direction, double progress) {
            this.variant = variant;
            this.amount = amount;
            this.direction = direction;
            this.progress = progress;
        }
    }
}
