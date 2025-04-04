package net.threetag.palladium.util.property;

import net.threetag.palladium.power.ability.EntityGlowAbility;

public class EntityGlowModeProperty extends EnumPalladiumProperty<EntityGlowAbility.Mode> {

    public EntityGlowModeProperty(String key) {
        super(key);
    }

    @Override
    public EntityGlowAbility.Mode[] getValues() {
        return EntityGlowAbility.Mode.values();
    }

    @Override
    public String getNameFromEnum(EntityGlowAbility.Mode value) {
        return value.name;
    }
}
