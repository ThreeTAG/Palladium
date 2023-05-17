package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;

public class StringArrayProperty extends PalladiumProperty<String[]> {

    public StringArrayProperty(String key) {
        super(key);
    }

    @Override
    public String[] fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new String[]{jsonElement.getAsString()};
        } else {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            String[] strings = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                strings[i] = jsonArray.get(i).getAsString();
            }
            return strings;
        }
    }

    @Override
    public JsonElement toJSON(String[] value) {
        JsonArray jsonArray = new JsonArray();
        for (String s : value) {
            jsonArray.add(s);
        }
        return jsonArray;
    }

    @Override
    public String[] fromNBT(Tag tag, String[] defaultValue) {
        if (tag instanceof ListTag listTag) {
            String[] strings = new String[listTag.size()];
            for (int i = 0; i < listTag.size(); i++) {
                strings[i] = listTag.getString(i);
            }
            return strings;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(String[] value) {
        ListTag listTag = new ListTag();
        for (String s : value) {
            listTag.add(StringTag.valueOf(s));
        }
        return listTag;
    }

    @Override
    public String[] fromBuffer(FriendlyByteBuf buf) {
        String[] strings = new String[buf.readInt()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = buf.readUtf();
        }
        return strings;
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        String[] strings = (String[]) value;
        buf.writeInt(strings.length);
        for (String string : strings) {
            buf.writeUtf(string);
        }
    }

    @Override
    public String getString(String[] value) {
        return value == null ? null : Arrays.toString(value);
    }

    @Override
    public String getPropertyType() {
        return "string_array";
    }
}
