package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum Easing implements StringRepresentable {

    LINEAR(0, "linear", f -> f), CONSTANT(1, "constant", f -> 0f),
    INSINE(2, "in_sine", Easing::inSine), OUTSINE(3, "out_sine", Easing::outSine), INOUTSINE(4, "in_out_sine", Easing::inOutSine),
    INCUBIC(5, "in_cubic", Easing::inCubic), OUTCUBIC(6, "out_cubic", Easing::outCubic), INOUTCUBIC(7, "in_out_cubic", Easing::inOutCubic),
    INQUAD(8, "in_quad", Easing::inQuad), OUTQUAD(9, "out_quad", Easing::outQuad), INOUTQUAD(10, "in_out_quad", Easing::inOutQuad),
    INQUART(11, "in_quart", Easing::inQuart), OUTQUART(12, "out_quart", Easing::outQuart), INOUTQUART(13, "in_out_quart", Easing::inOutQuart),
    INQUINT(14, "in_quint", Easing::inQuint), OUTQUINT(15, "out_quint", Easing::outQuint), INOUTQUINT(16, "in_out_quint", Easing::inOutQuint),
    INEXPO(17, "in_expo", Easing::inExpo), OUTEXPO(18, "out_expo", Easing::outExpo), INOUTEXPO(19, "in_out_expo", Easing::inOutExpo),
    INCIRC(20, "in_circ", Easing::inCirc), OUTCIRC(21, "out_circ", Easing::outCirc), INOUTCIRC(22, "in_out_circ", Easing::inOutCirc),
    INBACK(23, "in_back", Easing::inBack), OUTBACK(24, "out_back", Easing::outBack), INOUTBACK(25, "in_out_back", Easing::inOutBack),
    INELASTIC(26, "in_elastic", Easing::inElastic), OUTELASTIC(27, "out_elastic", Easing::outElastic), INOUTELASTIC(28, "in_out_elastic", Easing::inOutElastic),
    INBOUNCE(29, "in_bounce", Easing::inBounce), OUTBOUNCE(30, "out_bounce", Easing::outBack), INOUTBOUNCE(31, "in_out_bounce", Easing::inOutBounce);

    public static final IntFunction<Easing> BY_ID = ByIdMap.continuous(Easing::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<Easing> CODEC = StringRepresentable.fromEnum(Easing::values);
    public static final StreamCodec<ByteBuf, Easing> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Easing::id);

    private final int id;
    private final String name;
    private final Float2FloatFunction function;

    Easing(int id, String name, Float2FloatFunction function) {
        this.id = id;
        this.name = name;
        this.function = function;
    }

    public float apply(float f) {
        return this.function.apply(f);
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }

    private static final float c1 = 1.70158f;
    private static final float c2 = c1 * 1.525f;
    private static final float c3 = c1 + 1;
    private static final float c4 = (float) ((2 * Math.PI) / 3);
    private static final float c5 = (float) ((2 * Math.PI) / 4.5);
    private static final float n1 = 7.5625f;
    private static final float d1 = 2.75f;

    public static float inSine(float f) {
        return (float) (1 - Math.cos((f * Math.PI) / 2));
    }

    public static float outSine(float f) {
        return (float) (Math.sin((f * Math.PI) / 2));
    }

    public static float inOutSine(float f) {
        return (float) (-(Math.cos(Math.PI * f) - 1) / 2);
    }

    public static float inCubic(float f) {
        return f * f * f;
    }

    public static float outCubic(float f) {
        return (float) (1 - Math.pow(1 - f, 3));
    }

    public static float inOutCubic(float x) {
        return (float) ((x < 0.5) ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    public static float inQuad(float x) {
        return (x * x);
    }

    public static float outQuad(float x) {
        return (1 - (1 - x) * (1 - x));
    }

    public static float inOutQuad(float x) {
        return (float) ((x < 0.5) ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2);
    }

    public static float inQuart(float x) {
        return (x * x * x * x);
    }

    public static float outQuart(float x) {
        return (float) (1 - Math.pow(1 - x, 4));
    }

    public static float inOutQuart(float x) {
        return (float) (x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2);
    }

    public static float inQuint(float x) {
        return (x * x * x * x * x);
    }

    public static float outQuint(float x) {
        return (float) (1 - Math.pow(1 - x, 5));
    }

    public static float inOutQuint(float x) {
        return (float) (x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2);
    }

    public static float inExpo(float x) {
        return (float) (x == 0 ? 0 : Math.pow(2, 10 * x - 10));
    }

    public static float outExpo(float x) {
        return (float) (x == 1 ? 1 : 1 - Math.pow(2, -10 * x));
    }

    public static float inOutExpo(float x) {
        return (float) (x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2 : (2 - Math.pow(2, -20 * x + 10)) / 2);
    }

    public static float inCirc(float x) {
        return (float) (1 - Math.sqrt(1 - Math.pow(x, 2)));
    }

    public static float outCirc(float x) {
        return (float) (Math.sqrt(1 - Math.pow(x - 1, 2)));
    }

    public static float inOutCirc(float x) {
        return (float) (x < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2);
    }

    public static float inBack(float x) {
        return c3 * x * x * x - c1 * x * x;
    }

    public static float outBack(float x) {
        return (float) (1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2));
    }

    public static float inOutBack(float x) {
        return (float) (x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
    }

    public static float inElastic(float x) {
        return (float) (x == 0 ? 0 : x == 1 ? 1 : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4));
    }

    public static float outElastic(float x) {
        return (float) (x == 0 ? 0 : x == 1 ? 1 : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1);
    }

    public static float inOutElastic(float x) {
        return (float) (x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? -(Math.pow(2, 20 * x - 10) * Math.sin((20 * x - 11.125) * c5)) / 2 : (Math.pow(2, -20 * x + 10) * Math.sin((20 * x - 11.125) * c5)) / 2 + 1);
    }

    public static float inBounce(float x) {
        return 1 - outBounce(1 - x);
    }

    public static float outBounce(float x) {
        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return (float) (n1 * (x -= 1.5 / d1) * x + 0.75);
        } else if (x < 2.5 / d1) {
            return (float) (n1 * (x -= 2.25 / d1) * x + 0.9375);
        } else {
            return (float) (n1 * (x -= 2.625 / d1) * x + 0.984375);
        }
    }

    public static float inOutBounce(float x) {
        return x < 0.5 ? (1 - outBounce(1 - 2 * x)) / 2 : (1 + outBounce(2 * x - 1)) / 2;
    }

}
