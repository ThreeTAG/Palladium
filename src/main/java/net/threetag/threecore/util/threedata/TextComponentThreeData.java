package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;

public class TextComponentThreeData extends ThreeData<ITextComponent> {

    public TextComponentThreeData(String key) {
        super(key);
    }

    @Override
    public ITextComponent parseValue(JsonObject jsonObject, ITextComponent defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return ITextComponent.Serializer.func_240643_a_(JSONUtils.getJsonObject(jsonObject, this.jsonKey).toString());
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, ITextComponent value) {
        nbt.putString(this.key, ITextComponent.Serializer.toJson(value));
    }

    @Override
    public ITextComponent readFromNBT(CompoundNBT nbt, ITextComponent defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ITextComponent.Serializer.func_240643_a_(nbt.getString(this.key));
    }

    @Override
    public JsonElement serializeJson(ITextComponent value) {
        return ITextComponent.Serializer.toJsonTree(value);
    }
}
