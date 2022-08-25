package net.threetag.palladium.util.property;

import net.minecraft.world.entity.EquipmentSlot;

public class EquipmentSlotProperty extends EnumPalladiumProperty<EquipmentSlot> {

    public EquipmentSlotProperty(String key) {
        super(key);
    }

    @Override
    public EquipmentSlot[] getValues() {
        return EquipmentSlot.values();
    }

    @Override
    public String getNameFromEnum(EquipmentSlot value) {
        return value.getName();
    }
}
