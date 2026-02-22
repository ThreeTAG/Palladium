package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum StringComparator implements StringRepresentable {

    EQUALS("equals"),
    EQUALS_IGNORE_CASE("equals_ignore_case"),
    STARTS_WITH("starts_with"),
    ENDS_WITH("ends_with"),
    CONTAINS("contains"),
    CONTAINS_IGNORE_CASE("contains_ignore_case");

    public static final Codec<StringComparator> CODEC = StringRepresentable.fromEnum(StringComparator::values);

    private final String name;

    StringComparator(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean compare(String main, String compareTo) {
        return switch (this) {
            case EQUALS -> main.equals(compareTo);
            case EQUALS_IGNORE_CASE -> main.equalsIgnoreCase(compareTo);
            case STARTS_WITH -> main.startsWith(compareTo);
            case ENDS_WITH -> main.endsWith(compareTo);
            case CONTAINS -> main.contains(compareTo);
            case CONTAINS_IGNORE_CASE -> main.toLowerCase(Locale.ROOT).contains(compareTo.toLowerCase(Locale.ROOT));
        };
    }
}
