package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.*;
import net.threetag.palladium.util.property.PalladiumProperties;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        Power power = PowerManager.getInstance(entity.level).getPower(PalladiumProperties.SUPERPOWER_ID.get(entity));
        collector.addPower(power, Validator::new);
    }

    public static class Validator implements IPowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            var stored = PalladiumProperties.SUPERPOWER_ID.get(entity);
            return stored != null && stored.equals(power.getId());
        }
    }

}
