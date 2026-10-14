package com.skniro.industrial_elixir.item.init.equipment;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;

/**
 * Armour texture layers for the mod's armour sets.
 *
 * <p>26.1 models armour textures through a data-driven {@code EquipmentAsset}; 1.21.1 has no such
 * registry and instead resolves the texture straight from an {@link ArmorMaterial.Layer}.</p>
 */
public interface MapleEquipmentAssetKeys {
    ArmorMaterial.Layer QUANTUM = register("quantum");
    ArmorMaterial.Layer BRONZE = register("bronze");
    ArmorMaterial.Layer ELECTRIC_JETPACK = register("electric_jetpack");

    static ArmorMaterial.Layer register(String name) {
        return new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
    }

    public static void registerMapleArmorAssetsKeys() {
        IndustrialElixir.LOGGER.info("Registering Maple armor assets Keys for " + IndustrialElixir.MOD_ID);
    }
}
