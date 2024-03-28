package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.AccessorySlotProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.Collection;
import java.util.Optional;

public class AccessorySelectedCondition extends Condition {
    private final AccessorySlot accessorySlot;
    private final String accessory;

    public AccessorySelectedCondition(AccessorySlot accessorySlot, String accessory) {
        this.accessorySlot = accessorySlot;
        this.accessory = accessory;
    }

    @Override
    public boolean active(DataContext context) {
        Optional<AccessoryPlayerData> dataOptional = Accessory.getPlayerData(context.getPlayer());
        if (dataOptional.isEmpty()) return false;
        AccessoryPlayerData data = dataOptional.get();
        Collection<Accessory> accessories = data.accessories.get(accessorySlot);
        if (accessories == null || accessories.isEmpty()) return accessory.equals("");
        for (Accessory accessory : accessories) {
            if (accessory.toString().equals(this.accessory)) return true;
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ACCESSORY_SELECTED.get();
    }

    public static class Serializer extends ConditionSerializer {
        public static final PalladiumProperty<AccessorySlot> ACCESSORY_SLOT = new AccessorySlotProperty("accessory_slot").configurable("The ID of the accessory slot to read from");
        public static final PalladiumProperty<String> ACCESSORY = new StringProperty("accessory").configurable("The accessory which needs to be selected in order for the condition to return true (include the namespace!)");

        public Serializer() {
            this.withProperty(ACCESSORY_SLOT, AccessorySlot.HEAD);
            this.withProperty(ACCESSORY, "palladium:sea_pickle_hat");
        }

        @Override
        public Condition make(JsonObject json) {
            return new AccessorySelectedCondition(getProperty(json, ACCESSORY_SLOT), getProperty(json, ACCESSORY));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a specified accessory selected from a specific accessory slot. An empty string can be used to check for no accessory.";
        }
    }
}
