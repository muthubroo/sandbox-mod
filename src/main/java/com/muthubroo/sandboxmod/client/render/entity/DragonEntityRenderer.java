package com.muthubroo.sandboxmod.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.muthubroo.sandboxmod.SandboxMod;
import com.muthubroo.sandboxmod.client.render.entity.state.DragonEntityRenderState;
import com.muthubroo.sandboxmod.entity.passive.DragonEntity;
import com.muthubroo.sandboxmod.entity.passive.DragonPart;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value = EnvType.CLIENT)
public class DragonEntityRenderer extends EntityRenderer<DragonEntity, DragonEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.of(SandboxMod.MOD_ID, "textures/entity/dragon/dragon.png");
    private static final Identifier EYE_TEXTURE = Identifier.of(SandboxMod.MOD_ID, "textures/entity/dragon/dragon_eyes.png");
    private static final RenderLayer DRAGON_CUTOUT = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private static final RenderLayer DRAGON_EYES = RenderLayer.getEyes(EYE_TEXTURE);
    private final DragonEntityModel model;

    public DragonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new DragonEntityModel(context.getPart(DragonEntityModel.DRAGON));
    }

    public void render(DragonEntityRenderState dragonEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float f = dragonEntityRenderState.getLerpedFrame(7).yRot();
        float g = (float)(dragonEntityRenderState.getLerpedFrame(5).y() - dragonEntityRenderState.getLerpedFrame(10).y());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * 10.0F));
        matrixStack.translate(0.0F, 0.0F, 1.0F);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        this.model.setAngles(dragonEntityRenderState);
        VertexConsumer vertexConsumer3 = vertexConsumerProvider.getBuffer(DRAGON_CUTOUT);
        this.model.render(matrixStack, vertexConsumer3, i, OverlayTexture.getUv(0.0F, dragonEntityRenderState.hurt));

        VertexConsumer vertexConsumer4 = vertexConsumerProvider.getBuffer(DRAGON_EYES);
        this.model.render(matrixStack, vertexConsumer4, i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
        super.render(dragonEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    public DragonEntityRenderState createRenderState() {
        return new DragonEntityRenderState();
    }

    public void updateRenderState(DragonEntity dragonEntity, DragonEntityRenderState dragonEntityRenderState, float f) {
        super.updateRenderState(dragonEntity, dragonEntityRenderState, f);
        dragonEntityRenderState.wingPosition = MathHelper.lerp(f, dragonEntity.lastWingPosition, dragonEntity.wingPosition);
        dragonEntityRenderState.hurt = dragonEntity.hurtTime > 0;
        dragonEntityRenderState.tickProgress = f;
        dragonEntityRenderState.frameTracker.copyFrom(dragonEntity.frameTracker);
    }

    protected void appendHitboxes(DragonEntity dragonEntity, ImmutableList.Builder<EntityHitbox> builder, float f) {
        super.appendHitboxes(dragonEntity, builder, f);
        double d = -MathHelper.lerp((double)f, dragonEntity.lastRenderX, dragonEntity.getX());
        double e = -MathHelper.lerp((double)f, dragonEntity.lastRenderY, dragonEntity.getY());
        double g = -MathHelper.lerp((double)f, dragonEntity.lastRenderZ, dragonEntity.getZ());

        for (DragonPart dragonPart : dragonEntity.getBodyParts()) {
            Box box = dragonPart.getBoundingBox();
            EntityHitbox entityHitbox = new EntityHitbox(
                    box.minX - dragonPart.getX(),
                    box.minY - dragonPart.getY(),
                    box.minZ - dragonPart.getZ(),
                    box.maxX - dragonPart.getX(),
                    box.maxY - dragonPart.getY(),
                    box.maxZ - dragonPart.getZ(),
                    (float)(d + MathHelper.lerp((double)f, dragonPart.lastRenderX, dragonPart.getX())),
                    (float)(e + MathHelper.lerp((double)f, dragonPart.lastRenderY, dragonPart.getY())),
                    (float)(g + MathHelper.lerp((double)f, dragonPart.lastRenderZ, dragonPart.getZ())),
                    0.25F,
                    1.0F,
                    0.0F
            );
            builder.add(entityHitbox);
        }
    }

    protected boolean canBeCulled(DragonEntity dragonEntity) {
        return false;
    }
}
