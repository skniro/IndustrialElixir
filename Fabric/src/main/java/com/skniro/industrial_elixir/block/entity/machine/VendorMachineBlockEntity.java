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

import java.util.*;
import java.util.function.Supplier;

public class VendorMachineBlockEntity extends BlockEntity implements Merchant, ExtendedMenuProvider<BlockPos> {
    private final MerchantOffers offers = new MerchantOffers();
    @Nullable
    private Player tradingPlayer;

    public VendorMachineBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyBlockEntityType.VENDOR_MACHINE_BLOCK_ENTITY, pos, state);
    }

    private static final int TRADE_COUNT = 20;

    private record WeightedTrade(Supplier<MerchantOffer> trade, int weight) {}

    private static List<WeightedTrade> WEIGHTED_POOL = null;

    private void initOffers() {
        if (WEIGHTED_POOL == null) {
            WEIGHTED_POOL = createWeightedPool();
        }
        offers.clear();
        List<WeightedTrade> available = new ArrayList<>(WEIGHTED_POOL);
        Random rand = new Random(worldPosition.asLong());

        for (int pick = 0; pick < TRADE_COUNT && !available.isEmpty(); pick++) {
            int totalWeight = 0;
            for (WeightedTrade wt : available) {
                totalWeight += wt.weight;
            }
            int roll = rand.nextInt(totalWeight);
            int cumulative = 0;
            for (int i = 0; i < available.size(); i++) {
                cumulative += available.get(i).weight;
                if (roll < cumulative) {
                    offers.add(available.get(i).trade.get());
                    available.remove(i);
                    break;
                }
            }
        }
    }

    private static List<WeightedTrade> createWeightedPool() {
        List<WeightedTrade> pool = new ArrayList<>();

        // ========== COMMON（权重 30）：基础物资 ==========
        pool.add(wt(() -> makeOffer(em(1), items(Items.IRON_INGOT, 4)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.COPPER_INGOT, 4)), 30));
        pool.add(wt(() -> makeOffer(em(2), items(Items.COAL, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.ARROW, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.BONE, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.STRING, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.WHEAT, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.CARROT, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.POTATO, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.BEETROOT, 16)), 30));
        // 卖出
        pool.add(wt(() -> makeOffer(items(Items.COAL, 8), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.IRON_INGOT, 8), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.WHEAT, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.CARROT, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.POTATO, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.ROTTEN_FLESH, 8), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.BONE, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.STRING, 8), em(1)), 30));

        // ========== UNCOMMON（权重 20）：中等价值 ==========
        pool.add(wt(() -> makeOffer(em(1), items(Items.GOLD_INGOT, 2)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.REDSTONE, 8)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.LAPIS_LAZULI, 8)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.LEATHER, 4)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.SUGAR_CANE, 16)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.GUNPOWDER, 8)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.EGG, 12)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.INK_SAC, 8)), 20));
        // 卖出
        pool.add(wt(() -> makeOffer(items(Items.GOLD_INGOT, 4), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(Items.GUNPOWDER, 8), em(1)), 20));

        // ========== RARE（权重 10）：稀有物资 ==========
        pool.add(wt(() -> makeOffer(em(1), items(Items.DIAMOND, 1)), 10));
        pool.add(wt(() -> makeOffer(em(1), items(Items.QUARTZ, 4)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(Items.OBSIDIAN, 8)), 10));
        pool.add(wt(() -> makeOffer(em(1), items(Items.GLOWSTONE_DUST, 4)), 10));
        pool.add(wt(() -> makeOffer(em(3), items(Items.SLIME_BALL, 4)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(Items.ENDER_PEARL, 8)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(Items.BLAZE_ROD, 8)), 10));

        // ========== LEGENDARY（权重 3）：极稀有 ==========
        pool.add(wt(() -> makeOffer(em(3), items(Items.NETHERITE_INGOT, 1)), 3));
        pool.add(wt(() -> makeOffer(em(2), items(Items.HEART_OF_THE_SEA, 1)), 3));

        return pool;
    }

    private static ItemStack em(int count) {
        return new ItemStack(Items.EMERALD, count);
    }

    private static ItemStack items(net.minecraft.world.level.ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    private static WeightedTrade wt(Supplier<MerchantOffer> trade, int weight) {
        return new WeightedTrade(trade, weight);
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
