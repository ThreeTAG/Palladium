package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

public class ThreeDataBoolean extends ThreeData<Boolean>
{

    public ThreeDataBoolean(String key) {
        super(key);
    }

    @Override
    public Boolean parseValue(JsonObject jsonObject, Boolean defaultValue) {
        return JsonUtils.getBoolean(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, Boolean value) {
        nbt.putBoolean(this.key, value);
    }

    @Override
    public Boolean readFromNBT(NBTTagCompound nbt, Boolean defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getBoolean(this.key);
    }

}
