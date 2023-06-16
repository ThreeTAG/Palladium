package net.threetag.palladium.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;

public class SizeUtil {

    private static SizeUtil INSTANCE = new SizeUtil();

    public static void setInstance(SizeUtil sizeUtil) {
        INSTANCE = sizeUtil;
    }

    public static SizeUtil getInstance() {
        return INSTANCE;
    }

    public boolean startScaleChange(Entity entity, ResourceLocation scaleTypeId, float targetScale, int tickDelay) {
        return false;
    }

    public float getWidthScale(Entity entity) {
        return 1F;
    }

    public float getWidthScale(Entity entity, float delta) {
        return 1F;
    }

    public float getHeightScale(Entity entity) {
        return 1F;
    }

    public float getHeightScale(Entity entity, float delta) {
        return 1F;
    }

    public float getEyeHeightScale(Entity entity) {
        return 1F;
    }

    public float getEyeHeightScale(Entity entity, float delta) {
        return 1F;
    }

    public Collection<ResourceLocation> getScaleTypeIds() {
        return Collections.emptyList();
    }

}
