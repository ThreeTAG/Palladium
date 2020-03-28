package net.threetag.threecore.util.threedata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.Constants;

public class StringArrayThreeData extends ThreeData<String[]> {

    public StringArrayThreeData(String key) {
        super(key);
    }

    @Override
    public String[] parseValue(JsonObject jsonObject, String[] defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;

        JsonElement element = jsonObject.get(this.jsonKey);

        if (element.isJsonPrimitive()) {
            return new String[]{element.getAsString()};
        } else {
            JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, this.jsonKey);
            String[] array = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                array[i] = jsonArray.get(i).getAsString();
            }

            return array;
        }
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, String[] value) {
        ListNBT list = new ListNBT();
        for (String s : value) {
            list.add(StringNBT.valueOf(s));
        }
        nbt.put(this.key, list);
    }

    @Override
    public String[] readFromNBT(CompoundNBT nbt, String[] defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        ListNBT list = nbt.getList(this.key, Constants.NBT.TAG_STRING);
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getString(i);
        }
        return array;
    }

    @Override
    public JsonElement serializeJson(String[] value) {
        if (value.length == 1) {
            return new JsonPrimitive(value[0]);
        } else {
            JsonArray jsonArray = new JsonArray();
            for (String s : value) {
                jsonArray.add(s);
            }
            return jsonArray;
        }

    }
}
