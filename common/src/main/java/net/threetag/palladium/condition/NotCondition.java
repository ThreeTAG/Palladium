package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.ConditionArrayProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class NotCondition extends Condition {

    public final Condition[] conditions;

    public NotCondition(Condition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean active(DataContext context) {
        for (Condition condition : this.conditions) {
            if (condition.active(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.NOT.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Condition[]> CONDITIONS = new ConditionArrayProperty("conditions").configurable("Array of conditions that must be disabled");

        public Serializer() {
            this.withProperty(CONDITIONS, new Condition[0]);
        }

        @Override
        public Condition make(JsonObject json) {
            return new NotCondition(this.getProperty(json, CONDITIONS));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns true if all conditions are disabled.";
        }
    }
}
