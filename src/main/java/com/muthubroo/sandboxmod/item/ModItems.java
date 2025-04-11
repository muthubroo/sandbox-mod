package com.muthubroo.sandboxmod.item;

import com.muthubroo.sandboxmod.SandboxMod;
import com.muthubroo.sandboxmod.item.equipment.ModArmorMaterials;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    private static final Item COPPER_HELMET = register("copper_helmet", new Item.Settings().armor(ModArmorMaterials.COPPER, EquipmentType.HELMET));
    private static final Item COPPER_CHESTPLATE = register("copper_chestplate", new Item.Settings().armor(ModArmorMaterials.COPPER, EquipmentType.CHESTPLATE));
    private static final Item COPPER_LEGGINGS = register("copper_leggings", new Item.Settings().armor(ModArmorMaterials.COPPER, EquipmentType.LEGGINGS));
    private static final Item COPPER_BOOTS = register("copper_boots", new Item.Settings().armor(ModArmorMaterials.COPPER, EquipmentType.BOOTS));

    private static Item register(final String name, final Item.Settings settings) {
        final RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SandboxMod.MOD_ID, name));
        final Item item = new Item(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void registerItems() {
        SandboxMod.LOGGER.info("Registering mod items for " + SandboxMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(COPPER_HELMET);
            entries.add(COPPER_CHESTPLATE);
            entries.add(COPPER_LEGGINGS);
            entries.add(COPPER_BOOTS);
        });
    }
}
