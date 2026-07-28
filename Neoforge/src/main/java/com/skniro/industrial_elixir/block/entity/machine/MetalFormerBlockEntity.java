package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.handler.machine.MetalFormerScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class MetalFormerBlockEntity extends AbstractMachineEntity {
    private MetalFormerState recipe_state = MetalFormerState.ROLLING;
    protected final ContainerData propertyDelegate;

    public MetalFormerBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.MetalFormer_BLOCK_ENTITY.get(),pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MetalFormerBlockEntity.this.progress;
                    case 1 -> MetalFormerBlockEntity.this.maxProgress;
                    case 2 -> recipe_state.ordinal();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: MetalFormerBlockEntity.this.progress = value;
                    case 1: MetalFormerBlockEntity.this.maxProgress = value;
                    case 2: recipe_state = MetalFormerState.values()[value];
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("metal_former.state", recipe_state.ordinal());
    }

    public RecipeType<?> getCurrentRecipeType() {
        return switch (recipe_state) {
            case ROLLING -> AlchemyRecipeType.METALFORMER_ROLLING.type.get();
            case CUTTING -> AlchemyRecipeType.METALFORMER_CUTTING.type.get();
            case EXTRUDING -> AlchemyRecipeType.METALFORMER_EXTRUDING.type.get();
        };
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        this.recipe_state = MetalFormerState.values()[nbt.getIntOr("metal_former.state", 0)];
        super.loadAdditional(nbt);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.MetalFormer);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new MetalFormerScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    public void nextState() {
        recipe_state = MetalFormerState.values()[(recipe_state.ordinal() + 1) % MetalFormerState.values().length];
        setChanged();
    }

    public MetalFormerState getState() {
        return recipe_state;
    }

    public void setState(MetalFormerState state) {
        this.recipe_state = state;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public enum MetalFormerState{
        ROLLING,
        CUTTING,
        EXTRUDING;
    }

}
