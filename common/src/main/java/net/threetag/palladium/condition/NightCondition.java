package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class NightCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var level = context.getLevel();
        return level != null && level.isNight();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.NIGHT.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new NightCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if it's currently nighttime";
        }
    }
}
