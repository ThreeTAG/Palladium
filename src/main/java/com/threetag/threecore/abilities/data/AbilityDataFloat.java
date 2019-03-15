package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

public class AbilityDataFloat extends AbilityData<Float> {

    public AbilityDataFloat(String key) {
        super(key);
    }

    @Override
    public Float parseValue(JsonObject jsonObject, Float defaultValue) {
        return JsonUtils.getFloat(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, Float value) {
        nbt.putFloat(this.key, value);
    }

    @Override
    public Float readFromNBT(NBTTagCompound nbt, Float defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getFloat(this.key);
    }
}
