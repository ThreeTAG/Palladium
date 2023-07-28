package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.context.DataContext;

public class EntityHealthTextureVariable extends AbstractFloatTextureVariable{

    public EntityHealthTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public float getNumber(DataContext context) {
        return context.getEntity() instanceof LivingEntity livingEntity ? livingEntity.getHealth() : 0F;
    }
}
