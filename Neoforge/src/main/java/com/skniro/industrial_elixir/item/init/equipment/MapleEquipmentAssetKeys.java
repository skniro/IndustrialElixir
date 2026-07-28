package com.skniro.industrial_elixir.item.init.equipment;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public interface MapleEquipmentAssetKeys {
    ResourceKey<EquipmentAsset> QUANTUM = register("quantum");
    ResourceKey<EquipmentAsset> BRONZE = register("bronze");
    ResourceKey<EquipmentAsset> ELECTRIC_JETPACK = register("electric_jetpack");

    static ResourceKey<EquipmentAsset> register(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID,name));
    }

    public static void registerMapleArmorAssetsKeys() {
        IndustrialElixir.LOGGER.info("Registering Maple armor assets Keys for " + IndustrialElixir.MOD_ID);
    }
}