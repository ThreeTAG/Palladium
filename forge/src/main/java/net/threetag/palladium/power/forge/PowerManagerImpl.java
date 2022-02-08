package net.threetag.palladium.power.forge;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.forge.capability.PowerCapability;
import net.threetag.palladium.power.IPowerHolder;

public class PowerManagerImpl {

    public static IPowerHolder getPowerHolder(LivingEntity entity) {
        return entity.getCapability(PowerCapability.POWER).orElseThrow(() -> new RuntimeException("Entity does not have power capability!"));
    }

}
