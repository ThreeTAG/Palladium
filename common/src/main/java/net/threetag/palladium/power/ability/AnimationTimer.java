package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.Easing;

public interface AnimationTimer {

    /**
     * Calculates and returns the animation value
     *
     * @return Value between 0.0-1.0 that determines the state
     */
    float getAnimationValue(AbilityEntry entry, float partialTick);

    /**
     * Gets the raw animation timer value
     *
     * @return Value for the actual timer
     */
    float getAnimationTimer(AbilityEntry entry, float partialTick, boolean maxedOut);

    default float getAnimationTimer(AbilityEntry entry, float partialTick) {
        return this.getAnimationTimer(entry, partialTick, false);
    }

    static float getValue(LivingEntity entity, Ability ability, float partialTick, Easing easing) {
        if (ability instanceof AnimationTimer timer) {
            float f = 0F;

            for (AbilityEntry entry : AbilityUtil.getEntries(entity, ability)) {
                f = Math.max(f, timer.getAnimationValue(entry, partialTick));
            }

            return easing.apply(f);
        }

        return 0F;
    }

    static float getValue(LivingEntity entity, Ability ability, float partialTick) {
        return getValue(entity, ability, partialTick, Easing.LINEAR);
    }

}
