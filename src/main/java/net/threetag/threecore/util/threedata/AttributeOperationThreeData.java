package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
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
    public String getDisplay(AttributeModifier.Operation value) {
        return Integer.valueOf(value.getId()).toString();
    }

}
