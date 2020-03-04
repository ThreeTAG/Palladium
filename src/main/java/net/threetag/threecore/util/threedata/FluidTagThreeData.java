package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class FluidTagThreeData extends ThreeData<Tag<Fluid>> {

    public FluidTagThreeData(String key) {
        super(key);
    }

    @Override
    public Tag<Fluid> parseValue(JsonObject jsonObject, Tag<Fluid> defaultValue) {
        return new FluidTags.Wrapper(new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey, defaultValue.getId().toString())));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Tag<Fluid> value) {
        nbt.putString(this.key, value.getId().toString());
    }

    @Override
    public Tag<Fluid> readFromNBT(CompoundNBT nbt, Tag<Fluid> defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return new FluidTags.Wrapper(new ResourceLocation(nbt.getString(this.key)));
    }

    @Override
    public JsonElement serializeJson(Tag<Fluid> value) {
        return new JsonPrimitive(value.getId().toString());
    }
}
