package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;

public class FalseCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.FALSE.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new FalseCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "It's just false. That's it.";
        }
    }

}
