package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;

public class CrouchingTextureVariable extends AbstractBooleanTextureVariable {

    public CrouchingTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public boolean getBoolean(Entity entity) {
        return entity.isCrouching();
    }

}
