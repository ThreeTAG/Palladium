package net.threetag.threecore.util.threedata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

import java.awt.*;

public class ColorThreeData extends ThreeData<Color>
{

    public ColorThreeData(String key) {
        super(key);
    }

    @Override
    public Color parseValue(JsonObject jsonObject, Color defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        JsonArray array = JSONUtils.getJsonArray(jsonObject, this.jsonKey);
        return new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Color value) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("Red", value.getRed());
        tag.putInt("Green", value.getGreen());
        tag.putInt("Blue", value.getBlue());
        nbt.put(this.key, tag);
    }

    @Override
    public Color readFromNBT(CompoundNBT nbt, Color defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return new Color(nbt.getCompound(this.key).getInt("Red"), nbt.getCompound(this.key).getInt("Green"), nbt.getCompound(this.key).getInt("Blue"));
    }

    @Override
    public String getDisplay(Color value) {
        return "[" + value.getRed() + ", " + value.getGreen() + ", " + value.getBlue() + "]";
    }
}
