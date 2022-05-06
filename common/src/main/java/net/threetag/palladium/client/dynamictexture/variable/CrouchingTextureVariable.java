package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;

public class CrouchingTextureVariable extends AbstractBooleanTextureVariable {

    public CrouchingTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public boolean getBoolean(LivingEntity entity) {
        return entity.isCrouching();
    }

}
