package net.threetag.palladium.util.property;

import net.threetag.palladium.power.ability.AbilityConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class KeyTypeProperty extends EnumPalladiumProperty<AbilityConfiguration.KeyType> {

    public static final List<AbilityConfiguration.KeyType> ALL = List.of(AbilityConfiguration.KeyType.values());
    public static final List<AbilityConfiguration.KeyType> WITHOUT_SCROLLING = Arrays.stream(AbilityConfiguration.KeyType.values()).filter(t -> !t.toString().toLowerCase(Locale.ROOT).startsWith("scroll")).collect(Collectors.toList());
    public static final List<AbilityConfiguration.KeyType> WITHOUT_EITHER_SCROLLING = Arrays.stream(AbilityConfiguration.KeyType.values()).filter(t -> t != AbilityConfiguration.KeyType.SCROLL_EITHER).collect(Collectors.toList());

    private final List<AbilityConfiguration.KeyType> keyTypes;

    public KeyTypeProperty(String key, List<AbilityConfiguration.KeyType> keyTypes) {
        super(key);
        this.keyTypes = keyTypes;
    }

    @Override
    public AbilityConfiguration.KeyType[] getValues() {
        return this.keyTypes.toArray(new AbilityConfiguration.KeyType[0]);
    }

    @Override
    public String getNameFromEnum(AbilityConfiguration.KeyType value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

}
