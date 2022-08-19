package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;

public class EntityHealthTextureVariable extends AbstractFloatTextureVariable{

    public EntityHealthTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public float getNumber(LivingEntity entity) {
        return entity.getHealth();
    }
}
