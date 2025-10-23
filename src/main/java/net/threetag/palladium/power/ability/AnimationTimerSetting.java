package net.threetag.palladium.power.ability;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.util.PalladiumCodecs;
import net.threetag.palladium.util.Easing;

public record AnimationTimerSetting(int min, int max, Easing easing) {

    private static final Codec<AnimationTimerSetting> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PalladiumCodecs.TIME.optionalFieldOf("min", 0).forGetter(AnimationTimerSetting::min),
                    PalladiumCodecs.TIME.fieldOf("max").forGetter(AnimationTimerSetting::max),
                    Easing.CODEC.optionalFieldOf("easing", Easing.LINEAR).forGetter(AnimationTimerSetting::easing)
            ).apply(instance, AnimationTimerSetting::new));

    public static final Codec<AnimationTimerSetting> CODEC = Codec.either(DIRECT_CODEC, Codec.INT).xmap(
            either -> either.map(
                    left -> left,
                    right -> new AnimationTimerSetting(0, right, Easing.LINEAR)
            ),
            setting -> (setting.min == 0 && setting.easing == Easing.LINEAR) ? Either.right(setting.max) : Either.left(setting)
    );

    public AnimationTimer create() {
        return new AnimationTimer(this, this.min);
    }

}
