package com.muthubroo.sandboxmod.datagen;

import com.muthubroo.sandboxmod.item.ModItems;
import com.muthubroo.sandboxmod.item.equipment.ModEquipmentAssetKeys;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.registerArmor(ModItems.COPPER_BOOTS, ModEquipmentAssetKeys.COPPER, ItemModelGenerator.BOOTS_TRIM_ID_PREFIX, false);
        itemModelGenerator.registerArmor(ModItems.COPPER_CHESTPLATE, ModEquipmentAssetKeys.COPPER, ItemModelGenerator.CHESTPLATE_TRIM_ID_PREFIX, false);
        itemModelGenerator.registerArmor(ModItems.COPPER_HELMET, ModEquipmentAssetKeys.COPPER, ItemModelGenerator.HELMET_TRIM_ID_PREFIX, false);
        itemModelGenerator.registerArmor(ModItems.COPPER_LEGGINGS, ModEquipmentAssetKeys.COPPER, ItemModelGenerator.LEGGINGS_TRIM_ID_PREFIX, false);
    }
}
