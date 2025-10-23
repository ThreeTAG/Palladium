package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum AbilityColor implements StringRepresentable {

    WHITE(104, 0),
    ORANGE(104, 24),
    MAGENTA(104, 48),
    LIGHT_BLUE(104, 72),
    YELLOW(104, 96),
    LIME(104, 120),
    PINK(104, 133),
    GRAY(104, 168),
    LIGHT_GRAY(128, 0),
    CYAN(128, 24),
    PURPLE(128, 48),
    BLUE(128, 72),
    BROWN(128, 96),
    GREEN(128, 120),
    RED(128, 144),
    BLACK(128, 168);

    public static final Codec<AbilityColor> CODEC = StringRepresentable.fromEnum(AbilityColor::values);
    public static final StreamCodec<ByteBuf, AbilityColor> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(i -> AbilityColor.values()[i], Enum::ordinal);

    private final int u;
    private final int v;

    AbilityColor(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public static AbilityColor getByName(String name) {
        for (AbilityColor type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
