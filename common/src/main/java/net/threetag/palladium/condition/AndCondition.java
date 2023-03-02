package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.ConditionArrayProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class AndCondition extends Condition {

    public final Condition[] conditions;

    public AndCondition(Condition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        for (Condition condition : this.conditions) {
            if (!condition.active(entity, entry, power, holder)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.AND.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Condition[]> CONDITIONS = new ConditionArrayProperty("conditions").configurable("Array of conditions, all of which must be active");

        public Serializer() {
            this.withProperty(CONDITIONS, new Condition[0]);
        }

        @Override
        public Condition make(JsonObject json) {
            return new AndCondition(this.getProperty(json, CONDITIONS));
        }

        @Override
        public String getDocumentationDescription() {
            return "A condition that is active if all of the conditions in the array are active";
        }
    }
}
