package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public class EntityTicksTextureVariable extends AbstractIntegerTextureVariable {

    public EntityTicksTextureVariable(List<Pair<Operation, Integer>> operations) {
        super(operations);
    }

    public EntityTicksTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public int getNumber(DataContext context) {
        var entity = context.getEntity();
        return entity != null ? entity.tickCount : 0;
    }

}
