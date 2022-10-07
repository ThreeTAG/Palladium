package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;

public class ShrinkBodyOverlayAbility extends InterpolatedIntegerAbility {

    public static float getValue(LivingEntity entity) {
        float scale = 0F;
        for (AbilityEntry entry : Ability.getEntries(entity, Abilities.SHRINK_BODY_OVERLAY.get())) {
            float f = entry.getProperty(VALUE) / (float) entry.getProperty(MAX_VALUE);

            if(f > scale) {
                scale = f;
            }
        }

        return scale;
    }

}
