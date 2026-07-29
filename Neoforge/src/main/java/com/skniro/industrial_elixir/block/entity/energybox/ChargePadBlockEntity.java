package com.skniro.industrial_elixir.block.entity.energybox;

import com.skniro.industrial_elixir.api.block.ImplementedInventory;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.energybox.ChargePadScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class ChargePadBlockEntity extends BaseEnergyBoxBlockEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);

    private static final int Battery_OUTPUT_SLOT = 0;
    private static final int Battery_INPUT_SLOT = 1;


    public ChargePadBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.ChargePad_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putLong(("coal_generator.energy"), energyContainer.amount);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        energyContainer.amount = nbt.getLongOr("coal_generator.energy", 0);
        super.loadAdditional(nbt);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return switch (energyTier.ordinal()) {
            case 0 -> Component.translatable(FurnitureStrings.Charge_Pad);
            case 1 -> Component.translatable(FurnitureStrings.Charge_Pad_CESU);
            case 2 -> Component.translatable(FurnitureStrings.Charge_Pad_MFE);
            case 3 -> Component.translatable(FurnitureStrings.Charge_Pad_MFSU);
            default -> Component.translatable(FurnitureStrings.Charge_Pad);
        };
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ChargePadScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world != null && !world.isClientSide()) {
            charge(Battery_OUTPUT_SLOT);
            discharge(Battery_INPUT_SLOT);
            chargePlayersAbove();
            pushEnergyToNeighbours();
        }
    }

    public Optional<ImplementedInventory> getOptionalInventory() {
        if (this instanceof ImplementedInventory inventory) {
            return inventory == null ? Optional.empty() : Optional.of(inventory);
        } else {
            return Optional.empty();
        }
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