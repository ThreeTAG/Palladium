package net.threetag.palladium.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class FlightHandler {

    public static FlightType getAvailableFlightType(LivingEntity entity) {
        if (entity.getAttributes().hasAttribute(PalladiumAttributes.FLIGHT_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.FLIGHT_SPEED.get()) > 0D) {
            return FlightType.NORMAL;
        }

        if (entity.getAttributes().hasAttribute(PalladiumAttributes.LEVITATION_SPEED.get()) && entity.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) > 0D) {
            return FlightType.LEVITATION;
        }

        return FlightType.NONE;
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

}
