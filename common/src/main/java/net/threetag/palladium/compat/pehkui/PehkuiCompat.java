package net.threetag.palladium.compat.pehkui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.*;
import net.threetag.palladium.util.SizeUtil;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import virtuoel.pehkui.api.*;

import java.util.Collection;

public class PehkuiCompat extends SizeUtil {

    public static final ScaleType ABILITY_SCALE = ScaleRegistries.register(ScaleRegistries.SCALE_TYPES, Palladium.id("ability"), ScaleType.Builder.create().affectsDimensions().build());
    public static final ScaleModifier ABILITY_MODIFIER = ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, Palladium.id("ability"), new TypedScaleModifier(() -> ABILITY_SCALE));

    public static void init() {
        SizeUtil.setInstance(new PehkuiCompat());
        ScaleTypes.BASE.getDefaultBaseValueModifiers().add(ABILITY_MODIFIER);
        LivingEntityEvents.TICK.register(entity -> {
            float scale = getAbilityMultiplier(entity);

            if (scale != ABILITY_SCALE.getScaleData(entity).getTargetScale()) {
                ABILITY_SCALE.getScaleData(entity).setTargetScale(scale);
            }
        });
    }

    public static float getAbilityMultiplier(LivingEntity entity) {
        float f = 1F;

        try {
            for (AbilityEntry enabledEntry : AbilityUtil.getEnabledEntries(entity, Abilities.SIZE.get())) {
                f *= enabledEntry.getProperty(SizeAbility.SCALE);
            }
        } catch (Exception ignored) {

        }

        return f;
    }

    @Override
    public boolean startScaleChange(Entity entity, ResourceLocation scaleTypeId, float targetScale, int tickDelay) {
        var scaleType = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, scaleTypeId);

        if (scaleType != null) {
            var data = scaleType.getScaleData(entity);
            data.setScaleTickDelay(tickDelay);
            data.setTargetScale(targetScale);
            return true;
        }

        return false;
    }

    @Override
    public float getWidthScale(Entity entity) {
        return ScaleTypes.WIDTH.getScaleData(entity).getScale();
    }

    @Override
    public float getWidthScale(Entity entity, float delta) {
        return ScaleTypes.WIDTH.getScaleData(entity).getScale(delta);
    }

    @Override
    public float getHeightScale(Entity entity) {
        return ScaleTypes.HEIGHT.getScaleData(entity).getScale();
    }

    @Override
    public float getHeightScale(Entity entity, float delta) {
        return ScaleTypes.HEIGHT.getScaleData(entity).getScale(delta);
    }

    @Override
    public Collection<ResourceLocation> getScaleTypeIds() {
        return ScaleRegistries.SCALE_TYPES.keySet();
    }
}
