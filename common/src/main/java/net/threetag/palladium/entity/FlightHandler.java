package net.threetag.palladium.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.sound.FlightSound;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.util.Platform;

import java.util.concurrent.atomic.AtomicReference;

public class FlightHandler {


    public static Object SOUND;

    public static void init() {
        LivingEntityEvents.TICK.register(FlightHandler::handleFlight);
    }

    private static void handleFlight(LivingEntity entity) {
        if (!(entity instanceof Player) || entity.isOnGround() || PlayerUtil.isCreativeFlying(entity) || entity.isFallFlying() || !PalladiumProperties.JUMP_KEY_DOWN.isRegistered(entity)) {
            stopSound(entity);
            return;
        }

        double flightSpeed = entity.getAttributeValue(PalladiumAttributes.FLIGHT_SPEED.get());
        double levitationSpeed = entity.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get());
        double jetpackFlightSpeed = entity.getAttributeValue(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get());
        boolean hovering = entity.getAttributeValue(PalladiumAttributes.HOVERING.get()) > 0D;

        if (flightSpeed > 0D) {
            if (entity.zza > 0F) {
                startSound(entity);
                Vec3 vec = entity.getLookAngle();
                double speed = entity.isSprinting() ? flightSpeed * 1.5D : flightSpeed;
                entity.setDeltaMovement(vec.x * speed, vec.y * speed - (entity.isCrouching() ? entity.getBbHeight() * 0.2F : 0), vec.z * speed);
            } else if (entity.isCrouching()) {
                entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, entity.getBbHeight() * -0.2F, entity.getDeltaMovement().z));
                stopSound(entity);
            } else {
                entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.sin(entity.tickCount / 10F) / 100F, entity.getDeltaMovement().z));
                stopSound(entity);
            }
            entity.fallDistance = 0F;
        } else if (levitationSpeed > 0D) {
            Vec3 motion = entity.getDeltaMovement();
            double motionY = motion.y;

            if (!entity.isInWater()) {
                if (hovering) {
                    if (motionY < -0.125F) {
                        motionY += 0.125F;
                    } else if (motionY > 0.05F) {
                        motionY -= 0.05F;
                    } else {

                        motionY = Mth.sin(entity.tickCount / 15F) * 0.025F;
                    }
                    stopSound(entity);
                } else if (motionY < 0) {
                    motionY += 0.07;
                }
            }

            float f = hovering ? 0.2F : 1;

            if (entity.isSprinting()) {
                f *= 1.2F;
            }

            entity.setDeltaMovement(motion.x, motionY, motion.z);
            // default 0.075
            System.out.println(levitationSpeed);
            moveFlying(entity, entity.xxa, entity.zza, (float) (levitationSpeed * f));
            motion = entity.getDeltaMovement();
            motionY = motion.y;
            f = 1 + (f - 1) / 3;

            if (PalladiumProperties.JUMP_KEY_DOWN.get(entity)) {
                motionY += (hovering ? 0.2F : 0.125F) * f;
            }

            if (entity.isCrouching()) {
                motionY -= (hovering ? 0.125F : 0.075F) * f;
            }
            entity.setDeltaMovement(motion.x, motionY, motion.z);

            if (!hovering) {
                startSound(entity);
            }

            entity.fallDistance = 0F;
        } else if (jetpackFlightSpeed > 0D) {
            if (PalladiumProperties.JUMP_KEY_DOWN.get(entity) && !hovering) {
                Vec3 motion = entity.getDeltaMovement();
                double y = motion.y;

                if (y < 0) {
                    y += 0.18D;
                } else {
                    y += 0.12D;
                }

                entity.setDeltaMovement(motion.x, y, motion.z);
                moveFlying(entity, entity.xxa, entity.zza, (float) jetpackFlightSpeed);
                startSound(entity);
                entity.fallDistance = 0F;
            }

            if (hovering) {
                Vec3 motion = entity.getDeltaMovement();
                double motionY = motion.y;

                if (motionY < -0.15F) {
                    motionY += 0.15F;
                } else {
                    motionY = Mth.sin(entity.tickCount / 10F) * 0.025F;
                }

                entity.setDeltaMovement(motion.x, motionY, motion.z);
                stopSound(entity);
                entity.fallDistance = 0F;
            }
        } else {
            stopSound(entity);
        }
    }

    public static void startSound(LivingEntity entity) {
        if (SOUND == null && Platform.isClient())
            if (entity == Minecraft.getInstance().player)
                Minecraft.getInstance().getSoundManager().play((SoundInstance) (SOUND = new FlightSound(entity, SoundEvents.ELYTRA_FLYING, SoundSource.PLAYERS)));
    }

    public static void stopSound(LivingEntity entity) {
        if (SOUND != null && Platform.isClient())
            if (entity == Minecraft.getInstance().player) {
                ((FlightSound) SOUND).stop = true;
            }
    }

    public static void moveFlying(LivingEntity entity, float moveStrafing, float moveForward, float speed) {
        float f3 = moveStrafing * moveStrafing + moveForward * moveForward;

        if (f3 >= 1.0E-4F) {
            f3 = Mth.sqrt(f3);

            if (f3 < 1.0F) {
                f3 = 1.0F;
            }

            f3 = speed / f3;
            moveStrafing *= f3;
            moveForward *= f3;
            float f4 = Mth.sin(entity.getYRot() * (float) Math.PI / 180.0F);
            float f5 = Mth.cos(entity.getYRot() * (float) Math.PI / 180.0F);
            Vec3 motion = entity.getDeltaMovement();
            entity.setDeltaMovement(motion.x + (moveStrafing * f5 - moveForward * f4), motion.y, motion.z + (moveForward * f5 + moveStrafing * f4));
        }
    }

    public static FlightType getCurrentFlightType(Player playerEntity) {
        if (playerEntity.isOnGround() || PlayerUtil.isCreativeFlying(playerEntity) || playerEntity.isFallFlying()) {
            return FlightType.NONE;
        }

        double flightSpeed = playerEntity.getAttributeValue(PalladiumAttributes.FLIGHT_SPEED.get());
        double levitationSpeed = playerEntity.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get());
        double jetpackFlightSpeed = playerEntity.getAttributeValue(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get());
        boolean hovering = playerEntity.getAttributeValue(PalladiumAttributes.HOVERING.get()) > 0D;

        if (flightSpeed > 0D) {
            if (playerEntity.zza > 0F || playerEntity.isCrouching()) {
                return FlightType.NORMAL;
            } else {
                return FlightType.NORMAL_HOVERING;
            }
        } else if (levitationSpeed > 0D) {
            return hovering ? FlightType.LEVITATION_HOVERING : FlightType.LEVITATION;
        } else if (jetpackFlightSpeed > 0D) {
            AtomicReference<FlightType> result = new AtomicReference<>(FlightType.NONE);
            if (PalladiumProperties.JUMP_KEY_DOWN.get(playerEntity) && !hovering) {
                result.set(FlightType.JETPACK);
            }

            if (hovering) {
                result.set(FlightType.JETPACK_HOVERING);
            }
            return result.get();
        } else {
            return FlightType.NONE;
        }
    }

    public enum FlightType {

        NONE, NORMAL, NORMAL_HOVERING, LEVITATION, LEVITATION_HOVERING, JETPACK, JETPACK_HOVERING;

        public boolean isNotNull() {
            return this != NONE;
        }

        public boolean isNull() {
            return this == NONE;
        }

        public boolean isNormal() {
            return this == NORMAL || this == NORMAL_HOVERING;
        }

        public boolean isLevitation() {
            return this == LEVITATION || this == LEVITATION_HOVERING;
        }

        public boolean isJetpack() {
            return this == JETPACK || this == JETPACK_HOVERING;
        }

        public boolean isHovering() {
            return this == NORMAL_HOVERING || this == LEVITATION_HOVERING || this == JETPACK_HOVERING;
        }

    }

}
