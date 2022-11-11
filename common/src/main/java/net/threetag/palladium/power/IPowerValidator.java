package net.threetag.palladium.power;

import net.minecraft.world.entity.LivingEntity;

public interface IPowerValidator {

    IPowerValidator ALWAYS_ACTIVE = (entity, power) -> true;

    boolean stillValid(LivingEntity entity, Power power);

}
