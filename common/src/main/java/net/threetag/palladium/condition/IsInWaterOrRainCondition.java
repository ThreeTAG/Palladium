package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class IsInWaterOrRainCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWaterOrRain();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_IN_WATER_OR_RAIN.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsInWaterOrRainCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water or rain.";
        }
    }
}
