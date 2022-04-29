package net.threetag.palladium.power.fabric;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.components.fabric.PalladiumComponents;
import net.threetag.palladium.power.IPowerHandler;

import java.util.Optional;

public class PowerManagerImpl {

    public static Optional<IPowerHandler> getPowerHandler(LivingEntity entity) {
        try {
            return Optional.of(PalladiumComponents.POWER_HANDLER.get(entity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
