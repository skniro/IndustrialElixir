package com.skniro.industrial_elixir.block.entity.pipe;

import com.skniro.industrial_elixir.api.fluid.FluidConstants;
import com.skniro.industrial_elixir.block.init.pipe.PipeBlock;
import com.skniro.industrial_elixir.block.init.pipe.WoodFluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class FluidPipeBlockEntity extends PipeBlockEntity {
    private static final int STEP = 100;
    private static final int MAX_PACKETS = 64;
    private static final int BUCKET_VOLUME_MB = FluidConstants.BUCKET / 81;
    private static final int PIPE_CAPACITY = BUCKET_VOLUME_MB * 4;
    private static final double ARRIVAL_EPSILON = 1.0E-6;
    private final List<FluidPacket> fluids = new ArrayList<>();

    private final IFluidHandler fluidHandler = new PipeFluidHandler();

    protected FluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected boolean isConnectable(Direction dir) {
        if (isBlocked(dir)) return false;
        BlockPos target = worldPosition.relative(dir);
        BlockEntity be = level.getBlockEntity(target);
        if (be instanceof FluidPipeBlockEntity) return true;
        var storage = level.getCapability(Capabilities.FluidHandler.BLOCK, target, dir.getOpposite());
        return storage != null;
    }

    protected void moveFluids(Level world, BlockPos pos) {
        boolean changed = false;
        Iterator<FluidPacket> it = fluids.iterator();

        while (it.hasNext()) {
            FluidPacket packet = it.next();

            if (packet.variant.isEmpty() || packet.amount <= 0) {
                it.remove();
                changed = true;
                continue;
            }

            if (isBlocked(packet.direction)) {
                // Direction is manually blocked - try to re-route the packet through
                // another connected side instead of leaving it stuck forever.
                if (tryRoutePacket(world, pos, packet)) {
                    changed = true;
                    if (packet.amount <= 0 || packet.variant.isEmpty()) {
                        it.remove();
                    } else {
                        packet.progress = 1.0;
                    }
                }
                continue;
            }

            packet.progress += 0.1;
            if (packet.progress + ARRIVAL_EPSILON < 1.0) continue;
            packet.progress = 1.0;

            if (tryRoutePacket(world, pos, packet)) {
                changed = true;
                // Only remove the packet when it has been fully transferred/inserted.
                if (packet.amount <= 0 || packet.variant.isEmpty()) {
                    it.remove();
                } else {
                    // Partial transfer/insert: keep the remainder in this pipe.
                    packet.progress = 1.0;
                }
            } else {
                // No path available right now; keep the packet and retry next tick.
                packet.progress = 1.0;
            }
        }

        if (changed) {
            syncToClient();
        }
    }

    protected void extractFluids(Level world, BlockPos pos) {
        if (getFluidVolume() >= PIPE_CAPACITY) return;
        if (fluids.size() >= MAX_PACKETS) return;

        // If the player has configured a preferred extract direction (via
        // right-click), only pull from that side and keep it highlighted, even
        // while the source is temporarily unavailable.
        Direction preferredDir = getActivePreferredExtractDirection(world, pos);
        if (preferredDir != null) {
            if (tryExtractFromSide(world, pos, preferredDir)) {
                onExtractDirectionChanged(preferredDir);
                return;
            }
            onExtractDirectionChanged(preferredDir);
            return;
        }

        // No preferred direction: scan all connected sides.
        for (Direction dir : Direction.values()) {
            boolean canExtract = hasOutputPath(world, pos, dir)
                    || world.getFluidState(pos.relative(dir)).isSource();
            if (!canExtract) continue;
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

    /**
     * Return a Neoforge {@link IFluidHandler} that exposes this pipe's internal fluid packets
     * so adjacent blocks can insert/extract via the standard Neoforge fluid API.
     */
    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    private int getFluidVolume() {
        int total = 0;
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
            if (world.getCapability(Capabilities.FluidHandler.BLOCK, next, dir.getOpposite()) != null) return true;
            BlockEntity nextBe = world.getBlockEntity(next);
            if (nextBe instanceof FluidPipeBlockEntity) {
                if (searchOutputPath(world, next, dir.getOpposite(), visited)) return true;
            }
        }
        return false;
    }

    /**
     * Attempt to transfer a packet out of this pipe. The packet's current direction is tried
     * first, then every other connected, non-blocked side. This lets packets be re-routed
     * around dead ends and makes the initial direction chosen when a packet was created
     * (e.g. the side opposite a water source) non-critical.
     */
    private boolean tryRoutePacket(Level world, BlockPos pos, FluidPacket packet) {
        // Try the packet's current direction first (unless manually blocked).
        if (!isBlocked(packet.direction)) {
            BlockPos target = pos.relative(packet.direction);
            if (tryMoveToNext(world, target, packet.direction, packet)) return true;
            if (tryInsert(world, target, packet.direction, packet)) return true;
        }

        // Then try every other connected, non-blocked side.
        BlockState state = world.getBlockState(pos);
        for (Direction dir : Direction.values()) {
            if (dir == packet.direction) continue;
            if (isBlocked(dir)) continue;
            if (packet.lastDirection != null && dir == packet.lastDirection.getOpposite()) continue;
            BooleanProperty property = PipeBlock.PROPERTY_MAP.get(dir);
            if (property == null || !state.getValue(property)) continue;

            BlockPos target = pos.relative(dir);
            if (tryMoveToNext(world, target, dir, packet)) return true;
            if (tryInsert(world, target, dir, packet)) return true;
        }
        return false;
    }

    private boolean tryMoveToNext(Level world, BlockPos pos, Direction moveDir, FluidPacket packet) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof FluidPipeBlockEntity pipe)) return false;
        if (be instanceof WoodFluidPipeBlockEntity) return false;
        if (pipe.fluids.size() >= MAX_PACKETS) return false;
        int available = PIPE_CAPACITY - pipe.getFluidVolume();
        if (available <= 0) return false;
        int moveAmount = Math.min(packet.amount, available);
        if (moveAmount <= 0) return false;
        Direction newDir = getNextDirection(world, pos, moveDir);
        FluidPacket moved = new FluidPacket(packet.variant, moveAmount, newDir);
        moved.lastDirection = moveDir;
        pipe.fluids.add(moved);
        packet.amount -= moveAmount;
        if (packet.amount <= 0) {
            packet.amount = 0;
            packet.variant = FluidStack.EMPTY;
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
        for (Direction dir : Direction.values()) {
            if (pipe.isBlocked(dir)) continue;
            if (dir == moveDir.getOpposite()) continue;
            BooleanProperty property = PipeBlock.PROPERTY_MAP.get(dir);
            if (property != null && state.getValue(property)) possible.add(dir);
        }
        if (possible.isEmpty()) return moveDir.getOpposite();
        return possible.get(world.getRandom().nextInt(possible.size()));
    }

    private boolean tryInsert(Level world, BlockPos pos, Direction insertDir, FluidPacket packet) {
        if (isBlocked(insertDir)) return false;
        IFluidHandler storage = world.getCapability(Capabilities.FluidHandler.BLOCK, pos, insertDir.getOpposite());
        if (storage == null) return false;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof WoodFluidPipeBlockEntity) {
            BlockState state = world.getBlockState(pos);
            Direction pullSide = insertDir.getOpposite();
            BooleanProperty pullProp = WoodFluidPipeBlock.PULL_PROPERTY_MAP.get(pullSide);
            if (pullProp != null && state.getValue(pullProp)) return false;
        }
        int before = packet.amount;
        int inserted = storage.fill(packet.variant.copyWithAmount(before), IFluidHandler.FluidAction.EXECUTE);
        if (inserted <= 0) return false;
        packet.amount -= inserted;
        if (packet.amount <= 0) {
            packet.amount = 0;
            packet.variant = FluidStack.EMPTY;
        }
        syncToClient();
        return true;
    }

    private boolean tryExtractFromSide(Level world, BlockPos pos, Direction dir) {
        if (isBlocked(dir)) return false;
        BlockPos targetPos = pos.relative(dir);
        IFluidHandler storage = world.getCapability(Capabilities.FluidHandler.BLOCK, targetPos, dir.getOpposite());
        if (storage != null) {
            FluidStack resource = storage.getFluidInTank(0);
            if (!resource.isEmpty()) {
                int toExtract = Math.min(STEP, PIPE_CAPACITY - getFluidVolume());
                if (toExtract > 0) {
                    FluidStack drained = storage.drain(resource.copyWithAmount(toExtract), IFluidHandler.FluidAction.EXECUTE);
                    if (!drained.isEmpty()) {
                        fluids.add(new FluidPacket(variantOf(drained), drained.getAmount(), dir.getOpposite()));
                        syncToClient();
                        return true;
                    }
                }
            }
            // If the adjacent storage exists but is empty (or extraction failed),
            // fall through and try to pull directly from a source fluid block.
        }

        // Fallback: pull directly from source fluid blocks (e.g. water/lava source).
        FluidState fluidState = world.getFluidState(targetPos);
        if (!fluidState.isEmpty() && fluidState.isSource()) {
            int amount = Math.min(STEP, PIPE_CAPACITY - getFluidVolume());
            if (amount <= 0) return false;
            FluidStack source = new FluidStack(fluidState.getType(), amount);
            fluids.add(new FluidPacket(variantOf(source), amount, dir.getOpposite()));
            syncToClient();
            return true;
        }

        return false;
    }

    /**
     * Neoforge {@link IFluidHandler} that wraps the pipe's fluid packets.
     */
    private class PipeFluidHandler implements IFluidHandler {

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            for (FluidPacket packet : fluids) {
                if (!packet.variant.isEmpty() && packet.amount > 0) {
                    return packet.variant.copyWithAmount(packet.amount);
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return PIPE_CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return getFluidVolume() < PIPE_CAPACITY && fluids.size() < MAX_PACKETS;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return 0;

            int volume = getFluidVolume();
            if (volume >= PIPE_CAPACITY || fluids.size() >= MAX_PACKETS) return 0;
            int toInsert = Math.min(resource.getAmount(), PIPE_CAPACITY - volume);
            if (toInsert <= 0) return 0;

            // Try to merge into an existing matching packet first
            for (FluidPacket packet : fluids) {
                if (packet.variant.isFluidEqual(resource)) {
                    int added = Math.min(toInsert, PIPE_CAPACITY - getFluidVolume());
                    if (added <= 0) return 0;
                    if (action.execute()) {
                        packet.amount += added;
                        syncToClient();
                    }
                    return added;
                }
            }

            // New packet - the incoming direction is unknown, so lastDirection is
            // null. This lets tryRoutePacket send it out through any connected side.
            if (action.execute()) {
                FluidPacket newPacket = new FluidPacket(variantOf(resource), toInsert, Direction.NORTH);
                newPacket.lastDirection = null;
                fluids.add(newPacket);
                syncToClient();
            }
            return toInsert;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return FluidStack.EMPTY;

            Iterator<FluidPacket> it = fluids.iterator();
            while (it.hasNext()) {
                FluidPacket packet = it.next();
                if (packet.variant.isFluidEqual(resource)) {
                    int toExtract = Math.min(resource.getAmount(), packet.amount);
                    if (toExtract <= 0) return FluidStack.EMPTY;
                    if (action.execute()) {
                        packet.amount -= toExtract;
                        if (packet.amount <= 0) {
                            it.remove();
                        }
                        syncToClient();
                    }
                    return resource.copyWithAmount(toExtract);
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (maxDrain <= 0) return FluidStack.EMPTY;

            Iterator<FluidPacket> it = fluids.iterator();
            while (it.hasNext()) {
                FluidPacket packet = it.next();
                if (!packet.variant.isEmpty() && packet.amount > 0) {
                    int toExtract = Math.min(maxDrain, packet.amount);
                    if (toExtract <= 0) return FluidStack.EMPTY;
                    FluidStack drained = packet.variant.copyWithAmount(toExtract);
                    if (action.execute()) {
                        packet.amount -= toExtract;
                        if (packet.amount <= 0) {
                            it.remove();
                        }
                        syncToClient();
                    }
                    return drained;
                }
            }
            return FluidStack.EMPTY;
        }
    }

    private static FluidStack variantOf(FluidStack stack) {
        return stack.copyWithAmount(1);
    }

    private static final class FluidPacket {
        FluidStack variant;
        int amount;
        Direction direction;
        Direction lastDirection;
        double progress;

        FluidPacket(FluidStack variant, int amount, Direction direction) {
            this.variant = variant;
            this.amount = amount;
            this.direction = direction;
            this.lastDirection = direction;
            this.progress = 0.0;
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        fluids.clear();
        int count = Math.min((nbt.contains("pipe.fluid_count") ? nbt.getInt("pipe.fluid_count") : 0), MAX_PACKETS);
        for (int i = 0; i < count; i++) {
            Tag variantTag = nbt.get("pipe.variant_" + i);
            FluidStack variant = variantTag == null ? FluidStack.EMPTY
                    : FluidStack.parse(registries, variantTag).orElse(FluidStack.EMPTY);
            int amount = (nbt.contains("pipe.amt_" + i) ? nbt.getInt("pipe.amt_" + i) : 0);
            int dirId = (nbt.contains("pipe.dir_" + i) ? nbt.getInt("pipe.dir_" + i) : -1);
            int lastDirId = (nbt.contains("pipe.ldir_" + i) ? nbt.getInt("pipe.ldir_" + i) : -1);
            int progress = (nbt.contains("pipe.prog_" + i) ? nbt.getInt("pipe.prog_" + i) : 0);
            if (variant.isEmpty() || dirId < 0 || amount <= 0) continue;
            Direction dir = Direction.from3DDataValue(dirId);
            if (dir == null) continue;
            FluidPacket packet = new FluidPacket(variant, amount, dir);
            packet.progress = progress / 1000.0;
            packet.lastDirection = lastDirId >= 0 ? Direction.from3DDataValue(lastDirId) : null;
            if (packet.lastDirection == null && lastDirId >= 0) packet.lastDirection = dir;
            fluids.add(packet);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        int count = Math.min(fluids.size(), MAX_PACKETS);
        nbt.putInt("pipe.fluid_count", count);

        for (int i = 0; i < count; i++) {
            FluidPacket packet = fluids.get(i);
            nbt.put("pipe.variant_" + i, packet.variant.save(registries));
            nbt.putInt("pipe.amt_" + i, packet.amount);
            nbt.putInt("pipe.dir_" + i, packet.direction.get3DDataValue());
            nbt.putInt("pipe.ldir_" + i, packet.lastDirection != null ? packet.lastDirection.get3DDataValue() : -1);
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
        public final FluidStack variant;
        public final long amount;
        public final Direction direction;
        public final double progress;

        private PipeFluid(FluidStack variant, long amount, Direction direction, double progress) {
            this.variant = variant;
            this.amount = amount;
            this.direction = direction;
            this.progress = progress;
        }
    }
}
