package net.threetag.palladium.client.trail;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.TrailAbility;

import java.util.ArrayList;
import java.util.List;

public record Trail(float spacing, int lifetime, TrailRenderer<?> trailRenderer) {

    public static final Codec<Trail> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("spacing", 1F).forGetter(Trail::spacing),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("lifetime", 20).forGetter(Trail::lifetime),
            TrailRenderer.CODEC.fieldOf("renderer").forGetter(Trail::trailRenderer)
    ).apply(instance, Trail::new));

    public static List<Trail> getAvailableTrailsForEntity(Entity entity) {
        List<Trail> trails = new ArrayList<>();
        if (entity instanceof LivingEntity living) {
            for (AbilityInstance<TrailAbility> instance : AbilityUtil.getEnabledInstances(living, AbilitySerializers.TRAIL.get())) {
                var trailConfig = TrailManager.INSTANCE.get(instance.getAbility().trailRendererId);

                if (trailConfig != null) {
                    trails.addAll(trailConfig.trails());
                }
            }
        }
        return trails;
    }

}
