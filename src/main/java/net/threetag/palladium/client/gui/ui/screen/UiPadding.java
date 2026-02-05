package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public record UiPadding(int top, int bottom, int left, int right) {

    public static final UiPadding SEVEN = new UiPadding(7);

    public static final Codec<UiPadding> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("top", 0).forGetter(UiPadding::top),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("bottom", 0).forGetter(UiPadding::bottom),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("left", 0).forGetter(UiPadding::left),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("right", 0).forGetter(UiPadding::right)
    ).apply(instance, UiPadding::new));

    public static final Codec<UiPadding> CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, DIRECT_CODEC)
            .xmap(either -> either.map(
                            UiPadding::new,
                            Function.identity()),
                    padding ->
                            padding.top() == padding.bottom() && padding.bottom() == padding.left() && padding.left() == padding.right() ?
                                    Either.left(padding.top()) : Either.right(padding)
            );

    public UiPadding(int padding) {
        this(padding, padding, padding, padding);
    }

}
