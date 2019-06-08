package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

public class ThreeDataInteger extends ThreeData<Integer>
{

    public ThreeDataInteger(String key) {
        super(key);
    }

    @Override
    public Integer parseValue(JsonObject jsonObject, Integer defaultValue) {
        return JsonUtils.getInt(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, Integer value) {
        nbt.putInt(this.key, value);
    }

    @Override
    public Integer readFromNBT(NBTTagCompound nbt, Integer defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getInt(this.key);
    }
}
