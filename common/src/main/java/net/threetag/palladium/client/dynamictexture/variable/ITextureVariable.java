package net.threetag.palladium.client.dynamictexture.variable;

import net.minecraft.world.entity.Entity;

public interface ITextureVariable {

    Object get(Entity entity);

    default String replace(String base, String key, Entity entity) {
        return base.replaceAll("#" + key, get(entity).toString());
    }
}
