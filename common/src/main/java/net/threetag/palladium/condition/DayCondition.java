package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;

public class DayCondition extends Condition {

    @Override
    public boolean active(ConditionContext context) {
        var level = context.get(ConditionContextType.LEVEL);
        return level != null && level.isDay();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.DAY.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new DayCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if it's currently daytime";
        }
    }
}
