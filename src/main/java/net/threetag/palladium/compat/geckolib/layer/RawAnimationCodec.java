package net.threetag.palladium.compat.geckolib.layer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zigythebird.playeranimcore.animation.Animation;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.util.PalladiumCodecs;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.LoopType;

import java.util.Map;

public class RawAnimationCodec {

    public static final Codec<LoopType> LOOP_TYPE_CODEC = Codec.STRING.xmap(LoopType::fromString, RawAnimationCodec::getLoopTypeName);

    public static final Codec<RawAnimation.Stage> STAGE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("animation", "internal.wait").forGetter(RawAnimation.Stage::animationName),
            LOOP_TYPE_CODEC.optionalFieldOf("loop_type", LoopType.DEFAULT).forGetter(RawAnimation.Stage::loopType),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("wait_ticks", 0).forGetter(RawAnimation.Stage::waitTicks)
    ).apply(instance, RawAnimation.Stage::new));

    public static final Codec<RawAnimation> SIMPLE_CODEC = Codec.STRING.xmap(s -> {
        var animation = RawAnimation.begin();
        animation.thenPlay(s);
        return animation;
    }, rawAnimation -> rawAnimation.getAnimationStages().getFirst().animationName());

    public static final Codec<RawAnimation> DIRECT_CODEC = PalladiumCodecs.listOrPrimitive(STAGE_CODEC).xmap(stages -> {
        var animation = RawAnimation.begin();
        animation.getAnimationStages().addAll(stages);
        return animation;
    }, RawAnimation::getAnimationStages);

    public static final Codec<RawAnimation> CODEC = Codec.either(SIMPLE_CODEC, DIRECT_CODEC).xmap(
            either -> either.map(
                    rawAnimation -> rawAnimation,
                    rawAnimation -> rawAnimation
            ),
            rawAnimation -> rawAnimation.getAnimationStages().size() == 1
                    && rawAnimation.getAnimationStages().getFirst().loopType() == Animation.LoopType.DEFAULT
                    && rawAnimation.getAnimationStages().getFirst().waitTicks() == 0 ? Either.left(rawAnimation) : Either.right(rawAnimation)
    );

    private static String getLoopTypeName(LoopType loopType) {
        for (Map.Entry<String, LoopType> e : LoopType.LOOP_TYPES.entrySet()) {
            if (e.getValue() == loopType) {
                return e.getKey();
            }
        }

        return null;
    }

}
