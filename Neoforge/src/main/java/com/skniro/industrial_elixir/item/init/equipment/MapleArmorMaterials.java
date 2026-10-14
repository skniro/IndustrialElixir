package com.skniro.industrial_elixir.item.init.equipment;

import com.google.common.collect.Maps;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public interface MapleArmorMaterials
{
    Holder<ArmorMaterial> QUANTUM = register("quantum", new ArmorMaterial(
            createDefenseMap(4, 7, 10, 4, 15), 25, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.EMPTY,
            List.of(MapleEquipmentAssetKeys.QUANTUM), 5.0F, 0.1F
    ));

    Holder<ArmorMaterial> BRONZE = register("bronze", new ArmorMaterial(
            createDefenseMap(2, 5, 6, 2, 5), 9, SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(ModItemTags.REPAIRS_BRONZE_ARMOR), List.of(MapleEquipmentAssetKeys.BRONZE), 0.0F, 0.0F));

    Holder<ArmorMaterial> ELECTRIC_JETPACK = register("electric_jetpack", new ArmorMaterial(
            createDefenseMap(0, 0, 4, 0, 0), 25, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.EMPTY,
            List.of(MapleEquipmentAssetKeys.ELECTRIC_JETPACK), 0.0F, 0.0F
    ));

    private static Holder<ArmorMaterial> register(String name, ArmorMaterial material) {
        ResourceKey<ArmorMaterial> key = ResourceKey.create(Registries.ARMOR_MATERIAL,
                ResourceLocation.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name));
        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, key, material);
    }

    private static Map<ArmorItem.Type, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
        return Maps.newEnumMap(Map.of(ArmorItem.Type.BOOTS, bootsDefense, ArmorItem.Type.LEGGINGS, leggingsDefense, ArmorItem.Type.CHESTPLATE, chestplateDefense, ArmorItem.Type.HELMET, helmetDefense, ArmorItem.Type.BODY, bodyDefense));
    }
}
