package com.threetag.threecore.abilities.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

import java.awt.*;

public class ThreeDataColor extends ThreeData<Color>
{

    public ThreeDataColor(String key) {
        super(key);
    }

    @Override
    public Color parseValue(JsonObject jsonObject, Color defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        JsonArray array = JsonUtils.getJsonArray(jsonObject, this.jsonKey);
        return new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, Color value) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.putInt("Red", value.getRed());
        tag.putInt("Green", value.getGreen());
        tag.putInt("Blue", value.getBlue());
        nbt.put(this.key, tag);
    }

    @Override
    public Color readFromNBT(NBTTagCompound nbt, Color defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return new Color(nbt.getCompound(this.key).getInt("Red"), nbt.getCompound(this.key).getInt("Green"), nbt.getCompound(this.key).getInt("Blue"));
    }

    @Override
    public String getDisplay(Color value) {
        return "[" + value.getRed() + ", " + value.getGreen() + ", " + value.getBlue() + "]";
    }
}
