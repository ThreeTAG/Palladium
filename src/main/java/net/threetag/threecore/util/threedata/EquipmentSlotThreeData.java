package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class EquipmentSlotThreeData extends ThreeData<EquipmentSlotType> {

    public EquipmentSlotThreeData(String key) {
        super(key);
    }

    @Override
    public EquipmentSlotType parseValue(JsonObject jsonObject, EquipmentSlotType defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        try {
            return EquipmentSlotType.fromString(JSONUtils.getString(jsonObject, this.jsonKey));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, EquipmentSlotType value) {
        nbt.putString(this.key, value.getName());
    }

    @Override
    public EquipmentSlotType readFromNBT(CompoundNBT nbt, EquipmentSlotType defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        try {
            return EquipmentSlotType.fromString(nbt.getString(this.key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String getDisplay(EquipmentSlotType value) {
        return value.getName();
    }
}
