package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum NumberComparator implements StringRepresentable {

    EQUALS("="),
    NOT("!="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">=");

    public static final Codec<NumberComparator> CODEC = StringRepresentable.fromEnum(NumberComparator::values);
    public static final StreamCodec<ByteBuf, NumberComparator> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(i -> NumberComparator.values()[i], Enum::ordinal);

    private final String symbol;

    NumberComparator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.symbol;
    }

    public boolean compare(Number number1, Number number2) {
        return switch (this) {
            case EQUALS -> Objects.equals(number1, number2);
            case NOT -> !Objects.equals(number1, number2);
            case LESS_THAN -> number1.doubleValue() < number2.doubleValue();
            case GREATER_THAN -> number1.doubleValue() > number2.doubleValue();
            case LESS_OR_EQUAL -> number1.doubleValue() <= number2.doubleValue();
            case GREATER_OR_EQUAL -> number1.doubleValue() >= number2.doubleValue();
        };
    }
}
