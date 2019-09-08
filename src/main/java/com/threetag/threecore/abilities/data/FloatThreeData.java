package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class FloatThreeData extends ThreeData<Float>
{

    public FloatThreeData(String key) {
        super(key);
    }

    @Override
    public Float parseValue(JsonObject jsonObject, Float defaultValue) {
        return JSONUtils.getFloat(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Float value) {
        nbt.putFloat(this.key, value);
    }

    @Override
    public Float readFromNBT(CompoundNBT nbt, Float defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getFloat(this.key);
    }
}
