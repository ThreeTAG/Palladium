package net.threetag.palladium.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.util.Platform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Player.class)
public abstract class PlayerMixin implements PalladiumPlayerExtension {

    @Unique
    private Vec3 prevMovementDelta = null;

    @Unique
    private FlightHandler.FlightType flightType = FlightHandler.FlightType.NONE;

    @Unique
    private float flightSpeed = 1F;

    @Unique
    private boolean didFlew = false;

    @Unique
    private float flightBoost = 0F;

    @Unique
    private float prevFlightBoost = 0F;

    @Unique
    private int verticalHover = 0;

    @Unique
    private float levitation = 0F;

    @Unique
    private float prevLevitation = 0F;

    @Unique
    private float hovering = 0F;

    @Unique
    private float prevHovering = 0F;

    @Unique
    private Vec3 flightVector = Vec3.ZERO;

    @Unique
    private Vec3 prevFlightVector = Vec3.ZERO;

    @Unique
    private Vec3 prevLookAngle = Vec3.ZERO;

    @Unique
    private float speed = 0;

    @Unique
    private float prevSpeed = 0;

    @Unique
    private float horizontalSpeed = 0;

    @Unique
    private float prevHorizontalSpeed = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        var player = (Player) (Object) this;
        this.prevMovementDelta = player.getDeltaMovement();
        this.prevFlightBoost = this.flightBoost;
        this.prevLevitation = this.levitation;
        this.prevHovering = this.hovering;
        this.prevSpeed = this.speed;
        this.speed = (float) Math.sqrt(this.prevMovementDelta.x * this.prevMovementDelta.x + this.prevMovementDelta.y * this.prevMovementDelta.y + this.prevMovementDelta.z * this.prevMovementDelta.z);
        this.prevHorizontalSpeed = this.horizontalSpeed;
        this.horizontalSpeed = (float) Math.sqrt(this.prevMovementDelta.x * this.prevMovementDelta.x + this.prevMovementDelta.z * this.prevMovementDelta.z);
        this.prevLookAngle = player.getLookAngle();
        this.prevFlightVector = this.flightVector;

        // Reset flight type if on ground
        if (this.flightType.isNotNull() && (player.isOnGround() || player.isFallFlying() || player.isSwimming() || player.getAttributeValue(this.flightType.getAttribute()) <= 0D)) {
            this.flightType = FlightHandler.FlightType.NONE;
        }

        // Get/manage current flight boost
        if (this.flightType.isNotNull()) {
            if (player.isSprinting() && this.flightType == FlightHandler.FlightType.NORMAL) {
                if (PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost < 3F) {
                    this.flightBoost = Math.min(3F, this.flightBoost + 0.2F);
                } else if (!PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost > 0F) {
                    this.flightBoost = Math.max(0F, this.flightBoost - 0.4F);
                }
                this.didFlew = true;
            } else {
                if (PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost < 1F) {
                    this.flightBoost = Math.min(1F, this.flightBoost + 0.05F);
                    this.didFlew = false;
                } else if (!PalladiumProperties.FORWARD_KEY_DOWN.get(player) && this.flightBoost > 0F) {
                    this.flightBoost = Math.max(0F, this.flightBoost - 0.1F);
                }
            }

            if (this.didFlew) {
                this.levitation = 0F;
            } else {
                this.levitation = Mth.clamp(this.horizontalSpeed * 2F, 0, 1F);
            }
        } else {
            this.flightBoost = this.levitation = this.hovering = 0F;
            this.didFlew = false;
        }

        // Apply flight boost
        if (flightBoost > 0F) {
            if (this.hovering < 1F) {
                this.hovering = Math.min(1F, this.hovering + 0.1F);
            }

            var look = player.getLookAngle().scale(this.flightBoost / 2F * this.flightSpeed);

            if (PalladiumProperties.LEFT_KEY_DOWN.get(player)) {
                var sideVec = player.getLookAngle().yRot((float) Math.toRadians(90F)).normalize();
                look = look.add(sideVec.x, 0, sideVec.z);
            }

            if (PalladiumProperties.RIGHT_KEY_DOWN.get(player)) {
                var sideVec = player.getLookAngle().yRot((float) Math.toRadians(-90F)).normalize();
                look = look.add(sideVec.x, 0, sideVec.z);
            }

            var diff = look.subtract(this.flightVector);
            double flexibility = (10D - player.getAttributeValue(PalladiumAttributes.FLIGHT_FLEXIBILITY.get())) / 100D;
            diff = diff.length() > flexibility ? diff.scale(flexibility / diff.length()) : diff;
            this.flightVector = this.flightVector.add(diff);
            player.setDeltaMovement(this.flightVector);
            player.fallDistance = 0F;
            this.verticalHover = 0;
        } else if (this.flightType.isNotNull()) {
            // Hovering mid-air
            this.didFlew = false;

            if (PalladiumProperties.JUMP_KEY_DOWN.get(player)) {
                if (this.verticalHover < 20) {
                    this.verticalHover = Mth.clamp(this.verticalHover + 1, -20, 20);
                }
            } else {
                if (player.isCrouching()) {
                    if (this.verticalHover > -20) {
                        this.verticalHover = Mth.clamp(this.verticalHover - 1, -20, 20);
                    }
                } else if (this.verticalHover != 0) {
                    this.verticalHover = Mth.clamp(this.verticalHover + (this.verticalHover > 0 ? -1 : 1), -20, 20);
                }
            }

            player.setDeltaMovement(new Vec3(player.getDeltaMovement().x, this.verticalHover == 0D ? Math.sin(player.tickCount / 10F) / 100F : verticalHover / 60D, player.getDeltaMovement().z));
            player.fallDistance = 0F;
            this.flightVector = Vec3.ZERO;

            if (this.hovering < 1F) {
                this.hovering = Math.min(1F, this.hovering + 0.1F);
            }
        } else {
            // Reset everything
            this.didFlew = false;
            this.verticalHover = 0;
            this.flightVector = Vec3.ZERO;
            if (this.hovering > 0F) {
                this.hovering = Math.max(0F, this.hovering - 0.1F);
            }
        }

        // Update hitbox & play sound
        if ((this.flightBoost > 1F && this.prevFlightBoost <= 1F) || (this.flightBoost <= 1F && this.prevFlightBoost > 1F)) {
            player.refreshDimensions();

            if (this.flightBoost > 1F && this.prevFlightBoost <= 1F && Platform.isClient()) {
                FlightHandler.startSound(player);
            }
        }
    }

    @ModifyVariable(method = "getDimensions", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Pose getDimensions(Pose pose) {
        var hover = this.palladium_getHoveringAnimation(0);
        var levitation = this.palladium_getLevitationAnimation(0);
        var flight = this.palladium_getFlightAnimation(0);

        if (hover > 0F || levitation > 0F || flight > 0F) {
            if (this.flightBoost > 1F) {
                return Pose.FALL_FLYING;
            } else {
                return Pose.STANDING;
            }
        }
        return pose;
    }

    @ModifyVariable(method = "getStandingEyeHeight", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Pose getStandingEyeHeight(Pose pose) {
        var hover = this.palladium_getHoveringAnimation(0);
        var levitation = this.palladium_getLevitationAnimation(0);
        var flight = this.palladium_getFlightAnimation(0);

        if (hover > 0F || levitation > 0F || flight > 0F) {
            if (this.flightBoost > 1F) {
                return Pose.FALL_FLYING;
            } else {
                return Pose.STANDING;
            }
        }
        return pose;
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
        var player = (Player) (Object) this;
        var attribute = flightType.getAttribute();

        if (attribute != null) {
            this.flightSpeed = (float) player.getAttributeValue(attribute);
        }

        if (player.isSprinting()) {
            this.flightBoost = 1F;
        }
    }

    @Override
    public float palladium_getFlightAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevFlightBoost, this.flightBoost);
    }

    @Override
    public float palladium_getLevitationAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevLevitation, this.levitation);
    }

    @Override
    public float palladium_getHoveringAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevHovering, this.hovering);
    }

    @Override
    public Vec3 palladium_getFlightVector(float partialTicks) {
        return this.prevFlightVector.lerp(this.flightVector, partialTicks);
    }

    @Override
    public Vec3 palladium_getLookAngle(float partialTicks) {
        var player = (Player) (Object) this;
        return this.prevLookAngle.lerp(player.getLookAngle(), partialTicks);
    }

    @Override
    public float palladium_getSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevSpeed, this.speed);
    }

    @Override
    public float palladium_getHorizontalSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevHorizontalSpeed, this.horizontalSpeed);
    }

}
