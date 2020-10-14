package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class DoubleThreeData extends ThreeData<Double> {

    public DoubleThreeData(String key) {
        super(key);
    }

    @Override
    public Double parseValue(JsonObject jsonObject, Double defaultValue) {
        return (double) JSONUtils.getFloat(jsonObject, this.jsonKey, defaultValue.floatValue());
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Double value) {
        nbt.putDouble(this.key, value);
    }

    @Override
    public Double readFromNBT(CompoundNBT nbt, Double defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getDouble(this.key);
    }

    @Override
    public JsonElement serializeJson(Double value) {
        return new JsonPrimitive(value);
    }
}
