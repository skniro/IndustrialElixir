package com.skniro.industrial_elixir.block.entity.machine;

import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
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

        // ==================== 电池 / 能源 ====================
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.RE_BATTERY, 1)), 5));
        pool.add(wt(() -> makeOffer(em(20), items(GrowableOresItems.ADVANCED_RE_BATTERY, 1)), 3));
        pool.add(wt(() -> makeOffer(em(40), items(GrowableOresItems.ENERGY_CRYSTAL, 1)), 2));
        pool.add(wt(() -> makeOffer(em(64), items(GrowableOresItems.LAPOTRON_CRYSTAL, 1)), 1));


        // ==================== 橡胶 / 交通工具 ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.Rubber, 4)), 25));
        pool.add(wt(() -> makeOffer(em(16), items(GrowableOresItems.RUBBER_BOAT, 1)), 8));
        pool.add(wt(() -> makeOffer(em(20), items(GrowableOresItems.RUBBER_CHEST_BOAT, 1)), 5));


        // ==================== 升级模块 ====================
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.OVERCLOCKER, 1)), 8));
        pool.add(wt(() -> makeOffer(em(10), items(GrowableOresItems.ENERGY_STORAGE, 1)), 7));
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.TRANSFORMER, 1)), 8));
        pool.add(wt(() -> makeOffer(em(6), items(GrowableOresItems.REDSTONE_INVERTER, 1)), 10));


        // ==================== 反应堆组件 ====================
        pool.add(wt(() -> makeOffer(em(20), items(GrowableOresItems.SACRED_ESSENCE, 1)), 3));
        pool.add(wt(() -> makeOffer(em(12), items(GrowableOresItems.SACRED_SHARD, 1)), 5));
        pool.add(wt(() -> makeOffer(em(32), items(GrowableOresItems.SACRED_CORE, 1)), 1));

        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.COOLANT_CELL_10K, 1)), 15));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.COOLANT_CELL_30K, 1)), 10));
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.COOLANT_CELL_60K, 1)), 5));

        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.HEAT_VENT, 1)), 15));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.ADVANCED_HEAT_VENT, 1)), 10));
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.OVERCLOCKED_HEAT_VENT, 1)), 5));

        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.REACTOR_PLATING, 1)), 15));
        pool.add(wt(() -> makeOffer(em(12), items(GrowableOresItems.NEUTRON_REFLECTOR, 1)), 5));
        pool.add(wt(() -> makeOffer(em(10), items(GrowableOresItems.HEAT_EXCHANGER, 1)), 5));


        // ==================== 原矿 ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.Raw_Lead, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.Raw_Tin, 4)), 25));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.Raw_SACRED, 1)), 8));


        // ==================== Crushed Ore ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.CRUSHED_COPPER, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.CRUSHED_GOLD, 3)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.CRUSHED_IRON, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.CRUSHED_LEAD, 4)), 25));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.CRUSHED_SILVER, 3)), 15));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.CRUSHED_TIN, 4)), 25));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.CRUSHED_SACRED, 1)), 8));


        // ==================== Purified Ore ====================
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.PURIFIED_COPPER, 3)), 18));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.PURIFIED_GOLD, 2)), 12));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.PURIFIED_IRON, 3)), 18));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.PURIFIED_LEAD, 3)), 18));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.PURIFIED_SILVER, 2)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.PURIFIED_TIN, 3)), 18));
        pool.add(wt(() -> makeOffer(em(6), items(GrowableOresItems.PURIFIED_SACRED, 1)), 5));


        // ==================== Dust ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.BRONZE_DUST, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.CLAY_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.COAL_DUST, 6)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.COPPER_DUST, 4)), 25));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.DIAMOND_DUST, 1)), 8));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.ENERGIUM_DUST, 1)), 5));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.GOLD_DUST, 3)), 15));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.IRON_DUST, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.LAPIS_DUST, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.LEAD_DUST, 4)), 25));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.LITHIUM_DUST, 2)), 15));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.OBSIDIAN_DUST, 3)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SILICON_DIOXIDE_DUST, 4)), 25));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.SILVER_DUST, 2)), 10));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.STONE_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SULFUR_DUST, 4)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.TIN_DUST, 4)), 25));


        // ==================== Small Dust ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_BRONZE_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_COPPER_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_GOLD_DUST, 6)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_IRON_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_LAPIS_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_LEAD_DUST, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_LITHIUM_DUST, 4)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_OBSIDIAN_DUST, 6)), 25));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.SMALL_SILVER_DUST, 4)), 15));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_SULFUR_DUST, 6)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SMALL_TIN_DUST, 8)), 30));


        // ==================== 锭 ====================
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.BRONZE_INGOT, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.LEAD_INGOT, 1)), 15));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.SILVER_INGOT, 1)), 10));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.STEEL_INGOT, 1)), 8));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.TIN_INGOT, 1)), 15));
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.SACRED_INGOT, 1)), 3));


        // ==================== Plate ====================
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.BRONZE_PLATE, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.COPPER_PLATE, 1)), 15));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.GOLD_PLATE, 1)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.IRON_PLATE, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.LAPIS_PLATE, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.LEAD_PLATE, 1)), 15));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.OBSIDIAN_PLATE, 1)), 10));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.STEEL_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.TIN_PLATE, 1)), 15));


        // ==================== Dense Plate ====================
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.DENSE_BRONZE_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.DENSE_COPPER_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(7), items(GrowableOresItems.DENSE_GOLD_PLATE, 1)), 5));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.DENSE_IRON_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.DENSE_LAPIS_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.DENSE_LEAD_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.DENSE_OBSIDIAN_PLATE, 1)), 5));
        pool.add(wt(() -> makeOffer(em(10), items(GrowableOresItems.DENSE_STEEL_PLATE, 1)), 3));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.DENSE_TIN_PLATE, 1)), 8));


        // ==================== Casing ====================
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.BRONZE_CASING, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.COPPER_CASING, 1)), 15));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.GOLD_CASING, 1)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.IRON_CASING, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.LEAD_CASING, 1)), 15));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.STEEL_CASING, 1)), 8));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.TIN_CASING, 1)), 15));


        // ==================== 碳材料 ====================
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.CARBON_FIBRE, 1)), 10));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.CARBON_MESH, 1)), 10));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.CARBON_PLATE, 1)), 8));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.COAL_BALL, 2)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.COAL_BLOCK, 1)), 15));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.COAL_CHUNK, 1)), 15));
        pool.add(wt(() -> makeOffer(em(5), items(GrowableOresItems.INDUSTRIAL_DIAMOND, 1)), 5));


        // ==================== 铱 ====================
        pool.add(wt(() -> makeOffer(em(12), items(GrowableOresItems.IRIDIUM_SHARD, 1)), 3));
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.IRIDIUM_ORE, 1)), 4));
        pool.add(wt(() -> makeOffer(em(16), items(GrowableOresItems.IRIDIUM_PLATE, 1)), 2));


        // ==================== 合金 ====================
        pool.add(wt(() -> makeOffer(em(6), items(GrowableOresItems.ALLOY_INGOT, 1)), 5));
        pool.add(wt(() -> makeOffer(em(10), items(GrowableOresItems.ALLOY_PLATE, 1)), 3));


        // ==================== 其他材料 ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.Sticky_Resin, 2)), 20));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.Circuit, 1)), 10));
        pool.add(wt(() -> makeOffer(em(6), items(GrowableOresItems.Advanced_Circuit, 1)), 5));
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.Coil, 1)), 10));
        pool.add(wt(() -> makeOffer(em(4), items(GrowableOresItems.Electric_Motor, 1)), 8));


        // ==================== Scrap ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.Scrap, 4)), 30));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.Scrap_box, 1)), 20));


        // ==================== Fluid Cell ====================
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.EMPTY_CELL, 4)), 25));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.WATER_CELL, 2)), 20));
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.LAVA_CELL, 1)), 15));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.EMPTY_VESSEL, 4)), 25));


        // ==================== 其他反应材料 ====================
        pool.add(wt(() -> makeOffer(em(2), items(GrowableOresItems.IMPURE_SACRED_STONE, 1)), 10));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.SLAG, 4)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(GrowableOresItems.ASHES, 8)), 30));


        // ==================== Pattern Storage Crystal ====================
        pool.add(wt(() -> makeOffer(em(8), items(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL, 1)), 5));
        pool.add(wt(() -> makeOffer(em(16), items(GrowableOresItems.PATTERN_STORAGE_CRYSTAL, 1)), 2));


        // ==================== Heat Conductor ====================
        pool.add(wt(() -> makeOffer(em(3), items(GrowableOresItems.HEAT_CONDUCTOR, 1)), 8));

        // ========== COMMON（权重 30）：基础物资 ==========
        pool.add(wt(() -> makeOffer(em(1), items(Items.IRON_INGOT, 4)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.COPPER_INGOT, 8)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.COAL, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.ARROW, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.BONE, 16)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.STRING, 12)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.WHEAT, 20)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.CARROT, 20)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.POTATO, 20)), 30));
        pool.add(wt(() -> makeOffer(em(1), items(Items.BEETROOT, 24)), 30));


        // ========== COMMON：卖出 ==========
        pool.add(wt(() -> makeOffer(items(Items.COAL, 32), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.IRON_INGOT, 8), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.WHEAT, 20), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.CARROT, 20), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.POTATO, 20), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.ROTTEN_FLESH, 32), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.BONE, 24), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(Items.STRING, 20), em(1)), 30));


        // ========== UNCOMMON（权重 20）：中等价值 ==========
        pool.add(wt(() -> makeOffer(em(1), items(Items.GOLD_INGOT, 2)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.REDSTONE, 8)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.LAPIS_LAZULI, 8)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.LEATHER, 4)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.SUGAR_CANE, 12)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.GUNPOWDER, 4)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.EGG, 16)), 20));
        pool.add(wt(() -> makeOffer(em(1), items(Items.INK_SAC, 8)), 20));


        // ========== UNCOMMON：卖出 ==========
        pool.add(wt(() -> makeOffer(items(Items.GOLD_INGOT, 4), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(Items.GUNPOWDER, 8), em(1)), 20));

        // ========== RARE（权重 10）：稀有物资 ==========
        pool.add(wt(() -> makeOffer(em(16), items(Items.DIAMOND, 1)), 10));
        pool.add(wt(() -> makeOffer(em(1), items(Items.QUARTZ, 4)), 10));
        pool.add(wt(() -> makeOffer(em(2), items(Items.OBSIDIAN, 8)), 10));
        pool.add(wt(() -> makeOffer(em(1), items(Items.GLOWSTONE_DUST, 4)), 10));
        pool.add(wt(() -> makeOffer(em(3), items(Items.SLIME_BALL, 4)), 10));
        pool.add(wt(() -> makeOffer(em(4), items(Items.ENDER_PEARL, 1)), 10));
        pool.add(wt(() -> makeOffer(em(4), items(Items.BLAZE_ROD, 1)), 10));


        // ========== LEGENDARY（权重 3）：极稀有 ==========
        pool.add(wt(() -> makeOffer(em(32), items(Items.NETHERITE_INGOT, 1)), 3));

        pool.add(wt(() -> makeOffer(em(64), items(Items.HEART_OF_THE_SEA, 1)), 3));



        // ========== 橡胶 / 基础材料 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Rubber, 16), em(1)), 30));


        // ========== 原矿 / 粗加工材料 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Raw_Lead, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Raw_Tin, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Raw_SACRED, 8), em(1)), 10));


        // ========== Crushed Ore：粉碎矿物 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_COPPER, 12), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_IRON, 12), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_GOLD, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_LEAD, 12), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_TIN, 12), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_SILVER, 8), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CRUSHED_SACRED, 4), em(1)), 8));


        // ========== Purified Ore：纯化矿物 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_COPPER, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_IRON, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_GOLD, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_LEAD, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_TIN, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_SILVER, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PURIFIED_SACRED, 2), em(1)), 5));


        // ========== Dust：普通粉末 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.BRONZE_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CLAY_DUST, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COAL_DUST, 12), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COPPER_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.GOLD_DUST, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IRON_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LAPIS_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LEAD_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LITHIUM_DUST, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.OBSIDIAN_DUST, 8), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SILICON_DIOXIDE_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SILVER_DUST, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.STONE_DUST, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SULFUR_DUST, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.TIN_DUST, 8), em(1)), 20));


        // ========== Small Dust：小型粉末 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_BRONZE_DUST, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_COPPER_DUST, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_GOLD_DUST, 12), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_IRON_DUST, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_LAPIS_DUST, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_LEAD_DUST, 16), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_LITHIUM_DUST, 8), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_OBSIDIAN_DUST, 12), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_SILVER_DUST, 8), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_SULFUR_DUST, 12), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SMALL_TIN_DUST, 16), em(1)), 25));


        // ========== Ingot：金属锭 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.BRONZE_INGOT, 8), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LEAD_INGOT, 8), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SILVER_INGOT, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.STEEL_INGOT, 4), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.TIN_INGOT, 8), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SACRED_INGOT, 2), em(1)), 3));


        // ========== Plate：金属板 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.BRONZE_PLATE, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COPPER_PLATE, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.GOLD_PLATE, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IRON_PLATE, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LAPIS_PLATE, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LEAD_PLATE, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.OBSIDIAN_PLATE, 4), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.STEEL_PLATE, 3), em(1)), 5));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.TIN_PLATE, 6), em(1)), 15));


        // ========== Dense Plate：致密板 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_BRONZE_PLATE, 3), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_COPPER_PLATE, 3), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_GOLD_PLATE, 2), em(1)), 5));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_IRON_PLATE, 3), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_LAPIS_PLATE, 3), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_LEAD_PLATE, 3), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_OBSIDIAN_PLATE, 2), em(1)), 5));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_STEEL_PLATE, 2), em(1)), 3));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.DENSE_TIN_PLATE, 3), em(1)), 8));


        // ========== Casing：机器外壳 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.BRONZE_CASING, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COPPER_CASING, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.GOLD_CASING, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IRON_CASING, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LEAD_CASING, 6), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.STEEL_CASING, 3), em(1)), 8));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.TIN_CASING, 6), em(1)), 15));


        // ========== Carbon：碳材料 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CARBON_FIBRE, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CARBON_MESH, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.CARBON_PLATE, 3), em(1)), 8));


        // ========== Coal Processing：煤炭加工 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COAL_BALL, 8), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COAL_BLOCK, 2), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.COAL_CHUNK, 4), em(1)), 15));


        // ========== Diamond Processing：工业钻石 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.INDUSTRIAL_DIAMOND, 2), em(1)), 5));


        // ========== Iridium：铱 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IRIDIUM_SHARD, 2), em(1)), 3));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IRIDIUM_ORE, 2), em(1)), 4));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IRIDIUM_PLATE, 1), em(1)), 2));


        // ========== Alloy：合金 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.ALLOY_INGOT, 2), em(1)), 5));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.ALLOY_PLATE, 1), em(1)), 3));


        // ========== Sticky Resin：粘性树脂 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Sticky_Resin, 8), em(1)), 20));


        // ========== Circuit：电路 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Circuit, 3), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Advanced_Circuit, 2), em(1)), 5));


        // ========== Electric Components：电子组件 ==========

        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Coil, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Electric_Motor, 2), em(1)), 8));


        // ========== Scrap：废料 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Scrap, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.Scrap_box, 4), em(1)), 20));


        // ========== Fluid Cell：流体单元 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.EMPTY_CELL, 8), em(1)), 25));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.WATER_CELL, 4), em(1)), 20));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.LAVA_CELL, 2), em(1)), 15));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.EMPTY_VESSEL, 8), em(1)), 25));


        // ========== Sacred Materials：神圣材料 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.IMPURE_SACRED_STONE, 4), em(1)), 10));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.SLAG, 16), em(1)), 30));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.ASHES, 16), em(1)), 30));


        // ========== Pattern Storage Crystal：存储水晶 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL, 2), em(1)), 5));
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.PATTERN_STORAGE_CRYSTAL, 1), em(2)), 2));


        // ========== Heat Conductor：热传导材料 ==========
        pool.add(wt(() -> makeOffer(items(GrowableOresItems.HEAT_CONDUCTOR, 3), em(1)), 8));

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
