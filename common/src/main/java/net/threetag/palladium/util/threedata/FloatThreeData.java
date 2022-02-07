package net.threetag.palladium.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;

public class FloatThreeData extends ThreeData<Float> {

    public FloatThreeData(String key) {
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
}
