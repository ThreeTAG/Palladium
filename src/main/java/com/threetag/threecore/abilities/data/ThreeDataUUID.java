package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;

import java.util.UUID;

public class ThreeDataUUID extends ThreeData<UUID>
{

    public ThreeDataUUID(String key) {
        super(key);
    }

    @Override
    public UUID parseValue(JsonObject jsonObject, UUID defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return UUID.fromString(JsonUtils.getString(jsonObject, this.jsonKey));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, UUID value) {
        nbt.put(this.key, NBTUtil.writeUniqueId(value));
    }

    @Override
    public UUID readFromNBT(NBTTagCompound nbt, UUID defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return NBTUtil.readUniqueId(nbt.getCompound(this.key));
    }

    @Override
    public String getDisplay(UUID value) {
        return value.toString();
    }

    @Override
    public boolean displayAsString(UUID value) {
        return true;
    }

}
