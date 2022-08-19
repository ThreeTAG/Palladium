package net.threetag.palladium.power.forge;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.capability.forge.PalladiumCapabilities;
import net.threetag.palladium.power.IPowerHandler;

import java.util.Optional;

public class PowerManagerImpl {

    @SuppressWarnings("ConstantConditions")
    public static Optional<IPowerHandler> getPowerHandler(LivingEntity entity) {
        return entity.getCapability(PalladiumCapabilities.POWER_HANDLER).resolve();
    }

}
