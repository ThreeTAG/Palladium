package net.threetag.palladium.power.fabric;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.fabric.components.PalladiumComponents;
import net.threetag.palladium.power.IPowerHandler;

public class PowerManagerImpl {

    public static IPowerHandler getPowerHandler(LivingEntity entity) {
        return PalladiumComponents.POWER_HANDLER.get(entity);
    }

}
