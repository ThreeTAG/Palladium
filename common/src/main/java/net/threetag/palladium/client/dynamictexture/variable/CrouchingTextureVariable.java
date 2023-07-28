package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;

public class CrouchingTextureVariable extends AbstractBooleanTextureVariable {

    public CrouchingTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        var entity = context.getEntity();
        return entity != null && entity.isCrouching();
    }

}
