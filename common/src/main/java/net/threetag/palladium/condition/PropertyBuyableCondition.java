package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class PropertyBuyableCondition extends BuyableCondition {

    private final String propertyKey;
    private final int amount;
    private final IIcon icon;
    private final Component description;

    public PropertyBuyableCondition(String propertyKey, int amount, IIcon icon, Component description) {
        this.propertyKey = propertyKey;
        this.amount = amount;
        this.icon = icon;
        this.description = description;
    }

    @Override
    public AbilityConfiguration.UnlockData createData() {
        return new AbilityConfiguration.UnlockData(this.icon, this.amount, this.description);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean isAvailable(LivingEntity entity) {
        AtomicBoolean result = new AtomicBoolean(false);

        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property instanceof IntegerProperty integerProperty) {
                int value = handler.get(integerProperty);
                result.set(value >= this.amount);
            }
        });

        return result.get();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        AtomicBoolean result = new AtomicBoolean(false);

        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property instanceof IntegerProperty integerProperty) {
                int value = handler.get(integerProperty);
                if (value >= this.amount) {
                    handler.set(integerProperty, (int) (value - this.amount));
                    result.set(true);
                }
            }
        });

        return result.get();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.PROPERTY_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the (integer) palladium property");
        public static final PalladiumProperty<Integer> SCORE = new IntegerProperty("score").configurable("Required value of the property");
        public static final PalladiumProperty<IIcon> ICON = new IconProperty("icon").configurable("Icon that will be displayed during buying");
        public static final PalladiumProperty<Component> DESCRIPTION = new ComponentProperty("description").configurable("Name of the property that will be displayed during buying");

        public Serializer() {
            this.withProperty(PROPERTY, "property_name");
            this.withProperty(SCORE, 3);
            this.withProperty(ICON, new ItemIcon(Items.COMMAND_BLOCK));
            this.withProperty(DESCRIPTION, Component.literal("Property"));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public Condition make(JsonObject json) {
            return new PropertyBuyableCondition(getProperty(json, PROPERTY), getProperty(json, SCORE), getProperty(json, ICON), getProperty(json, DESCRIPTION));
        }

        @Override
        public String getDocumentationDescription() {
            return "A buyable condition that requires a certain value in an integer palladium property.";
        }
    }
}
