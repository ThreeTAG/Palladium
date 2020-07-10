package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class AttributeThreeData extends ThreeData<Attribute> {

    public AttributeThreeData(String key) {
        super(key);
    }

    @Override
    public Attribute parseValue(JsonObject jsonObject, Attribute defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey)));
        if (attribute == null)
            throw new JsonSyntaxException("Attribute " + JSONUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
        return attribute;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Attribute value) {
        nbt.putString(this.key, Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getKey(value)).toString());
    }

    @Override
    public Attribute readFromNBT(CompoundNBT nbt, Attribute defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(nbt.getString(this.key)));
        return attribute != null ? attribute : defaultValue;
    }

    @Override
    public JsonElement serializeJson(Attribute value) {
        return new JsonPrimitive(ForgeRegistries.ATTRIBUTES.getKey(value).toString());
    }
}
