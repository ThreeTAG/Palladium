package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

import java.util.List;

public class EntityTicksTextureVariable extends AbstractIntegerTextureVariable {

    public EntityTicksTextureVariable(List<Pair<Operation, Integer>> operations) {
        super(operations);
    }

    public EntityTicksTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public int getNumber(IModelLayerContext context) {
        return context.getAsEntity().ticksExisted;
    }

}
