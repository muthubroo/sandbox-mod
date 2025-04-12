package com.muthubroo.sandboxmod.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.boss.dragon.EnderDragonFrameTracker;

@Environment(EnvType.CLIENT)
public class DragonEntityRenderState extends EntityRenderState {
    public float wingPosition;
    public boolean hurt;
    public float tickProgress;
    public final EnderDragonFrameTracker frameTracker = new EnderDragonFrameTracker();

    public EnderDragonFrameTracker.Frame getLerpedFrame(int age) {
        return this.frameTracker.getLerpedFrame(age, this.tickProgress);
    }

    public float getNeckPartPitchOffset(int id) {
        return (float)id;
    }
}
