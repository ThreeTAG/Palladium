package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;

public class FloatArrayProperty extends PalladiumProperty<Float[]> {

    public FloatArrayProperty(String key) {
        super(key);
    }

    @Override
    public Float[] fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new Float[]{jsonElement.getAsFloat()};
        } else {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Float[] floats = new Float[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                floats[i] = jsonArray.get(i).getAsFloat();
            }
            return floats;
        }
    }

    @Override
    public JsonElement toJSON(Float[] value) {
        JsonArray jsonArray = new JsonArray();
        for (Float f : value) {
            jsonArray.add(f);
        }
        return jsonArray;
    }

    @Override
    public Float[] fromNBT(Tag tag, Float[] defaultValue) {
        if (tag instanceof ListTag listTag) {
            Float[] floats = new Float[listTag.size()];
            for (int i = 0; i < listTag.size(); i++) {
                floats[i] = listTag.getFloat(i);
            }
            return floats;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Float[] value) {
        ListTag listTag = new ListTag();
        for (Float f : value) {
            listTag.add(FloatTag.valueOf(f));
        }
        return listTag;
    }

    @Override
    public Float[] fromBuffer(FriendlyByteBuf buf) {
        Float[] floats = new Float[buf.readInt()];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = buf.readFloat();
        }
        return floats;
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        Float[] floats = (Float[]) value;
        buf.writeInt(floats.length);
        for (Float f : floats) {
            buf.writeFloat(f);
        }
    }

    @Override
    public String getString(Float[] value) {
        return value == null ? null : Arrays.toString(value);
    }

    @Override
    public String getPropertyType() {
        return "float_array";
    }
}
