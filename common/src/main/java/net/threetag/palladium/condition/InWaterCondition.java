package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class InWaterCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWater();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IN_WATER.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new InWaterCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water.";
        }
    }
}
