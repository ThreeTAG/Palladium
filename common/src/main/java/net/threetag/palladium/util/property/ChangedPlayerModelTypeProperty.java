package net.threetag.palladium.util.property;

import java.util.Locale;

public class ChangedPlayerModelTypeProperty extends EnumPalladiumProperty<ChangedPlayerModelTypeProperty.ChangedModelType> {

    public ChangedPlayerModelTypeProperty(String key) {
        super(key);
    }

    @Override
    public ChangedModelType[] getValues() {
        return ChangedModelType.values();
    }

    @Override
    public String getNameFromEnum(ChangedModelType value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

    public enum ChangedModelType {

        KEEP,
        NORMAL,
        SLIM

    }

}
