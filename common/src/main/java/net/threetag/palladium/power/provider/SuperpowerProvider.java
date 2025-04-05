package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.*;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        for (Holder<Power> superpower : SuperpowerUtil.getHandler(entity).getSuperpowers()) {
            collector.addPower(superpower, () -> Validator.INSTANCE);
        }
    }

    public static class Validator implements PowerValidator {

        private static final Validator INSTANCE = new Validator();

        @Override
        public boolean stillValid(LivingEntity entity, Holder<Power> power) {
            return SuperpowerUtil.getHandler(entity).getSuperpowers().contains(power);
        }
    }

}
