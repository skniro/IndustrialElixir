package com.skniro.industrial_elixir;


import com.skniro.growableoresir.block.GrowableICOresBlocks;
import com.skniro.industrial_elixir.api.energytier.EnergyTier;
import com.skniro.industrial_elixir.api.fluid.item.FullFluidCellHandler;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.client.particle.MapleParticleTypes;
import com.skniro.industrial_elixir.energy.api.EnergyStorage;
import com.skniro.industrial_elixir.energy.api.base.SimpleEnergyItem;
import com.skniro.industrial_elixir.entity.MapleEntityType;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.fluid.MapleFluidTypes;
import com.skniro.industrial_elixir.item.*;
import com.skniro.industrial_elixir.item.alchemy.IndustrialElixirPotions;
import com.skniro.industrial_elixir.item.init.FluidCellItem;
import com.skniro.industrial_elixir.item.init.equipment.MapleEquipmentAssetKeys;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.screen.AlchemyScreenHandlerType;
import com.skniro.industrial_elixir.world.gamerules.MapleGameRules;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;
import java.util.Locale;


public class  ModContent {


    public static void registerItem(IEventBus eventBus) {
        GrowableOresItems.shield_item(eventBus);
        AdvancedItems.registerAdvancedItem(eventBus);
        MapleArmorItems.registerMapleArmorItems(eventBus);
        MapleFoodComponents.registerMapleFoodItems(eventBus);
        MapleEquipmentAssetKeys.registerMapleArmorAssetsKeys();
        IndustrialElixirPotions.registerPotions(eventBus);
    }

    public static void registerBlock(IEventBus eventBus) {
        GrowableICOresBlocks.registerModBlocks(eventBus);
        GrowableOresBlocks.registerGrowableOresBlocks(eventBus);
        AlchemyRecipeType.registerRecipes(eventBus);
        AlchemyBlockEntityType.registerMapleBlockEntityType(eventBus);
        AlchemyScreenHandlerType.registeralchemyscreenhandlertype(eventBus);
        GeneralBlocks.registerNetherOresBlock(eventBus);
        MapleSignBlocks.registerMapleSignBlocks(eventBus);
    }

    public static void registerFluids(IEventBus eventBus) {
        MapleFluidTypes.register(eventBus);
        IndustrialElixirFluids.registerFluids(eventBus);
        IndustrialElixirFluidItems.registerFluidItems(eventBus);
        IndustrialElixirFluidBlocks.registerFluidBlocks(eventBus);
    }

    public static void CreativeTab(IEventBus eventBus) {
        ModCreativeTab.register(eventBus);
    }

    public static void registerEntity(IEventBus eventBus) {
        MapleEntityType.registerMapleEntityType(eventBus);
    }

    public static void WorldGen(IEventBus eventBus) {

    }

    public static void registerCommand(IEventBus eventBus) {
        MapleGameRules.maplegamerule(eventBus);
    }

    public static void registerOthers(IEventBus eventBus) {
        MapleParticleTypes.registerParticleTypes(eventBus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(EnergyStorage.ITEM,
                (stack, context) -> SimpleEnergyItem.createStorage(context,
                        ((SimpleEnergyItem) stack.getItem()).getEnergyCapacity(stack),
                        ((SimpleEnergyItem) stack.getItem()).getEnergyMaxInput(stack),
                        ((SimpleEnergyItem) stack.getItem()).getEnergyMaxOutput(stack)),
                GrowableOresItems.RE_BATTERY.get(),
                GrowableOresItems.ADVANCED_RE_BATTERY.get(),
                GrowableOresItems.ENERGY_CRYSTAL.get(),
                GrowableOresItems.LAPOTRON_CRYSTAL.get(),
                MapleArmorItems.Quantum_HELMET.get(),
                MapleArmorItems.Quantum_CHESTPLATE.get(),
                MapleArmorItems.Quantum_LEGGINGS.get(),
                MapleArmorItems.Quantum_BOOTS.get(),
                MapleArmorItems.Electric_Jetpack.get()
        );
    }

    @SubscribeEvent
    public static void FluidCellinit(RegisterCapabilitiesEvent event) {
        List<Item> items = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof FluidCellItem).toList();
        event.registerItem(Capabilities.Fluid.ITEM, (stack, ctx) -> {
                    if (stack.getItem() instanceof FluidCellItem cellItem) {
                        return new FullFluidCellHandler(ctx, cellItem);
                    } else {
                        return null;
                    }
                },
                items.toArray(Item[]::new)
        );
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
