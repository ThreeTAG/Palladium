package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;

public class TrueCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        return true;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.TRUE.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new TrueCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "It's just true. That's it.";
        }
    }

}
