package net.threetag.palladium.power.ability.keybind;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class MouseClickKeyBind extends KeyBindType {

    public static final MapCodec<MouseClickKeyBind> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ClickType.CODEC.fieldOf("click_type").forGetter(k -> k.clickType),
            Codec.BOOL.optionalFieldOf("cancel_interaction", false).forGetter(k -> k.cancelInteraction)
    ).apply(instance, MouseClickKeyBind::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MouseClickKeyBind> STREAM_CODEC = StreamCodec.composite(
            ClickType.STREAM_CODEC, k -> k.clickType,
            ByteBufCodecs.BOOL, k -> k.cancelInteraction,
            MouseClickKeyBind::new
    );

    public final ClickType clickType;
    public final boolean cancelInteraction;

    public MouseClickKeyBind(ClickType clickType, boolean cancelInteraction) {
        this.clickType = clickType;
        this.cancelInteraction = cancelInteraction;
    }

    @Override
    public KeyBindTypeSerializer<?> getSerializer() {
        return KeyBindTypeSerializers.MOUSE_CLICK.get();
    }

    public static class Serializer extends KeyBindTypeSerializer<MouseClickKeyBind> {

        @Override
        public MapCodec<MouseClickKeyBind> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MouseClickKeyBind> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public enum ClickType implements StringRepresentable {

        LEFT_CLICK(0, "left_click"),
        RIGHT_CLICK(1, "right_click"),
        MIDDLE_CLICK(2, "middle_click");

        public static final IntFunction<ClickType> BY_ID = ByIdMap.continuous(ClickType::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<ClickType> CODEC = StringRepresentable.fromEnum(ClickType::values);
        public static final StreamCodec<ByteBuf, ClickType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ClickType::id);

        private final int id;
        private final String name;

        ClickType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public int id() {
            return this.id;
        }
    }

}
