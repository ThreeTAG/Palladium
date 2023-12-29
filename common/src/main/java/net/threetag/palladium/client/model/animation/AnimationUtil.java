package net.threetag.palladium.client.model.animation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.AnimationTimer;
import net.threetag.palladium.util.Easing;

public class AnimationUtil {

    public static float ease(Easing ease, float value) {
        return ease.apply(value);
    }

    public static float getAnimationTimerAbilityValue(LivingEntity entity, ResourceLocation powerId, String abilityKey, float partialTicks) {
        var entry = AbilityUtil.getEntry(entity, powerId, abilityKey);

        if (entry != null && entry.getConfiguration().getAbility() instanceof AnimationTimer timer) {
            return timer.getAnimationValue(entry, partialTicks);
        }

        return 0F;
    }

    public static float getAnimationTimerAbilityValue(LivingEntity entity, ResourceLocation powerId, String abilityKey, float partialTicks, float start, float end) {
        var entry = AbilityUtil.getEntry(entity, powerId, abilityKey);

        if (entry != null && entry.getConfiguration().getAbility() instanceof AnimationTimer timer) {
            var val = timer.getAnimationTimer(entry, partialTicks);
            return getInbetweenProgress(val, start, end);
        }

        return 0F;
    }

    public static float getInbetweenProgress(float progress, float startingPoint, float endPoint) {
        float shiftedEnd = endPoint - startingPoint;
        float shifted = Mth.clamp(progress - startingPoint, 0, shiftedEnd);
        return shifted / shiftedEnd;
    }

}
