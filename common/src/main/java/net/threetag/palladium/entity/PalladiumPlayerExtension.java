package net.threetag.palladium.entity;

import net.minecraft.world.phys.Vec3;

public interface PalladiumPlayerExtension {

    Vec3 palladium_getPrevMovementDelta();

    FlightHandler.FlightType palladium_getFlightType();

    void palladium_setFlightType(FlightHandler.FlightType flightType);

    float palladium_getFlightAnimation(float partialTicks);

    float palladium_getLevitationAnimation(float partialTicks);

    float palladium_getHoveringAnimation(float partialTicks);

    Vec3 palladium_getFlightVector(float partialTicks);

    Vec3 palladium_getLookAngle(float partialTicks);

    float palladium_getSpeed(float partialTicks);

    float palladium_getHorizontalSpeed(float partialTicks);

}
