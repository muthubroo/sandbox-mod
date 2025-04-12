package com.muthubroo.sandboxmod.entity.passive;

import com.muthubroo.sandboxmod.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonFrameTracker;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class DragonEntity extends MobEntity {
    private final DragonPart head;
    private final DragonPart neck;
    private final DragonPart body;
    private final DragonPart tail1;
    private final DragonPart tail2;
    private final DragonPart tail3;
    private final DragonPart rightWing;
    private final DragonPart leftWing;
    private final DragonPart[] parts;

    public float lastWingPosition;
    public float wingPosition;
    public boolean slowedDownByBlock;
    public float yawAcceleration;
    public final EnderDragonFrameTracker frameTracker = new EnderDragonFrameTracker();

    public DragonEntity(EntityType<? extends DragonEntity> entityType, World world) {
        super(ModEntities.DRAGON, world);
        this.head = new DragonPart(this, "head", 1.0F, 1.0F);
        this.neck = new DragonPart(this, "neck", 3.0F, 3.0F);
        this.body = new DragonPart(this, "body", 5.0F, 3.0F);
        this.tail1 = new DragonPart(this, "tail1", 2.0F, 2.0F);
        this.tail2 = new DragonPart(this, "tail2", 2.0F, 2.0F);
        this.tail3 = new DragonPart(this, "tail3", 2.0F, 2.0F);
        this.rightWing = new DragonPart(this, "rightWing", 4.0F, 2.0F);
        this.leftWing = new DragonPart(this, "leftWing", 4.0F, 2.0F);
        this.parts = new DragonPart[]{this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.rightWing, this.leftWing};
        this.noClip = true;
    }

    public static DefaultAttributeContainer.Builder createDragonAttributes() {
        return AnimalEntity.createAnimalAttributes()
                .add(EntityAttributes.MAX_HEALTH, 200.0);
    }

    @Override
    public boolean isFlappingWings() {
        float f = MathHelper.cos(this.wingPosition * (float) (Math.PI * 2));
        float g = MathHelper.cos(this.lastWingPosition * (float) (Math.PI * 2));
        return g <= -0.3F && f >= -0.3F;
    }

    @Override
    public void addFlapEffects() {
        if (this.getWorld().isClient && !this.isSilent()) {
            this.getWorld()
                    .playSoundClient(
                            this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, this.getSoundCategory(), 3.0F, 0.8F + this.random.nextFloat() * 0.3F, false
                    );
        }
    }

    @Override
    public void tickMovement() {
        this.addAirTravelEffects();

        this.lastWingPosition = this.wingPosition;
        Vec3d velocity = this.getVelocity();
        float g = 0.2F / ((float) velocity.horizontalLength() * 10.0F + 1.0F);
        g *= (float)Math.pow(2.0, velocity.y);
        if (this.slowedDownByBlock) {
            this.wingPosition += g * 0.5F;
        } else {
            this.wingPosition += g;
        }

        this.setYaw(MathHelper.wrapDegrees(this.getYaw()));
        if (this.isAiDisabled()) {
            this.wingPosition = 0.5F;
        } else {
            this.frameTracker.tick(this.getY(), this.getYaw());
            if (this.getWorld() instanceof ServerWorld) {
                Vec3d target = this.getPos();
                if (target != null) {
                    double d = target.x - this.getX();
                    double e = target.y - this.getY();
                    double i = target.z - this.getZ();
                    double j = d * d + e * e + i * i;
                    float k = 1.0F;
                    double l = Math.sqrt(d * d + i * i);
                    if (l > 0.0) {
                        e = MathHelper.clamp(e / l, (double)(-k), (double)k);
                    }

                    this.setVelocity(this.getVelocity().add(0.0, e * 0.01, 0.0));
                    this.setYaw(MathHelper.wrapDegrees(this.getYaw()));
                    Vec3d displacement = target.subtract(this.getX(), this.getY(), this.getZ()).normalize();
                    Vec3d orientation = new Vec3d(
                            MathHelper.sin(this.getYaw() * (float) (Math.PI / 180.0)), this.getVelocity().y, -MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0))
                    )
                            .normalize();
                    float m = Math.max(((float)orientation.dotProduct(displacement) + 0.5F) / 1.5F, 0.0F);
                    if (Math.abs(d) > 1.0E-5F || Math.abs(i) > 1.0E-5F) {
                        float n = MathHelper.clamp(MathHelper.wrapDegrees(180.0F - (float)MathHelper.atan2(d, i) * (180.0F / (float)Math.PI) - this.getYaw()), -50.0F, 50.0F);
                        this.yawAcceleration *= 0.8F;
                        this.yawAcceleration = this.yawAcceleration + n * this.getYawAcceleration();
                        this.setYaw(this.getYaw() + this.yawAcceleration * 0.1F);
                    }

                    float n = (float)(2.0 / (j + 1.0));
                    this.updateVelocity(0.06F * (m * n + (1.0F - n)), new Vec3d(0.0, 0.0, -1.0));
                    if (this.slowedDownByBlock) {
                        this.move(MovementType.SELF, this.getVelocity().multiply(0.8F));
                    } else {
                        this.move(MovementType.SELF, this.getVelocity());
                    }

                    Vec3d normalizedVelocity = this.getVelocity().normalize();
                    double p = 0.8 + 0.15 * (normalizedVelocity.dotProduct(orientation) + 1.0) / 2.0;
                    this.setVelocity(this.getVelocity().multiply(p, 0.91F, p));
                }
            } else {
                this.interpolator.tick();
            }

            if (!this.getWorld().isClient()) {
                this.tickBlockCollision();
            }

            this.bodyYaw = this.getYaw();
            Vec3d[] partPositions = new Vec3d[this.parts.length];

            for (int q = 0; q < this.parts.length; q++) {
                partPositions[q] = new Vec3d(this.parts[q].getX(), this.parts[q].getY(), this.parts[q].getZ());
            }

            float r = (float)(this.frameTracker.getFrame(5).y() - this.frameTracker.getFrame(10).y()) * 10.0F * (float) (Math.PI / 180.0);
            float s = MathHelper.cos(r);
            float t = MathHelper.sin(r);
            float u = this.getYaw() * (float) (Math.PI / 180.0);
            float v = MathHelper.sin(u);
            float w = MathHelper.cos(u);
            this.movePart(this.body, v * 0.5F, 0.0, -w * 0.5F);
            this.movePart(this.rightWing, w * 4.5F, 2.0, v * 4.5F);
            this.movePart(this.leftWing, w * -4.5F, 2.0, v * -4.5F);
            if (this.getWorld() instanceof ServerWorld serverWorld && this.hurtTime == 0) {
                this.launchLivingEntities(
                        serverWorld,
                        serverWorld.getOtherEntities(
                                this, this.rightWing.getBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR
                        )
                );
                this.launchLivingEntities(
                        serverWorld,
                        serverWorld.getOtherEntities(
                                this, this.leftWing.getBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR
                        )
                );
                this.damageLivingEntities(
                        serverWorld, serverWorld.getOtherEntities(this, this.head.getBoundingBox().expand(1.0), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR)
                );
                this.damageLivingEntities(
                        serverWorld, serverWorld.getOtherEntities(this, this.neck.getBoundingBox().expand(1.0), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR)
                );
            }

            float x = MathHelper.sin(this.getYaw() * (float) (Math.PI / 180.0) - this.yawAcceleration * 0.01F);
            float y = MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0) - this.yawAcceleration * 0.01F);
            float z = -1.0F;
            this.movePart(this.head, x * 6.5F * s, z + t * 6.5F, -y * 6.5F * s);
            this.movePart(this.neck, x * 5.5F * s, z + t * 5.5F, -y * 5.5F * s);
            EnderDragonFrameTracker.Frame frame = this.frameTracker.getFrame(5);

            for (int aa = 0; aa < 3; aa++) {
                DragonPart dragonPart = null;
                if (aa == 0) {
                    dragonPart = this.tail1;
                }

                if (aa == 1) {
                    dragonPart = this.tail2;
                }

                if (aa == 2) {
                    dragonPart = this.tail3;
                }

                EnderDragonFrameTracker.Frame frame2 = this.frameTracker.getFrame(12 + aa * 2);
                float ab = this.getYaw() * (float) (Math.PI / 180.0) + this.wrapYawChange(frame2.yRot() - frame.yRot()) * (float) (Math.PI / 180.0);
                float ac = MathHelper.sin(ab);
                float mx = MathHelper.cos(ab);
                float o = (aa + 1) * 2.0F;
                this.movePart(dragonPart, -(v * 1.5F + ac * o) * s, frame2.y() - frame.y() - (o + 1.5F) * t + 1.5, (w * 1.5F + mx * o) * s);
            }

            if (this.getWorld() instanceof ServerWorld serverWorld4) {
                this.slowedDownByBlock = this.destroyBlocks(serverWorld4, this.head.getBoundingBox())
                        | this.destroyBlocks(serverWorld4, this.neck.getBoundingBox())
                        | this.destroyBlocks(serverWorld4, this.body.getBoundingBox());
            }

            for (int aa = 0; aa < this.parts.length; aa++) {
                this.parts[aa].lastX = partPositions[aa].x;
                this.parts[aa].lastY = partPositions[aa].y;
                this.parts[aa].lastZ = partPositions[aa].z;
                this.parts[aa].lastRenderX = partPositions[aa].x;
                this.parts[aa].lastRenderY = partPositions[aa].y;
                this.parts[aa].lastRenderZ = partPositions[aa].z;
            }
        }
    }

    private float getYawAcceleration() {
        float f = (float)this.getVelocity().horizontalLength() + 1.0F;
        float g = Math.min(f, 40.0F);
        return 0.7F / g / f;
    }

    private void movePart(DragonPart dragonPart, double dx, double dy, double dz) {
        dragonPart.setPosition(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
    }

    private void launchLivingEntities(ServerWorld world, List<Entity> entities) {
        double d = (this.body.getBoundingBox().minX + this.body.getBoundingBox().maxX) / 2.0;
        double e = (this.body.getBoundingBox().minZ + this.body.getBoundingBox().maxZ) / 2.0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                double f = entity.getX() - d;
                double g = entity.getZ() - e;
                double h = Math.max(f * f + g * g, 0.1);
                entity.addVelocity(f / h * 4.0, 0.2F, g / h * 4.0);
            }
        }
    }

    private void damageLivingEntities(ServerWorld world, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                DamageSource damageSource = this.getDamageSources().mobAttack(this);
                entity.damage(world, damageSource, 10.0F);
                EnchantmentHelper.onTargetDamaged(world, entity, damageSource);
            }
        }
    }

    private float wrapYawChange(double yawDegrees) {
        return (float)MathHelper.wrapDegrees(yawDegrees);
    }

    private boolean destroyBlocks(ServerWorld world, Box box) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int m = MathHelper.floor(box.maxY);
        int n = MathHelper.floor(box.maxZ);
        boolean bl = false;

        for (int o = i; o <= l; o++) {
            for (int p = j; p <= m; p++) {
                for (int q = k; q <= n; q++) {
                    BlockPos blockPos = new BlockPos(o, p, q);
                    BlockState blockState = world.getBlockState(blockPos);
                    if (!blockState.isAir() && !blockState.isIn(BlockTags.DRAGON_TRANSPARENT)) {
                        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || blockState.isIn(BlockTags.DRAGON_IMMUNE)) {
                            bl = true;
                        }
                    }
                }
            }
        }

        return bl;
    }

    public boolean damagePart(ServerWorld world, DragonPart part, DamageSource source, float amount) {
        if (part != this.head) {
            amount = amount / 4.0F + Math.min(amount, 1.0F);
        }

        if (amount < 0.01F) {
            return false;
        } else {
            if (source.getAttacker() instanceof PlayerEntity || source.isIn(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
                super.damage(world, source, amount);
            }

            return true;
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return this.damagePart(world, this.body, source, amount);
    }

    @Override
    public void kill(ServerWorld world) {
        this.remove(Entity.RemovalReason.KILLED);
        this.emitGameEvent(GameEvent.ENTITY_DIE);
    }

    public DragonPart[] getBodyParts() {
        return this.parts;
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        DragonPart[] dragonParts = this.getBodyParts();

        for (int i = 0; i < dragonParts.length; i++) {
            dragonParts[i].setId(i + packet.getEntityId() + 1);
        }
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return target.canTakeDamage();
    }

    @Override
    protected float clampScale(float scale) {
        return 1.0F;
    }
}
