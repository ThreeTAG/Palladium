package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.util.property.ConditionArrayProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class OrCondition extends Condition {

    public final Condition[] conditions;

    public OrCondition(Condition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean active(ConditionContext context) {
        for (Condition condition : this.conditions) {
            if (condition.active(context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.OR.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Condition[]> CONDITIONS = new ConditionArrayProperty("conditions").configurable("Array of conditions, at least one of which must be active");

        public Serializer() {
            this.withProperty(CONDITIONS, new Condition[0]);
        }

        @Override
        public Condition make(JsonObject json) {
            return new OrCondition(this.getProperty(json, CONDITIONS));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns true if at least one of the conditions is active.";
        }
    }
}
