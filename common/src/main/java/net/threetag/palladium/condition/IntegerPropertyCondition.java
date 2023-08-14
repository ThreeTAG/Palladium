package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.concurrent.atomic.AtomicBoolean;

public class IntegerPropertyCondition extends Condition {

    private final String propertyKey;
    private final int min, max;

    public IntegerPropertyCondition(String propertyKey, int min, int max) {
        this.propertyKey = propertyKey;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        AtomicBoolean result = new AtomicBoolean(false);

        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property instanceof IntegerProperty integerProperty) {
                int value = handler.get(integerProperty);
                result.set(value >= this.min && value <= this.max);
            }
        });

        return result.get();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.INTEGER_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the integer property in the entity");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the property value");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the property value");

        public Serializer() {
            this.withProperty(PROPERTY, "value");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new IntegerPropertyCondition(
                    this.getProperty(json, PROPERTY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a certain amount of a certain integer property.";
        }
    }
}
