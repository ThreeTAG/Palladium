package net.threetag.palladium.entity.flight;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public abstract class FlightAnimationHandler<T extends FlightType> {

    protected float roll, prevRoll;
    protected float pitch, prevPitch;
    protected float yaw, prevYaw;
    protected float limbRoll, prevLimbRoll;
    protected float limbPitch, prevLimbPitch;
    protected float limbYaw, prevLimbYaw;

    public void tick(LivingEntity entity, boolean flightStopped) {
        this.prevRoll = this.roll;
        this.prevPitch = this.pitch;
        this.prevYaw = this.yaw;
        this.prevLimbRoll = this.limbRoll;
        this.prevLimbPitch = this.limbPitch;
        this.prevLimbYaw = this.limbYaw;
    }

    public abstract Identifier getAnimationAssetId();

    public float getRoll(float partialTick) {
        return Mth.lerp(partialTick, this.prevRoll, this.roll);
    }

    public float getPitch(float partialTick) {
        return Mth.lerp(partialTick, this.prevPitch, this.pitch);
    }

    public float getYaw(float partialTick) {
        return Mth.lerp(partialTick, this.prevYaw, this.yaw);
    }

    public float getLimbRoll(float partialTick) {
        return Mth.lerp(partialTick, this.prevLimbRoll, this.limbRoll);
    }

    public float getLimbPitch(float partialTick) {
        return Mth.lerp(partialTick, this.prevLimbPitch, this.limbPitch);
    }

    public float getLimbYaw(float partialTick) {
        return Mth.lerp(partialTick, this.prevLimbYaw, this.limbYaw);
    }
}
