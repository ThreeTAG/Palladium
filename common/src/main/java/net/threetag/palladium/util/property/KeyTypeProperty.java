package net.threetag.palladium.util.property;

import net.threetag.palladium.power.ability.AbilityConfiguration;

import java.util.Locale;

public class KeyTypeProperty extends EnumPalladiumProperty<AbilityConfiguration.KeyType> {

    public KeyTypeProperty(String key) {
        super(key);
    }

    @Override
    public AbilityConfiguration.KeyType[] getValues() {
        return AbilityConfiguration.KeyType.values();
    }

    @Override
    public String getNameFromEnum(AbilityConfiguration.KeyType value) {
        return value.name().toLowerCase(Locale.ROOT);
    }
}
