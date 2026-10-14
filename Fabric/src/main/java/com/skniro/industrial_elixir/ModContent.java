package com.skniro.industrial_elixir;


import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.replicator.ReplicatorValueMap;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.client.particle.MapleParticleTypes;
import com.skniro.industrial_elixir.compat.jei.IndustrialElixirJEIUtils;
import com.skniro.industrial_elixir.entity.MapleEntityType;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.item.*;
import com.skniro.industrial_elixir.item.alchemy.IndustrialElixirPotions;
import com.skniro.industrial_elixir.item.init.equipment.MapleEquipmentAssetKeys;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipe;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.*;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.util.ModFuel;
import com.skniro.industrial_elixir.util.ModLootTableModifiers;
import com.skniro.industrial_elixir.world.gamerules.MapleGameRules;
import com.skniro.industrial_elixir.world.gen.ModOreGeneration;
import com.skniro.industrial_elixir.world.gen.ModTreeGeneration;
import com.skniro.industrial_elixir.world.gen.trunk.RubberTrunkPlacer;

import java.util.Locale;


public class  ModContent {


    public static void registerItem() {
        GrowableOresItems.shield_item();
        AdvancedItems.registerAdvancedItem();
        MapleArmorItems.registerMapleArmorItems();
        MapleFoodComponents.registerMapleFoodItems();
        MapleEquipmentAssetKeys.registerMapleArmorAssetsKeys();
        ModFuel.registerFuel();
        IndustrialElixirPotions.registerPotions();
        ReplicatorValueMap.registerDefaults();
    }

    public static void registerBlock() {
        GrowableOresBlocks.registerGrowableOresBlocks();
        AlchemyRecipeType.registerRecipes();
        AlchemyBlockEntityType.registerMapleBlockEntityType();
        AlchemyBlockEntityType.registerMachineEnergyEntity();
        AlchemyScreenHandlerType.registeralchemyscreenhandlertype();
        GeneralBlocks.registerNetherOresBlock();
        MapleSignBlocks.registerMapleSignBlocks();
        RubberTrunkPlacer.registerTrunkPlacerType();
    }

    public static void registerFluids() {
        IndustrialElixirFluids.registerFluids();
        IndustrialElixirFluidItems.registerFluidItems();
        IndustrialElixirFluidBlocks.registerFluidBlocks();
    }

    public static void CreativeTab() {
        ModCreativeTab.CreativeTab();
        ModCreativeTab.CreativeTabItem();
    }

    public static void registerEntity() {
        MapleEntityType.registerMapleEntityType();
    }

    public static void WorldGen() {
        ModOreGeneration.generateOres();
        ModTreeGeneration.generateTrees();
        ModLootTableModifiers.modifyLootTables();
    }

    public static void registerCommand() {
        MapleGameRules.maplegamerule();
    }

    public static void registerOthers() {
        MapleParticleTypes.registerParticleTypes();
    }

    public static void Compat() {
        // 26.1 needed Fabric's recipe synchronisation API
        // (net.fabricmc.fabric.api.recipe.v1.sync) to push the custom recipe
        // serializers to the client. That API does not exist in 1.21.1: vanilla
        // resolves every recipe through BuiltInRegistries.RECIPE_SERIALIZER and
        // ships the serializers' stream codecs in ClientboundUpdateRecipesPacket,
        // so as long as the serializers are registered (see AlchemyRecipeType)
        // nothing else has to be synced here.
        if (IndustrialElixirJEIUtils.isJEIAvailable()) {
            // Reserved for JEI-time registration hooks.
        }
    }


    public enum Cables {
        COPPER(128, 12.0F, true, EnergyTier.TIER2),
        TIN(32, 12.0F, true, EnergyTier.TIER1),
        GOLD(512, 12.0F, true, EnergyTier.TIER3),
        HV(2048, 12.0F, true, EnergyTier.TIER4),
        GLASSFIBER(8192, 12.0F, false, EnergyTier.TIER5),
        INSULATED_TIN(32, 12.0F, false, EnergyTier.TIER1),
        INSULATED_COPPER(128, 10.0F, false, EnergyTier.TIER2),
        INSULATED_GOLD(512, 10.0F, false, EnergyTier.TIER3),
        INSULATED_HV(2048, 10.0F, false, EnergyTier.TIER4),
        SUPERCONDUCTOR(536870911, 10.0F, false, EnergyTier.INFINITE);

        public final String name;
        public final int transferRate;
        public final int defaultTransferRate;
        public final double cableThickness;
        public final boolean canKill;
        public final boolean defaultCanKill;
        public final EnergyTier tier;

        private Cables(int transferRate, double cableThickness, boolean canKill, EnergyTier tier) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.transferRate = transferRate;
            this.defaultTransferRate = transferRate;
            this.cableThickness = cableThickness / 2.0 / 16.0;
            this.canKill = canKill;
            this.defaultCanKill = canKill;
            this.tier = tier;
        }
    }
}
