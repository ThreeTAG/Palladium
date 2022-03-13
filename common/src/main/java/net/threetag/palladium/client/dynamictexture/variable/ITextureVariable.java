package net.threetag.palladium.client.dynamictexture.variable;

import net.minecraft.world.entity.LivingEntity;

public interface ITextureVariable {

    Object get(LivingEntity entity);

    default String replace(String base, String key, LivingEntity entity) {
        return base.replaceAll("#" + key, get(entity).toString());
    }
}
