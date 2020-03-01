package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class StringThreeData extends ThreeData<String> {

    public StringThreeData(String key) {
        super(key);
    }

    @Override
    public String parseValue(JsonObject jsonObject, String defaultValue) {
        return JSONUtils.getString(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, String value) {
        nbt.putString(this.key, value);
    }

    @Override
    public String readFromNBT(CompoundNBT nbt, String defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getString(this.key);
    }

    @Override
    public JsonElement serializeJson(String value) {
        return new JsonPrimitive(value);
    }
}
