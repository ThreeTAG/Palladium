package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;

public class MoonPhaseTextureVariable extends AbstractIntegerTextureVariable{

    public MoonPhaseTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public int getNumber(Entity entity) {
        return entity.level.getMoonPhase();
    }
}
