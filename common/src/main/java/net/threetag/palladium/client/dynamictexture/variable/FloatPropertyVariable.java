package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public float getNumber(Entity entity) {
        AtomicReference<Float> result = new AtomicReference<>(0F);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);

            if (property instanceof FloatProperty floatProperty) {
                result.set(handler.get(floatProperty));
            }
        });

        return 0F;
    }
}
