package com.muthubroo.sandboxmod.entity;

import com.muthubroo.sandboxmod.SandboxMod;
import com.muthubroo.sandboxmod.entity.passive.DragonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<DragonEntity> DRAGON = register("dragon",
            EntityType.Builder.create(DragonEntity::new, SpawnGroup.CREATURE)
                    .makeFireImmune()
                    .dimensions(16.0F, 8.0F)
                    .passengerAttachments(3.0F)
                    .maxTrackingRange(10));

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SandboxMod.MOD_ID, name));
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(DRAGON, DragonEntity.createDragonAttributes().build());
    }
}
