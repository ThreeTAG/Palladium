package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

public class AbilityDataString extends AbilityData<String> {

    public AbilityDataString(String key) {
        super(key);
    }

    @Override
    public String parseValue(JsonObject jsonObject, String defaultValue) {
        return JsonUtils.getString(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String value) {
        nbt.putString(this.key, value);
    }

    @Override
    public String readFromNBT(NBTTagCompound nbt, String defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getString(this.key);
    }

    @Override
    public boolean displayAsString(String value) {
        return true;
    }
}
