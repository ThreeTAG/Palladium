package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

public class IsHoveringOrLevitatingCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        if (entity instanceof PalladiumPlayerExtension extension) {
            float flight = extension.palladium$getFlightHandler().getFlightAnimation(1F);
            return extension.palladium$getFlightHandler().getHoveringAnimation(1F) > 0F || (flight > 0F && flight <= 1F);
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_HOVERING_OR_LEVITATING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsHoveringOrLevitatingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is hovering mid-air or levitating.";
        }
    }
}
