package net.threetag.palladium.power.dampening;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public abstract class PowerDampeningSource {

    public abstract boolean isDampened(LivingEntity entity, Holder<Power> powerHolder);

    public static boolean isDampened(Holder<Power> power, LivingEntity entity) {
        for (PowerDampeningSource source : entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER_DAMPENING_SOURCE)) {
            if (source.isDampened(entity, power)) {
                return true;
            }
        }

        return false;
    }

}
