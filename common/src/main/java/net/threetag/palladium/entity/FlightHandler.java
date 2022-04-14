package net.threetag.palladium.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.sound.FlightSound;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.concurrent.atomic.AtomicReference;

public class FlightHandler {

    public static final PalladiumProperty<Boolean> JUMP_KEY_DOWN = new BooleanProperty("jump_key_down");
    public static Object SOUND;

    public static void init() {
        PalladiumEvents.REGISTER_PROPERTY.register(handler -> handler.register(JUMP_KEY_DOWN, false));
        PalladiumEvents.LIVING_UPDATE.register(FlightHandler::handleFlight);
    }

    private static void handleFlight(LivingEntity entity) {
        if (!(entity instanceof Player) || entity.isOnGround() || PlayerUtil.isCreativeFlying(entity) || entity.isFallFlying() || !JUMP_KEY_DOWN.isRegistered(entity)) {
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
            moveFlying(entity, entity.xxa, entity.zza, (float) (levitationSpeed * f));
            motion = entity.getDeltaMovement();
            motionY = motion.y;
            f = 1 + (f - 1) / 3;

            if (JUMP_KEY_DOWN.get(entity)) {
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
            if (JUMP_KEY_DOWN.get(entity) && !hovering) {
                Vec3 motion = entity.getDeltaMovement();
                double y = motion.y;

                if (y < 0) {
                    y += 0.18D;
                } else {
                    y += 0.12D;
                }

                entity.setDeltaMovement(motion.x, y, motion.z);
                moveFlying(entity, entity.xxa, entity.zza, 0.15F);
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
        if (SOUND == null && Platform.getEnvironment() == Env.CLIENT)
            if (entity == Minecraft.getInstance().player)
                Minecraft.getInstance().getSoundManager().play((SoundInstance) (SOUND = new FlightSound(entity, SoundEvents.ELYTRA_FLYING, SoundSource.PLAYERS)));
    }

    public static void stopSound(LivingEntity entity) {
        if (SOUND != null && Platform.getEnvironment() == Env.CLIENT)
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

    public static FlightType getCurrentlyUsedFlightType(Player playerEntity) {
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
            if (JUMP_KEY_DOWN.get(playerEntity) && !hovering) {
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

    @Environment(EnvType.CLIENT)
    public static void animation(LivingEntity entityIn, HumanoidModel<?> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof Player && !entityIn.isOnGround()
                && entityIn.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) <= 0D
                && entityIn.getAttributeValue(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get()) > 0D) {
            float speed = (float) Mth.clamp(Math
                    .sqrt((entityIn.xo - entityIn.position().x) * (entityIn.xo - entityIn.position().x) + (entityIn.zo - entityIn.position().z) * (entityIn.zo
                            - entityIn.position().z)), 0F, 1F);
            double d1 = 1 - speed;

            model.rightArm.xRot *= d1;
            model.rightArm.yRot *= d1;
            model.rightArm.zRot *= d1;
            model.leftArm.xRot *= d1;
            model.leftArm.yRot *= d1;
            model.leftArm.zRot *= d1;
            model.rightLeg.xRot *= d1;
            model.rightLeg.yRot *= d1;
            model.rightLeg.zRot *= d1;
            model.leftLeg.xRot *= d1;
            model.leftLeg.yRot *= d1;
            model.leftLeg.zRot *= d1;

            int type = (entityIn.isOnGround() || (entityIn instanceof Player && ((Player) entityIn).getAbilities().flying) ? 0 : entityIn.zza < 0 ? -1 : 1);
            speed *= type;
            model.head.xRot -= 0.52359877559829887307710723054658 * 2 * speed;
            model.hat.xRot = model.head.xRot;
        }
    }

    @Environment(EnvType.CLIENT)
    public static void setupRotations(AbstractClientPlayer player, PlayerRenderer playerRenderer, PoseStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        if (!player.isOnGround()
                && player.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) <= 0D
                && player.getAttributeValue(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get()) > 0D) {
            float speed = (float) Mth.clamp(Math
                    .sqrt((player.xo - player.position().x) * (player.xo - player.position().x) + (player.zo - player.position().z) * (player.zo
                            - player.position().z)), 0F, 1F);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-60 * speed));
        }
    }

    public enum FlightType {

        NONE, NORMAL, NORMAL_HOVERING, LEVITATION, LEVITATION_HOVERING, JETPACK, JETPACK_HOVERING;

        public boolean isNotNull() {
            return this != NONE;
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
