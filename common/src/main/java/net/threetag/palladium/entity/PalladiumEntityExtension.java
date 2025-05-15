package net.threetag.palladium.entity;

import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.util.property.EntityPropertyHandler;

public interface PalladiumEntityExtension {

    EntityPropertyHandler palladium$getPropertyHandler();

    TrailHandler palladium$getTrailHandler();

    Vec3 palladium$getLastTickPos();

}
