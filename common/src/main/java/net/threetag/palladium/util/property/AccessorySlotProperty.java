package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.util.json.GsonUtil;

public class AccessorySlotProperty extends PalladiumProperty<AccessorySlot> {

    public AccessorySlotProperty(String key) {
        super(key);
    }

    @Override
    public AccessorySlot fromJSON(JsonElement jsonElement) {
        var slot = AccessorySlot.getSlotByName(GsonUtil.convertToResourceLocation(jsonElement, this.getKey()));

        if (slot == null) {
            throw new JsonParseException("Unknown accessory slot '" + jsonElement.getAsString() + "'");
        }

        return slot;
    }

    @Override
    public JsonElement toJSON(AccessorySlot value) {
        return new JsonPrimitive(value.getName().toString());
    }

    @Override
    public AccessorySlot fromNBT(Tag tag, AccessorySlot defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return AccessorySlot.getSlotByName(new ResourceLocation(stringTag.getAsString()));
        }

        return defaultValue;
    }

    @Override
    public Tag toNBT(AccessorySlot value) {
        return StringTag.valueOf(value.getName().toString());
    }

    @Override
    public AccessorySlot fromBuffer(FriendlyByteBuf buf) {
        return AccessorySlot.getSlotByName(buf.readResourceLocation());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeResourceLocation(((AccessorySlot) value).getName());
    }

    @Override
    public String getString(AccessorySlot value) {
        return value != null ? value.getName().toString() : null;
    }

    @Override
    public String getPropertyType() {
        return "accessory_slot";
    }
}
