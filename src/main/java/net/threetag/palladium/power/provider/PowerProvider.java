package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.PowerCollector;

public abstract class PowerProvider {

    public abstract void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector);

}
