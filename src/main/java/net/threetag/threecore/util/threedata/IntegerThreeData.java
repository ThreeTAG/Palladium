package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class IntegerThreeData extends ThreeData<Integer>
{

    public IntegerThreeData(String key) {
        super(key);
    }

    @Override
    public Integer parseValue(JsonObject jsonObject, Integer defaultValue) {
        return JSONUtils.getInt(jsonObject, this.jsonKey, defaultValue);
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Integer value) {
        nbt.putInt(this.key, value);
    }

    @Override
    public Integer readFromNBT(CompoundNBT nbt, Integer defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getInt(this.key);
    }
}
