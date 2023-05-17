package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class FloatProperty extends PalladiumProperty<Float> {

    public FloatProperty(String key) {
        super(key);
    }

    @Override
    public Float fromJSON(JsonElement jsonElement) {
        return jsonElement.getAsFloat();
    }

    @Override
    public JsonElement toJSON(Float value) {
        return new JsonPrimitive(value);
    }

    @Override
    public Float fromNBT(Tag tag, Float defaultValue) {
        if (tag instanceof FloatTag floatTag) {
            return floatTag.getAsFloat();
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Float value) {
        return FloatTag.valueOf(value);
    }

    @Override
    public Float fromBuffer(FriendlyByteBuf buf) {
        return buf.readFloat();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeFloat((Float) value);
    }

    @Override
    public String getPropertyType() {
        return "float";
    }
}
