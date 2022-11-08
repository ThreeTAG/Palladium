package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.*;
import net.threetag.palladium.util.property.PalladiumProperties;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
            Power power = PowerManager.getInstance(entity.level).getPower(id);
            collector.addPower(power, Validator::new);
        }
    }

    public static class Validator implements IPowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            var stored = PalladiumProperties.SUPERPOWER_IDS.get(entity);
            return stored != null && stored.contains(power.getId());
        }
    }

}
