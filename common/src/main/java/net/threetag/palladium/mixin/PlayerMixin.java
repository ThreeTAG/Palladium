package net.threetag.palladium.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.property.PalladiumProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements PalladiumPlayerExtension {

    @Shadow
    public abstract void playNotifySound(SoundEvent sound, SoundSource source, float volume, float pitch);

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Unique
    private Vec3 prevMovementDelta = null;

    @Unique
    private float flightBoost = 0F;

    @Unique
    private Vec3 flightVector = Vec3.ZERO;

    @Unique
    private float speed = 0;

    @Unique
    private float prevSpeed = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        var player = (Player) (Object) this;
        this.prevMovementDelta = player.getDeltaMovement();
        this.prevSpeed = this.speed;
        this.speed = (float) Math.sqrt(this.prevMovementDelta.x * this.prevMovementDelta.x + this.prevMovementDelta.y * this.prevMovementDelta.y + this.prevMovementDelta.z * this.prevMovementDelta.z);

        if (FlightHandler.getCurrentFlightType(player).isNotNull()) {
            if (PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost < 1F) {
                this.flightBoost = Math.min(1F, this.flightBoost + 0.05F);
            } else if (!PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost > 0F) {
                this.flightBoost = Math.max(0F, this.flightBoost - 0.05F);
            }
        } else {
            if (this.flightBoost > 0F) {
                this.flightBoost = Math.max(0F, this.flightBoost - 0.1F);
            }
        }

        if (flightBoost > 0F) {
            var look = player.getLookAngle().scale(this.flightBoost);

            if (PalladiumProperties.LEFT_KEY_DOWN.get(player)) {
                var sideVec = player.getLookAngle().yRot((float) Math.toRadians(90F)).normalize();
                look = look.add(sideVec.x, 0, sideVec.z);
            }

            if (PalladiumProperties.RIGHT_KEY_DOWN.get(player)) {
                var sideVec = player.getLookAngle().yRot((float) Math.toRadians(-90F)).normalize();
                look = look.add(sideVec.x, 0, sideVec.z);
            }

            var diff = look.subtract(this.flightVector);
            double flexibility = 0.1D;
            diff = diff.length() > flexibility ? diff.scale(flexibility / diff.length()) : diff;
            this.flightVector = this.flightVector.add(diff);
            player.setDeltaMovement(this.flightVector);
        } else if (FlightHandler.getCurrentFlightType(player).isNotNull()) {
            player.setDeltaMovement(new Vec3(player.getDeltaMovement().x, Math.sin(player.tickCount / 10F) / 100F, player.getDeltaMovement().z));
        }
    }

    @Override
    public Vec3 palladium_getPrevMovementDelta() {
        return this.prevMovementDelta;
    }

    @Override
    public float palladium_getFlightAnimation(float partialTicks) {
        return 0F;
    }

    @Override
    public float palladium_getSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevSpeed, this.speed);
    }
}
