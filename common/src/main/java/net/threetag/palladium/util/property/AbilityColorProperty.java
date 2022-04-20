package net.threetag.palladium.util.property;

import net.threetag.palladium.power.ability.AbilityColor;

import java.util.Locale;

public class AbilityColorProperty extends EnumPalladiumProperty<AbilityColor> {

    public AbilityColorProperty(String key) {
        super(key);
    }

    @Override
    public AbilityColor[] getValues() {
        return AbilityColor.values();
    }

    @Override
    public String getNameFromEnum(AbilityColor value) {
        return value.name().toLowerCase(Locale.ROOT);
    }


}
