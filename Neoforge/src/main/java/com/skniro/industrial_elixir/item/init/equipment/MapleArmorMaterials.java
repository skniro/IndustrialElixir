package com.skniro.industrial_elixir.item.init.equipment;

import com.google.common.collect.Maps;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public interface MapleArmorMaterials
{
    public static final ArmorMaterial QUANTUM = new ArmorMaterial(
            37, Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 4);
                map.put(ArmorType.LEGGINGS, 7);
                map.put(ArmorType.CHESTPLATE, 10);
                map.put(ArmorType.HELMET, 4);
                map.put(ArmorType.BODY, 15);
            }), 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 5.0F, 0.1F,null, MapleEquipmentAssetKeys.QUANTUM
    );

    ArmorMaterial BRONZE = new ArmorMaterial(15, createDefenseMap(2, 5, 6, 2, 5), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ModItemTags.REPAIRS_BRONZE_ARMOR, MapleEquipmentAssetKeys.BRONZE);

    ArmorMaterial ELECTRIC_JETPACK = new ArmorMaterial(
            12, Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.CHESTPLATE, 4);
            }), 25, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, null, MapleEquipmentAssetKeys.ELECTRIC_JETPACK
    );

    private static Map<ArmorType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
        return Maps.newEnumMap(Map.of(ArmorType.BOOTS, bootsDefense, ArmorType.LEGGINGS, leggingsDefense, ArmorType.CHESTPLATE, chestplateDefense, ArmorType.HELMET, helmetDefense, ArmorType.BODY, bodyDefense));
    }
}
