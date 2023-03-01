package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityHealthTextureVariable extends AbstractFloatTextureVariable{

    public EntityHealthTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public float getNumber(Entity entity) {
        return entity instanceof LivingEntity livingEntity ? livingEntity.getHealth() : 0F;
    }
}
