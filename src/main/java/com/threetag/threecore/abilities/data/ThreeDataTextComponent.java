package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;

public class ThreeDataTextComponent extends ThreeData<ITextComponent>
{

    public ThreeDataTextComponent(String key) {
        super(key);
    }

    @Override
    public ITextComponent parseValue(JsonObject jsonObject, ITextComponent defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return ITextComponent.Serializer.fromJson(JsonUtils.getJsonObject(jsonObject, this.jsonKey).toString());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, ITextComponent value) {
        nbt.putString(this.key, ITextComponent.Serializer.toJson(value));
    }

    @Override
    public ITextComponent readFromNBT(NBTTagCompound nbt, ITextComponent defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ITextComponent.Serializer.fromJson(nbt.getString(this.key));
    }

    @Override
    public String getDisplay(ITextComponent value) {
        return ITextComponent.Serializer.toJson(value);
    }
}
