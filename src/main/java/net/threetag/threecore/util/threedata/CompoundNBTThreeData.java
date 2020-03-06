package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.threetag.threecore.util.TCJsonUtil;

public class CompoundNBTThreeData extends ThreeData<CompoundNBT> {

    public CompoundNBTThreeData(String key) {
        super(key);
    }

    @Override
    public CompoundNBT parseValue(JsonObject jsonObject, CompoundNBT defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        try {
            return JsonToNBT.getTagFromJson(JSONUtils.getJsonObject(jsonObject, this.jsonKey).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, CompoundNBT value) {
        nbt.put(this.key, value);
    }

    @Override
    public CompoundNBT readFromNBT(CompoundNBT nbt, CompoundNBT defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return nbt.getCompound(this.key);
    }

    @Override
    public JsonElement serializeJson(CompoundNBT value) {
        return TCJsonUtil.nbtToJson(value);
    }
}
