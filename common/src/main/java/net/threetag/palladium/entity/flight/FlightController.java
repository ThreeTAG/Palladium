package net.threetag.palladium.entity.flight;

import net.minecraft.world.entity.LivingEntity;

public class FlightController<T extends FlightType> {

    public void tick(LivingEntity entity, T flightType) {
        if (entity.onGround() || entity.getVehicle() != null) {
            this.stopFlight(entity);
        }
    }

    public void start(LivingEntity entity, T flightType) {

    }

    public void stop(LivingEntity entity, T flightType) {

    }

    public final void stopFlight(LivingEntity entity) {
        EntityFlightHandler.get(entity).stopFlight();
    }

    public void clampRotation(LivingEntity entity, T flightType) {

    }

}
