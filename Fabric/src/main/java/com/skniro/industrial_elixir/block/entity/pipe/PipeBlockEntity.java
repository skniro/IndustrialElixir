package com.skniro.industrial_elixir.block.entity.pipe;

import com.skniro.industrial_elixir.block.init.pipe.PipeBlock;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
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
        super.loadAdditional(nbt);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("pipe.preferred_extract_side",
                preferredExtractDirection == null ? -1 : preferredExtractDirection.get3DDataValue());
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
        if (isBlocked(dir)) {
            return false;
        }
        BlockPos target = worldPosition.relative(dir);
        BlockState neighborState = level.getBlockState(target);
        Block neighborBlock = neighborState.getBlock();

        if (neighborBlock instanceof PipeBlock) {
            return true;
        }

        Storage<ItemVariant> storage =
                ItemStorage.SIDED.find(level, target, dir.getOpposite());

        return storage != null;
    }

    void moveItems(Level world, BlockPos pos, BlockState state) {
        Iterator<PipeItem> it = items.iterator();

        while (it.hasNext()) {
            PipeItem item = it.next();

            if (isBlocked(item.direction)) {
                continue;
            }

            if (item.variant.isBlank() || item.amount <= 0) {
                it.remove();
                continue;
            }

            item.progress += 0.1;

            if (item.progress >= 1.0) {
                BlockPos target = pos.relative(item.direction);

                if (!tryMoveToNext(world, target, item)
                        && !tryInsert(world, target, item)) {

                    item.progress = 1.0;
                    continue;
                }

                it.remove();
            }
        }
        
        if (items.isEmpty()) {
            currentExtractingFrom = null;
        }
    }

    private boolean tryMoveToNext(Level world, BlockPos pos, PipeItem item) {
        BlockEntity be = world.getBlockEntity(pos);

        if (be instanceof WoodPipeBlockEntity) {
            // 木头管道不接受其他管道的物品
            return false;
        }
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

    private boolean tryInsert(Level world, BlockPos pos, PipeItem item) {
        if (item.variant.isBlank() || item.amount <= 0) {
            return false;
        }

        if (isBlocked(item.direction)) {
            return false;
        }
        Storage<ItemVariant> storage =
                ItemStorage.SIDED.find(world, pos, item.direction.getOpposite());

        if (storage == null) {
            return false;
        }

        // 禁止向木头管道的pull面插入物品
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof WoodPipeBlockEntity) {
            BlockState state = world.getBlockState(pos);
            Direction insertDir = item.direction.getOpposite();
            BooleanProperty pullProp = com.skniro.industrial_elixir.block.init.pipe.WoodPipeBlock.PULL_PROPERTY_MAP.get(insertDir);
            if (pullProp != null && state.getValue(pullProp)) {
                return false;
            }
        }
        try (Transaction tx = Transaction.openOuter()) {
            long inserted = storage.insert(item.variant, item.amount, tx);

            if (inserted > 0) {
                item.amount -= inserted;
                if (item.amount <= 0) {
                    item.variant = ItemVariant.blank();
                }
                tx.commit();
                return true;
            } else {
                // 插入失败，物品保留在当前管道，等待下次tick
                return false;
            }
        }

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

    void extractItems(Level world, BlockPos pos, BlockState state) {
        if (!items.isEmpty()) {
            onExtractDirectionChanged(getDisplayExtractDirection(world, pos));
            return;
        }

        Direction preferredDir = getActivePreferredExtractDirection(world, pos);
        if (preferredDir != null) {
            onExtractDirectionChanged(preferredDir);
            tryExtractFromSide(world, pos, preferredDir);
            return;
        }

        Direction extractedDir = tryExtractFromAuto(world, pos);
        if (extractedDir != null) {
            onExtractDirectionChanged(extractedDir);
            return;
        }

        onExtractDirectionChanged(findFirstConnectedStorageSide(world, pos));
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

    private @Nullable Direction getActivePreferredExtractDirection(Level world, BlockPos pos) {
        if (preferredExtractDirection == null) {
            return null;
        }

        if (isBlocked(preferredExtractDirection)) {
            return null;
        }

        return findStorage(world, pos, preferredExtractDirection) == null ? null : preferredExtractDirection;
    }

    private @Nullable Direction findFirstConnectedStorageSide(Level world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (isBlocked(dir)) {
                continue;
            }
            if (findStorage(world, pos, dir) != null) {
                return dir;
            }
        }
        return null;
    }

    private @Nullable Direction tryExtractFromAuto(Level world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (tryExtractFromSide(world, pos, dir)) {
                return dir;
            }
        }
        return null;
    }

    private boolean tryExtractFromSide(Level world, BlockPos pos, Direction dir) {
        if (isBlocked(dir)) {
            return false;
        }

        Storage<ItemVariant> storage = findStorage(world, pos, dir);
        if (storage == null) {
            return false;
        }

        try (Transaction tx = Transaction.openOuter()) {
            StorageView<ItemVariant> view = null;

            for (StorageView<ItemVariant> next : storage) {
                if (!next.isResourceBlank()) {
                    view = next;
                    break;
                }
            }

            if (view == null) {
                return false;
            }

            long extracted = storage.extract(view.getResource(), 1, tx);
            if (extracted <= 0) {
                return false;
            }

            currentExtractingFrom = dir;
            Direction outDir = dir.getOpposite();
            items.add(new PipeItem(view.getResource(), extracted, outDir));
            tx.commit();
            return true;
        }
    }
    
    public @Nullable Direction getCurrentExtractingFrom() {
        return currentExtractingFrom;
    }

    private @Nullable Storage<ItemVariant> findStorage(Level world, BlockPos pos, Direction dir) {
        BlockPos target = pos.relative(dir);
        return ItemStorage.SIDED.find(world, target, dir.getOpposite());
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
        public ItemVariant variant;
        public long amount;

        public Direction direction;
        public Direction lastDirection;

        public double progress;

        public PipeItem(ItemVariant variant, long amount, Direction dir) {
            this.variant = variant;
            this.amount = amount;
            this.direction = dir;
            this.lastDirection = dir;
            this.progress = 0.0;
        }
    }
}
