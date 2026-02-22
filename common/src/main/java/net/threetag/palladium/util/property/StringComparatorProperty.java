package net.threetag.palladium.util.property;

import net.threetag.palladium.util.StringComparator;

public class StringComparatorProperty extends EnumPalladiumProperty<StringComparator> {

    public StringComparatorProperty(String key) {
        super(key);
    }

    @Override
    public StringComparator[] getValues() {
        return StringComparator.values();
    }

    @Override
    public String getNameFromEnum(StringComparator value) {
        return value.getSerializedName();
    }
}
