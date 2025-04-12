package com.muthubroo.sandboxmod.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class DragonPart extends Entity {
    public final DragonEntity owner;
    public final String name;
    private final EntityDimensions partDimensions;

    public DragonPart(DragonEntity owner, String name, float width, float height) {
        super(owner.getType(), owner.getWorld());
        this.partDimensions = EntityDimensions.changing(width, height);
        this.calculateDimensions();
        this.owner = owner;
        this.name = name;
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return !isAlwaysInvulnerableTo(source) && this.owner.damagePart(world, this, source, amount);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return this == entity || this.owner == entity;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return this.partDimensions;
    }
}
