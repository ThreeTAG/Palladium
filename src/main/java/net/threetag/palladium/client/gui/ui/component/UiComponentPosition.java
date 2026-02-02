package net.threetag.palladium.client.gui.ui.component;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.client.gui.component.UiAlignment;

import java.util.Arrays;
import java.util.function.Function;

public record UiComponentPosition(UiAlignment alignment, int xOffset, int yOffset) {

    public static final UiComponentPosition TOP_LEFT = topLeft(0, 0);

    private static final Codec<UiComponentPosition> ARRAY_CODEC = Codec.INT.listOf(2, 2).xmap(list ->
            new UiComponentPosition(UiAlignment.TOP_LEFT, list.getFirst(), list.get(1)), pos -> Arrays.asList(pos.xOffset, pos.yOffset));

    private static final Codec<UiComponentPosition> SIMPLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(UiComponentPosition::xOffset),
            Codec.INT.fieldOf("y").forGetter(UiComponentPosition::yOffset)
            ).apply(instance, (x, y) -> new UiComponentPosition(UiAlignment.TOP_LEFT, x, y)));

    private static final Codec<UiComponentPosition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UiAlignment.CODEC.fieldOf("alignment").forGetter(UiComponentPosition::alignment),
            Codec.INT.optionalFieldOf("x_offset", 0).forGetter(UiComponentPosition::xOffset),
            Codec.INT.optionalFieldOf("y_offset", 0).forGetter(UiComponentPosition::yOffset)
    ).apply(instance, UiComponentPosition::new));

    public static final Codec<UiComponentPosition> CODEC = Codec.either(DIRECT_CODEC, Codec.withAlternative(SIMPLE_CODEC, ARRAY_CODEC))
            .xmap(either -> either.map(Function.identity(), Function.identity()),
                    pos -> pos.alignment() == UiAlignment.TOP_LEFT ? Either.right(pos) : Either.left(pos));

    public static UiComponentPosition topLeft(int x, int y) {
        return new UiComponentPosition(UiAlignment.TOP_LEFT, x, y);
    }

}
