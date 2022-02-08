package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class StringProperty extends PalladiumProperty<String> {

    public StringProperty(String key) {
        super(key);
    }

    @Override
    public String fromJSON(JsonElement jsonElement) {
        return jsonElement.getAsString();
    }

    @Override
    public JsonElement toJSON(String value) {
        return new JsonPrimitive(value);
    }

    @Override
    public String fromNBT(Tag tag, String defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return stringTag.getAsString();
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(String value) {
        return StringTag.valueOf(value);
    }

    @Override
    public String fromBuffer(FriendlyByteBuf buf) {
        return buf.readUtf();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeUtf((String) value);
    }
}
