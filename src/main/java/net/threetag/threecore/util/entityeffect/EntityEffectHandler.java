package net.threetag.threecore.util.entityeffect;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class EntityEffectHandler {

    public static void start(Entity anchor, EntityEffect entityEffect) {
        if (!anchor.world.isRemote) {
            EffectEntity effectEntity = new EffectEntity(anchor.world, anchor, entityEffect);
            anchor.world.addEntity(effectEntity);
        }
    }

    public static void stop(Entity anchor, Predicate<EntityEffect> predicate) {
        if (!anchor.world.isRemote) {
            anchor.world.getEntitiesInAABBexcluding(anchor, anchor.getBoundingBox().grow(2), entity -> entity instanceof EffectEntity && ((EffectEntity) entity).getAnchorEntity() == anchor && predicate.test(((EffectEntity) entity).entityEffect)).forEach(Entity::remove);
        }
    }

    public static void stop(Entity anchor, EntityEffectType entityEffectType) {
        stop(anchor, effect -> effect.getEntityEffectType() == entityEffectType);
    }

}
