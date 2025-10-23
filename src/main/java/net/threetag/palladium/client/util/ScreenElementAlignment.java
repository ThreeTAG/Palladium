package net.threetag.palladium.client.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum ScreenElementAlignment implements StringRepresentable {

    TOP_LEFT(0, "top_left", 0, 0, false),
    TOP_CENTER(1, "top_center", 1, 0, false),
    TOP_RIGHT(2, "top_right", 2, 0, false),
    MIDDLE_LEFT(3, "middle_left", 0, 1, false),
    CENTER(4, "center", 1, 1, false),
    MIDDLE_RIGHT(5, "middle_right", 2, 1, false),
    BOTTOM_LEFT(6, "bottom_left", 0, 2, false),
    BOTTOM_CENTER(7, "bottom_center", 1, 2, false),
    BOTTOM_RIGHT(8, "bottom_right", 2, 2, false),
    STRETCH(9, "stretch", 0, 0, true);

    public static final IntFunction<ScreenElementAlignment> BY_ID = ByIdMap.continuous(ScreenElementAlignment::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<ScreenElementAlignment> CODEC = StringRepresentable.fromEnum(ScreenElementAlignment::values);
    public static final StreamCodec<ByteBuf, ScreenElementAlignment> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ScreenElementAlignment::id);

    private final int id;
    private final String name;
    private final int horizontal, vertical;
    private final boolean stretch;

    ScreenElementAlignment(int id, String name, int horizontal, int vertical, boolean stretch) {
        this.id = id;
        this.name = name;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.stretch = stretch;
    }

    public int id() {
        return this.id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public boolean isStretched() {
        return stretch;
    }

}
