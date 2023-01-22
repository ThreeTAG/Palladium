package net.threetag.palladium.entity;

import net.minecraft.world.phys.Vec3;

public interface PalladiumPlayerExtension {

    Vec3 palladium_getPrevMovementDelta();

    float palladium_getFlightAnimation(float partialTicks);

    float palladium_getSpeed(float partialTicks);

}
