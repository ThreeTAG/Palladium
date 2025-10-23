package net.threetag.palladium.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Collections;

public class EntityScaleUtil {

    private static EntityScaleUtil INSTANCE = new EntityScaleUtil();

    public static void setInstance(EntityScaleUtil entityScaleUtil) {
        INSTANCE = entityScaleUtil;
    }

    public static EntityScaleUtil getInstance() {
        return INSTANCE;
    }

    public boolean startScaleChange(Entity entity, ResourceLocation scaleTypeId, float targetScale, int tickDelay) {
        return false;
    }

    public float getWidthScale(Entity entity) {
        return entity instanceof LivingEntity living ? living.getScale() : 1F;
    }

    public float getWidthScale(Entity entity, float delta) {
        return this.getWidthScale(entity);
    }

    public float getHeightScale(Entity entity) {
        return entity instanceof LivingEntity living ? living.getScale() : 1F;
    }

    public float getHeightScale(Entity entity, float delta) {
        return this.getHeightScale(entity);
    }

    public float getModelWidthScale(Entity entity) {
        return this.getWidthScale(entity);
    }

    public float getModelWidthScale(Entity entity, float delta) {
        return this.getWidthScale(entity, delta);
    }

    public float getModelHeightScale(Entity entity) {
        return this.getHeightScale(entity);
    }

    public float getModelHeightScale(Entity entity, float delta) {
        return this.getHeightScale(entity, delta);
    }

    public float getEyeHeightScale(Entity entity) {
        return this.getHeightScale(entity);
    }

    public float getEyeHeightScale(Entity entity, float delta) {
        return this.getHeightScale(entity, delta);
    }

    public Collection<ResourceLocation> getScaleTypeIds() {
        return Collections.emptyList();
    }

}
