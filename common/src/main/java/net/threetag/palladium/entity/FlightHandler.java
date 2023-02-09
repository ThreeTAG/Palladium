package net.threetag.palladium.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FlightHandler {

    public static void init() {
//        LivingEntityEvents.TICK.register(FlightHandler::handleFlight);
    }

    public static FlightType getAvailableFlightType(LivingEntity entity) {
        if (entity.getAttributes().hasAttribute(PalladiumAttributes.FLIGHT_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.FLIGHT_SPEED.get()) > 0D) {
            return FlightType.NORMAL;
        }

        if (entity.getAttributes().hasAttribute(PalladiumAttributes.LEVITATION_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) > 0D) {
            return FlightType.LEVITATION;
        }

        if (entity.getAttributes().hasAttribute(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get()) > 0D) {
            return FlightType.JETPACK;
        }

        return FlightType.NONE;
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

    public enum FlightType {

        NONE, NORMAL, LEVITATION, JETPACK;

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

        public boolean isJetpack() {
            return this == JETPACK;
        }

    }

}
