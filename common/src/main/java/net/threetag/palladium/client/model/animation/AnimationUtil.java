package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Easing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.AnimationTimerAbility;

public class AnimationUtil {

    public static float ease(Ease ease, float value) {
        return Easing.easingFromEnum(ease, value);
    }

    public static float getAnimationTimerAbilityValue(LivingEntity entity, ResourceLocation powerId, String abilityKey, float partialTicks) {
        var entry = AbilityUtil.getEntry(entity, powerId, abilityKey);

        if (entry != null && entry.getConfiguration().getAbility() instanceof AnimationTimerAbility) {
            return Mth.lerp(partialTicks, entry.getProperty(AnimationTimerAbility.PREV_VALUE), entry.getProperty(AnimationTimerAbility.VALUE)) / (float) entry.getProperty(AnimationTimerAbility.MAX_VALUE);
        }

        return 0F;
    }

}
