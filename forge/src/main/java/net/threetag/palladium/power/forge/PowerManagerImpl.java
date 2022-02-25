package net.threetag.palladium.power.forge;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.capability.forge.PalladiumCapabilities;
import net.threetag.palladium.power.IPowerHandler;

public class PowerManagerImpl {

    public static IPowerHandler getPowerHandler(LivingEntity entity) {
        return entity.getCapability(PalladiumCapabilities.POWER_HANDLER).orElseThrow(() -> new RuntimeException("Entity does not have power capability!"));
    }

}
