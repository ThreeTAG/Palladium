package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.*;
import org.jetbrains.annotations.Nullable;

public class FloatPropertyCondition extends Condition {

    private final String propertyKey;
    private final float min, max;

    public FloatPropertyCondition(String propertyKey, float min, float max) {
        this.propertyKey = propertyKey;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        var handler = EntityPropertyHandler.getHandler(entity);
        PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);

        if (property instanceof FloatProperty floatProperty) {
            float value = handler.get(floatProperty);
            return value >= this.min && value <= this.max;
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.FLOAT_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the float property in the entity");
        public static final PalladiumProperty<Float> MIN = new FloatProperty("min").configurable("Minimum required amount of the property value");
        public static final PalladiumProperty<Float> MAX = new FloatProperty("max").configurable("Maximum required amount of the property value");

        public Serializer() {
            this.withProperty(PROPERTY, "value");
            this.withProperty(MIN, 0F);
            this.withProperty(MAX, 0F);
        }

        @Override
        public Condition make(JsonObject json) {
            return new FloatPropertyCondition(
                    this.getProperty(json, PROPERTY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a float property with a value between the given minimum and maximum";
        }
    }
}
