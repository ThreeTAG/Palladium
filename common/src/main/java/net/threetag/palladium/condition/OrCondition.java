package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.ConditionArrayProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class OrCondition extends Condition {

    public final Condition[] conditions;

    public OrCondition(Condition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        for (Condition condition : this.conditions) {
            if (condition.active(entity, entry, power, holder)) {
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

        public static final PalladiumProperty<Condition[]> CONDITIONS = new ConditionArrayProperty("conditions").configurable("Array of conditions that must be disabled");

        public Serializer() {
            this.withProperty(CONDITIONS, new Condition[0]);
        }

        @Override
        public Condition make(JsonObject json) {
            return new OrCondition(this.getProperty(json, CONDITIONS));
        }
    }

}
