package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class DayCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var level = context.get(DataContextType.LEVEL);
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
