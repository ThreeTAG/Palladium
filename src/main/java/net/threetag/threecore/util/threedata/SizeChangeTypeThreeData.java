package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.sizechanging.SizeChangeType;

import java.util.Objects;

public class SizeChangeTypeThreeData extends ThreeData<SizeChangeType> {

    public SizeChangeTypeThreeData(String key) {
        super(key);
    }

    @Override
    public SizeChangeType parseValue(JsonObject jsonObject, SizeChangeType defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        SizeChangeType sizeChangeType = SizeChangeType.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey)));
        if (sizeChangeType == null)
            throw new JsonSyntaxException("Size change type " + JSONUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
        return sizeChangeType;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, SizeChangeType value) {
        nbt.putString(this.key, Objects.requireNonNull(value.getRegistryName()).toString());
    }

    @Override
    public SizeChangeType readFromNBT(CompoundNBT nbt, SizeChangeType defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        SizeChangeType type = SizeChangeType.REGISTRY.getValue(new ResourceLocation(nbt.getString(this.key)));
        return type != null ? type : defaultValue;
    }

    @Override
    public JsonElement serializeJson(SizeChangeType value) {
        return new JsonPrimitive(SizeChangeType.REGISTRY.getKey(value).toString());
    }
}
