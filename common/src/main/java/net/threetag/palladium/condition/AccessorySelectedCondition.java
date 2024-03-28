package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.Collection;
import java.util.Optional;

public class AccessorySelectedCondition extends Condition {
    private final String accessorySlot, accessory;

    public AccessorySelectedCondition(String accessorySlot, String accessory) {
        this.accessorySlot = accessorySlot;
        this.accessory = accessory;
    }

    @Override
    public boolean active(DataContext context) {
        Optional<AccessoryPlayerData> dataOptional = Accessory.getPlayerData(context.getPlayer());
        if (dataOptional.isEmpty()) return false;
        AccessoryPlayerData data = dataOptional.get();
        Collection<Accessory> accessories = data.accessories.get(AccessorySlot.getSlotByName(ResourceLocation.of(accessorySlot, ':')));
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
        public static final PalladiumProperty<String> ACCESSORY_SLOT = new StringProperty("accessory_slot").configurable("The ID of the accessory slot to read from"), ACCESSORY = new StringProperty("accessory").configurable("The accessory which needs to be selected in order for the condition to return true (include the namespace!)");

        public Serializer() {
            this.withProperty(ACCESSORY_SLOT, "palladium:head");
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
