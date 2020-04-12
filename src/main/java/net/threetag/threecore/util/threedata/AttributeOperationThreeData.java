package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class AttributeOperationThreeData extends ThreeData<AttributeModifier.Operation> {

    public AttributeOperationThreeData(String key) {
        super(key);
    }

    @Override
    public AttributeModifier.Operation parseValue(JsonObject jsonObject, AttributeModifier.Operation defaultValue) {
        return AttributeModifier.Operation.byId(JSONUtils.getInt(jsonObject, this.jsonKey, defaultValue.getId()));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, AttributeModifier.Operation value) {
        nbt.putInt(this.key, value.getId());
    }

    @Override
    public AttributeModifier.Operation readFromNBT(CompoundNBT nbt, AttributeModifier.Operation defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return AttributeModifier.Operation.byId(nbt.getInt(this.key));
    }

    @Override
    public JsonElement serializeJson(AttributeModifier.Operation value) {
        return new JsonPrimitive(value.getId());
    }
}
