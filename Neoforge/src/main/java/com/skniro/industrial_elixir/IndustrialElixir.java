package com.skniro.industrial_elixir;

import com.skniro.industrial_elixir.api.item.replicator.ReplicatorValueMap;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.entity.cable.CableElectrocutionEvent;
import com.skniro.industrial_elixir.compat.jei.IndustrialElixirJEIUtils;
import com.skniro.industrial_elixir.compat.rei.IndustrialModREIUtils;
import com.skniro.industrial_elixir.energy.heat.impl.HeatImpl;
import com.skniro.industrial_elixir.item.init.QuantumSuitItem;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.util.ModFuel;

import com.skniro.industrial_elixir.energy.impl.EnergyImpl;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(IndustrialElixir.MOD_ID)
public class IndustrialElixir {
    public static final String MOD_ID = "industrial_elixir";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public IndustrialElixir(IEventBus modEventBus) {
        ModContent.registerCommand(modEventBus);
        ModContent.registerItem(modEventBus);
        ModContent.registerFluids(modEventBus);
        ModContent.registerBlock(modEventBus);
        ModContent.registerEntity(modEventBus);
        ModContent.CreativeTab(modEventBus);
        ModContent.WorldGen(modEventBus);
        ModContent.registerOthers(modEventBus);

        EnergyImpl.register(modEventBus);
        HeatImpl.register(modEventBus);

        modEventBus.addListener(RegisterCapabilitiesEvent.class, EnergyImpl::init);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, HeatImpl::init);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, ModContent::FluidCellinit);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, AlchemyBlockEntityType::registerMachineEnergyEntity);
        modEventBus.addListener(FMLCommonSetupEvent.class, event -> {
            ReplicatorValueMap.registerDefaults();
            ModFuel.registerFuel();

        });

        if (IndustrialElixirJEIUtils.isJEIAvailable() || IndustrialModREIUtils.isREIAvailable()) {
            NeoForge.EVENT_BUS.addListener(false, OnDatapackSyncEvent.class, e -> e.sendRecipes(
                    AlchemyRecipeType.MACERATOR.type.get(),
                    AlchemyRecipeType.COMPRESSOR.type.get(),
                    AlchemyRecipeType.METALFORMER_ROLLING.type.get(),
                    AlchemyRecipeType.METALFORMER_CUTTING.type.get(),
                    AlchemyRecipeType.METALFORMER_EXTRUDING.type.get(),
                    AlchemyRecipeType.MOLECULAR_TRANSFORMER.type.get(),
                    AlchemyRecipeType.EXTRACTOR.type.get(),
                    AlchemyRecipeType.CANE_CONVERTER.type.get(),
                    AlchemyRecipeType.RECYCLER.type.get(),
                    AlchemyRecipeType.CUTTING.type.get(),
                    AlchemyRecipeType.BREW_REACTOR.type.get(),
                    AlchemyRecipeType.ORE_WASHING.type.get(),
                    AlchemyRecipeType.HEAT_CENTRIFUGE.type.get(),
                    AlchemyRecipeType.MOD_BLAST_FURNACE.type.get(),
                    AlchemyRecipeType.COFFEE_MACHINE.type.get(),
                    AlchemyRecipeType.CROP_FARM.type.get()
            ));
        }

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public static boolean onLivingDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) {
            return false;
        }
        DamageSource source = event.getSource();

        // Fall damage negation (Quantum Boots)
        if (source.typeHolder().is(DamageTypeTags.IS_FALL)) {
            return !QuantumSuitItem.tryNegateFallDamage(player);
        }

        // Lava immunity (full Quantum set)
        if (source.typeHolder().is(DamageTypeTags.IS_FIRE)
                && player.isInLava()
                && QuantumSuitItem.hasFullQuantumSet(player)) {
            return false;
        }
        float amount = event.getNewDamage();
        // General damage absorption for all quantum pieces
        if (amount > 0) {
            return QuantumSuitItem.absorbDamage(player, amount, source);
        }

        return true;
    }



        @SubscribeEvent
        public void onElectrocution(CableElectrocutionEvent event) {
            LivingEntity entity = event.getLivingEntity();

            ModContent.Cables cable = event.getCableType();

            // 你的触电逻辑
        }
}
