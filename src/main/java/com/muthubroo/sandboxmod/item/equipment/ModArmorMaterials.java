package com.muthubroo.sandboxmod.item.equipment;

import com.google.common.collect.Maps;
import com.muthubroo.sandboxmod.SandboxMod;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModArmorMaterials {
    private static final TagKey<Item> REPAIRS_COPPER_ARMOR = TagKey.of(RegistryKeys.ITEM, Identifier.of(SandboxMod.MOD_ID, "repairs_copper_armor"));
    public static final ArmorMaterial COPPER = new ArmorMaterial(
            15, createDefenseMap(2, 5, 6, 2, 5), 9, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0f, 0.1f, REPAIRS_COPPER_ARMOR, ModEquipmentAssetKeys.COPPER
    );

    private static Map<EquipmentType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
        return Maps.newEnumMap(
                Map.of(
                        EquipmentType.BOOTS,
                        bootsDefense,
                        EquipmentType.LEGGINGS,
                        leggingsDefense,
                        EquipmentType.CHESTPLATE,
                        chestplateDefense,
                        EquipmentType.HELMET,
                        helmetDefense,
                        EquipmentType.BODY,
                        bodyDefense
                )
        );
    }
}
