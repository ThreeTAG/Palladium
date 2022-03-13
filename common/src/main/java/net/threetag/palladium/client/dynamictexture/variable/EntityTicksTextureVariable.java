package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class EntityTicksTextureVariable extends AbstractIntegerTextureVariable {

    public EntityTicksTextureVariable(List<Pair<Operation, Integer>> operations) {
        super(operations);
    }

    public EntityTicksTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public int getNumber(LivingEntity entity) {
        return entity.tickCount;
    }

}
