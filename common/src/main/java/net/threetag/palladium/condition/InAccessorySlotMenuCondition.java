package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.AccessorySlotProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class InAccessorySlotMenuCondition extends Condition {

    public static AccessorySlot CURRENT_SLOT = null;
    private final AccessorySlot slot;

    public InAccessorySlotMenuCondition(AccessorySlot slot) {
        this.slot = slot;
    }

    @Override
    public boolean active(DataContext context) {
        return CURRENT_SLOT != null && CURRENT_SLOT == this.slot;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IN_ACCESSORY_SLOT_MENU.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<AccessorySlot> SLOT = new AccessorySlotProperty("accessory_slot").configurable("The acessory slot that must be open in the accessory menu.");

        public Serializer() {
            this.withProperty(SLOT, AccessorySlot.CHEST);
        }

        @Override
        public Condition make(JsonObject json) {
            return new InAccessorySlotMenuCondition(this.getProperty(json, SLOT));
        }

        @Override
        public String getDocumentationDescription() {
            return "Let's you check if the accessory menu is currently opened and the specified slot is selected. Only available for client-side conditions.";
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.ASSETS;
        }
    }
}
