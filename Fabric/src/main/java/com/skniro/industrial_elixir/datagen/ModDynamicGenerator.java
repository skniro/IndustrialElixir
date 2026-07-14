package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import java.util.concurrent.CompletableFuture;

public class ModDynamicGenerator extends FabricDynamicRegistryProvider {
    public ModDynamicGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // HERE GOES FUTURE WORLD GEN!
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
    }

    @Override
    public String getName() {
        return IndustrialElixir.MOD_ID;
    }
}