package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.StringComparator;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringComparatorProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.concurrent.atomic.AtomicBoolean;

public class StringPropertyCondition extends Condition {

    private final String propertyKey;
    private final StringComparator comparator;
    private final String compareTo;

    public StringPropertyCondition(String propertyKey, StringComparator comparator, String compareTo) {
        this.propertyKey = propertyKey;
        this.comparator = comparator;
        this.compareTo = compareTo;
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
            if (property instanceof StringProperty stringProperty) {
                result.set(this.comparator.compare(handler.get(stringProperty), this.compareTo));
            }
        });

        return result.get();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.STRING_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the float property in the entity");
        public static final PalladiumProperty<StringComparator> COMPARATOR = new StringComparatorProperty("comparator").configurable("The comparator type being used on the string property");
        public static final PalladiumProperty<String> COMPARE_TO = new StringProperty("compare_to").configurable("The string the property is compared to");

        public Serializer() {
            this.withProperty(PROPERTY, "value");
            this.withProperty(COMPARATOR, StringComparator.EQUALS);
            this.withProperty(COMPARE_TO, "");
        }

        @Override
        public Condition make(JsonObject json) {
            return new StringPropertyCondition(
                    this.getProperty(json, PROPERTY),
                    this.getProperty(json, COMPARATOR),
                    this.getProperty(json, COMPARE_TO));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a string property that matches the given comparison.";
        }
    }
}
