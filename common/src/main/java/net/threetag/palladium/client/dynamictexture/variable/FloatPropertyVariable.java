package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

public class FloatPropertyVariable extends AbstractFloatTextureVariable {

    private final String propertyKey;

    public FloatPropertyVariable(String propertyKey, List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
        this.propertyKey = propertyKey;
    }

    public FloatPropertyVariable(JsonObject json) {
        super(json);
        this.propertyKey = GsonHelper.getAsString(json, "property");
    }

    @Override
    public float getNumber(LivingEntity entity) {
        var handler = EntityPropertyHandler.getHandler(entity);
        PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);

        if (property instanceof FloatProperty floatProperty) {
            return handler.get(floatProperty);
        }

        return 0F;
    }
}
