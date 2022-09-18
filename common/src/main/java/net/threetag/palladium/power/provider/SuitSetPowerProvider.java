package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.SuitSetPowerManager;

import java.util.List;

public class SuitSetPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler) {
        for (SuitSet suitSet : SuitSet.REGISTRY.getValues()) {
            if (suitSet.isWearing(entity)) {
                List<Power> powers = SuitSetPowerManager.getInstance().getPowerForSuitSet(suitSet);

                if (powers != null) {
                    for (Power power : powers) {
                        if (power != null && !handler.hasPower(power)) {
                            handler.setPowerHolder(power, new PowerHolder(entity, power, suitSet));
                        }
                    }
                }
            }
        }
    }

    public static class PowerHolder extends DefaultPowerHolder {

        public final SuitSet suitSet;

        public PowerHolder(LivingEntity entity, Power power, SuitSet suitSet) {
            super(entity, power, null);
            this.suitSet = suitSet;
        }

        @Override
        public boolean isInvalid() {
            return this.getPower().isInvalid() || !this.suitSet.isWearing(this.entity);
        }
    }

}
