package com.threetag.threecore.abilities.condition;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class AbilityConditionSerializer {

    private static Map<ResourceLocation, IAbilityConditionSerializer> REGISTRY = Maps.newHashMap();

    static {
        register(AbilityConditionKarma.Serializer.INSTANCE);
    }

    public static <S extends IAbilityConditionSerializer<T>, T extends AbilityCondition> S register(S serializer) {
        if (REGISTRY.containsKey(serializer.getId())) {
            throw new IllegalArgumentException("Duplicate ability condition serializer " + serializer.getId());
        } else {
            REGISTRY.put(serializer.getId(), serializer);
            return serializer;
        }
    }

    public static AbilityCondition deserialize(JsonObject jsonObject) {
        ResourceLocation s = new ResourceLocation(JsonUtils.getString(jsonObject, "type"));
        IAbilityConditionSerializer<?> serializer = REGISTRY.get(s);
        if (serializer == null) {
            throw new JsonSyntaxException("Invalid or unsupported ability condition type '" + s + "'");
        } else {
            return serializer.read(jsonObject);
        }
    }

    public static AbilityCondition deserialize(NBTTagCompound nbt) {
        ResourceLocation s = new ResourceLocation(nbt.getString("Type"));
        IAbilityConditionSerializer<?> serializer = REGISTRY.get(s);
        if (serializer == null) {
            return null;
        } else {
            return serializer.read(nbt);
        }
    }

}
