package com.threetag.threecore.abilities.condition;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface IAbilityConditionSerializer<T extends AbilityCondition> {

    T read(JsonObject json);

    T read(NBTTagCompound nbt);

    NBTTagCompound serialize(T condition);

    default NBTTagCompound serializeExt(AbilityCondition condition) {
        NBTTagCompound nbt = serialize((T) condition);
        nbt.putString("Type", getId().toString());
        return nbt;
    }

    ResourceLocation getId();

}
