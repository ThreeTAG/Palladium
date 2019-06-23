package com.threetag.threecore.util.render;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class IconSerializer {

    private static Map<ResourceLocation, IIconSerializer> REGISTRY = Maps.newHashMap();

    static {
        register(ItemIcon.Serializer.INSTANCE);
        register(TexturedIcon.Serializer.INSTANCE);
    }

    public static <S extends IIconSerializer<T>, T extends IIcon> S register(S serializer) {
        if (REGISTRY.containsKey(serializer.getId())) {
            throw new IllegalArgumentException("Duplicate icon serializer " + serializer.getId());
        } else {
            REGISTRY.put(serializer.getId(), serializer);
            return serializer;
        }
    }

    public static IIcon deserialize(JsonObject jsonObject) {
        ResourceLocation s = new ResourceLocation(JSONUtils.getString(jsonObject, "type"));
        IIconSerializer<?> serializer = REGISTRY.get(s);
        if (serializer == null) {
            throw new JsonSyntaxException("Invalid or unsupported icon type '" + s + "'");
        } else {
            return serializer.read(jsonObject);
        }
    }

    public static IIcon deserialize(CompoundNBT nbt) {
        ResourceLocation s = new ResourceLocation(nbt.getString("Type"));
        IIconSerializer<?> serializer = REGISTRY.get(s);
        if (serializer == null) {
            return null;
        } else {
            return serializer.read(nbt);
        }
    }

}
