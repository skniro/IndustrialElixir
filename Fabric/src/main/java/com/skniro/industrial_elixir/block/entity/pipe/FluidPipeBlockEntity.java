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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class FluidPipeBlockEntity extends PipeBlockEntity {
    private static final long STEP = 100;
    private static final int MAX_PACKETS = 64;
    private static final long BUCKET_VOLUME_MB = FluidConstants.BUCKET / 81;
    private static final long PIPE_CAPACITY = BUCKET_VOLUME_MB * 4;
    private static final double ARRIVAL_EPSILON = 1.0E-6;
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

            if (packet.variant.isBlank() || packet.amount <= 0) {
                it.remove();
                changed = true;
                continue;
            }

            packet.progress += 0.1;
            if (packet.progress + ARRIVAL_EPSILON < 1.0) continue;
            packet.progress = 1.0;

            if (tryRoutePacket(world, pos, packet)) {
                changed = true;

                // 只有 Packet 完全转移/插入后才能删除
                if (packet.amount <= 0 || packet.variant.isBlank()) {
                    it.remove();
                } else {
                    // 还有剩余液体，继续留在当前管道
                    packet.progress = 1.0;
                }

                continue;
            }

            packet.progress = 1.0;
        }

        if (changed) {
            syncToClient();
        }
    }

    /**
     * 尝试将流体包路由到其他管道或机器。
     * 优先沿当前方向移动，失败时尝试其余已连接的、非封堵的方向（避免回流）。
     */
    private boolean tryRoutePacket(Level world, BlockPos pos, FluidPacket packet) {
        Direction backDir = packet.lastDirection != null ? packet.lastDirection.getOpposite() : null;

        // 先尝试按当前方向移动/插入
        if (!isBlocked(packet.direction)) {
            BlockPos target = pos.relative(packet.direction);
            if (tryMoveToNext(world, target, packet.direction, packet)
                    || tryInsert(world, target, packet.direction, packet)) {
                return true;
            }
        }

        // 再尝试其他已连接的、非封堵的方向（排除回流方向）
        BlockState state = getBlockState();
        for (Direction dir : Direction.values()) {
            if (dir == packet.direction) continue;
            if (backDir != null && dir == backDir) continue;
            if (isBlocked(dir)) continue;
            BooleanProperty property = PipeBlock.PROPERTY_MAP.get(dir);
            if (property == null || !state.getValue(property)) continue;

            BlockPos target = pos.relative(dir);
            if (tryMoveToNext(world, target, dir, packet)
                    || tryInsert(world, target, dir, packet)) {
                return true;
            }
        }

        return false;
    }

    protected void extractFluids(Level world, BlockPos pos) {
        if (getFluidVolume() >= PIPE_CAPACITY) return;
        if (fluids.size() >= MAX_PACKETS) return;

        Direction preferredDir = getPreferredExtractDirection();

        // 玩家指定了抽取方向
        if (preferredDir != null) {

            // ① 优先从 FluidStorage 抽取
            if (tryExtractFromSide(world, pos, preferredDir)) {
                onExtractDirectionChanged(preferredDir);
                return;
            }

            // ② FluidStorage 抽取失败，尝试吸取水源方块
            if (tryExtractSourceFluid(world, pos, preferredDir)) {
                onExtractDirectionChanged(preferredDir);
                return;
            }

            // 保持当前指示方向
            onExtractDirectionChanged(preferredDir);
            return;
        }

/*        if (preferredDir != null) {
            boolean canExtract = hasOutputPath(world, pos, preferredDir)
                    || world.getFluidState(pos.relative(preferredDir)).isSource();
            if (canExtract && tryExtractFromSide(world, pos, preferredDir)) {
                onExtractDirectionChanged(preferredDir);
            } else {
                onExtractDirectionChanged(preferredDir);
            }
            return;
        }*/

        for (Direction dir : Direction.values()) {
            if (!hasOutputPath(world, pos, dir)) continue;
            if (tryExtractFromSide(world, pos, dir)) {
                onExtractDirectionChanged(dir);
                return;
            }
        }

        for (Direction dir : Direction.values()) {
            if (tryExtractSourceFluid(world, pos, dir)) {
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

    private long getFluidVolume() {
        long total = 0;
        for (FluidPacket packet : fluids) {
            total += packet.amount;
        }
        return total;
    }

    private boolean hasOutputPath(Level world, BlockPos pos, Direction extractDir) {
        return searchOutputPath(world, pos, extractDir, new HashSet<>());
    }

    private boolean searchOutputPath(Level world, BlockPos pos, Direction excludeDir, Set<BlockPos> visited) {
        if (!visited.add(pos)) return false;
        BlockState state = world.getBlockState(pos);
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof FluidPipeBlockEntity pipe)) return false;

        for (Direction dir : Direction.values()) {
            if (dir == excludeDir) continue;
            if (pipe.isBlocked(dir)) continue;
            if (!state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) continue;

            BlockPos next = pos.relative(dir);
            if (FluidStorage.SIDED.find(world, next, dir.getOpposite()) != null) return true;

            BlockEntity nextBe = world.getBlockEntity(next);
            if (nextBe instanceof FluidPipeBlockEntity) {
                if (searchOutputPath(world, next, dir.getOpposite(), visited)) return true;
            }
        }
        return false;
    }

    private boolean tryMoveToNext(Level world, BlockPos pos, Direction moveDir, FluidPacket packet) {
        if (isBlocked(moveDir)) return false;
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof FluidPipeBlockEntity pipe)) return false;
        if (be instanceof WoodFluidPipeBlockEntity) return false;
        if (pipe.fluids.size() >= MAX_PACKETS) return false;
        long available = PIPE_CAPACITY - pipe.getFluidVolume();
        if (available <= 0) return false;
        long moveAmount = Math.min(packet.amount, available);
        if (moveAmount <= 0) return false;
        Direction newDir = getNextDirection(world, pos, moveDir);
        // 目标管道没有向前的出口，避免进入后立即被送回（来回弹跳）
        if (newDir == moveDir.getOpposite()) return false;
        FluidPacket moved = new FluidPacket(packet.variant, moveAmount, newDir);
        moved.lastDirection = moveDir;
        pipe.fluids.add(moved);
        packet.amount -= moveAmount;
        if (packet.amount <= 0) {
            packet.amount = 0;
            packet.variant = FluidVariant.blank();
        }
        pipe.syncToClient();
        return true;
    }

    private Direction getNextDirection(Level world, BlockPos pos, Direction moveDir) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof FluidPipeBlockEntity pipe)) {
            return moveDir.getOpposite();
        }
        List<Direction> possible = new ArrayList<>();
        BlockState state = world.getBlockState(pos);
        Direction backDir = moveDir.getOpposite();
        for (Direction dir : Direction.values()) {
            if (pipe.isBlocked(dir)) continue;
            if (dir == backDir) continue;
            BooleanProperty property = PipeBlock.PROPERTY_MAP.get(dir);
            if (property != null && state.getValue(property)) possible.add(dir);
        }
        if (possible.isEmpty()) return backDir;
        return possible.get(world.getRandom().nextInt(possible.size()));
    }

    private boolean tryInsert(Level world, BlockPos pos, Direction insertDir, FluidPacket packet) {
        if (isBlocked(insertDir)) return false;
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, pos, insertDir.getOpposite());
        if (storage == null) return false;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof WoodFluidPipeBlockEntity) {
            BlockState state = world.getBlockState(pos);
            Direction pullSide = insertDir.getOpposite();
            BooleanProperty pullProp = WoodPipeBlock.PULL_PROPERTY_MAP.get(pullSide);
            if (pullProp != null && state.getValue(pullProp)) return false;
        }
        long before = packet.amount;
        try (Transaction tx = Transaction.openOuter()) {
            long inserted = storage.insert(packet.variant, before, tx);
            if (inserted <= 0) return false;
            packet.amount -= inserted;
            tx.commit();
            if (packet.amount <= 0) {
                packet.amount = 0;
                packet.variant = FluidVariant.blank();
            }
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
                    FluidVariant resource = view.getResource();
                    long toExtract = Math.min(STEP, PIPE_CAPACITY - getFluidVolume());
                    if (toExtract <= 0) return false;
                    long extracted = storage.extract(resource, toExtract, tx);
                    if (extracted > 0) {
                        fluids.add(new FluidPacket(resource, extracted, dir.getOpposite()));
                        tx.commit();
                        syncToClient();
                        return true;
                    }
                }
            }
        }

        // Fallback: pull directly from source fluid blocks (e.g. water/lava source).
/*        FluidState fluidState = world.getFluidState(targetPos);
        if (!fluidState.isEmpty() && fluidState.isSource()) {
            FluidVariant source = FluidVariant.of(fluidState.getType());
            fluids.add(new FluidPacket(source, BUCKET_VOLUME_MB, dir.getOpposite()));
            world.removeBlock(targetPos, false);
            syncToClient();
            return true;
        }*/

        return false;
    }

    private boolean tryExtractSourceFluid(Level world, BlockPos pos, Direction dir) {
        if (isBlocked(dir)) return false;
        BlockPos targetPos = pos.relative(dir);
        FluidState fluidState = world.getFluidState(targetPos);
        if (!fluidState.isSource()) {
            return false;
        }
        long amount = Math.min(STEP, PIPE_CAPACITY - getFluidVolume());
        if (amount <= 0) {
            return false;
        }

        FluidVariant source = FluidVariant.of(fluidState.getType());
        fluids.add(new FluidPacket(source, amount, dir.getOpposite()));
        syncToClient();

        return true;
    }

    private static final class FluidPacket {
        FluidVariant variant;
        long amount;
        Direction direction;
        Direction lastDirection;
        double progress;

        FluidPacket(FluidVariant variant, long amount, Direction direction) {
            this.variant = variant;
            this.amount = amount;
            this.direction = direction;
            this.lastDirection = direction;
            this.progress = 0.0;
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
            int lastDirId = nbt.getIntOr("pipe.ldir_" + i, -1);
            int progress = nbt.getIntOr("pipe.prog_" + i, 0);
            if (fluidId < 0 || dirId < 0 || amount <= 0) continue;
            Direction dir = Direction.from3DDataValue(dirId);
            if (dir == null) continue;
            FluidPacket packet = new FluidPacket(FluidVariant.of(BuiltInRegistries.FLUID.byId(fluidId)), amount, dir);
            packet.progress = progress / 1000.0;
            packet.lastDirection = lastDirId >= 0 ? Direction.from3DDataValue(lastDirId) : dir;
            if (packet.lastDirection == null) packet.lastDirection = dir;
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
            nbt.putInt("pipe.ldir_" + i, packet.lastDirection.get3DDataValue());
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
