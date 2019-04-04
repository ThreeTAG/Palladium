package com.threetag.threecore.util.render;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface IIconSerializer<T extends IIcon> {

    T read(JsonObject json);

    T read(NBTTagCompound nbt);

    NBTTagCompound serialize(T icon);

    default NBTTagCompound serializeExt(IIcon icon) {
        NBTTagCompound nbt = serialize((T) icon);
        nbt.putString("Type", getId().toString());
        return nbt;
    }

    ResourceLocation getId();

}
