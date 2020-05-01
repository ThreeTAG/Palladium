package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.threetag.threecore.ability.EnumAbilityColor;

public class AbilityColorThreeData extends ThreeData<EnumAbilityColor> {

    public AbilityColorThreeData(String key) {
        super(key);
    }

    @Override
    public EnumAbilityColor parseValue(JsonObject jsonObject, EnumAbilityColor defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        EnumAbilityColor type = EnumAbilityColor.getByName(JSONUtils.getString(jsonObject, this.jsonKey));
        return type != null ? type : defaultValue;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, EnumAbilityColor value) {
        nbt.putInt(this.key, value.ordinal());
    }

    @Override
    public EnumAbilityColor readFromNBT(CompoundNBT nbt, EnumAbilityColor defaultValue) {
        if(!nbt.contains(this.key))
            return defaultValue;
        try {
            EnumAbilityColor color = EnumAbilityColor.values()[nbt.getInt(this.key)];
            return color != null ? color : defaultValue;
        } catch (Exception e) {

        }
        return defaultValue;
    }

    @Override
    public JsonElement serializeJson(EnumAbilityColor value) {
        return new JsonPrimitive(value.toString().toLowerCase());
    }

}
