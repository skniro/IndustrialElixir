package com.skniro.industrial_elixir.block.entity.pipe;

import com.skniro.industrial_elixir.block.init.pipe.PipeBlock;
import com.skniro.industrial_elixir.block.init.pipe.WoodFluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class PipeBlockEntity extends BlockEntity implements ItemOwner, PipeExtractDirectionController {

    private final List<PipeItem> items = new ArrayList<>();
    private final EnumSet<Direction> blocked = EnumSet.noneOf(Direction.class);
    private @Nullable Direction preferredExtractDirection;
    private @Nullable Direction currentExtractingFrom;

    public PipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isBlocked(Direction dir) {
        return blocked.contains(dir);
    }

    public void setBlocked(Direction dir, boolean value) {
        if (value) {
            blocked.add(dir);
        } else {
            blocked.remove(dir);
        }
        setChanged();
    }

    @Override
    public @Nullable Direction getPreferredExtractDirection() {
        return preferredExtractDirection;
    }

    @Override
    public boolean setPreferredExtractDirection(@Nullable Direction direction) {
        if (preferredExtractDirection == direction) {
            return false;
        }

        preferredExtractDirection = direction;
        setChanged();

        if (level != null && !level.isClientSide()) {
            onExtractDirectionChanged(getDisplayExtractDirection(level, worldPosition));
        }

        return true;
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        updateConnections();
    }

    public List<PipeItem> getItems() {
        return new ArrayList<>(items);
    }

    public void neighborUpdate() {
        updateConnections();
        if (level != null && !level.isClientSide()) {
            onExtractDirectionChanged(getDisplayExtractDirection(level, worldPosition));
        }
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        preferredExtractDirection = readDirection(nbt.getIntOr("pipe.preferred_extract_side", -1));
        blocked.clear();
        for (Direction dir : Direction.values()) {
            if (nbt.getIntOr("pipe.blocked_" + dir.name(), 0) == 1) {
                blocked.add(dir);
            }
        }
        super.loadAdditional(nbt);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("pipe.preferred_extract_side",
                preferredExtractDirection == null ? -1 : preferredExtractDirection.get3DDataValue());
        for (Direction dir : Direction.values()) {
            nbt.putInt("pipe.blocked_" + dir.name(), blocked.contains(dir) ? 1 : 0);
        }
    }

    protected void updateConnections() {
        if (level == null || level.isClientSide()) {
            return;
        }

        BlockState state = getBlockState();
        BlockState newState = state;

        for (Direction dir : Direction.values()) {
            boolean connected = isConnectable(dir);
            newState = newState.setValue(PipeBlock.PROPERTY_MAP.get(dir), connected);
        }

        if (newState != state) {
            level.setBlockAndUpdate(worldPosition, newState);
        }
    }

    protected boolean isConnectable(Direction dir) {
        return false;
    }

    void moveItems(Level world, BlockPos pos, BlockState state) {
        Iterator<PipeItem> it = items.iterator();

        while (it.hasNext()) {
            PipeItem item = it.next();

            if (isBlocked(item.direction)) {
                continue;
            }

            item.progress += 0.1;

            if (item.progress >= 1.0) {
                BlockPos target = pos.relative(item.direction);


                it.remove();
            }
        }
        
        if (items.isEmpty()) {
            currentExtractingFrom = null;
        }
    }

    private boolean tryMoveToNext(Level world, BlockPos pos, PipeItem item) {
        BlockEntity be = world.getBlockEntity(pos);

        if (be instanceof PipeBlockEntity pipe) {
            if (isBlocked(item.direction)) {
                return false;
            }
            
            Direction fromDir = item.direction.getOpposite();
            if (pipe.getCurrentExtractingFrom() == fromDir) {
                return false;
            }
            
            Direction newDir = getNextDirection(world, pos, item);

            PipeItem newItem = new PipeItem(item.variant, item.amount, newDir);
            newItem.lastDirection = item.direction;
            // 只有目标管道items容量未超限时才转移，否则不转移
            if (pipe.items.size() < 64) { // 假定最大堆叠数64，可根据实际调整
                pipe.items.add(newItem);
                return true;
            } else {
                // 目标管道已满，转移失败，物品保留在当前管道
                return false;
            }
        }

        return false;
    }

    private Direction getNextDirection(Level world, BlockPos pos, Direction from) {
        List<Direction> possible = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            if (dir == from.getOpposite()) {
                continue;
            }
            if (isBlocked(dir)) {
                continue;
            }
            BlockState state = world.getBlockState(pos);
            if (state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) {
                possible.add(dir);
            }
        }

        if (possible.isEmpty()) {
            return from;
        }

        return possible.get(world.getRandom().nextInt(possible.size()));
    }

    private Direction getNextDirection(Level world, BlockPos pos, PipeItem item) {
        List<Direction> possible = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            if (isBlocked(dir)) {
                continue;
            }
            if (dir == item.lastDirection.getOpposite()) {
                continue;
            }

            BlockState state = world.getBlockState(pos);
            if (state.getValue(PipeBlock.PROPERTY_MAP.get(dir))) {
                possible.add(dir);
            }
        }

        if (possible.isEmpty()) {
            return item.lastDirection.getOpposite();
        }

        return possible.get(world.getRandom().nextInt(possible.size()));
    }

    protected void onExtractDirectionChanged(@Nullable Direction direction) {
    }

    private @Nullable Direction getDisplayExtractDirection(Level world, BlockPos pos) {
        Direction preferredDir = getActivePreferredExtractDirection(world, pos);
        if (preferredDir != null) {
            return preferredDir;
        }
        return findFirstConnectedStorageSide(world, pos);
    }

    /**
     * Returns the player-configured extract direction while it is still valid
     * (not blocked and still connected). The actual presence of fluid is checked
     * by the extraction logic later, so the displayed direction reflects the
     * player's choice even while the source is temporarily empty.
     */
    protected @Nullable Direction getActivePreferredExtractDirection(Level world, BlockPos pos) {
        if (preferredExtractDirection == null) {
            return null;
        }

        if (isBlocked(preferredExtractDirection)) {
            return null;
        }

        BlockState state = world.getBlockState(pos);
        BooleanProperty property = PipeBlock.PROPERTY_MAP.get(preferredExtractDirection);
        if (property == null || !state.getValue(property)) {
            return null;
        }

        return preferredExtractDirection;
    }

    /**
     * Finds the first connected, non-blocked side that exposes a fluid capability
     * or is a source fluid block. Used for the automatic extraction indication
     * when no preferred direction is configured.
     */
    private @Nullable Direction findFirstConnectedStorageSide(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        for (Direction dir : Direction.values()) {
            if (isBlocked(dir)) {
                continue;
            }
            BooleanProperty property = PipeBlock.PROPERTY_MAP.get(dir);
            if (property == null || !state.getValue(property)) {
                continue;
            }
            BlockPos target = pos.relative(dir);
            if (world.getCapability(Capabilities.Fluid.BLOCK, target, dir.getOpposite()) != null) {
                return dir;
            }
            if (world.getFluidState(target).isSource()) {
                return dir;
            }
        }
        return null;
    }

    private @Nullable Direction tryExtractFromAuto(Level world, BlockPos pos) {
        return null;
    }


    
    public @Nullable Direction getCurrentExtractingFrom() {
        return currentExtractingFrom;
    }

    private static @Nullable Direction readDirection(int value) {
        if (value < 0 || value >= Direction.values().length) {
            return null;
        }
        return Direction.from3DDataValue(value);
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
        return 0;
    }

    public class PipeItem {
        public ItemResource variant;
        public long amount;

        public Direction direction;
        public Direction lastDirection;

        public double progress;

        public PipeItem(ItemResource variant, long amount, Direction dir) {
            this.variant = variant;
            this.amount = amount;
            this.direction = dir;
            this.lastDirection = dir;
            this.progress = 0.0;
        }
    }
}
