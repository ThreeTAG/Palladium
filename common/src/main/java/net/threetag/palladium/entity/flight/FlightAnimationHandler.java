package net.threetag.palladium.entity.flight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public abstract class FlightAnimationHandler<T extends FlightType> {

    protected float roll, prevRoll;
    protected float pitch, prevPitch;
    protected float limbRoll, prevLimbRoll;
    protected float limbPitch, prevLimbPitch;

    public void tick(LivingEntity entity, boolean flightStopped) {
        this.prevRoll = this.roll;
        this.prevPitch = this.pitch;
        this.prevLimbRoll = this.limbRoll;
        this.prevLimbPitch = this.limbPitch;
    }

    public abstract ResourceLocation getAnimationAssetId();

    public float getRoll(float partialTick) {
        return Mth.lerp(partialTick, this.prevRoll, this.roll);
    }

    public float getPitch(float partialTick) {
        return Mth.lerp(partialTick, this.prevPitch, this.pitch);
    }

    public float getLimbRoll(float partialTick) {
        return Mth.lerp(partialTick, this.prevLimbRoll, this.limbRoll);
    }

    public float getLimbPitch(float partialTick) {
        return Mth.lerp(partialTick, this.prevLimbPitch, this.limbPitch);
    }
}
