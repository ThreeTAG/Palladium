package net.threetag.palladium.entity.effect;

import net.minecraft.world.entity.Entity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.EffectEntity;

import java.util.function.Predicate;

public abstract class EntityEffect {

    public abstract void tick(EffectEntity entity, Entity anchor);

    public boolean isPlaying(EffectEntity entity) {
        return !entity.isDonePlaying();
    }

    public void stopPlaying(EffectEntity entity) {
        entity.setDonePlaying(true);
    }

    public static void start(Entity anchor, EntityEffect entityEffect) {
        Palladium.PROXY.spawnEffectEntity(anchor, entityEffect);
    }

    public static void stop(Entity anchor, Predicate<EntityEffect> predicate) {
        anchor.level().getEntities(anchor, anchor.getBoundingBox().inflate(2), entity -> entity instanceof EffectEntity && ((EffectEntity) entity).getAnchorEntity() == anchor && predicate.test(((EffectEntity) entity).entityEffect)).forEach(Entity::discard);
    }

    public static void stop(Entity anchor, EntityEffect entityEffectType) {
        stop(anchor, effect -> effect == entityEffectType);
    }

}
