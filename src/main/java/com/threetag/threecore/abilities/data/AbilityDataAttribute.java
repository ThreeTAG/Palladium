package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.threetag.threecore.util.attributes.AttributeRegistry;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class AbilityDataAttribute extends AbilityData<IAttribute> {

    public AbilityDataAttribute(String key) {
        super(key);
    }

    @Override
    public IAttribute parseValue(JsonObject jsonObject, IAttribute defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        AttributeRegistry.AttributeEntry attributeEntry = AttributeRegistry.REGISTRY.get(new ResourceLocation(JsonUtils.getString(jsonObject, this.jsonKey)));
        if (attributeEntry == null)
            throw new JsonSyntaxException("Attribute " + JsonUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
        return attributeEntry.getAttribute();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, IAttribute value) {
        nbt.putString(this.key, Objects.requireNonNull(AttributeRegistry.REGISTRY.getKey(AttributeRegistry.getEntry(value))).toString());
    }

    @Override
    public IAttribute readFromNBT(NBTTagCompound nbt, IAttribute defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return AttributeRegistry.REGISTRY.get(new ResourceLocation(nbt.getString(this.key))).getAttribute();
    }
}
