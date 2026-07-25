package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.screen.handler.machine.VendorMachineScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VendorMachineBlockEntity extends BlockEntity implements Merchant, ExtendedMenuProvider<BlockPos> {
    private final MerchantOffers offers = new MerchantOffers();
    @Nullable
    private Player tradingPlayer;

    public VendorMachineBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.VENDOR_MACHINE_BLOCK_ENTITY, pos, state);
    }

    private void initOffers() {
        // 所有交易不限量（maxUses = Integer.MAX_VALUE）、全解锁、无经验
        // 1.21 格式: new MerchantOffer(ItemCost 输入1, Optional<ItemCost> 输入2, ItemStack 输出, maxUses, xp, priceMultiplier)
        offers.clear();
        offers.add(makeOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.IRON_INGOT, 4)));
        offers.add(makeOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.GOLD_INGOT, 2)));
        offers.add(makeOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.DIAMOND, 1)));
        offers.add(makeOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.REDSTONE, 8)));
        offers.add(makeOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.LAPIS_LAZULI, 8)));
        offers.add(makeOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(Items.COAL, 16)));
        offers.add(makeOffer(new ItemStack(Items.COAL, 8), new ItemStack(Items.EMERALD, 1)));
        offers.add(makeOffer(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Items.EMERALD, 1)));
    }

    private static MerchantOffer makeOffer(ItemStack cost, ItemStack result) {
        return new MerchantOffer(
                new ItemCost(cost.getItem(), cost.getCount()),
                Optional.empty(),
                result,
                Integer.MAX_VALUE, 0, 0f
        );
    }

    // Merchant
    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        return this.offers;
    }

    protected void updateTrades(ServerLevel level){
        initOffers();
    };

    @Override
    public void overrideOffers(MerchantOffers offers) {
        this.offers.addAll(offers);
        if (tradingPlayer instanceof ServerPlayer sp) {
            sendOffers(sp);
        }
    }

    private void sendOffers(ServerPlayer player) {
        if (player.containerMenu instanceof MerchantMenu menu) {
            player.connection.send(new ClientboundMerchantOffersPacket(menu.containerId, this.offers, 1, 0, false, false)
            );
        }
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.resetUses();
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int xp) {
    }

    @Override
    public boolean isClientSide() {
        return this.level == null || this.level.isClientSide();
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public boolean canRestock() {
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5,
                (double) this.worldPosition.getY() + 0.5,
                (double) this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    // ExtendedMenuProvider
    @Override
    public Component getDisplayName() {
        return Component.translatable(FurnitureStrings.VendorMachine);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("vendor_machine.offers", MerchantOffers.CODEC, this.offers);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.offers.clear();
        input.read("vendor_machine.offers", MerchantOffers.CODEC).ifPresent(this.offers::addAll);
        if (this.offers.isEmpty()) {
            initOffers();
        }
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveCustomOnly(registryLookup);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(
                    new ClientboundMerchantOffersPacket(syncId, this.offers, 1, 0, false, false
                    )
            );
        }
        return new VendorMachineScreenHandler(syncId, playerInventory, this, this);
    }
}
