package net.threetag.palladium.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.property.PalladiumProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements PalladiumPlayerExtension {

    @Unique
    private Vec3 prevMovementDelta = null;

    @Unique
    private FlightHandler.FlightType flightType = FlightHandler.FlightType.NONE;

    @Unique
    private float flightBoost = 0F;

    @Unique
    private float prevFlightBoost = 0F;

    @Unique
    private float hovering = 0F;

    @Unique
    private float prevHovering = 0F;

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
        this.prevFlightBoost = this.flightBoost;
        this.prevHovering = this.hovering;
        this.prevSpeed = this.speed;
        this.speed = (float) Math.sqrt(this.prevMovementDelta.x * this.prevMovementDelta.x + this.prevMovementDelta.y * this.prevMovementDelta.y + this.prevMovementDelta.z * this.prevMovementDelta.z);

        // Reset flight type if on ground
        if (this.flightType.isNotNull() && (player.isOnGround() || player.isFallFlying())) {
            this.flightType = FlightHandler.FlightType.NONE;
        }

        // Get/manage current flight boost
        if (this.flightType.isNotNull()) {
            if(player.isSprinting()) {
                if (PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost < 3F) {
                    this.flightBoost = Math.min(3F, this.flightBoost + 0.2F);
                } else if (!PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost > 0F) {
                    this.flightBoost = Math.max(0F, this.flightBoost - 0.4F);
                }
            } else {
                if (PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost < 1F) {
                    this.flightBoost = Math.min(1F, this.flightBoost + 0.05F);
                } else if (!PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost > 0F) {
                    this.flightBoost = Math.max(0F, this.flightBoost - 0.05F);
                }
            }
        } else {
            if (this.flightBoost > 0F) {
                this.flightBoost = Math.max(0F, this.flightBoost - 0.1F);
            }
        }

        // Apply flight boost
        if (flightBoost > 0F) {
            if (this.hovering < 1F) {
                this.hovering = Math.min(1F, this.hovering + 0.1F);
            }

            var look = player.getLookAngle().scale(this.flightBoost / 2F);

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
        } else if (this.flightType.isNotNull()) {
            player.setDeltaMovement(new Vec3(player.getDeltaMovement().x, Math.sin(player.tickCount / 10F) / 100F, player.getDeltaMovement().z));

            if (this.hovering < 1F) {
                this.hovering = Math.min(1F, this.hovering + 0.1F);
            }
        } else {
            if (this.hovering > 0F) {
                this.hovering = Math.max(0F, this.hovering - 0.1F);
            }
        }
    }

    @Override
    public Vec3 palladium_getPrevMovementDelta() {
        return this.prevMovementDelta;
    }

    @Override
    public FlightHandler.FlightType palladium_getFlightType() {
        return this.flightType;
    }

    @Override
    public void palladium_setFlightType(FlightHandler.FlightType flightType) {
        this.flightType = flightType;
    }

    @Override
    public float palladium_getFlightAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevFlightBoost, this.flightBoost);
    }

    @Override
    public float palladium_getHoveringAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevHovering, this.hovering);
    }

    @Override
    public float palladium_getSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevSpeed, this.speed);
    }
}
