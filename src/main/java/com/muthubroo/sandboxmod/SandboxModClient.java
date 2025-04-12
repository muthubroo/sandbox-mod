package com.muthubroo.sandboxmod;

import com.muthubroo.sandboxmod.client.render.entity.DragonEntityModel;
import com.muthubroo.sandboxmod.client.render.entity.DragonEntityRenderer;
import com.muthubroo.sandboxmod.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SandboxModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(DragonEntityModel.DRAGON, DragonEntityModel::createTexturedModelData);
        EntityRendererRegistry.register(ModEntities.DRAGON, DragonEntityRenderer::new);
    }
}
