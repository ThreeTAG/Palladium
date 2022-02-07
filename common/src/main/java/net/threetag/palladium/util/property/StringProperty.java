package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

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
}
