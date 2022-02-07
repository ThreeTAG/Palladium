package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;

public class BooleanProperty extends PalladiumProperty<Boolean> {

    public BooleanProperty(String key) {
        super(key);
    }

    @Override
    public Boolean fromJSON(JsonElement jsonElement) {
        return jsonElement.getAsBoolean();
    }

    @Override
    public JsonElement toJSON(Boolean value) {
        return new JsonPrimitive(value);
    }

    @Override
    public Boolean fromNBT(Tag tag, Boolean defaultValue) {
        if (tag instanceof ByteTag byteTag) {
            return byteTag.getAsByte() != 0;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Boolean value) {
        return ByteTag.valueOf(value);
    }
}
