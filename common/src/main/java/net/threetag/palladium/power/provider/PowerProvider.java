package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.PowerCollector;
import net.threetag.palladiumcore.registry.PalladiumRegistry;

public abstract class PowerProvider {

    public static final PalladiumRegistry<PowerProvider> REGISTRY = PalladiumRegistry.create(PowerProvider.class, Palladium.id("power_providers"));

    public abstract void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector);

}
