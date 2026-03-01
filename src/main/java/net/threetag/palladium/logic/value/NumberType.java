package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum NumberType implements StringRepresentable {

    INTEGER("integer"),
    FLOAT("float"),
    DOUBLE("double");

    public static final Codec<NumberType> CODEC = StringRepresentable.fromEnum(NumberType::values);

    private final String name;

    NumberType(String name) {
        this.name = name;
    }

    public Number convertTo(Number value) {
        return switch (this) {
            case INTEGER -> value.intValue();
            case FLOAT -> value.floatValue();
            case DOUBLE -> value.doubleValue();
        };
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
