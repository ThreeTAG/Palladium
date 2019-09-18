package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JSONUtils;

import java.util.UUID;

public class UUIDThreeData extends ThreeData<UUID>
{

    public UUIDThreeData(String key) {
        super(key);
    }

    @Override
    public UUID parseValue(JsonObject jsonObject, UUID defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return UUID.fromString(JSONUtils.getString(jsonObject, this.jsonKey));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, UUID value) {
        nbt.put(this.key, NBTUtil.writeUniqueId(value));
    }

    @Override
    public UUID readFromNBT(CompoundNBT nbt, UUID defaultValue) {
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
