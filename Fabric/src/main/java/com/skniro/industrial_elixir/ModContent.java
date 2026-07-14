package com.skniro.industrial_elixir;


import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.item.replicator.ReplicatorValueMap;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.compat.jei.IndustrialElixirJEIUtils;
import com.skniro.industrial_elixir.entity.MapleEntityType;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import com.skniro.industrial_elixir.item.ModCreativeTab;
import com.skniro.industrial_elixir.item.alchemy.IndustrialElixirPotions;
import com.skniro.industrial_elixir.item.init.equipment.MapleEquipmentAssetKeys;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipe;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.*;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.util.ModFuel;
import com.skniro.industrial_elixir.util.ModLootTableModifiers;
import com.skniro.industrial_elixir.world.gen.ModOreGeneration;
import com.skniro.industrial_elixir.world.gen.ModTreeGeneration;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;

import java.util.Locale;


public class  ModContent {


    public static void registerItem() {
        GrowableOresItems.shield_item();
        AdvancedItems.registerAdvancedItem();
        MapleArmorItems.registerMapleArmorItems();
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

    public static void Compat() {
        if (IndustrialElixirJEIUtils.isJEIAvailable()) {
            RecipeSynchronization.synchronizeRecipeSerializer(MaceratorCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(CompressorCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(MetalFormerRollingCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(MetalFormerCuttingCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(MetalFormerExtrudingCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(MolecularTransformerCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(ExtractorCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(AlchemyCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(RecyclerCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(CuttingCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(BrewReactorCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(OreWashingCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(HeatCentrifugeCraftingRecipe.SERIALIZER);
            RecipeSynchronization.synchronizeRecipeSerializer(ModBlastFurnaceCraftingRecipe.SERIALIZER);
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
