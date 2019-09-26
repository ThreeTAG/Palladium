package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationThreeData extends ThreeData<ResourceLocation> {

    public ResourceLocationThreeData(String key) {
        super(key);
    }

    @Override
    public ResourceLocation parseValue(JsonObject jsonObject, ResourceLocation defaultValue) {
        return new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey, defaultValue.toString()));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, ResourceLocation value) {
        nbt.putString(this.key, value.toString());
    }

    @Override
    public ResourceLocation readFromNBT(CompoundNBT nbt, ResourceLocation defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return new ResourceLocation(nbt.getString(this.key));
    }

    @Override
    public boolean displayAsString(ResourceLocation value) {
        return true;
    }
}
