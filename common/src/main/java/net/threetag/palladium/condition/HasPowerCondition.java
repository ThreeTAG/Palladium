package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class HasPowerCondition extends Condition {

    public final ResourceLocation powerId;

    public HasPowerCondition(ResourceLocation powerId) {
        this.powerId = powerId;
    }

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);
        return handler != null && handler.getPowerHolders().containsKey(this.powerId);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HAS_POWER.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> POWER = new ResourceLocationProperty("power").configurable("ID of the power that is required");

        public Serializer() {
            this.withProperty(POWER, new ResourceLocation("example:power_id"));
        }

        @Override
        public Condition make(JsonObject json) {
            return new HasPowerCondition(this.getProperty(json, POWER));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a power with the given ID.";
        }
    }
}
