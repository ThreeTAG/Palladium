package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.power.*;

import java.util.List;

public class SuitSetPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        for (SuitSet suitSet : SuitSet.REGISTRY.getValues()) {
            if (suitSet.isWearing(entity)) {
                List<Power> powers = SuitSetPowerManager.getInstance().getPowerForSuitSet(suitSet);

                if (powers != null) {
                    for (Power power : powers) {
                        collector.addPower(power, () -> new Validator(suitSet));
                    }
                }
            }
        }
    }

    public record Validator(SuitSet suitSet) implements IPowerValidator {

        @Override
            public boolean stillValid(LivingEntity entity, Power power) {
                return this.suitSet.isWearing(entity);
            }
        }

}
