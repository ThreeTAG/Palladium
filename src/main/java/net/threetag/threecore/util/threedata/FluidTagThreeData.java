package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;

public class FluidTagThreeData extends ThreeData<ITag.INamedTag<Fluid>> {

    public FluidTagThreeData(String key) {
        super(key);
    }

    @Override
    public ITag.INamedTag<Fluid> parseValue(JsonObject jsonObject, ITag.INamedTag<Fluid> defaultValue) {
        return FluidTags.makeWrapperTag(JSONUtils.getString(jsonObject, this.jsonKey, defaultValue.func_230234_a_().toString()));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, ITag.INamedTag<Fluid> value) {
        nbt.putString(this.key, value.func_230234_a_().toString());
    }

    @Override
    public ITag.INamedTag<Fluid> readFromNBT(CompoundNBT nbt, ITag.INamedTag<Fluid> defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return FluidTags.makeWrapperTag(nbt.getString(this.key));
    }

    @Override
    public JsonElement serializeJson(ITag.INamedTag<Fluid> value) {
        return new JsonPrimitive(value.func_230234_a_().toString());
    }
}
