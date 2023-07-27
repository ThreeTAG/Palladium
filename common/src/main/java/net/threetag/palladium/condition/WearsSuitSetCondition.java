package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.SuitSetPropertyPalladium;

public class WearsSuitSetCondition extends Condition {

    public final SuitSet suitSet;

    public WearsSuitSetCondition(SuitSet suitSet) {
        this.suitSet = suitSet;
    }

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return this.suitSet != null && this.suitSet.isWearing(entity);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.WEARS_SUIT_SET.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<SuitSet> SUIT_SET = new SuitSetPropertyPalladium("suit_set").configurable("ID of the suit set that must be worn");

        public Serializer() {
            this.withProperty(SUIT_SET, null);
        }

        @Override
        public Condition make(JsonObject json) {
            return new WearsSuitSetCondition(this.getProperty(json, SUIT_SET));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is wearing a specific suit set.";
        }
    }
}
