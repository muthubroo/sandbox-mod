package com.muthubroo.sandboxmod.item.equipment;

import com.muthubroo.sandboxmod.SandboxMod;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import static net.minecraft.item.equipment.EquipmentAssetKeys.REGISTRY_KEY;

public interface ModEquipmentAssetKeys {
    RegistryKey<EquipmentAsset> COPPER = register("copper");

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(SandboxMod.MOD_ID, name));
    }
}
