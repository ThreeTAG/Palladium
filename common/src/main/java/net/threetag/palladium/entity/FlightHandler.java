package net.threetag.palladium.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.sound.FlightSound;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.util.Platform;

import java.util.function.Supplier;

public class FlightHandler {

    public static Object CACHED_SOUND = null;

    private final Player player;
    private Vec3 prevMovementDelta = null;
    private FlightHandler.FlightType flightType = FlightHandler.FlightType.NONE;
    private float flightSpeed = 1F;
    private boolean didFlew = false;
    public float flightBoost = 0F;
    private float prevFlightBoost = 0F;
    private int verticalHover = 0;
    private float levitation = 0F;
    private float prevLevitation = 0F;
    private float hovering = 0F;
    private float prevHovering = 0F;
    private Vec3 flightVector = Vec3.ZERO;
    private Vec3 prevFlightVector = Vec3.ZERO;
    private Vec3 prevLookAngle = Vec3.ZERO;
    private float speed = 0;
    private float prevSpeed = 0;
    private float horizontalSpeed = 0;
    private float prevHorizontalSpeed = 0;

    public FlightHandler(Player player) {
        this.player = player;
    }

    public void tick() {
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
            if (this.flightType.isNotNull()) {
                this.flightSpeed = (float) this.player.getAttributeValue(this.flightType.getAttribute());
            }

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

    public Vec3 getPrevMovementDelta() {
        return this.prevMovementDelta;
    }

    public FlightHandler.FlightType getFlightType() {
        return this.flightType;
    }

    public void setFlightType(FlightHandler.FlightType flightType) {
        this.flightType = flightType;
        var attribute = flightType.getAttribute();

        if (attribute != null) {
            this.flightSpeed = (float) this.player.getAttributeValue(attribute);
        }

        if (this.player.isSprinting()) {
            this.flightBoost = 1F;
        }

        this.player.refreshDimensions();
    }

    public float getFlightAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevFlightBoost, this.flightBoost);
    }

    public float getLevitationAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevLevitation, this.levitation);
    }

    public float getHoveringAnimation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevHovering, this.hovering);
    }

    public Vec3 getFlightVector(float partialTicks) {
        return this.prevFlightVector.lerp(this.flightVector, partialTicks);
    }

    public Vec3 getLookAngle(float partialTicks) {
        return this.prevLookAngle.lerp(this.player.getLookAngle(), partialTicks);
    }

    public float getSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevSpeed, this.speed);
    }

    public float getHorizontalSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevHorizontalSpeed, this.horizontalSpeed);
    }

    public static FlightType getAvailableFlightType(LivingEntity entity) {
        if (entity.getAttributes().hasAttribute(PalladiumAttributes.FLIGHT_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.FLIGHT_SPEED.get()) > 0D) {
            return FlightType.NORMAL;
        }

        if (entity.getAttributes().hasAttribute(PalladiumAttributes.LEVITATION_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) > 0D) {
            return FlightType.LEVITATION;
        }

        return FlightType.NONE;
    }

    public static FlightAnimationType getAnimationType(LivingEntity entity) {
        if (entity.getAttributes().hasAttribute(PalladiumAttributes.HEROIC_FLIGHT_TYPE.get()) && entity.getAttributeValue(PalladiumAttributes.HEROIC_FLIGHT_TYPE.get()) > 0D) {
            return FlightAnimationType.HEROIC;
        }

        return FlightAnimationType.NORMAL;
    }

    @Environment(EnvType.CLIENT)
    public static void startSound(Player player) {
        if (player == Minecraft.getInstance().player) {
            if (CACHED_SOUND instanceof FlightSound sound) {
                sound.stop = true;
            }

            Minecraft.getInstance().getSoundManager().play((SoundInstance) (CACHED_SOUND = new FlightSound(player, SoundEvents.ELYTRA_FLYING, player.getSoundSource())));
        }
    }

    public enum FlightType {

        NONE(() -> null), NORMAL(PalladiumAttributes.FLIGHT_SPEED), LEVITATION(PalladiumAttributes.LEVITATION_SPEED);

        FlightType(Supplier<Attribute> attributeSupplier) {
            this.attributeSupplier = attributeSupplier;
        }

        private final Supplier<Attribute> attributeSupplier;

        public Attribute getAttribute() {
            return this.attributeSupplier.get();
        }

        public boolean isNotNull() {
            return this != NONE;
        }

        public boolean isNull() {
            return this == NONE;
        }

        public boolean isNormal() {
            return this == NORMAL;
        }

        public boolean isLevitation() {
            return this == LEVITATION;
        }

    }

    public enum FlightAnimationType {

        NORMAL,
        HEROIC

    }

}
