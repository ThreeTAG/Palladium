package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;

public interface OpacityChanging<T extends Ability> {

    float getOpacity(LivingEntity entity, AbilityInstance<T> instance, boolean isLocalPlayer, float partialTick);

    float getMobVisibilityMultiplier(LivingEntity entity, AbilityInstance<T> instance, float partialTick);

    @SuppressWarnings({"rawtypes", "unchecked"})
    static float getOpacity(LivingEntity entity, boolean isLocalPlayer, float partialTick) {
        float f = 1F;

        for (AbilityInstance ability : AbilityUtil.getInstances(entity)) {
            if (ability.getAbility() instanceof OpacityChanging<?> opacityChanging) {
                f = Math.min(f, opacityChanging.getOpacity(entity, ability, isLocalPlayer, partialTick));
            }
        }

        return f;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static float getMobVisibilityMultiplier(LivingEntity entity, float partialTick) {
        float f = 1F;

        for (AbilityInstance ability : AbilityUtil.getInstances(entity)) {
            if (ability.getAbility() instanceof OpacityChanging<?> opacityChanging) {
                f = Math.min(f, opacityChanging.getMobVisibilityMultiplier(entity, ability, partialTick));
            }
        }

        return f;
    }

}
