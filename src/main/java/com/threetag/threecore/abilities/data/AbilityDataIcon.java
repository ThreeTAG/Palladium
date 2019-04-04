package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import com.threetag.threecore.util.render.IIcon;
import com.threetag.threecore.util.render.IconSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

public class AbilityDataIcon extends AbilityData<IIcon> {

    public AbilityDataIcon(String key) {
        super(key);
    }

    @Override
    public IIcon parseValue(JsonObject jsonObject, IIcon defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return IconSerializer.deserialize(jsonObject);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, IIcon value) {
        nbt.put(this.key, value.getSerializer().serializeExt(value));
    }

    @Override
    public IIcon readFromNBT(NBTTagCompound nbt, IIcon defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        IIcon icon = IconSerializer.deserialize(nbt);
        return icon == null ? defaultValue : icon;
    }

    @Override
    public String getDisplay(IIcon value) {
        // TODO Icon serialized
        return super.getDisplay(value);
    }

}
