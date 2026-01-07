package net.threetag.palladium.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PalladiumCodecs {

    public static final Codec<Float> FLOAT_OR_BOOLEAN_CODEC = Codec.either(Codec.FLOAT, Codec.BOOL).xmap(
            either -> either.map(
                    left -> left,
                    right -> right ? 1F : 0F
            ),
            f -> f == 0F || f == 1F ? Either.right(f == 1F) : Either.left(f)
    );

    /**
     * Codec for colors. Does NOT support alpha
     */
    public static final Codec<Color> COLOR_CODEC = Codec.withAlternative(
            Codec.STRING.xmap(s -> Color.decode(s.startsWith("#") ? s : "#" + s), color -> "#" + Integer.toHexString(color.getRGB()).substring(2)),
            Codec.withAlternative(
                    Codec.INT.listOf(3, 3).xmap(integers -> new Color(integers.getFirst(), integers.get(1), integers.get(2)), color -> {
                        List<Integer> integers = new ArrayList<>();
                        integers.add(color.getRed());
                        integers.add(color.getGreen());
                        integers.add(color.getBlue());
                        return integers;
                    }),
                    Codec.INT.xmap(Color::new, Color::getRGB)
            )
    );
    public static final StreamCodec<ByteBuf, Color> COLOR_STREAM_CODEC = ByteBufCodecs.VAR_INT.map(Color::new, Color::getRGB);

    @SuppressWarnings("deprecation")
    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.withAlternative(
            Ingredient.CODEC,
            Item.CODEC.xmap(itemHolder -> Ingredient.of(itemHolder.value()), ingredient -> ingredient.items().toList().getFirst())
    );

    public static final Codec<Vec2> VEC2_CODEC = Codec.FLOAT.listOf().comapFlatMap((list) -> Util.fixedSize(list, 2).map((floats) -> new Vec2(floats.getFirst(), floats.get(1))), (vec2) -> List.of(vec2.x, vec2.y));
    public static final StreamCodec<ByteBuf, Vec2> VEC2_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, v -> v.x, ByteBufCodecs.FLOAT, v -> v.y, Vec2::new);
    public static final Codec<Vector2f> VECTOR_2F_CODEC = VEC2_CODEC.xmap(vec2 -> new Vector2f(vec2.x, vec2.y), vector2f -> new Vec2(vector2f.x, vector2f.y));
    public static final Codec<Vector3f> VECTOR_3F_CODEC = Vec3.CODEC.xmap(Vec3::toVector3f, Vec3::new);
    public static final Codec<Vector3f> VOXEL_VECTOR_3F = VECTOR_3F_CODEC.xmap(vector3f -> vector3f.div(16, -16, 16), vector3f -> vector3f.mul(16, -16, 16));
    public static final Codec<Vector2f> VOXEL_VECTOR_2F = VECTOR_2F_CODEC.xmap(vector2f -> vector2f.div(16), vector2f -> vector2f.mul(16));
    public static final Codec<Float> VOXEL_FLOAT = Codec.FLOAT.xmap(f -> f / 16, f -> f * 16);
    public static final Codec<Float> NON_NEGATIVE_VOXEL_FLOAT = ExtraCodecs.NON_NEGATIVE_FLOAT.xmap(f -> f / 16, f -> f * 16);
    public static final Codec<Float> FLOAT_0_TO_1 = floatRangeMinInclusiveWithMessage(0F, 1F, f -> "Value must be within 0.0 and 1.0: " + f);

    public static final Codec<Integer> TIME = Codec.withAlternative(
            ExtraCodecs.NON_NEGATIVE_INT,
            Codec.STRING.comapFlatMap(PalladiumCodecs::readTime, String::valueOf).stable()
    );

    public static <T> Codec<List<T>> listOrPrimitive(Codec<T> codec) {
        return Codec.either(codec.listOf(), codec).xmap(
                either -> either.map(
                        left -> left,
                        Collections::singletonList
                ),
                list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list)
        );
    }

    private static Codec<Float> floatRangeMinInclusiveWithMessage(float min, float max, Function<Float, String> errorMessage) {
        return Codec.FLOAT
                .validate(
                        f -> f.compareTo(min) >= 0 && f.compareTo(max) <= 0
                                ? DataResult.success(f)
                                : DataResult.error(() -> errorMessage.apply(f))
                );
    }

    @SuppressWarnings("DuplicateExpressions")
    public static DataResult<Integer> readTime(String string) {
        try {
            int time;

            if (string.endsWith("s")) {
                double val = Double.parseDouble(string.subSequence(0, string.length() - 1).toString());
                time = (int) (val * 20);
            } else if (string.endsWith("t")) {
                time = Integer.parseInt(string.subSequence(0, string.length() - 1).toString());
            } else if (string.endsWith("m")) {
                double val = Double.parseDouble(string.subSequence(0, string.length() - 1).toString());
                time = (int) (val * 20 * 60);
            } else {
                time = Integer.parseInt(string);
            }

            return DataResult.success(time);
        } catch (Exception e) {
            return DataResult.error(() -> {
                return "Not a valid time unit: " + string + " " + e.getMessage();
            });
        }
    }

}
