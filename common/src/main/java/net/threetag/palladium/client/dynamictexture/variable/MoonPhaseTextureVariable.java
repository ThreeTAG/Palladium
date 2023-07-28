package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;

public class MoonPhaseTextureVariable extends AbstractIntegerTextureVariable {

    public MoonPhaseTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public int getNumber(DataContext context) {
        var level = context.getLevel();
        return level != null ? level.getMoonPhase() : 0;
    }
}
